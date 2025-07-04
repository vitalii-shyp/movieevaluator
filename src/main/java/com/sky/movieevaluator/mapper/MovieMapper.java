package com.sky.movieevaluator.mapper;

import com.sky.movieevaluator.model.Movie;
import com.sky.movieevaluator.model.Rating;
import com.sky.movieevaluator.model.dto.MovieDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mapping(target = "averageRating", expression = "java(calculateAverage(movie))")
    MovieDto toDto(Movie movie);

    default Double calculateAverage(Movie movie) {
        return movie.getRatings().stream()
                .mapToInt(Rating::getRatingValue)
                .average()
                .orElse(0.0);
    }
}
