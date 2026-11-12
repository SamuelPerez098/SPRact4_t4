package com.ejemplo.act4_api_jwt.security;

import com.ejemplo.act4_api_jwt.entity.Usuario;
import com.ejemplo.act4_api_jwt.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Le enseña a Spring Security a buscar usuarios en nuestra tabla "usuarios". */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        return User.withUsername(u.getEmail())
                .password(u.getPassword())
                .authorities(u.getRol())
                .build();
    }
}
