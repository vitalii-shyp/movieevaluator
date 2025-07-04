package com.sky.movieevaluator.service;

import com.sky.movieevaluator.exception.NotFoundException;
import com.sky.movieevaluator.model.Rating;
import com.sky.movieevaluator.model.dto.RatingDto;
import com.sky.movieevaluator.repository.MovieRepository;
import com.sky.movieevaluator.repository.RatingRepository;
import com.sky.movieevaluator.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public RatingDto addOrUpdate(Long userId, Long movieId, int ratingValue) {
        var user  = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", "id", userId));
        var movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie", "id", movieId));

        var rating = ratingRepository.findByUserAndMovie(user, movie)
                .map(r -> {
                    r.setRatingValue(ratingValue);
                    r.setRatedAt(Instant.now());
                    return r;
                })
                .orElseGet(() -> Rating.builder()
                        .user(user)
                        .movie(movie)
                        .ratingValue(ratingValue)
                        .build());

        var saved = ratingRepository.save(rating);
        return new RatingDto(
                saved.getId(),
                movieId,
                userId,
                saved.getRatingValue()
        );
    }

    public void delete(Long ratingId, Long userId) {
        var rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found"));
        if (!rating.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Cannot delete another user’s rating");
        }
        ratingRepository.delete(rating);
    }
}
