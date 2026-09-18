package com.abc.onlinebookstore.repository;

import com.abc.onlinebookstore.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    Optional<CartItemEntity> findByCartIdAndBookId(Long cartId, Long bookId);
}