package com.pacifico.repuestos.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pacifico.repuestos.model.Inventario;
import com.pacifico.repuestos.model.Producto;
import com.pacifico.repuestos.repository.InventarioRepository;
import com.pacifico.repuestos.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración end-to-end: contexto Spring completo (seguridad,
 * JWT, JPA reales) contra una base H2 en memoria, sin depender de Aiven.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Integración — flujos end-to-end de la API")
class ApiFlujosIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ProductoRepository productoRepo;
    @Autowired private InventarioRepository inventarioRepo;

    private Producto productoSeed;

    @BeforeEach
    void seedCatalogo() {
        productoSeed = productoRepo.save(Producto.builder()
            .codigo("INT-001")
            .nombre("Empaque de Motor Integración")
            .descripcion("Producto sembrado para pruebas de integración")
            .precio(new BigDecimal("150.00"))
            .destacado(false)
            .activo(true)
            .build());

        inventarioRepo.save(Inventario.builder()
            .producto(productoSeed)
            .stock(20)
            .stockMinimo(5)
            .build());
    }

    // ───── REGISTRO ─────
    @Test
    @DisplayName("POST /api/auth/register crea un usuario nuevo y devuelve token")
    void registro_creaUsuarioYDevuelveToken() throws Exception {
        String correo = "nuevo." + UUID.randomUUID() + "@test.com";
        Map<String, String> body = Map.of(
            "nombre", "Usuario Integración",
            "correo", correo,
            "password", "clave12345",
            "telefono", "966111222"
        );

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.token").isNotEmpty())
            .andExpect(jsonPath("$.correo").value(correo))
            .andExpect(jsonPath("$.rol").value("CLIENTE"));
    }

    // ───── LOGIN ─────
    @Test
    @DisplayName("POST /api/auth/login con credenciales válidas devuelve token JWT")
    void login_credencialesValidas_devuelveToken() throws Exception {
        registrarCliente("valido." + UUID.randomUUID() + "@test.com", "clave12345");

        Map<String, String> loginBody = Map.of(
            "correo", ultimoCorreoRegistrado,
            "password", "clave12345"
        );

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginBody)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isNotEmpty())
            .andExpect(jsonPath("$.tipo").value("Bearer"));
    }

    @Test
    @DisplayName("POST /api/auth/login con credenciales inválidas es rechazado")
    void login_credencialesInvalidas_esRechazado() throws Exception {
        registrarCliente("invalido." + UUID.randomUUID() + "@test.com", "clave12345");

        Map<String, String> loginBody = Map.of(
            "correo", ultimoCorreoRegistrado,
            "password", "claveIncorrecta"
        );

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginBody)))
            .andExpect(status().isBadRequest());
    }

    // ───── CATÁLOGO ─────
    @Test
    @DisplayName("GET /api/productos devuelve 200 y la lista de productos")
    void listarProductos_devuelve200YLista() throws Exception {
        mockMvc.perform(get("/api/productos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[?(@.codigo=='INT-001')]").exists());
    }

    @Test
    @DisplayName("GET /api/productos/buscar encuentra el producto por texto")
    void buscarProductos_porTexto_devuelve200() throws Exception {
        mockMvc.perform(get("/api/productos/buscar").param("q", "Integración"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].codigo").value("INT-001"));
    }

    // ───── PEDIDO CON AUTENTICACIÓN ─────
    @Test
    @DisplayName("POST /api/pedidos con token válido crea el pedido")
    void crearPedido_conTokenValido_devuelve201() throws Exception {
        String token = registrarCliente("cliente.pedido." + UUID.randomUUID() + "@test.com", "clave12345");

        Map<String, Object> item = Map.of("productoId", productoSeed.getId(), "cantidad", 2);
        Map<String, Object> pedidoBody = Map.of(
            "items", java.util.List.of(item),
            "tipoDelivery", "RECOJO"
        );

        mockMvc.perform(post("/api/pedidos")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedidoBody)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.estado").value("PENDIENTE"))
            .andExpect(jsonPath("$.detalles[0].productoId").value(productoSeed.getId()));
    }

    // ───── ACCESO SIN TOKEN ─────
    @Test
    @DisplayName("GET /api/pedidos sin token es rechazado (ruta protegida, solo ADMIN)")
    void accederRutaProtegida_sinToken_esRechazado() throws Exception {
        mockMvc.perform(get("/api/pedidos"))
            .andExpect(status().isForbidden());
    }

    // ───── HELPER ─────
    private String ultimoCorreoRegistrado;

    private String registrarCliente(String correo, String password) throws Exception {
        this.ultimoCorreoRegistrado = correo;
        Map<String, String> body = Map.of(
            "nombre", "Cliente Integración",
            "correo", correo,
            "password", password,
            "telefono", "966333444"
        );

        String respuesta = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(respuesta).get("token").asText();
    }
}
