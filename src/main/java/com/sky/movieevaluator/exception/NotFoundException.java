package com.sky.movieevaluator.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String resource, String field, Object value) {
        super(String.format("%s not found with %s = %s", resource, field, value));
    }
}
