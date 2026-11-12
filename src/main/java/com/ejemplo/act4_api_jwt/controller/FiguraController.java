package com.ejemplo.act4_api_jwt.controller;

import com.ejemplo.act4_api_jwt.dto.FiguraRequest;
import com.ejemplo.act4_api_jwt.dto.FiguraResponse;
import com.ejemplo.act4_api_jwt.dto.PageResponse;
import com.ejemplo.act4_api_jwt.service.FiguraService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/figuras")
public class FiguraController {

    private final FiguraService figuraService;

    public FiguraController(FiguraService figuraService) {
        this.figuraService = figuraService;
    }

    // GET /api/figuras?page=0&size=10&sort=precio,desc  -> 200 (paginado)
    @GetMapping
    public ResponseEntity<PageResponse<FiguraResponse>> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(PageResponse.de(figuraService.listar(pageable)));
    }

    // GET /api/figuras/{id}  -> 200 | 404
    @GetMapping("/{id}")
    public ResponseEntity<FiguraResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(figuraService.buscar(id));
    }

    // POST /api/figuras  -> 201 + cabecera Location | 400 si los datos son inválidos
    @PostMapping
    public ResponseEntity<FiguraResponse> crear(@Valid @RequestBody FiguraRequest req) {
        FiguraResponse creada = figuraService.crear(req);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.id())
                .toUri();
        return ResponseEntity.created(ubicacion).body(creada);
    }

    // PUT /api/figuras/{id}  -> 200 | 400 | 404
    @PutMapping("/{id}")
    public ResponseEntity<FiguraResponse> actualizar(@PathVariable Long id,
                                                     @Valid @RequestBody FiguraRequest req) {
        return ResponseEntity.ok(figuraService.actualizar(id, req));
    }

    // DELETE /api/figuras/{id}  -> 204 | 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        figuraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
