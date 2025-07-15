package org.example.ecommerce.service;


import jakarta.transaction.Transactional;
import org.example.ecommerce.common.dto.category_content.CategoryManagementDTO;
import org.example.ecommerce.common.dto.category_content.ParentCategoryDTO;
import org.example.ecommerce.common.exception.CategoryException;
import org.example.ecommerce.common.mapper.CategoryMapper;
import org.example.ecommerce.entity.Category;
import org.example.ecommerce.repository.CategoryRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;
    private final CategoryMapper categoryMapper;
    private final UploadImageFileImpl uploadImageFile;


    public CategoryService(CategoryRepository categoryRepo, CategoryMapper categoryMapper, UploadImageFileImpl uploadImageFile) {
        this.categoryRepo = categoryRepo;
        this.categoryMapper = categoryMapper;
        this.uploadImageFile = uploadImageFile;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepo.findAll();
        if (categories == null) return List.of();
        return categories.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Category getById(Integer id) {
        return categoryRepo.findById(id).orElse(null);
    }

    public void save(Category category) {
        categoryRepo.save(category);
    }

    //    @Cacheable("categories")
//    public List<CategoryManagementDTO> getCategory() {
//        System.out.println("Querying DB for categories...");
//        return categoryMapper.toDTOs(
//                categoryRepo.findAll(Sort.by(Sort.Direction.ASC, "id"))
//        );
//    }


    public List<ParentCategoryDTO> getAllParentCategories() {
        List<Category> categories = categoryRepo.findAll();
        // lọc ra category không có cha (hoặc tùy bạn muốn giới hạn)
        List<Category> parents = categories.stream()
                .filter(c -> c.getParent() == null) // Hoặc c.getStatus().equals("ACTIVE") nếu muốn
                .collect(Collectors.toList());
        return categoryMapper.toParentDTOs(parents); // map sang DTO nhẹ
    }

    public Page<CategoryManagementDTO> getCategory(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.ASC, "id"));
        Page<Category> categories = categoryRepo.findAll(pageable);
        List<CategoryManagementDTO> categoryManagementDTOS = categoryMapper.toDTOs(categories.getContent());
        return new PageImpl<>(categoryManagementDTOS, pageable, categories.getTotalElements());

    }

    @Transactional
    public void add(CategoryManagementDTO categoryDTO, String type) throws IOException {
        if (type.equalsIgnoreCase("add")) {
            for (Category category : getAllCategories()) {
                if (categoryDTO.getCategoryname().equalsIgnoreCase(category.getCategoryname())) {
                    throw new CategoryException("Tên không được trùng");
                }
            }
        }


        Category category = categoryMapper.toEntity(categoryDTO);
        if (categoryDTO.getParentId() == 0) {
            category.setParent(null);
        }
        if(category.getImage() != null){
            if (category.getImage().contains("base64") ) {
                category.setImage(uploadImageFile.uploadImage(base64ToMultipart(category.getImage())));
            }
        }



        category.setCreate_at(LocalDateTime.now());
        System.out.println("Test--------" + category);
        categoryRepo.save(category);
    }

    private MultipartFile base64ToMultipart(String base64Full) {
        String[] parts = base64Full.split(",");
        String metadata = parts[0]; // ví dụ: data:image/png;base64
        String base64Data = parts[1];

        String contentType = metadata.substring(metadata.indexOf(":") + 1, metadata.indexOf(";")); // image/png
        String extension = contentType.split("/")[1];
        String filename = UUID.randomUUID() + "." + extension;

        byte[] decoded = Base64.getDecoder().decode(base64Data);
        return new MockMultipartFile("file", filename, contentType, decoded);
    }

    public void updateStatus(Integer id, String status) {
        categoryRepo.findById(id).ifPresent(category -> {


            category.setStatus(status);
            categoryRepo.save(category);


        });
    }


    public void delete(Integer id) {
        categoryRepo.deleteById(id);
    }

}
