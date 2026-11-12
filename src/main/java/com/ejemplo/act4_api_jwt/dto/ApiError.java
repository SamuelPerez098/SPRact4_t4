package com.ejemplo.act4_api_jwt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

/** Formato único de error para toda la API. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> errores) {

    public static ApiError of(HttpStatus status, String message, String path, Map<String, String> errores) {
        return new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, path, errores);
    }
}
