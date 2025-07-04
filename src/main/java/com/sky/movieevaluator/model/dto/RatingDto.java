package com.sky.movieevaluator.model.dto;

public record RatingDto(
        Long id,
        Long movieId,
        Long userId,
        Integer ratingValue) {}
