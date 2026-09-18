package com.abc.onlinebookstore.service;

import com.abc.onlinebookstore.dto.CreateOrderRequestDTO;
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
import com.abc.onlinebookstore.repository.BookRepository;
import com.abc.onlinebookstore.repository.CartItemRepository;
import com.abc.onlinebookstore.repository.CartRepository;
import com.abc.onlinebookstore.repository.OrderItemRepository;
import com.abc.onlinebookstore.repository.OrderRepository;
import com.abc.onlinebookstore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private OrderServiceImpl orderService;


    @Test
    void shouldCreateOrderFromCart() {

        UserEntity user = new UserEntity();
        user.setId(1L);

        BookEntity book = new BookEntity();
        book.setId(2L);
        book.setTitle("Effective Java");
        book.setPrice(new BigDecimal("699.00"));

        CartEntity cart = new CartEntity();
        cart.setId(1L);
        cart.setUser(user);

        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        CreateOrderRequestDTO request = new CreateOrderRequestDTO();
        request.setUserId(1L);

        OrderEntity order = new OrderEntity();
        order.setId(1L);
        order.setUser(user);
        order.setTotalPrice(new BigDecimal("1398.00"));
        order.setOrderDate(LocalDateTime.now());

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUserId(1L))
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findAll())
                .thenReturn(List.of(cartItem));

        when(orderRepository.save(any(OrderEntity.class)))
                .thenReturn(order);

        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setId(1L);
        orderItem.setOrder(order);
        orderItem.setBook(book);
        orderItem.setQuantity(2);
        orderItem.setPrice(new BigDecimal("699.00"));

        when(orderItemRepository.save(any(OrderItemEntity.class)))
                .thenReturn(orderItem);

        OrderResponseDTO response =
                orderService.createOrder(request);

        assertEquals(1L, response.getOrderId());
        assertEquals(1L, response.getUserId());
        assertEquals(
                new BigDecimal("1398.00"),
                response.getTotalPrice()
        );

        verify(orderRepository).save(any(OrderEntity.class));
        verify(orderItemRepository).save(any(OrderItemEntity.class));
    }


    @Test
    void shouldGetOrderById() {

        UserEntity user = new UserEntity();
        user.setId(1L);

        BookEntity book = new BookEntity();
        book.setId(2L);
        book.setTitle("Effective Java");
        book.setPrice(new BigDecimal("699.00"));

        OrderEntity order = new OrderEntity();
        order.setId(1L);
        order.setUser(user);
        order.setTotalPrice(new BigDecimal("1398.00"));
        order.setOrderDate(LocalDateTime.now());

        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setId(1L);
        orderItem.setOrder(order);
        orderItem.setBook(book);
        orderItem.setQuantity(2);
        orderItem.setPrice(new BigDecimal("699.00"));

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(orderItemRepository.findAll())
                .thenReturn(List.of(orderItem));

        OrderResponseDTO response =
                orderService.getOrderById(1L);

        assertEquals(1L, response.getOrderId());
        assertEquals(1L, response.getUserId());
        assertEquals(
                new BigDecimal("1398.00"),
                response.getTotalPrice()
        );
        assertEquals(1, response.getItems().size());
        assertEquals(
                2,
                response.getItems().get(0).getQuantity()
        );
    }


    @Test
    void shouldGetOrdersByUser() {

        UserEntity user = new UserEntity();
        user.setId(1L);

        OrderEntity order = new OrderEntity();
        order.setId(1L);
        order.setUser(user);
        order.setTotalPrice(new BigDecimal("1398.00"));
        order.setOrderDate(LocalDateTime.now());

        when(orderRepository.findByUserId(1L))
                .thenReturn(List.of(order));

        when(orderItemRepository.findAll())
                .thenReturn(List.of());

        List<OrderResponseDTO> response =
                orderService.getOrdersByUser(1L);

        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getOrderId());
        assertEquals(1L, response.get(0).getUserId());
        assertEquals(
                new BigDecimal("1398.00"),
                response.get(0).getTotalPrice()
        );
    }


    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        CreateOrderRequestDTO request =
                new CreateOrderRequestDTO();

        request.setUserId(99L);

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> orderService.createOrder(request)
        );
    }


    @Test
    void shouldThrowExceptionWhenCartNotFound() {

        UserEntity user = new UserEntity();
        user.setId(1L);

        CreateOrderRequestDTO request =
                new CreateOrderRequestDTO();

        request.setUserId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                CartNotFoundException.class,
                () -> orderService.createOrder(request)
        );
    }


    @Test
    void shouldThrowExceptionWhenOrderNotFound() {

        when(orderRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getOrderById(99L)
        );
    }
}