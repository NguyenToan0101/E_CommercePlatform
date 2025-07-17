package org.example.ecommerce.service;


import jakarta.transaction.Transactional;
import org.example.ecommerce.common.dto.CategoryDTO;
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

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UploadImageFileImpl uploadImageFile;


    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, UploadImageFileImpl uploadImageFile) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.uploadImageFile = uploadImageFile;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories == null) return List.of();
        return categories.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public Category getById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    //    @Cacheable("categories")
//    public List<CategoryManagementDTO> getCategory() {
//        System.out.println("Querying DB for categories...");
//        return categoryMapper.toDTOs(
//                categoryRepo.findAll(Sort.by(Sort.Direction.ASC, "id"))
//        );
//    }


    public List<ParentCategoryDTO> getAllParentCategories() {
        List<Category> categories = categoryRepository.findAll();
        // lọc ra category không có cha (hoặc tùy bạn muốn giới hạn)
        List<Category> parents = categories.stream()
                .filter(c -> c.getParent() == null) // Hoặc c.getStatus().equals("ACTIVE") nếu muốn
                .collect(Collectors.toList());
        return categoryMapper.toParentDTOs(parents); // map sang DTO nhẹ
    }

    public Page<CategoryManagementDTO> getCategory(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.ASC, "id"));
        Page<Category> categories = categoryRepository.findAll(pageable);
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
        categoryRepository.save(category);
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
        categoryRepository.findById(id).ifPresent(category -> {


            category.setStatus(status);
            categoryRepository.save(category);


        });
    }


    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }

    //Phong
    @Cacheable("rootCategories")
    public List<Category> getRootCategories() {
        return categoryRepository.findByParentIsNullWithChildren();
    }


    @Cacheable(value = "childCategories", key = "#parentId")
    public List<Category> getChildCategories(Integer parentId) {
        if (parentId == null) {
            return getRootCategories();
        }
        return categoryRepository.findByParentIdWithChildren(parentId);
    }


    public Category getCategoryWithParentPath(Integer id) {
        return categoryRepository.findByIdWithParent(id).orElse(null);
    }


    public CategoryDTO convertToDTO(Category category) {
        if (category == null) return null;

        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setCategoryname(category.getCategoryname());
        dto.setImage(category.getImage());


        if (category.getParent() != null) {
            try {
                dto.setParentId(category.getParent().getId());
                dto.setParentName(category.getParent().getCategoryname());
            } catch (Exception e) {
            }
        }

        return dto;
    }

    public List<CategoryDTO> convertToDTOList(List<Category> categories) {
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Cacheable("allCategories")
    public List<CategoryDTO> getAllCategoriesForFrontend() {
        List<Category> allCategories = categoryRepository.findAllWithParent();
        return convertToDTOList(allCategories);
    }
    public boolean isCategoryOrSubcategory(Integer parentId, Integer childCategoryId) {
        Category parent = categoryRepository.findById(parentId).orElse(null);
        Category child = categoryRepository.findById(childCategoryId).orElse(null);
        if (parent == null || child == null) return false;
        while (child != null) {
            if (child.getId().equals(parent.getId())) return true;
            child = child.getParent();
        }
        return false;
    }

}
