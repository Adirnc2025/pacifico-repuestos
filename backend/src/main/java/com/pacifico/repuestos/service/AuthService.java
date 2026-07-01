package com.pacifico.repuestos.service;

import com.pacifico.repuestos.dto.request.*;
import com.pacifico.repuestos.dto.response.AuthResponse;
import com.pacifico.repuestos.exception.BusinessException;
import com.pacifico.repuestos.model.*;
import com.pacifico.repuestos.repository.*;
import com.pacifico.repuestos.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new BusinessException("Este correo ya tiene una cuenta registrada");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(Usuario.Rol.CLIENTE)
                .activo(true)
                .build();

        usuario = usuarioRepository.save(usuario);

        Cliente cliente = Cliente.builder()
                .usuario(usuario)
                .telefono(request.getTelefono())
                .build();

        clienteRepository.save(cliente);

        String token = jwtUtil.generateToken(
                usuario.getCorreo(),
                usuario.getRol().name(),
                usuario.getId()
        );

        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .usuarioId(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new BusinessException("Correo o contraseña incorrectos"));

        if (!usuario.getActivo()) {
            throw new BusinessException("Cuenta desactivada. Contacta al administrador.");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new BusinessException("Correo o contraseña incorrectos");
        }

        String token = jwtUtil.generateToken(
                usuario.getCorreo(),
                usuario.getRol().name(),
                usuario.getId()
        );

        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .usuarioId(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol().name())
                .build();
    }
}
