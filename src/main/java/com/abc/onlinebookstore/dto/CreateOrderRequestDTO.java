package com.abc.onlinebookstore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;
}