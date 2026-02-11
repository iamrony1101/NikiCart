package com.ecommerce.NikiCart.service;

import com.ecommerce.NikiCart.DTO.ProductDTO;
import com.ecommerce.NikiCart.DTO.ProductResponse;
import com.ecommerce.NikiCart.model.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    ProductDTO addProduct(Long categoryId, Product product);

    ProductResponse getAllProducts();

    ProductResponse searchByCategory(Long categoryId);
    ProductResponse getProductsByKeyword(String keyword);

    ProductDTO updateProduct(Long productId,Product product);

    ProductDTO deleteProduct(Long productId);
}
