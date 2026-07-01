# Arquitectura del Sistema — Pacífico Repuestos

**Proyecto:** Pacífico Repuestos  
**Versión:** 1.0  
**Metodología:** Scrum  
**Etapa:** D — Diseño  

---

## 1. Arquitectura General (Modelo C4)

### Nivel 1 — Contexto del Sistema

```
┌─────────────────────────────────────────────────────────────────┐
│                     SISTEMA PACÍFICO REPUESTOS                  │
│                                                                 │
│   ┌─────────────┐         ┌─────────────────────────────────┐  │
│   │   CLIENTE   │────────▶│        Sistema Web              │  │
│   │  (Navegador)│◀────────│   pacifico-repuestos.vercel.app │  │
│   └─────────────┘         └─────────────────────────────────┘  │
│                                           │                     │
│   ┌─────────────┐                         │ HTTPS / REST API    │
│   │    ADMIN    │────────▶                │                     │
│   │  (Navegador)│◀────────    ┌───────────▼─────────────────┐  │
│   └─────────────┘             │       Backend API           │  │
│                               │  pacifico-api.render.com    │  │
│                               └───────────┬─────────────────┘  │
│                                           │ JDBC / JPA          │
│                               ┌───────────▼─────────────────┐  │
│                               │    Base de Datos            │  │
│                               │  PostgreSQL — Railway       │  │
│                               └─────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

---

### Nivel 2 — Contenedores

```
┌──────────────────────────────────────────────────────────────────────────┐
│  FRONTEND — React + Tailwind CSS                                         │
│  Desplegado en: Vercel                                                   │
│                                                                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌───────────────┐  │
│  │   Páginas   │  │ Componentes │  │   Context   │  │   Services    │  │
│  │  (Routes)   │  │     UI      │  │ (Auth/Cart) │  │ (API Calls)   │  │
│  └─────────────┘  └─────────────┘  └─────────────┘  └───────────────┘  │
└──────────────────────────────────────┬───────────────────────────────────┘
                                       │ HTTP REST (JSON)
┌──────────────────────────────────────▼───────────────────────────────────┐
│  BACKEND — Spring Boot (Java 17)                                         │
│  Desplegado en: Render                                                   │
│                                                                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌───────────────┐  │
│  │ Controllers │  │  Services   │  │Repositories │  │    DTOs /     │  │
│  │ (REST API)  │  │ (Lógica)    │  │  (JPA)      │  │   Mappers     │  │
│  └─────────────┘  └─────────────┘  └─────────────┘  └───────────────┘  │
│                                                                          │
│  ┌─────────────────────────────────────────────────┐                    │
│  │  Spring Security + JWT — Autenticación y Roles  │                    │
│  └─────────────────────────────────────────────────┘                    │
└──────────────────────────────────────┬───────────────────────────────────┘
                                       │ JPA / Hibernate
┌──────────────────────────────────────▼───────────────────────────────────┐
│  BASE DE DATOS — PostgreSQL                                               │
│  Desplegado en: Railway                                                   │
│                                                                          │
│  15 tablas relacionales — modelo detallado en sección 3                  │
└──────────────────────────────────────────────────────────────────────────┘
```

---

### Nivel 3 — Componentes del Backend

```
com.pacifico.repuestos/
│
├── controller/
│   ├── AuthController          # /api/auth/register, /api/auth/login
│   ├── ProductoController      # /api/productos
│   ├── CatalogoController      # /api/marcas, /api/modelos, /api/generaciones, /api/motores
│   ├── CategoriaController     # /api/categorias
│   ├── PedidoController        # /api/pedidos
│   ├── DeliveryController      # /api/delivery
│   ├── ReporteController       # /api/reportes
│   └── ClienteController       # /api/clientes
│
├── service/
│   ├── AuthService             # Lógica de registro y autenticación
│   ├── ProductoService         # CRUD productos, búsqueda y filtros
│   ├── CatalogoService         # CRUD marcas, modelos, generaciones, motores
│   ├── CompatibilidadService   # Gestión de compatibilidades producto-motor
│   ├── PedidoService           # Creación, gestión y estados de pedidos
│   ├── InventarioService       # Control de stock
│   ├── DeliveryService         # Zonas y tarifas de envío
│   └── ReporteService          # Generación de reportes y métricas
│
├── repository/
│   ├── UsuarioRepository
│   ├── ClienteRepository
│   ├── ProductoRepository
│   ├── MarcaRepository
│   ├── ModeloRepository
│   ├── GeneracionRepository
│   ├── MotorRepository
│   ├── CategoriaRepository
│   ├── CompatibilidadRepository
│   ├── PedidoRepository
│   ├── DetallePedidoRepository
│   ├── ImagenProductoRepository
│   ├── EnvioRepository
│   └── ZonaDeliveryRepository
│
├── model/ (Entidades JPA)
│   ├── Usuario
│   ├── Cliente
│   ├── Producto
│   ├── Categoria
│   ├── Marca
│   ├── Modelo
│   ├── Generacion
│   ├── Motor
│   ├── Compatibilidad
│   ├── Pedido
│   ├── DetallePedido
│   ├── ImagenProducto
│   ├── Inventario
│   ├── Envio
│   └── ZonaDelivery
│
├── dto/
│   ├── request/
│   │   ├── RegisterRequest
│   │   ├── LoginRequest
│   │   ├── ProductoRequest
│   │   └── PedidoRequest
│   └── response/
│       ├── AuthResponse
│       ├── ProductoResponse
│       ├── PedidoResponse
│       └── DashboardResponse
│
├── security/
│   ├── JwtUtil
│   ├── JwtFilter
│   └── SecurityConfig
│
└── exception/
    ├── GlobalExceptionHandler
    ├── ResourceNotFoundException
    └── BusinessException
```

---

### Nivel 3 — Componentes del Frontend

```
src/
│
├── pages/
│   ├── Home.jsx               # Página principal
│   ├── Productos.jsx          # Listado y búsqueda
│   ├── ProductoDetalle.jsx    # Ficha de producto
│   ├── Carrito.jsx            # Carrito de compras
│   ├── Checkout.jsx           # Proceso de pedido
│   ├── MisPedidos.jsx         # Historial del cliente
│   ├── Login.jsx
│   ├── Registro.jsx
│   └── admin/
│       ├── Dashboard.jsx
│       ├── ProductosAdmin.jsx
│       ├── PedidosAdmin.jsx
│       ├── CatalogoAdmin.jsx
│       ├── ClientesAdmin.jsx
│       └── ReportesAdmin.jsx
│
├── components/
│   ├── layout/
│   │   ├── Header.jsx
│   │   ├── Footer.jsx
│   │   └── Sidebar.jsx
│   ├── catalogo/
│   │   ├── BuscadorVehiculo.jsx
│   │   ├── ProductoCard.jsx
│   │   ├── FiltroCategoria.jsx
│   │   └── GaleriaImagenes.jsx
│   ├── carrito/
│   │   ├── CarritoItem.jsx
│   │   └── CarritoResumen.jsx
│   └── common/
│       ├── BotonWhatsApp.jsx
│       ├── LoadingSpinner.jsx
│       └── ProtectedRoute.jsx
│
├── context/
│   ├── AuthContext.jsx        # Estado de autenticación global
│   └── CarritoContext.jsx     # Estado del carrito global
│
├── services/
│   ├── authService.js         # Llamadas a /api/auth
│   ├── productoService.js     # Llamadas a /api/productos
│   ├── pedidoService.js       # Llamadas a /api/pedidos
│   └── catalogoService.js     # Llamadas a /api/marcas, modelos, etc.
│
└── utils/
    ├── axiosConfig.js         # Axios con interceptores JWT
    └── constants.js           # URLs base, constantes globales
```

---

## 2. Diseño de API REST

### Convenciones
- Base URL producción: `https://pacifico-api.render.com/api`
- Formato: JSON
- Autenticación: Bearer Token (JWT) en header `Authorization`
- Códigos de respuesta: 200, 201, 400, 401, 403, 404, 500

### Endpoints principales

#### Autenticación
| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | /api/auth/register | Registrar nuevo cliente | No |
| POST | /api/auth/login | Iniciar sesión | No |
| POST | /api/auth/recuperar | Solicitar recuperación contraseña | No |
| POST | /api/auth/reset | Restablecer contraseña con token | No |

#### Productos
| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | /api/productos | Listar productos con filtros | No |
| GET | /api/productos/{id} | Ver detalle de producto | No |
| GET | /api/productos/buscar?q= | Buscar por nombre o código | No |
| POST | /api/productos | Crear producto | ADMIN |
| PUT | /api/productos/{id} | Editar producto | ADMIN |
| DELETE | /api/productos/{id} | Eliminar producto | ADMIN |
| POST | /api/productos/{id}/imagenes | Subir imagen | ADMIN |
| DELETE | /api/productos/{id}/imagenes/{imgId} | Eliminar imagen | ADMIN |

#### Catálogo Automotriz
| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | /api/marcas | Listar marcas | No |
| POST | /api/marcas | Crear marca | ADMIN |
| PUT | /api/marcas/{id} | Editar marca | ADMIN |
| DELETE | /api/marcas/{id} | Eliminar marca | ADMIN |
| GET | /api/modelos?marcaId= | Listar modelos por marca | No |
| POST | /api/modelos | Crear modelo | ADMIN |
| GET | /api/generaciones?modeloId= | Listar generaciones | No |
| POST | /api/generaciones | Crear generación | ADMIN |
| GET | /api/motores?generacionId= | Listar motores | No |
| POST | /api/motores | Crear motor | ADMIN |

#### Pedidos
| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | /api/pedidos | Crear pedido | CLIENTE |
| GET | /api/pedidos/mis-pedidos | Pedidos del cliente autenticado | CLIENTE |
| GET | /api/pedidos/{id} | Detalle de pedido | CLIENTE/ADMIN |
| GET | /api/pedidos | Todos los pedidos | ADMIN |
| PUT | /api/pedidos/{id}/estado | Actualizar estado | ADMIN |

#### Reportes
| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | /api/reportes/dashboard | Métricas del dashboard | ADMIN |
| GET | /api/reportes/ventas?desde=&hasta= | Reporte de ventas por período | ADMIN |

---

## 3. Modelo de Base de Datos — PostgreSQL

### Diagrama Entidad-Relación (texto estructurado)

```
USUARIOS ──────────── CLIENTES
   │                     │
   │ (rol: ADMIN/CLIENTE) │
   │                     │
                    PEDIDOS ──────── DETALLE_PEDIDO ──── PRODUCTOS
                       │                                     │
                    ENVIOS                           IMAGENES_PRODUCTO
                                                             │
                                                      COMPATIBILIDADES
                                                             │
                                                          MOTORES
                                                             │
                                                        GENERACIONES
                                                             │
                                                          MODELOS
                                                             │
                                                          MARCAS

PRODUCTOS ──── CATEGORIAS
PRODUCTOS ──── INVENTARIO
PEDIDOS ────── ZONAS_DELIVERY
```

### Tablas y Atributos

#### tabla: usuarios
```
id              BIGSERIAL PRIMARY KEY
nombre          VARCHAR(100) NOT NULL
correo          VARCHAR(150) UNIQUE NOT NULL
password        VARCHAR(255) NOT NULL         -- BCrypt
rol             VARCHAR(20) NOT NULL          -- CLIENTE | ADMIN
activo          BOOLEAN DEFAULT TRUE
fecha_registro  TIMESTAMP DEFAULT NOW()
```

#### tabla: clientes
```
id              BIGSERIAL PRIMARY KEY
usuario_id      BIGINT REFERENCES usuarios(id)
telefono        VARCHAR(20)
direccion       VARCHAR(255)
dni             VARCHAR(15)
```

#### tabla: marcas
```
id              BIGSERIAL PRIMARY KEY
nombre          VARCHAR(100) UNIQUE NOT NULL
logo_url        VARCHAR(500)
activo          BOOLEAN DEFAULT TRUE
```

#### tabla: modelos
```
id              BIGSERIAL PRIMARY KEY
nombre          VARCHAR(100) NOT NULL
marca_id        BIGINT REFERENCES marcas(id)
activo          BOOLEAN DEFAULT TRUE
```

#### tabla: generaciones
```
id              BIGSERIAL PRIMARY KEY
nombre          VARCHAR(100) NOT NULL
anio_inicio     INT
anio_fin        INT
modelo_id       BIGINT REFERENCES modelos(id)
```

#### tabla: motores
```
id              BIGSERIAL PRIMARY KEY
codigo          VARCHAR(50) UNIQUE NOT NULL   -- Ej: 2GD-FTV
descripcion     VARCHAR(200)
cilindrada      VARCHAR(20)
generacion_id   BIGINT REFERENCES generaciones(id)
```

#### tabla: categorias
```
id              BIGSERIAL PRIMARY KEY
nombre          VARCHAR(100) UNIQUE NOT NULL
descripcion     VARCHAR(300)
imagen_url      VARCHAR(500)
activo          BOOLEAN DEFAULT TRUE
```

#### tabla: productos
```
id              BIGSERIAL PRIMARY KEY
codigo          VARCHAR(50) UNIQUE NOT NULL
nombre          VARCHAR(200) NOT NULL
descripcion     TEXT
precio          DECIMAL(10,2) NOT NULL
medidas         VARCHAR(100)
destacado       BOOLEAN DEFAULT FALSE
activo          BOOLEAN DEFAULT TRUE
categoria_id    BIGINT REFERENCES categorias(id)
fecha_creacion  TIMESTAMP DEFAULT NOW()
```

#### tabla: inventario
```
id              BIGSERIAL PRIMARY KEY
producto_id     BIGINT UNIQUE REFERENCES productos(id)
stock           INT NOT NULL DEFAULT 0
stock_minimo    INT DEFAULT 5
ultima_actualizacion TIMESTAMP DEFAULT NOW()
```

#### tabla: imagenes_producto
```
id              BIGSERIAL PRIMARY KEY
producto_id     BIGINT REFERENCES productos(id)
url             VARCHAR(500) NOT NULL
es_principal    BOOLEAN DEFAULT FALSE
orden           INT DEFAULT 0
```

#### tabla: compatibilidades
```
id              BIGSERIAL PRIMARY KEY
producto_id     BIGINT REFERENCES productos(id)
motor_id        BIGINT REFERENCES motores(id)
observacion     VARCHAR(300)
UNIQUE(producto_id, motor_id)
```

#### tabla: zonas_delivery
```
id              BIGSERIAL PRIMARY KEY
nombre          VARCHAR(100) NOT NULL
tipo            VARCHAR(20) NOT NULL          -- LOCAL | INTERPROVINCIAL
tarifa          DECIMAL(8,2) NOT NULL
activo          BOOLEAN DEFAULT TRUE
```

#### tabla: pedidos
```
id              BIGSERIAL PRIMARY KEY
numero_pedido   VARCHAR(20) UNIQUE NOT NULL   -- Ej: PED-2025-0001
cliente_id      BIGINT REFERENCES clientes(id)
estado          VARCHAR(30) NOT NULL          -- PENDIENTE|CONFIRMADO|EN_PREPARACION|ENVIADO|ENTREGADO|CANCELADO
subtotal        DECIMAL(10,2) NOT NULL
costo_delivery  DECIMAL(8,2) DEFAULT 0
total           DECIMAL(10,2) NOT NULL
tipo_delivery   VARCHAR(20) NOT NULL          -- RECOJO|LOCAL|INTERPROVINCIAL
direccion_entrega VARCHAR(300)
zona_id         BIGINT REFERENCES zonas_delivery(id)
observacion     TEXT
fecha_pedido    TIMESTAMP DEFAULT NOW()
fecha_actualizacion TIMESTAMP DEFAULT NOW()
```

#### tabla: detalle_pedido
```
id              BIGSERIAL PRIMARY KEY
pedido_id       BIGINT REFERENCES pedidos(id)
producto_id     BIGINT REFERENCES productos(id)
cantidad        INT NOT NULL
precio_unitario DECIMAL(10,2) NOT NULL
subtotal        DECIMAL(10,2) NOT NULL
```

#### tabla: envios
```
id              BIGSERIAL PRIMARY KEY
pedido_id       BIGINT UNIQUE REFERENCES pedidos(id)
transportista   VARCHAR(100)
numero_guia     VARCHAR(100)
fecha_envio     TIMESTAMP
fecha_entrega_estimada DATE
fecha_entrega_real     TIMESTAMP
estado          VARCHAR(30)
```

---

## 4. Patrones de Diseño Aplicados

| Patrón | Aplicación en el proyecto |
|--------|--------------------------|
| MVC | Controllers reciben peticiones, Services procesan lógica, Repositories acceden a datos |
| DTO | Objetos de transferencia entre capas (Request/Response) para no exponer entidades |
| Repository | Abstracción de acceso a datos mediante JPA Repository |
| Service Layer | Toda la lógica de negocio encapsulada en Services, separada de Controllers |
| Singleton | Beans de Spring (Services, Repositories) son singleton por defecto |
| Factory | JwtUtil genera y valida tokens |
| Filter Chain | JwtFilter intercepta peticiones antes de llegar al Controller |

---

## 5. Seguridad

```
Petición HTTP
     │
     ▼
JwtFilter (valida token Bearer)
     │
     ├── Token inválido/ausente ──▶ 401 Unauthorized
     │
     ▼
SecurityConfig (verifica rol)
     │
     ├── Rol insuficiente ──────── ▶ 403 Forbidden
     │
     ▼
Controller ──▶ Service ──▶ Repository ──▶ PostgreSQL
```

**Reglas de acceso:**
- Rutas públicas: `/api/auth/**`, `GET /api/productos/**`, `GET /api/marcas/**`, `GET /api/modelos/**`, `GET /api/generaciones/**`, `GET /api/motores/**`, `GET /api/categorias/**`
- Rutas cliente: `POST /api/pedidos`, `GET /api/pedidos/mis-pedidos`
- Rutas admin: `POST|PUT|DELETE /api/productos/**`, `GET|PUT /api/pedidos/**`, `/api/reportes/**`

---

## 6. Despliegue en Producción

```
GitHub (main)
     │
     ├──▶ Vercel (auto-deploy frontend React)
     │         URL: https://pacifico-repuestos.vercel.app
     │
     └──▶ Render (auto-deploy backend Spring Boot JAR)
               URL: https://pacifico-api.onrender.com
                         │
                         └──▶ Railway PostgreSQL
                                   Host: railway.app
                                   Puerto: 5432
```

**Variables de entorno — Backend (Render):**
```
DATABASE_URL=jdbc:postgresql://railway-host:5432/pacifico_db
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=****
JWT_SECRET=clave-secreta-256bits
JWT_EXPIRATION=86400000
FRONTEND_URL=https://pacifico-repuestos.vercel.app
```

**Variables de entorno — Frontend (Vercel):**
```
VITE_API_URL=https://pacifico-api.onrender.com/api
```

---

*Documento generado en la Etapa D (Diseño) del flujo ADIPD — Proyecto Pacífico Repuestos*  
*Metodología: Scrum | Herramienta de apoyo: IA (Claude)*
