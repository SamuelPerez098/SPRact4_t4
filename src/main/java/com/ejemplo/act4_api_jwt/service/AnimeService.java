package com.ejemplo.act4_api_jwt.service;

import com.ejemplo.act4_api_jwt.dto.AnimeResponse;
import com.ejemplo.act4_api_jwt.repository.AnimeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnimeService {

    private final AnimeRepository animeRepository;

    public AnimeService(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    @Transactional(readOnly = true)
    public List<AnimeResponse> listar() {
        return animeRepository.findAll(Sort.by("titulo")).stream()
                .map(AnimeResponse::desde)
                .toList();
    }
}
