package com.abc.onlinebookstore.controller;

import com.abc.onlinebookstore.dto.CreateOrderRequestDTO;
import com.abc.onlinebookstore.dto.OrderResponseDTO;
import com.abc.onlinebookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order and checkout APIs")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create order", description = "Create an order from the user's shopping cart")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "404", description = "User or cart not found")
    })
    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody CreateOrderRequestDTO request) {

        OrderResponseDTO response = orderService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get order by ID", description = "Retrieve order details using order ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderService.getOrderById(orderId)
        );
    }

    @Operation(summary = "Get user orders", description = "Retrieve all orders belonging to a user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                orderService.getOrdersByUser(userId)
        );
    }
}