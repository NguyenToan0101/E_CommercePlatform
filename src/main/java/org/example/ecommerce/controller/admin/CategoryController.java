package org.example.ecommerce.controller.admin;

import org.example.ecommerce.common.dto.admin.productManagement.MainCategoryDTO;
import org.example.ecommerce.common.dto.category_content.CategoryManagementDTO;


import org.example.ecommerce.common.dto.category_content.CategoryUpdateDTO;
import org.example.ecommerce.common.dto.category_content.ParentCategoryDTO;

import org.example.ecommerce.repository.CategoryRepository;
import org.example.ecommerce.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService     categoryService;
    private final CategoryRepository  repo;

    public CategoryController(CategoryService categoryService,
                              CategoryRepository repo) {
        this.categoryService = categoryService;
        this.repo            = repo;
    }

    @PostMapping("/add")
    public void addCategory(@RequestBody CategoryManagementDTO dto) throws IOException {
        categoryService.add(dto, "add");
    }

    @PostMapping("/update")
    public void updateCategory(@RequestBody CategoryManagementDTO dto) throws IOException {
        categoryService.add(dto, "update");
    }

    @GetMapping("/data")
    public Page<CategoryManagementDTO> getCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws IOException {
        return categoryService.getCategory(page, size);
    }

    @GetMapping("/parent-list")
    public List<ParentCategoryDTO> getParentCategory() {
        return categoryService.getAllParentCategories();
    }

    @PostMapping("/setStatusInactive")
    public void setStatusInactive(@RequestBody CategoryUpdateDTO dto) {
        categoryService.updateStatus(dto.getId(), dto.getStatus());
    }

    @PostMapping("/setStatusActive")
    public void setStatusActive(@RequestBody CategoryUpdateDTO dto) {
        categoryService.updateStatus(dto.getId(), dto.getStatus());
    }

    @PostMapping("/delete")
    public void deleteCategory(@RequestBody CategoryManagementDTO dto) {
        categoryService.delete(dto.getId());
    }

    /** Lấy danh mục chính (parentid IS NULL) */
    @GetMapping("/main")
    public List<MainCategoryDTO> getMainCategories() {
        return repo.findByParentIsNull().stream()
                .map(c -> new MainCategoryDTO(
                        c.getId(),
                        c.getCategoryname()
                ))
                .collect(Collectors.toList());
    }

}
