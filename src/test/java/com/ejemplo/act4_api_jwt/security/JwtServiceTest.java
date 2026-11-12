package com.ejemplo.act4_api_jwt.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET = "una-clave-de-prueba-con-mas-de-32-caracteres-123456";

    private final UserDetails usuario = User.withUsername("ana@correo.com")
            .password("x")
            .authorities("ROLE_USER")
            .build();

    @Test
    void generaUnTokenQueSePuedeLeerYValidar() {
        JwtService jwt = new JwtService(SECRET, 60_000);
        String token = jwt.generarToken(usuario);

        assertEquals("ana@correo.com", jwt.extraerEmail(token));
        assertTrue(jwt.esValido(token, usuario));
    }

    @Test
    void tokenExpiradoNoEsValido() {
        JwtService jwt = new JwtService(SECRET, -1_000);
        String token = jwt.generarToken(usuario);

        assertFalse(jwt.esValido(token, usuario));
    }

    @Test
    void tokenFirmadoConOtraClaveNoEsValido() {
        JwtService propio = new JwtService(SECRET, 60_000);
        JwtService ajeno = new JwtService("otra-clave-distinta-con-mas-de-32-caracteres-999", 60_000);

        assertFalse(propio.esValido(ajeno.generarToken(usuario), usuario));
    }

    @Test
    void textoQueNoEsUnJwtNoEsValido() {
        JwtService jwt = new JwtService(SECRET, 60_000);

        assertFalse(jwt.esValido("esto.no.es.un.jwt", usuario));
    }

    @Test
    void unaClaveCortaSeRechazaAlArrancar() {
        assertThrows(IllegalStateException.class, () -> new JwtService("corta", 1_000));
    }
}
