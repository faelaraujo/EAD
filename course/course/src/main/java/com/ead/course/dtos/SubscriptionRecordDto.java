package com.ead.course.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SubscriptionRecordDto(@NotNull(message = "Userid is mandatory")UUID userId) {
}
