package com.ejemplo.act4_api_jwt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Datos que el cliente puede enviar al crear o actualizar una figura. */
public record FiguraRequest(
        @NotBlank(message = "El personaje es obligatorio")
        @Size(max = 100, message = "El personaje no puede pasar de 100 caracteres")
        String personaje,

        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser mayor que 0")
        Double precio,

        @NotNull(message = "El animeId es obligatorio")
        Long animeId) {
}
