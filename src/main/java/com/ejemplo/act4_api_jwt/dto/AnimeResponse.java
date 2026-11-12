package com.ejemplo.act4_api_jwt.dto;

import com.ejemplo.act4_api_jwt.entity.Anime;

public record AnimeResponse(Long id, String titulo) {

    public static AnimeResponse desde(Anime a) {
        return new AnimeResponse(a.getId(), a.getTitulo());
    }
}
