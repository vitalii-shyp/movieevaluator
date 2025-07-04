package com.sky.movieevaluator.service;

import com.sky.movieevaluator.model.Movie;
import com.sky.movieevaluator.model.Rating;
import com.sky.movieevaluator.model.dto.MovieDto;
import com.sky.movieevaluator.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    void test_listAll() {
        //GIVEN
        Rating r1 = new Rating();
        r1.setId(1L);
        r1.setRatingValue(1);
        Movie m1 = new Movie(1L, "A", List.of(r1));

        Rating r2 = new Rating();
        r2.setId(1L);
        r2.setRatingValue(5);
        Rating r3 = new Rating();
        r3.setId(1L);
        r3.setRatingValue(4);
        Movie m2 = new Movie(2L, "B", List.of(r2, r3));

        when(movieRepository.findAll()).thenReturn(List.of(m1, m2));

        //WHEN
        List<MovieDto> result = movieService.listAll();

        //THEN
        MovieDto dto1 = result.get(0);
        assertThat(dto1.id()).isEqualTo(1L);
        assertThat(dto1.title()).isEqualTo("A");
        assertThat(dto1.averageRating()).isEqualTo(1);

        MovieDto dto2 = result.get(1);
        assertThat(dto2.id()).isEqualTo(2L);
        assertThat(dto2.title()).isEqualTo("B");
        assertThat(dto2.averageRating()).isEqualTo(4.5);

        verify(movieRepository).findAll();
    }

    @Test
    void test_listAll_returnsEmptyListWhenNoMovies() {
        //GIVEN
        when(movieRepository.findAll()).thenReturn(List.of());

        //WHEN
        List<MovieDto> result = movieService.listAll();

        //THEN
        assertThat(result).isEmpty();
        verify(movieRepository).findAll();
    }

    @Test
    void test_listTopRated_returnsTopRatedMovieDtos() {
        //GIVEN
        int topRated = 3;
        Rating r1 = new Rating();
        r1.setId(1L);
        r1.setRatingValue(1);
        Rating r2 = new Rating();
        r2.setId(2L);
        r2.setRatingValue(4);
        Rating r3 = new Rating();
        r3.setId(3L);
        r3.setRatingValue(2);
        Rating r4 = new Rating();
        r4.setId(4L);
        r4.setRatingValue(2);

        Movie m1 = new Movie(1L, "Top Rated", List.of(r1, r2));
        Movie m2 = new Movie(2L, "Top Rated", List.of(r3, r4));
        when(movieRepository.findTopRated(PageRequest.of(0, topRated))).thenReturn(List.of(m1, m2));

        //WHEN
        List<MovieDto> result = movieService.listTopRated(topRated);

        //THEN
        assertThat(result).hasSize(2)
                .first()
                .satisfies(dto -> {
                    assertThat(dto.id()).isEqualTo(1L);
                    assertThat(dto.title()).isEqualTo("Top Rated");
                    assertThat(dto.averageRating()).isEqualTo(2.5);
                });

        assertThat(result).element(1)
                .satisfies(dto -> {
                    assertThat(dto.id()).isEqualTo(2L);
                    assertThat(dto.title()).isEqualTo("Top Rated");
                    assertThat(dto.averageRating()).isEqualTo(2.0);
                });

        verify(movieRepository).findTopRated(PageRequest.of(0, topRated));
    }

}