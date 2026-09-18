package com.abc.onlinebookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderItemResponseDTO {

    private Long bookId;
    private String title;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
}