package com.sky.movieevaluator.model.dto;

import jakarta.validation.constraints.NotBlank;

public record MovieRequest(
        @NotBlank String title
) { }
