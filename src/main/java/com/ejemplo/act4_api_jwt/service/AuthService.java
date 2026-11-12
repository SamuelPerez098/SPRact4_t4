package com.ejemplo.act4_api_jwt.service;

import com.ejemplo.act4_api_jwt.dto.AuthResponse;
import com.ejemplo.act4_api_jwt.dto.LoginRequest;
import com.ejemplo.act4_api_jwt.dto.RegisterRequest;
import com.ejemplo.act4_api_jwt.dto.UsuarioResponse;
import com.ejemplo.act4_api_jwt.entity.Usuario;
import com.ejemplo.act4_api_jwt.exception.ConflictoException;
import com.ejemplo.act4_api_jwt.repository.UsuarioRepository;
import com.ejemplo.act4_api_jwt.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public UsuarioResponse registrar(RegisterRequest req) {
        String email = req.email().trim().toLowerCase();
        if (usuarioRepository.existsByEmail(email)) {
            throw new ConflictoException("Ya existe un usuario registrado con el email " + email);
        }
        Usuario u = new Usuario();
        u.setNombre(req.nombre().trim());
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(req.password())); // se guarda el hash BCrypt
        return UsuarioResponse.desde(usuarioRepository.save(u));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        String email = req.email().trim().toLowerCase();
        // Si el email o la contraseña son incorrectos lanza AuthenticationException (-> 401)
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, req.password()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Usuario u = usuarioRepository.findByEmail(email).orElseThrow();
        String token = jwtService.generarToken(userDetails);
        return new AuthResponse(token, "Bearer", jwtService.getExpirationMs() / 1000, u.getEmail(), u.getNombre());
    }
}
