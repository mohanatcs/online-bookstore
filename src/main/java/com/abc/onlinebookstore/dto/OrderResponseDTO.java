package com.abc.onlinebookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponseDTO {

    private Long orderId;
    private Long userId;
    private List<OrderItemResponseDTO> items;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
}