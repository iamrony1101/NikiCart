package com.ecommerce.NikiCart.controller;

import com.ecommerce.NikiCart.DTO.CategoryDTO;
import com.ecommerce.NikiCart.DTO.CategoryResponse;
import com.ecommerce.NikiCart.config.AppConstants;
import com.ecommerce.NikiCart.model.Category;
import com.ecommerce.NikiCart.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
      CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
      return new ResponseEntity<>(categoryResponse,HttpStatus.OK);
    }

    @PostMapping("/public/create")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO saveCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(saveCategory,HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id){
       CategoryDTO status = categoryService.deleteCategory(id);
        return new ResponseEntity<>(status, HttpStatus.OK);
        }

    @PutMapping("/public/updatecategory/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @PathVariable Long id, @RequestBody CategoryDTO categoryDTO){
      CategoryDTO updateCategoryDTO=  categoryService.updateCategory(id,categoryDTO);
      return new ResponseEntity<>(updateCategoryDTO,HttpStatus.OK);
    }
}
