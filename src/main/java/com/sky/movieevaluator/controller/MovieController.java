package com.sky.movieevaluator.controller;

import com.sky.movieevaluator.model.dto.MovieDto;
import com.sky.movieevaluator.model.dto.MovieRequest;
import com.sky.movieevaluator.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public List<MovieDto> listAll() {
        return movieService.listAll();
    }

    @GetMapping("/top")
    public List<MovieDto> listTopRated(@RequestParam(name="limit", defaultValue="5") int limit) {
        return movieService.listTopRated(limit);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieDto create(@RequestBody @Valid MovieRequest req) {
        return movieService.createMovie(req);
    }
}
