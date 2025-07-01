package org.example.ecommerce.service.seller;

import org.example.ecommerce.entity.Category;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.Productimage;
import org.example.ecommerce.entity.Shop;
import org.example.ecommerce.repository.CategoryRepository;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.repository.ProductimageRepository;
import org.example.ecommerce.repository.ShopRepository;
import org.example.ecommerce.service.UploadImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private ProductimageRepository imageRepository;
    @Autowired private UploadImageFile uploadImageFile;
    @Autowired private ShopRepository shopRepository;
    @Autowired private CategoryRepository categoryRepository;

    public List<Product> findAllProductsByShop(Integer shopId) {
        return productRepository.findByShopidId(shopId);
    }

    public List<Product> searchByKeyword(String keyword, Integer shopId) {
        return productRepository.findByShopidIdAndNameContainingIgnoreCase(shopId, keyword);
    }

    public Product getById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public void save(Product product, MultipartFile[] images, Integer shopId) {
        if (product.getStatus() == null || product.getStatus().isEmpty()) {
            product.setStatus("available");
        }

        if (product.getCreatedat() == null) {
            product.setCreatedat(Instant.now());
        }

        if (product.getCategoryid() != null) {
            Category category = categoryRepository.findById(product.getCategoryid().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategoryid(category);
        }

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop không tồn tại"));
        product.setShopid(shop);

        Product saved = productRepository.save(product);

        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                try {
                    String imageUrl = uploadImageFile.uploadImage(image);
                    Productimage img = new Productimage();
                    img.setProductid(saved);
                    img.setImageurl(imageUrl);
                    imageRepository.save(img);
                } catch (IOException e) {
                    throw new RuntimeException("Lỗi upload ảnh: " + e.getMessage());
                }
            }
        }
    }

    public void softDeleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStatus("INACTIVE");
        productRepository.save(product);
    }

    public void updateProduct(Integer id, Product updatedProduct, MultipartFile[] images) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setStatus(updatedProduct.getStatus());
        existingProduct.setCreatedat(Instant.now());

        if (updatedProduct.getCategoryid() != null) {
            Category category = categoryRepository.findById(updatedProduct.getCategoryid().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingProduct.setCategoryid(category);
        }

        Product saved = productRepository.save(existingProduct);

        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                try {
                    String imageUrl = uploadImageFile.uploadImage(image);
                    Productimage img = new Productimage();
                    img.setProductid(saved);
                    img.setImageurl(imageUrl);
                    imageRepository.save(img);
                } catch (IOException e) {
                    throw new RuntimeException("Lỗi upload ảnh: " + e.getMessage());
                }
            }
        }
    }
}
