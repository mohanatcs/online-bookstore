package com.abc.onlinebookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CartItemResponseDTO {

    private Long bookId;
    private String title;
    private String author;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
}