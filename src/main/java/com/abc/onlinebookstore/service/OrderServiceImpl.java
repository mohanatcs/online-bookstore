package com.abc.onlinebookstore.service;

import com.abc.onlinebookstore.dto.CreateOrderRequestDTO;
import com.abc.onlinebookstore.dto.OrderItemResponseDTO;
import com.abc.onlinebookstore.dto.OrderResponseDTO;
import com.abc.onlinebookstore.entity.BookEntity;
import com.abc.onlinebookstore.entity.CartEntity;
import com.abc.onlinebookstore.entity.CartItemEntity;
import com.abc.onlinebookstore.entity.OrderEntity;
import com.abc.onlinebookstore.entity.OrderItemEntity;
import com.abc.onlinebookstore.entity.UserEntity;
import com.abc.onlinebookstore.exception.CartNotFoundException;
import com.abc.onlinebookstore.exception.OrderNotFoundException;
import com.abc.onlinebookstore.exception.UserNotFoundException;
import com.abc.onlinebookstore.repository.CartItemRepository;
import com.abc.onlinebookstore.repository.CartRepository;
import com.abc.onlinebookstore.repository.OrderItemRepository;
import com.abc.onlinebookstore.repository.OrderRepository;
import com.abc.onlinebookstore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            CartRepository cartRepository,
                            CartItemRepository cartItemRepository,
                            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public OrderResponseDTO createOrder(CreateOrderRequestDTO request) {

        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        CartEntity cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new CartNotFoundException("Cart not found"));

        List<CartItemEntity> cartItems = cartItemRepository.findAll()
                .stream()
                .filter(item ->
                        item.getCart().getId().equals(cart.getId()))
                .toList();

        BigDecimal totalPrice = cartItems.stream()
                .map(item ->
                        item.getBook()
                                .getPrice()
                                .multiply(
                                        BigDecimal.valueOf(
                                                item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setTotalPrice(totalPrice);
        order.setOrderDate(LocalDateTime.now());

        OrderEntity savedOrder = orderRepository.save(order);

        List<OrderItemResponseDTO> items = cartItems.stream()
                .map(item -> {

                    BigDecimal itemTotal =
                            item.getBook()
                                    .getPrice()
                                    .multiply(
                                            BigDecimal.valueOf(
                                                    item.getQuantity()));

                    OrderItemEntity orderItem =
                            new OrderItemEntity();

                    orderItem.setOrder(savedOrder);
                    orderItem.setBook(item.getBook());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setPrice(item.getBook().getPrice());

                    orderItemRepository.save(orderItem);

                    return new OrderItemResponseDTO(
                            item.getBook().getId(),
                            item.getBook().getTitle(),
                            item.getQuantity(),
                            item.getBook().getPrice(),
                            itemTotal
                    );
                })
                .toList();

        // Clear cart after successful checkout
        cartItems.forEach(cartItem ->
                cartItemRepository.delete(cartItem));

        return new OrderResponseDTO(
                savedOrder.getId(),
                user.getId(),
                items,
                totalPrice,
                savedOrder.getOrderDate()
        );
    }

    @Override
    public OrderResponseDTO getOrderById(Long orderId) {

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found"));

        List<OrderItemResponseDTO> items =
                orderItemRepository.findAll()
                        .stream()
                        .filter(item ->
                                item.getOrder()
                                        .getId()
                                        .equals(orderId))
                        .map(item -> {

                            BigDecimal itemTotal =
                                    item.getPrice()
                                            .multiply(
                                                    BigDecimal.valueOf(
                                                            item.getQuantity()));

                            return new OrderItemResponseDTO(
                                    item.getBook().getId(),
                                    item.getBook().getTitle(),
                                    item.getQuantity(),
                                    item.getPrice(),
                                    itemTotal
                            );
                        })
                        .toList();

        return new OrderResponseDTO(
                order.getId(),
                order.getUser().getId(),
                items,
                order.getTotalPrice(),
                order.getOrderDate()
        );
    }

    @Override
    public List<OrderResponseDTO> getOrdersByUser(Long userId) {

        return orderRepository.findByUserId(userId)
                .stream()
                .map(order -> {

                    List<OrderItemResponseDTO> items =
                            orderItemRepository.findAll()
                                    .stream()
                                    .filter(item ->
                                            item.getOrder()
                                                    .getId()
                                                    .equals(order.getId()))
                                    .map(item -> {

                                        BigDecimal itemTotal =
                                                item.getPrice()
                                                        .multiply(
                                                                BigDecimal.valueOf(
                                                                        item.getQuantity()));

                                        return new OrderItemResponseDTO(
                                                item.getBook().getId(),
                                                item.getBook().getTitle(),
                                                item.getQuantity(),
                                                item.getPrice(),
                                                itemTotal
                                        );
                                    })
                                    .toList();

                    return new OrderResponseDTO(
                            order.getId(),
                            order.getUser().getId(),
                            items,
                            order.getTotalPrice(),
                            order.getOrderDate()
                    );
                })
                .toList();
    }
}