package com.codepanthers.product_service.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductRequest(@NotBlank(message = "product name is required") String name, @NotBlank(message = "product price is required") Double price) {
}
