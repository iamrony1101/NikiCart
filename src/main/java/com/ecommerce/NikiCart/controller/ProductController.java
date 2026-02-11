package com.ecommerce.NikiCart.controller;

import com.ecommerce.NikiCart.DTO.CategoryResponse;
import com.ecommerce.NikiCart.DTO.ProductDTO;
import com.ecommerce.NikiCart.DTO.ProductResponse;
import com.ecommerce.NikiCart.model.Product;
import com.ecommerce.NikiCart.service.CategoryService;
import com.ecommerce.NikiCart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId,
                                                 @RequestBody Product product) {
      ProductDTO productDTO=  productService.addProduct(categoryId,product);
      return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(){
       ProductResponse allProduct= productService.getAllProducts();
        return new ResponseEntity<>(allProduct,HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId){
        ProductResponse productResponse= productService.searchByCategory(categoryId);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }



}
