package com.sky.movieevaluator.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RatingRequest(
        @NotNull Long movieId,
        @Min(1) @Max(5) int ratingValue
) {}
