package com.tcc.logica.config;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Single error shape for every API failure. {@code position} is only set for
 * formula syntax errors (character offset into the submitted formula); it's
 * omitted from the JSON entirely for every other kind of error.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(int status, String message, Integer position) {

    public ApiError(int status, String message) {
        this(status, message, null);
    }
}
