package com.sky.movieevaluator.repository;

import com.sky.movieevaluator.model.Movie;
import com.sky.movieevaluator.model.Rating;
import com.sky.movieevaluator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {
    Optional<Rating> findByUserAndMovie(User user, Movie movie);
}
