package com.abc.onlinebookstore.service;

import com.abc.onlinebookstore.dto.AddToCartRequestDTO;
import com.abc.onlinebookstore.dto.CartResponseDTO;
import com.abc.onlinebookstore.dto.UpdateCartRequestDTO;

public interface CartService {

    CartResponseDTO addToCart(AddToCartRequestDTO request);

    CartResponseDTO getCart(Long userId);

    CartResponseDTO updateCartItem(Long cartItemId, UpdateCartRequestDTO request);

    void removeCartItem(Long cartItemId);
}