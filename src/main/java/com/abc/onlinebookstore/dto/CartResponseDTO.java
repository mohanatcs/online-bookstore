package com.abc.onlinebookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class CartResponseDTO {

    private Long cartId;
    private Long userId;
    private List<CartItemResponseDTO> items;
    private BigDecimal totalPrice;
}