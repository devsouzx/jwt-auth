package com.devsouzx.auth.domain.product;

import jakarta.annotation.Nonnull;

public record ProductRequestDTO(
        @Nonnull
        String name,

        @Nonnull
        Integer price
) {
}