package com.abc.onlinebookstore.repository;

import com.abc.onlinebookstore.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}