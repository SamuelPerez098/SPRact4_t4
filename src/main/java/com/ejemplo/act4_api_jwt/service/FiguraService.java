package com.ejemplo.act4_api_jwt.service;

import com.ejemplo.act4_api_jwt.dto.FiguraRequest;
import com.ejemplo.act4_api_jwt.dto.FiguraResponse;
import com.ejemplo.act4_api_jwt.entity.Anime;
import com.ejemplo.act4_api_jwt.entity.Figura;
import com.ejemplo.act4_api_jwt.exception.RecursoNoEncontradoException;
import com.ejemplo.act4_api_jwt.exception.SolicitudInvalidaException;
import com.ejemplo.act4_api_jwt.repository.AnimeRepository;
import com.ejemplo.act4_api_jwt.repository.FiguraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class FiguraService {

    private static final Set<String> CAMPOS_ORDENABLES = Set.of("id", "personaje", "precio");

    private final FiguraRepository figuraRepository;
    private final AnimeRepository animeRepository;

    public FiguraService(FiguraRepository figuraRepository, AnimeRepository animeRepository) {
        this.figuraRepository = figuraRepository;
        this.animeRepository = animeRepository;
    }

    @Transactional(readOnly = true)
    public Page<FiguraResponse> listar(Pageable pageable) {
        for (Sort.Order orden : pageable.getSort()) {
            if (!CAMPOS_ORDENABLES.contains(orden.getProperty())) {
                throw new SolicitudInvalidaException("No se puede ordenar por '" + orden.getProperty()
                        + "'. Campos permitidos: " + CAMPOS_ORDENABLES);
            }
        }
        return figuraRepository.findAll(pageable).map(FiguraResponse::desde);
    }

    @Transactional(readOnly = true)
    public FiguraResponse buscar(Long id) {
        return FiguraResponse.desde(obtener(id));
    }

    @Transactional
    public FiguraResponse crear(FiguraRequest req) {
        Figura figura = new Figura(req.personaje().trim(), req.precio(), buscarAnime(req.animeId()));
        return FiguraResponse.desde(figuraRepository.save(figura));
    }

    @Transactional
    public FiguraResponse actualizar(Long id, FiguraRequest req) {
        Figura figura = obtener(id);
        figura.setPersonaje(req.personaje().trim());
        figura.setPrecio(req.precio());
        figura.setAnime(buscarAnime(req.animeId()));
        return FiguraResponse.desde(figuraRepository.save(figura));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!figuraRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("No existe una figura con id " + id);
        }
        figuraRepository.deleteById(id);
    }

    private Figura obtener(Long id) {
        return figuraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una figura con id " + id));
    }

    private Anime buscarAnime(Long animeId) {
        return animeRepository.findById(animeId)
                .orElseThrow(() -> new SolicitudInvalidaException("El anime con id " + animeId + " no existe"));
    }
}
