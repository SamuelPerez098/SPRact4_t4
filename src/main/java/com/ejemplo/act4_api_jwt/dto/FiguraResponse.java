package com.ejemplo.act4_api_jwt.dto;

import com.ejemplo.act4_api_jwt.entity.Figura;

/** Lo que la API devuelve: incluye el anime completo (relación), no solo su id. */
public record FiguraResponse(Long id, String personaje, Double precio, AnimeResponse anime) {

    public static FiguraResponse desde(Figura f) {
        return new FiguraResponse(f.getId(), f.getPersonaje(), f.getPrecio(), AnimeResponse.desde(f.getAnime()));
    }
}
