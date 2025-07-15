package org.example.ecommerce.controller.admin;

import org.example.ecommerce.common.dto.category_content.CategoryManagementDTO;


import org.example.ecommerce.common.dto.category_content.CategoryUpdateDTO;
import org.example.ecommerce.common.dto.category_content.ParentCategoryDTO;
import org.example.ecommerce.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @PostMapping("/add")
    public void addCategory(@RequestBody CategoryManagementDTO category) throws IOException {


            categoryService.add(category,"add");

    }
    @PostMapping("/update")
    public void updateCategory(@RequestBody CategoryManagementDTO category) throws IOException {


        categoryService.add(category,"update");

    }
    @GetMapping("/data")
    public Page<CategoryManagementDTO> getCategory(@RequestParam(defaultValue = "0")  int page,
                                                   @RequestParam(defaultValue = "10")  int size) throws IOException {
        System.out.println("getCategory---------------------------------"+categoryService.getCategory(page, size));
        return categoryService.getCategory(page, size);
    }
    @GetMapping("/parent-list")
    public List<ParentCategoryDTO> getParentCategory(){
        return categoryService.getAllParentCategories();
    }

    @PostMapping("/setStatusInactive")
    public void setStatusInactive(@RequestBody CategoryUpdateDTO category) {
        categoryService.updateStatus(category.getId(), category.getStatus());
    }
    @PostMapping("/setStatusActive")
    public void setStatusActive(@RequestBody CategoryUpdateDTO category)  {
        categoryService.updateStatus(category.getId(), category.getStatus());
    }
    @PostMapping("/delete")
    public void deleteCategory(@RequestBody CategoryManagementDTO category)  {
        categoryService.delete(category.getId());
    }

}
