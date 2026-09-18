package com.abc.onlinebookstore.service;

import com.abc.onlinebookstore.dto.AddToCartRequestDTO;
import com.abc.onlinebookstore.dto.CartItemResponseDTO;
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           UserRepository userRepository,
                           BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public CartResponseDTO addToCart(AddToCartRequestDTO request) {

        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        BookEntity book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        CartEntity cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        CartItemEntity cartItem = cartItemRepository
                .findByCartIdAndBookId(cart.getId(), book.getId())
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(
                    cartItem.getQuantity() + request.getQuantity()
            );
        } else {
            cartItem = new CartItemEntity();
            cartItem.setCart(cart);
            cartItem.setBook(book);
            cartItem.setQuantity(request.getQuantity());
        }

        cartItemRepository.save(cartItem);

        return getCart(user.getId());
    }

    @Override
    public CartResponseDTO getCart(Long userId) {

        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        List<CartItemResponseDTO> items = cartItemRepository
                .findAll()
                .stream()
                .filter(item -> item.getCart().getId().equals(cart.getId()))
                .map(this::mapToResponse)
                .toList();

        BigDecimal totalPrice = items.stream()
                .map(CartItemResponseDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDTO(
                cart.getId(),
                userId,
                items,
                totalPrice
        );
    }

    @Override
    public CartResponseDTO updateCartItem(
            Long cartItemId,
            UpdateCartRequestDTO request) {

        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));

        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);

        return getCart(cartItem.getCart().getUser().getId());
    }

    @Override
    public void removeCartItem(Long cartItemId) {

        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));

        cartItemRepository.delete(cartItem);
    }

    private CartItemResponseDTO mapToResponse(CartItemEntity item) {

        BigDecimal totalPrice = item.getBook()
                .getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));

        return new CartItemResponseDTO(
                item.getBook().getId(),
                item.getBook().getTitle(),
                item.getBook().getAuthor(),
                item.getBook().getPrice(),
                item.getQuantity(),
                totalPrice
        );
    }
}