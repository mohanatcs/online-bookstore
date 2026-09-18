package com.abc.onlinebookstore.service;

import com.abc.onlinebookstore.dto.CreateOrderRequestDTO;
import com.abc.onlinebookstore.dto.OrderResponseDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder(CreateOrderRequestDTO request);

    OrderResponseDTO getOrderById(Long orderId);

    List<OrderResponseDTO> getOrdersByUser(Long userId);
}