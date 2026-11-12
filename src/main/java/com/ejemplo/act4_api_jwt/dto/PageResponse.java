package com.ejemplo.act4_api_jwt.dto;

import org.springframework.data.domain.Page;

import java.util.List;

/** Respuesta paginada con un formato estable (no se expone PageImpl directamente). */
public record PageResponse<T>(
        List<T> contenido,
        int pagina,
        int tamano,
        long totalElementos,
        int totalPaginas,
        boolean ultima) {

    public static <T> PageResponse<T> de(Page<T> p) {
        return new PageResponse<>(p.getContent(), p.getNumber(), p.getSize(),
                p.getTotalElements(), p.getTotalPages(), p.isLast());
    }
}
