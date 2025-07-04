package com.sky.movieevaluator.service;

import com.sky.movieevaluator.model.Movie;
import com.sky.movieevaluator.model.Rating;
import com.sky.movieevaluator.model.User;
import com.sky.movieevaluator.model.dto.RatingDto;
import com.sky.movieevaluator.repository.MovieRepository;
import com.sky.movieevaluator.repository.RatingRepository;
import com.sky.movieevaluator.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {
    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RatingService ratingService;

    @Test
    void test_addOrUpdate_updatesExistingRating() {
        //GIVEN
        Long userId = 10L;
        Long movieId = 20L;
        int newValue = 5;

        User user = new User();
        user.setId(userId);
        Movie movie = new Movie();
        movie.setId(movieId);
        Rating existing = Rating.builder()
                .id(99L)
                .user(user)
                .movie(movie)
                .ratingValue(1)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(ratingRepository.findByUserAndMovie(user, movie)).thenReturn(Optional.of(existing));
        when(ratingRepository.save(any(Rating.class))).thenAnswer(inv -> inv.getArgument(0));

        //WHEN
        RatingDto dto = ratingService.addOrUpdate(userId, movieId, newValue);

        //THEN
        assertThat(dto.id()).isEqualTo(99L);
        assertThat(dto.userId()).isEqualTo(userId);
        assertThat(dto.movieId()).isEqualTo(movieId);
        assertThat(dto.ratingValue()).isEqualTo(newValue);

        verify(ratingRepository).findByUserAndMovie(user, movie);
    }

    @Test
    void test_addOrUpdate_createsNewRating() {
        //GIVEN
        Long userId = 11L;
        Long movieId = 21L;
        int value = 3;

        User user = new User();
        user.setId(userId);
        Movie movie = new Movie();
        movie.setId(movieId);
        Rating saved = Rating.builder()
                .id(100L)
                .user(user)
                .movie(movie)
                .ratingValue(value)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(ratingRepository.findByUserAndMovie(user, movie)).thenReturn(Optional.empty());
        when(ratingRepository.save(any(Rating.class))).thenReturn(saved);

        //WHEN
        RatingDto dto = ratingService.addOrUpdate(userId, movieId, value);

        //THEN
        assertThat(dto.id()).isEqualTo(100L);
        assertThat(dto.userId()).isEqualTo(userId);
        assertThat(dto.movieId()).isEqualTo(movieId);
        assertThat(dto.ratingValue()).isEqualTo(value);

        verify(ratingRepository).findByUserAndMovie(user, movie);
    }

    @Test
    void test_delete_deletesOwnRating() {
        //GIVEN
        Long ratingId = 50L;
        Long userId = 5L;
        User user = new User(); user.setId(userId);
        Rating rating = Rating.builder().id(ratingId).user(user).build();

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));

        //WHEN
        ratingService.delete(ratingId, userId);

        //THEN
        verify(ratingRepository).delete(rating);
    }

    @Test
    void test_delete_throwsWhenNotOwner() {
        //GIVEN
        Long ratingId = 51L;
        Long ownerId = 6L;
        Long otherId = 7L;
        User owner = new User(); owner.setId(ownerId);
        Rating rating = Rating.builder().id(ratingId).user(owner).build();

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));

        //WHEN / THEN
        assertThatThrownBy(() -> ratingService.delete(ratingId, otherId))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Cannot delete another user’s rating");

        verify(ratingRepository, never()).delete(any());
    }
}