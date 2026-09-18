package com.abc.onlinebookstore.controller;

import com.abc.onlinebookstore.dto.AddToCartRequestDTO;
import com.abc.onlinebookstore.dto.CartResponseDTO;
import com.abc.onlinebookstore.dto.UpdateCartRequestDTO;
import com.abc.onlinebookstore.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Shopping Cart", description = "Shopping cart management APIs")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(
            summary = "Add book to cart",
            description = "Add a book to the user's shopping cart"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book added to cart"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("update/items")
    public ResponseEntity<CartResponseDTO> addToCart(
            @Valid @RequestBody AddToCartRequestDTO request) {

        return ResponseEntity.ok(cartService.addToCart(request));
    }

    @Operation(
            summary = "View cart",
            description = "Retrieve the shopping cart for a user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDTO> getCart(
            @PathVariable Long userId) {

        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @Operation(
            summary = "Update cart item",
            description = "Update the quantity of a book in the cart"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart item updated successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @PutMapping("update/items/{cartItemId}")
    public ResponseEntity<CartResponseDTO> updateCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartRequestDTO request) {

        return ResponseEntity.ok(
                cartService.updateCartItem(cartItemId, request)
        );
    }

    @Operation(
            summary = "Remove cart item",
            description = "Remove a book from the shopping cart"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart item removed successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @DeleteMapping("delete/items/{cartItemId}")
    public ResponseEntity<String> removeCartItem(
            @PathVariable Long cartItemId) {

        cartService.removeCartItem(cartItemId);

        return ResponseEntity.ok("Cart item removed successfully");
    }
}