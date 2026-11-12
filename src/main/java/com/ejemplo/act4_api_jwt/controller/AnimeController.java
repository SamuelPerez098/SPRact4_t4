package com.ejemplo.act4_api_jwt.controller;

import com.ejemplo.act4_api_jwt.dto.AnimeResponse;
import com.ejemplo.act4_api_jwt.service.AnimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Catálogo de animes (solo lectura) para conocer los ids que se usan al crear figuras. */
@RestController
@RequestMapping("/api/animes")
public class AnimeController {

    private final AnimeService animeService;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @GetMapping
    public ResponseEntity<List<AnimeResponse>> listar() {
        return ResponseEntity.ok(animeService.listar());
    }
}
