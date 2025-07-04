package com.sky.movieevaluator.integration;

import com.sky.movieevaluator.model.dto.MovieDto;
import com.sky.movieevaluator.model.dto.RatingDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtIntegrationTest {
    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String baseUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void test_accessSecuredEndpoint_noToken() {
        //WHEN
        ResponseEntity<String> resp = restTemplate.getForEntity(baseUrl("/api/ratings"), String.class);

        //THEN
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void test_signup_validJwt() {
        var signupReq = Map.of("email","inttest@example.com","password","password123");
        restTemplate.postForEntity(baseUrl("/api/auth/signup"), signupReq, Void.class);

        ResponseEntity<String> signinResp = restTemplate.postForEntity(
                baseUrl("/api/auth/signin"),
                signupReq,
                String.class
        );
        String token = signinResp.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        var movieReq = Map.of("title","Integration Movie Test");
        ResponseEntity<MovieDto> movieResp = restTemplate.exchange(
                baseUrl("/api/movies"),
                HttpMethod.POST,
                new HttpEntity<>(movieReq, headers),
                MovieDto.class
        );

        var ratingReq = Map.<String,Object>of(
                "movieId",     1L,
                "ratingValue", 5
        );
        ResponseEntity<RatingDto> ratingResp = restTemplate.exchange(
                baseUrl("/api/ratings"),
                HttpMethod.POST,
                new HttpEntity<>(ratingReq, headers),
                RatingDto.class
        );

        assertThat(movieResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(signinResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ratingResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ratingResp.getBody().movieId()).isEqualTo(1L);
        assertThat(ratingResp.getBody().ratingValue()).isEqualTo(5);
    }

    @Test
    void test_access_invalidJwt() {
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("this.is.not.a.valid.token");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> resp = restTemplate.exchange(
                baseUrl("/api/ratings"),
                HttpMethod.GET,
                entity,
                String.class
        );

        //THEN
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
