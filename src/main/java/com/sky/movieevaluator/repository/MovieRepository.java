package com.sky.movieevaluator.repository;

import com.sky.movieevaluator.model.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {
    @Query("SELECT m FROM Movie m JOIN m.ratings r GROUP BY m ORDER BY AVG(r.ratingValue) DESC")
    List<Movie> findTopRated(Pageable pageable);
}
