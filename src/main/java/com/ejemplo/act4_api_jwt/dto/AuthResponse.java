package com.ejemplo.act4_api_jwt.dto;

public record AuthResponse(
        String token,
        String tipo,
        long expiraEnSegundos,
        String email,
        String nombre) {
}
