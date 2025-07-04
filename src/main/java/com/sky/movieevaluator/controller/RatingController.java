package com.sky.movieevaluator.controller;

import com.sky.movieevaluator.model.dto.RatingDto;
import com.sky.movieevaluator.model.dto.RatingRequest;
import com.sky.movieevaluator.repository.UserRepository;
import com.sky.movieevaluator.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final UserRepository userRepository;

    @PostMapping
    public RatingDto addOrUpdate(@RequestBody @Valid RatingRequest req) {
        return ratingService.addOrUpdate(
                currentUserId(),
                req.movieId(),
                req.ratingValue()
        );
    }

    @DeleteMapping("/{ratingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long ratingId) {
        ratingService.delete(ratingId, currentUserId());
    }

    private Long currentUserId() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email))
                .getId();
    }
}
