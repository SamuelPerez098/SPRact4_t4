package com.ejemplo.act4_api_jwt.controller;

import com.ejemplo.act4_api_jwt.dto.AuthResponse;
import com.ejemplo.act4_api_jwt.dto.LoginRequest;
import com.ejemplo.act4_api_jwt.dto.RegisterRequest;
import com.ejemplo.act4_api_jwt.dto.UsuarioResponse;
import com.ejemplo.act4_api_jwt.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 201 Created: registra al usuario (la respuesta nunca incluye el password)
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(req));
    }

    // 200 OK: devuelve el token JWT si las credenciales son correctas
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}
