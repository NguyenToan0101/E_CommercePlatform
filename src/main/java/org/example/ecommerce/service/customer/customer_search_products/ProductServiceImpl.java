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
        Product product = productRepo.findById(productId).orElse(null);
        Shop shop = shopRepo.findById(product.getShopid().getId()).orElse(null);
        List<Inventory> inventories = inventoryRepo.findAllByProductid(product);
        List<Productimage> images = imageRepo.findAllByProductid(product);
        List<Review> reviews = reviewRepo.findAllByProductidOrderByCreatedatDesc(product);
        List<Wishlist> wishlists = wishlistRepo.findAllByProductid(product);

        Float avgRating = reviewRepo.findAverageRatingByProductid(product);
        float rate = (avgRating != null) ? avgRating : 0f;

        Integer sumSold = inventoryRepo.findSumsolditemsByProductid(product);
        int solditems = (sumSold != null) ? sumSold : 0;

        Integer sumReview = reviewRepo.countByProductid(product);
        int sumReviewRating = (sumReview != null) ? sumReview : 0;

        return new ProductDetail(product, shop, inventories, images, reviews, wishlists, inventoryRepo.findFirstByProductidOrderByPriceAsc(product).getPrice(), rate, solditems, sumReviewRating);
    }

    //Admin Product Management
    @Override
    public List<ProductDTO> listAllProducts() {
        return productRepo.findAll().stream()
                .map(p -> {
                    Inventory inv = p.getInventories().stream()
                            .findFirst()
                            .orElseGet(() -> {
                                Inventory e = new Inventory();
                                e.setPrice(BigDecimal.ZERO);
                                e.setSolditems(0);
                                return e;
                            });


                    String thumbnail = p.getProductimages().stream()
                            .map(Productimage::getImageurl)
                            .findFirst()
                            .orElse("");

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
                })
                .collect(Collectors.toList());
    }

    //danh muc chinh
    public List<ProductDTO> listByMainCategory(Integer mainCategoryId) {
        return mapToDTO(productRepo.findByMainCategoryId(mainCategoryId));
    }

    private List<ProductDTO> mapToDTO(List<Product> products) {
        return products.stream().map(p -> {
            Inventory inv = p.getInventories().stream()
                    .findFirst()
                    .orElseGet(() -> {
                        var e = new Inventory();
                        e.setPrice(BigDecimal.ZERO);
                        e.setSolditems(0);
                        return e;
                    });
            String thumb = p.getProductimages().stream()
                    .map(Productimage::getImageurl)
                    .findFirst()
                    .orElse("");
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

        // Map images
        List<String> imgs = p.getProductimages().stream()
                .map(pi -> pi.getImageurl())
                .collect(Collectors.toList());

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
