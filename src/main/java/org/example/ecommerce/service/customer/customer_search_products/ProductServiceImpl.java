package org.example.ecommerce.service.customer.customer_search_products;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.example.ecommerce.common.dto.admin.productManagement.AdminProductDetailDTO;
import org.example.ecommerce.common.dto.admin.productManagement.InventoryDTO;
import org.example.ecommerce.common.dto.admin.productManagement.ProductDTO;
import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.*;
import org.example.ecommerce.service.customer.cusromer_aut.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private final ProductRepository productRepo;

    @Autowired
    private final InventoryRepository inventoryRepo;

    @Autowired
    private final ProductimageRepository imageRepo;

    @Autowired
    private final ReviewRepository reviewRepo;

    @Autowired
    private final ShopRepository shopRepo;

    @Autowired
    private final WishlistRepository wishlistRepo;

    @Autowired
    private EmailService emailService;


    public ProductServiceImpl(ProductRepository productRepo, InventoryRepository inventoryRepo, ProductimageRepository imageRepo, ReviewRepository reviewRepo, ShopRepository shopRepo, WishlistRepository wishlistRepo) {
        this.productRepo = productRepo;
        this.inventoryRepo = inventoryRepo;
        this.imageRepo = imageRepo;
        this.reviewRepo = reviewRepo;
        this.shopRepo = shopRepo;
        this.wishlistRepo = wishlistRepo;
    }

    public ProductDetail getProductDetail(Integer productId) {
        // Try optimized query first to get solditems
        List<Object[]> detailResults = productRepo.findProductDetailOptimized(productId);
        if (detailResults.isEmpty()) {
            return null;
        }

        Object[] detailRow = detailResults.get(0);
        Long solditems = (Long) detailRow[12]; // solditems is at index 12

        // Get full product with relations for other data
        Product product = productRepo.findWithAllRelationsById(productId).orElse(null);
        if (product == null) {
            return null;
        }

        Shop shop = product.getShopid();
        // Create defensive copies to avoid ConcurrentModificationException
        List<Inventory> inventories = new ArrayList<>();
        for (Inventory inv : product.getInventories()) {
            if (!inventories.contains(inv)) {
                inventories.add(inv);
            }
        }

        // Load product images separately to avoid embedding field issues
        List<Object[]> imageData = imageRepo.findImageDataByProductId(productId);
        List<Productimage> images = new ArrayList<>();
        for (Object[] data : imageData) {
            Productimage img = new Productimage();
            img.setId((Integer) data[0]);
            img.setImageurl((String) data[1]);
            img.setProductid(product);
            images.add(img);
        }
        List<Review> reviews = new ArrayList<>(product.getReviews());
        List<Wishlist> wishlists = new ArrayList<>(product.getWishlists());

        float rate = (float) reviews.stream().mapToDouble(Review::getRating).average().orElse(0);

        // Use solditems from optimized query
        int solditemsInt = solditems != null ? solditems.intValue() : 0;

        int sumReviewRating = reviews.stream().mapToInt(Review::getRating).sum();

        BigDecimal price = inventories.stream().map(Inventory::getPrice).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

        return new ProductDetail(product, shop, inventories, images, reviews, wishlists, price, rate, solditemsInt, sumReviewRating);
    }

    //Admin Product Management
    @Override
    public List<ProductDTO> listAllProducts() {
        try {
            return productRepo.findAllWithoutEmbeddingIssues().stream()
                    .map(p -> {
                        try {
                            Inventory inv = p.getInventories().stream()
                                    .findFirst()
                                    .orElseGet(() -> {
                                        Inventory e = new Inventory();
                                        e.setPrice(BigDecimal.ZERO);
                                        e.setSolditems(0);
                                        return e;
                                    });

                            // Load image URL riêng biệt để tránh lỗi embedding
                            String thumbnail = "";
                            try {
                                List<Object[]> imageData = imageRepo.findImageDataByProductId(p.getId());
                                if (!imageData.isEmpty()) {
                                    thumbnail = (String) imageData.get(0)[1]; // imageurl ở index 1
                                }
                            } catch (Exception e) {
                                // Nếu có lỗi khi load image, bỏ qua
                                System.err.println("Lỗi khi load image cho product " + p.getId() + ": " + e.getMessage());
                            }

                            return new ProductDTO(
                                    p.getId(),
                                    p.getName(),
                                    p.getShopid().getShopname(),
                                    p.getCategoryid().getCategoryname(),
                                    p.getStatus(),
                                    p.getCreatedat(),
                                    inv.getPrice(),
                                    inv.getSolditems(),
                                    thumbnail
                            );
                        } catch (Exception e) {
                            // Fallback cho sản phẩm có lỗi
                            return new ProductDTO(
                                    p.getId(),
                                    p.getName(),
                                    p.getShopid() != null ? p.getShopid().getShopname() : "Unknown",
                                    p.getCategoryid() != null ? p.getCategoryid().getCategoryname() : "Unknown",
                                    p.getStatus(),
                                    p.getCreatedat(),
                                    BigDecimal.ZERO,
                                    0,
                                    ""
                            );
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Log lỗi và trả về danh sách rỗng
            System.err.println("Lỗi khi load products: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    //danh muc chinh
    public List<ProductDTO> listByMainCategory(Integer mainCategoryId) {
        return mapToDTO(productRepo.findByMainCategoryId(mainCategoryId));
    }

    private List<ProductDTO> mapToDTO(List<Product> products) {
        return products.stream().map(p -> {
            try {
                Inventory inv = p.getInventories().stream()
                        .findFirst()
                        .orElseGet(() -> {
                            var e = new Inventory();
                            e.setPrice(BigDecimal.ZERO);
                            e.setSolditems(0);
                            return e;
                        });

                // Load image URL riêng biệt để tránh lỗi embedding
                String thumb = "";
                try {
                    List<Object[]> imageData = imageRepo.findImageDataByProductId(p.getId());
                    if (!imageData.isEmpty()) {
                        thumb = (String) imageData.get(0)[1]; // imageurl ở index 1
                    }
                } catch (Exception e) {
                    // Nếu có lỗi khi load image, bỏ qua
                    System.err.println("Lỗi khi load image cho product " + p.getId() + ": " + e.getMessage());
                }

                return new ProductDTO(
                        p.getId(),
                        p.getName(),
                        p.getShopid().getShopname(),
                        p.getCategoryid().getCategoryname(),
                        p.getStatus(),
                        p.getCreatedat(),
                        inv.getPrice(),
                        inv.getSolditems(),
                        thumb
                );
            } catch (Exception e) {
                // Fallback cho sản phẩm có lỗi
                return new ProductDTO(
                        p.getId(),
                        p.getName(),
                        p.getShopid() != null ? p.getShopid().getShopname() : "Unknown",
                        p.getCategoryid() != null ? p.getCategoryid().getCategoryname() : "Unknown",
                        p.getStatus(),
                        p.getCreatedat(),
                        BigDecimal.ZERO,
                        0,
                        ""
                );
            }
        }).collect(Collectors.toList());
    }

    //xem chi tiet
    @Override
    public AdminProductDetailDTO getAdminProductDetail(Integer id) {
        Product p = productRepo.findWithAllById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy product " + id));

        // Map inventory
        List<InventoryDTO> invs = p.getInventories().stream()
                .map(inv -> new InventoryDTO(
                        inv.getSolditems(),
                        inv.getPrice(),
                        inv.getColor(),
                        inv.getDimension(),
                        inv.getUpdatedAt()
                ))
                .collect(Collectors.toList());

        // Map images - load riêng biệt để tránh lỗi embedding
        List<String> imgs = new ArrayList<>();
        try {
            List<Object[]> imageData = imageRepo.findImageDataByProductId(id);
            for (Object[] data : imageData) {
                imgs.add((String) data[1]); // imageurl ở index 1
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi load images cho product " + id + ": " + e.getMessage());
        }

        return new AdminProductDetailDTO(
                p.getId(),
                p.getName(),
                p.getShopid().getShopname(),
                p.getCategoryid().getCategoryname(),
                p.getDescription(),
                p.getStatus(),
                p.getCreatedat(),
                invs,
                imgs
        );
    }

    //duyet san pham
    @Override
    @Transactional
    public void approveProduct(Integer productId) {
        Product p = productRepo.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy product " + productId));

        if (!"pending_approval".equals(p.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Chỉ duyệt được khi status = 'pending_approval'");
        }

        p.setStatus("available");
        try {
            String email = p.getShopid().getInvoiceEmail();
            emailService.sendProductApprovedEmail(email, p.getName());
        } catch (MessagingException e) {
            throw new RuntimeException("Gửi mail duyệt sản phẩm thất bại", e);
        }
    }


    //tu choi
    @Override
    @Transactional
    public void rejectProduct(Integer productId) {
        Product p = productRepo.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy product " + productId));

        if (!"pending_approval".equals(p.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Chỉ từ chối được khi status = 'pending_approval'");
        }

        p.setStatus("rejected");
        try {
            String email = p.getShopid().getInvoiceEmail();
            emailService.sendProductRejectedEmail(email, p.getName());
        } catch (MessagingException e) {
            throw new RuntimeException("Gửi mail từ chối sản phẩm thất bại", e);
        }
    }


    //khoa san pham
    @Override
    @Transactional
    public void lockProduct(Integer productId, int durationMinutes) {
        Product p = productRepo.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy product " + productId));

        if ("locked".equals(p.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Sản phẩm đã bị khóa");
        }

        p.setStatus("locked");
        Instant until = Instant.now().plusSeconds(durationMinutes * 60L);
        p.setLockedUntil(until);

        try {
            String email = p.getShopid().getInvoiceEmail();
            emailService.sendProductLockedEmail(
                    email,
                    p.getName(),
                    until.toString()
            );
        } catch (MessagingException e) {
            throw new RuntimeException("Gửi mail khóa sản phẩm thất bại", e);
        }
    }


    //mo khoa
    @Override
    @Transactional
    public void unlockProduct(Integer productId) {
        Product p = productRepo.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy product " + productId));

        if (!"locked".equals(p.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Chỉ mở khóa khi status = 'locked'");
        }

        p.setStatus("available");
        p.setLockedUntil(null);

        try {
            String email = p.getShopid().getInvoiceEmail();
            emailService.sendProductUnlockedEmail(email, p.getName());
        } catch (MessagingException e) {
            throw new RuntimeException("Gửi mail mở khóa sản phẩm thất bại", e);
        }
    }

    @Override
    @Transactional
    public void deleteProduct(Integer productId) {
        Product p = productRepo.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found with id " + productId));

        // Lấy email shop trước khi xóa
        String to = p.getShopid().getInvoiceEmail();
        String productName = p.getName();

        // Xóa sản phẩm
        productRepo.delete(p);

        // Gửi email bất đồng bộ
        try {
            emailService.sendProductDeletedEmail(to, productName);
        } catch (MessagingException e) {
            // Nếu lỗi gửi mail, bạn có thể log hoặc throw tiếp tuỳ nhu cầu
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Deleted product but failed to send notification email", e);
        }
    }
}