package com.ejemplo.act4_api_jwt.dto;

import com.ejemplo.act4_api_jwt.entity.Usuario;

/** Datos públicos del usuario. Nunca incluye el password. */
public record UsuarioResponse(Long id, String nombre, String email, String rol) {

    public static UsuarioResponse desde(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNombre(), u.getEmail(), u.getRol());
    }
}
