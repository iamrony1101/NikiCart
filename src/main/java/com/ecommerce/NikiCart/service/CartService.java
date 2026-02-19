package com.ecommerce.NikiCart.service;

import com.ecommerce.NikiCart.DTO.CartDTO;

public interface CartService {

    CartDTO addProductToCart(Long productId, Integer quantity);
}
