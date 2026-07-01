package com.pacifico.repuestos.service;

import com.pacifico.repuestos.dto.request.*;
import com.pacifico.repuestos.dto.response.AuthResponse;
import com.pacifico.repuestos.exception.BusinessException;
import com.pacifico.repuestos.model.*;
import com.pacifico.repuestos.repository.*;
import com.pacifico.repuestos.security.JwtUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService — Pruebas unitarias")
class AuthServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setNombre("Juan Pérez");
        registerRequest.setCorreo("juan@test.com");
        registerRequest.setPassword("password123");
        registerRequest.setTelefono("966123456");

        loginRequest = new LoginRequest();
        loginRequest.setCorreo("juan@test.com");
        loginRequest.setPassword("password123");

        usuarioMock = Usuario.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .correo("juan@test.com")
                .password("$2a$10$encoded")
                .rol(Usuario.Rol.CLIENTE)
                .activo(true)
                .build();
    }

    // ==================== REGISTER ====================

    @Test
    @DisplayName("Registro exitoso devuelve token y datos del usuario")
    void register_exitoso() {
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encoded");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(new Cliente());
        when(jwtUtil.generateToken(anyString(), anyString(), anyLong())).thenReturn("jwt-token");

        AuthResponse response = authService.register(registerRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getTipo()).isEqualTo("Bearer");
        assertThat(response.getCorreo()).isEqualTo("juan@test.com");
        assertThat(response.getRol()).isEqualTo("CLIENTE");

        verify(usuarioRepository).save(any(Usuario.class));
        verify(clienteRepository).save(any(Cliente.class));
        verify(passwordEncoder).encode("password123");
    }

    @Test
    @DisplayName("Registro lanza excepción si el correo ya existe")
    void register_correoYaExiste_lanzaExcepcion() {
        when(usuarioRepository.existsByCorreo("juan@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("correo ya tiene una cuenta");

        verify(usuarioRepository, never()).save(any());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Registro cifra la contraseña con BCrypt")
    void register_cifraClave() {
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$encoded");
        when(usuarioRepository.save(any())).thenReturn(usuarioMock);
        when(clienteRepository.save(any())).thenReturn(new Cliente());
        when(jwtUtil.generateToken(anyString(), anyString(), anyLong())).thenReturn("token");

        authService.register(registerRequest);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("$2a$10$encoded");
        assertThat(captor.getValue().getPassword()).isNotEqualTo("password123");
    }

    @Test
    @DisplayName("Registro asigna rol CLIENTE por defecto")
    void register_asignaRolCliente() {
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(usuarioRepository.save(any())).thenReturn(usuarioMock);
        when(clienteRepository.save(any())).thenReturn(new Cliente());
        when(jwtUtil.generateToken(anyString(), anyString(), anyLong())).thenReturn("token");

        authService.register(registerRequest);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getRol()).isEqualTo(Usuario.Rol.CLIENTE);
    }

    // ==================== LOGIN ====================

    @Test
    @DisplayName("Login exitoso devuelve token y datos del usuario")
    void login_exitoso() {
        when(usuarioRepository.findByCorreo("juan@test.com")).thenReturn(Optional.of(usuarioMock));
        when(passwordEncoder.matches("password123", "$2a$10$encoded")).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString(), anyLong())).thenReturn("jwt-token");

        AuthResponse response = authService.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getNombre()).isEqualTo("Juan Pérez");
        assertThat(response.getRol()).isEqualTo("CLIENTE");
    }

    @Test
    @DisplayName("Login lanza excepción si el correo no existe")
    void login_correoNoExiste_lanzaExcepcion() {
        when(usuarioRepository.findByCorreo("juan@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Correo o contraseña incorrectos");

        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Login lanza excepción si la contraseña es incorrecta")
    void login_passwordIncorrecta_lanzaExcepcion() {
        when(usuarioRepository.findByCorreo("juan@test.com")).thenReturn(Optional.of(usuarioMock));
        when(passwordEncoder.matches("password123", "$2a$10$encoded")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Correo o contraseña incorrectos");

        verify(jwtUtil, never()).generateToken(anyString(), anyString(), anyLong());
    }

    @Test
    @DisplayName("Login lanza excepción si la cuenta está desactivada")
    void login_cuentaDesactivada_lanzaExcepcion() {
        usuarioMock.setActivo(false);
        when(usuarioRepository.findByCorreo("juan@test.com")).thenReturn(Optional.of(usuarioMock));

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Cuenta desactivada");

        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Login genera token con correo y rol correctos")
    void login_generaTokenConDatosCorrectos() {
        when(usuarioRepository.findByCorreo("juan@test.com")).thenReturn(Optional.of(usuarioMock));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString(), anyLong())).thenReturn("token");

        authService.login(loginRequest);

        verify(jwtUtil).generateToken("juan@test.com", "CLIENTE", 1L);
    }
}
