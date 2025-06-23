package org.example.ecommerce.service.seller;


import org.example.ecommerce.entity.Category;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.Productimage;
import org.example.ecommerce.entity.Shop;
import org.example.ecommerce.repository.CategoryRepository;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.repository.ProductimageRepository;
import org.example.ecommerce.service.FileSystemStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private ProductimageRepository imageRepository;

    @Autowired private FileSystemStorageService storageService;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchByKeyword(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }


    public Product getById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public void save(Product product, MultipartFile[] images) {

        if (product.getStatus() == null || product.getStatus().isEmpty()) {
            product.setStatus("available");
        }


        if (product.getCreatedat() == null) {
            product.setCreatedat(Instant.now());
        }

        // Gán danh mục (nếu chỉ có categoryid.id)
        if (product.getCategoryid() != null) {
            Category category = categoryRepository.findById(product.getCategoryid().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategoryid(category);
        }

        // Lưu sản phẩm
        Product saved = productRepository.save(product);

        // Lưu ảnh
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                storageService.store(image);
                Productimage img = new Productimage();
                img.setProductid(saved);
                img.setImageurl(image.getOriginalFilename());
                imageRepository.save(img);
            }
        }
    }



}
