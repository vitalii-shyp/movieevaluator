package com.sky.movieevaluator.service;

import com.sky.movieevaluator.mapper.MovieMapper;
import com.sky.movieevaluator.model.Movie;
import com.sky.movieevaluator.model.Rating;
import com.sky.movieevaluator.model.dto.MovieDto;
import com.sky.movieevaluator.model.dto.MovieRequest;
import com.sky.movieevaluator.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public List<MovieDto> listAll() {
        return movieRepository.findAll().stream()
                .map(m -> new MovieDto(
                        m.getId(),
                        m.getTitle(),
                        m.getRatings().stream()
                                .mapToInt(Rating::getRatingValue)
                                .average().orElse(0.0)
                ))
                .toList();
    }

    public List<MovieDto> listTopRated(int topRated) {
        var movies = movieRepository.findTopRated(PageRequest.of(0, topRated));
        return movies.stream()
                .map(m -> new MovieDto(
                        m.getId(),
                        m.getTitle(),
                        m.getRatings().stream()
                                .mapToInt(Rating::getRatingValue)
                                .average().orElse(0.0)
                ))
                .toList();
    }

    public MovieDto createMovie(MovieRequest req) {
        var movie = Movie.builder()
                .title(req.title())
                .ratings(new ArrayList<>())
                .build();
        var saved = movieRepository.save(movie);
        return movieMapper.toDto(saved);
    }

}
