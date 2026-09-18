package com.abc.onlinebookstore.service;

import com.abc.onlinebookstore.dto.AddToCartRequestDTO;
import com.abc.onlinebookstore.dto.CartResponseDTO;
import com.abc.onlinebookstore.dto.UpdateCartRequestDTO;
import com.abc.onlinebookstore.entity.BookEntity;
import com.abc.onlinebookstore.entity.CartEntity;
import com.abc.onlinebookstore.entity.CartItemEntity;
import com.abc.onlinebookstore.entity.UserEntity;
import com.abc.onlinebookstore.exception.BookNotFoundException;
import com.abc.onlinebookstore.exception.CartItemNotFoundException;
import com.abc.onlinebookstore.exception.CartNotFoundException;
import com.abc.onlinebookstore.exception.UserNotFoundException;
import com.abc.onlinebookstore.repository.BookRepository;
import com.abc.onlinebookstore.repository.CartItemRepository;
import com.abc.onlinebookstore.repository.CartRepository;
import com.abc.onlinebookstore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void shouldAddBookToCart() {

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("mohana");

        BookEntity book = new BookEntity();
        book.setId(2L);
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setPrice(new BigDecimal("699.00"));

        AddToCartRequestDTO request = new AddToCartRequestDTO();
        request.setUserId(1L);
        request.setBookId(2L);
        request.setQuantity(2);

        CartEntity cart = new CartEntity();
        cart.setId(1L);
        cart.setUser(user);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookRepository.findById(2L))
                .thenReturn(Optional.of(book));

        when(cartRepository.findByUserId(1L))
                .thenReturn(Optional.empty(), Optional.of(cart));

        when(cartRepository.save(any(CartEntity.class)))
                .thenReturn(cart);

        when(cartItemRepository.findByCartIdAndBookId(1L, 2L))
                .thenReturn(Optional.empty());

        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        when(cartItemRepository.save(any(CartItemEntity.class)))
                .thenReturn(cartItem);

        // Execute
        cartService.addToCart(request);

        // Verify
        verify(cartItemRepository).save(any(CartItemEntity.class));
    }
    @Test
    void shouldGetCart() {

        UserEntity user = new UserEntity();
        user.setId(1L);

        BookEntity book = new BookEntity();
        book.setId(2L);
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setPrice(new BigDecimal("699.00"));

        CartEntity cart = new CartEntity();
        cart.setId(1L);
        cart.setUser(user);

        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        when(cartRepository.findByUserId(1L))
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findAll())
                .thenReturn(List.of(cartItem));

        // Execute
        CartResponseDTO response = cartService.getCart(1L);

        // Verify
        assertEquals(1L, response.getCartId());
        assertEquals(1L, response.getUserId());
        assertEquals(1, response.getItems().size());
        assertEquals(2, response.getItems().get(0).getQuantity());
        assertEquals(new BigDecimal("1398.00"), response.getTotalPrice());
    }
    @Test
    void shouldUpdateCartItem() {

        UserEntity user = new UserEntity();
        user.setId(1L);

        CartEntity cart = new CartEntity();
        cart.setId(1L);
        cart.setUser(user);

        BookEntity book = new BookEntity();
        book.setId(2L);
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setPrice(new BigDecimal("699.00"));

        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        UpdateCartRequestDTO request = new UpdateCartRequestDTO();
        request.setQuantity(5);

        when(cartItemRepository.findById(1L))
                .thenReturn(Optional.of(cartItem));

        when(cartItemRepository.save(any(CartItemEntity.class)))
                .thenReturn(cartItem);

        when(cartRepository.findByUserId(1L))
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findAll())
                .thenReturn(List.of(cartItem));

        CartResponseDTO response =
                cartService.updateCartItem(1L, request);

        assertEquals(5, cartItem.getQuantity());
        assertEquals(5, response.getItems().get(0).getQuantity());

        verify(cartItemRepository).save(cartItem);
    }
    @Test
    void shouldRemoveCartItem() {

        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setId(1L);

        when(cartItemRepository.findById(1L))
                .thenReturn(Optional.of(cartItem));

        cartService.removeCartItem(1L);

        verify(cartItemRepository).delete(cartItem);
    }
    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        AddToCartRequestDTO request = new AddToCartRequestDTO();
        request.setUserId(99L);
        request.setBookId(1L);
        request.setQuantity(1);

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> cartService.addToCart(request)
        );
    }
    @Test
    void shouldThrowExceptionWhenBookNotFound() {

        UserEntity user = new UserEntity();
        user.setId(1L);

        AddToCartRequestDTO request = new AddToCartRequestDTO();
        request.setUserId(1L);
        request.setBookId(99L);
        request.setQuantity(1);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> cartService.addToCart(request)
        );
    }
    @Test
    void shouldThrowExceptionWhenCartItemNotFound() {

        when(cartItemRepository.findById(99L))
                .thenReturn(Optional.empty());

        UpdateCartRequestDTO request = new UpdateCartRequestDTO();
        request.setQuantity(5);

        assertThrows(
                CartItemNotFoundException.class,
                () -> cartService.updateCartItem(99L, request)
        );
    }
    @Test
    void shouldThrowExceptionWhenCartNotFound() {

        when(cartRepository.findByUserId(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                CartNotFoundException.class,
                () -> cartService.getCart(99L)
        );
    }
}