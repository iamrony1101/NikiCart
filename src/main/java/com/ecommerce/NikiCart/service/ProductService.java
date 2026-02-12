package com.ecommerce.NikiCart.service;

import com.ecommerce.NikiCart.DTO.ProductDTO;
import com.ecommerce.NikiCart.DTO.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    ProductDTO addProduct(Long categoryId, ProductDTO product);

    ProductResponse getAllProducts();

    ProductResponse searchByCategory(Long categoryId);
    ProductResponse getProductsByKeyword(String keyword);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
