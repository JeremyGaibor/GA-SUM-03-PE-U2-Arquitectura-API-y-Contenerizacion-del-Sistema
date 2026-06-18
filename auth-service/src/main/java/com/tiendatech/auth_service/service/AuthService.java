package com.tiendatech.auth_service.service;

import com.tiendatech.auth_service.dto.LoginRequest;
import com.tiendatech.auth_service.dto.LoginResponse;
import com.tiendatech.auth_service.dto.RegisterRequest;
import com.tiendatech.auth_service.model.Usuario;
import com.tiendatech.auth_service.repository.UsuarioRepository;
import com.tiendatech.auth_service.security.JwtUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostConstruct
    public void crearUsuarioAdmin() {
        if (usuarioRepository.findByEmail("admin@tiendatech.com").isEmpty()) {
            Usuario admin = Usuario.builder()
                    .nombre("Administrador TiendaTech")
                    .email("admin@tiendatech.com")
                    .password(passwordEncoder.encode("1234"))
                    .rol("ADMIN")
                    .build();

            usuarioRepository.save(admin);
        }
    }

    public LoginResponse register(RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya esta registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .build();

        Usuario guardado = usuarioRepository.save(usuario);

        String token = jwtUtil.generateToken(
                guardado.getEmail(),
                guardado.getRol()
        );

        return new LoginResponse(
                "success",
                "Usuario registrado correctamente",
                token
        );
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtUtil.generateToken(
                usuario.getEmail(),
                usuario.getRol()
        );

        return new LoginResponse(
                "success",
                "Login correcto",
                token
        );
    }

    public boolean validate(String token) {
        return jwtUtil.validateToken(token);
    }
}