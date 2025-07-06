package org.example.ecommerce.controller.seller.product;

import org.example.ecommerce.dto.CategoryDTO;
import org.example.ecommerce.entity.Category;
import org.example.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/root")
    public ResponseEntity<List<CategoryDTO>> getRootCategories() {
        List<Category> rootCategories = categoryService.getRootCategories();
        List<CategoryDTO> dtoList = categoryService.convertToDTOList(rootCategories);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/children/{parentId}")
    public ResponseEntity<List<CategoryDTO>> getChildCategories(@PathVariable Integer parentId) {
        List<Category> childCategories = categoryService.getChildCategories(parentId);
        List<CategoryDTO> dtoList = categoryService.convertToDTOList(childCategories);
        return ResponseEntity.ok(dtoList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Integer id) {
        Category category = categoryService.getCategoryWithParentPath(id);
        if (category != null) {
            CategoryDTO dto = categoryService.convertToDTO(category);
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> allCategories = categoryService.getAllCategoriesForFrontend();
        return ResponseEntity.ok(allCategories);
    }
}
