# ESPECIFICACION.md — Fuente de Verdad
# Pacífico Repuestos — Sistema Web de Gestión y Comercialización de Repuestos Automotrices

**Versión:** 1.0.0  
**Fecha:** Junio 2026  
**Estado:** Completado  
**Metodología:** Scrum — 4 sprints de 2 semanas  
**Flujo de trabajo:** ADIPD (Análisis → Diseño → Implementación → Pruebas → Despliegue)  
**Herramienta de apoyo IA:** Claude (Anthropic) — SDD (Specification Driven Development) con Kiro  
**Ubicación del negocio:** Ayacucho, Perú  
**Repositorio:** https://github.com/Adirnc2025/pacifico-repuestos  

> Este documento es la **fuente de verdad** del proyecto.  
> Todo el código, pruebas y despliegue se generan a partir de esta especificación.  
> Cualquier cambio en el sistema debe reflejarse primero aquí.

---

## ÍNDICE

1. [Descripción del Proyecto](#1-descripción-del-proyecto)
2. [Tecnologías](#2-tecnologías)
3. [Actores del Sistema](#3-actores-del-sistema)
4. [Requerimientos Funcionales](#4-requerimientos-funcionales)
5. [Requerimientos No Funcionales](#5-requerimientos-no-funcionales)
6. [Historias de Usuario por Sprint](#6-historias-de-usuario-por-sprint)
7. [Arquitectura del Sistema](#7-arquitectura-del-sistema)
8. [Modelo de Base de Datos](#8-modelo-de-base-de-datos)
9. [Diseño de API REST](#9-diseño-de-api-rest)
10. [Estructura del Proyecto](#10-estructura-del-proyecto)
11. [Seguridad](#11-seguridad)
12. [Despliegue](#12-despliegue)
13. [Pruebas](#13-pruebas)
14. [Product Backlog y Sprints](#14-product-backlog-y-sprints)
15. [Restricciones y Decisiones Técnicas](#15-restricciones-y-decisiones-técnicas)

---

## 1. Descripción del Proyecto

**Nombre del sistema:** Pacífico Repuestos  
**Tipo:** Plataforma web de comercialización de repuestos automotrices  
**Negocio:** Tienda de repuestos automotrices ubicada en Ayacucho, Perú  

### Datos del negocio
| Dato | Valor |
|------|-------|
| Local 1 | Jr. Protzel N° 114, Ayacucho |
| Local 2 | Av. Mariscal Cáceres N° 258, Ayacucho |
| WhatsApp | 981 869 554 |
| Teléfono | 981 869 554 |

### Objetivo
Desarrollar una plataforma web profesional, moderna y escalable que permita:
- Venta de repuestos automotrices en línea.
- Gestión completa de inventario y catálogo técnico.
- Búsqueda avanzada de repuestos por compatibilidad de vehículo y motor.
- Delivery local (Ayacucho) e interprovincial.
- Panel de administración para gestión integral del negocio.

### Alcance v1.0
- Módulo de autenticación (cliente y administrador).
- Catálogo automotriz con búsqueda por vehículo.
- Gestión completa de productos, stock e imágenes.
- Carrito de compras y flujo de pedidos.
- Panel de administración.
- Dashboard y reportes básicos.
- Despliegue en producción.

### Fuera de alcance v1.0
- Pasarela de pago en línea (el pago se coordina externamente).
- App móvil nativa.
- Inteligencia artificial como funcionalidad operativa.
- Recuperación de contraseña por correo electrónico.
- Confirmación de registro por correo electrónico.
- Notificaciones por correo electrónico (cambio de estado de pedidos u otros eventos).
- Edición y eliminación de modelos, generaciones y motores desde el panel admin (actualmente solo se crean; queda para v2).
- Gestión de imágenes de productos desde la interfaz web (el backend existe, la UI queda para v2).
- Gestión de compatibilidades producto-motor desde la interfaz web (el backend existe, la UI queda para v2).
- Gestión de zonas y tarifas de delivery desde el panel admin (actualmente se configuran por script SQL; queda para v2).

---

## 2. Tecnologías

| Capa | Tecnología | Versión |
|------|-----------|---------|
| Frontend | React + Vite | React 18 |
| Estilos | Tailwind CSS | v3 |
| Backend | Spring Boot (Java) | Java 17 / Spring Boot 3.2 |
| Base de datos | Microsoft SQL Server (local) | Puerto 1433, BD `pacifico_db` |
| ORM | Spring Data JPA / Hibernate | — |
| Seguridad | Spring Security + JWT + BCrypt | — |
| Pruebas backend | JUnit 5 + Mockito + JaCoCo | JaCoCo 0.8.11 |
| IDE | Visual Studio Code | — |
| Control de versiones | GitHub | github.com/Adirnc2025/pacifico-repuestos |
| Ejecución frontend | Local — Vite dev server | localhost:5173 |
| Ejecución backend | Local — Spring Boot embebido | localhost:8080 |
| Ejecución base de datos | Local — SQL Server | localhost:1433 |

---

## 3. Actores del Sistema

| Actor | Descripción | Rol en el sistema |
|-------|-------------|-------------------|
| Cliente | Usuario registrado que compra repuestos | CLIENTE |
| Administrador | Gestiona el sistema completo | ADMIN |
| Sistema | Procesos automáticos (stock, notificaciones) | Interno |

---

## 4. Requerimientos Funcionales

### Módulo Autenticación
| ID | Descripción | Prioridad |
|----|-------------|-----------|
| RF01 | Registro de cliente con nombre, correo, contraseña y teléfono | Alta |
| RF02 | Inicio de sesión con correo y contraseña (JWT) | Alta |
| RF03 | Inicio de sesión del administrador con rol ADMIN | Alta |
| RF04 | Cierre de sesión desde cualquier página | Alta |
| ~~RF05~~ | ~~Confirmación de registro por correo~~ | ~~Media~~ — **Fuera de alcance v1.0** (sin envío de correos) |
| ~~RF06~~ | ~~Recuperación de contraseña por enlace al correo (expira 30 min)~~ | ~~Media~~ — **Fuera de alcance v1.0** (sin envío de correos) |

### Módulo Catálogo Automotriz
| ID | Descripción | Prioridad |
|----|-------------|-----------|
| RF07 | Mostrar marcas de vehículos disponibles | Alta |
| RF08 | Mostrar modelos por marca | Alta |
| RF09 | Mostrar generaciones por modelo | Alta |
| RF10 | Mostrar motores por generación | Alta |
| RF11 | CRUD de marcas (admin) | Alta |
| RF12 | CRUD de modelos (admin) — creación implementada; edición/eliminación queda para v2 | Media - v2 |
| RF13 | CRUD de generaciones (admin) — creación implementada; edición/eliminación queda para v2 | Media - v2 |
| RF14 | CRUD de motores (admin) — creación implementada; edición/eliminación queda para v2 | Media - v2 |

### Módulo Productos
| ID | Descripción | Prioridad |
|----|-------------|-----------|
| RF15 | Mostrar productos con nombre, descripción, precio, stock e imágenes | Alta |
| RF16 | Filtrar productos por marca, modelo, generación, motor y categoría | Alta |
| RF17 | Mostrar compatibilidades de cada producto | Alta |
| RF18 | Búsqueda por nombre o código (mínimo 3 caracteres, con sugerencias) | Alta |
| RF19 | Productos destacados en página principal | Media |
| RF20 | CRUD de productos (admin) | Alta |
| RF21 | Gestión de compatibilidades producto-motor (admin) — backend implementado; UI web queda para v2 | Media - v2 |
| RF22 | Subida y gestión de imágenes de productos (admin) — backend implementado; UI web queda para v2 | Media - v2 |
| RF23 | Gestión de stock por producto (admin) | Alta |
| RF24 | CRUD de categorías (admin) | Alta |

### Módulo Carrito y Pedidos
| ID | Descripción | Prioridad |
|----|-------------|-----------|
| RF25 | Agregar productos al carrito | Alta |
| RF26 | Modificar cantidades y eliminar del carrito | Alta |
| RF27 | Realizar pedido con dirección y tipo de delivery | Alta |
| RF28 | Consultar estado e historial de pedidos | Alta |
| ~~RF29~~ | ~~Notificación de cambio de estado del pedido~~ | ~~Media~~ — **Fuera de alcance v1.0** (sin envío de correos) |
| RF30 | Gestión y cambio de estado de pedidos (admin) | Alta |
| RF31 | Registro completo del detalle de pedidos | Alta |

### Módulo Delivery
| ID | Descripción | Prioridad |
|----|-------------|-----------|
| RF32 | Delivery local (Ayacucho) e interprovincial | Alta |
| RF33 | Ingreso de dirección de entrega al pedir | Alta |
| RF34 | Gestión de zonas y tarifas de delivery (admin) — configuradas por script SQL; panel admin queda para v2 | Media - v2 |
| RF35 | Mostrar costo de delivery según zona | Media |

### Módulo Contacto
| ID | Descripción | Prioridad |
|----|-------------|-----------|
| RF36 | Botón de contacto directo por WhatsApp (981 869 554) | Alta |
| RF37 | Footer con contacto, ubicación (Jr. Protzel N° 114 y Av. Mariscal Cáceres N° 258, Ayacucho) y horarios | Alta |

### Módulo Reportes
| ID | Descripción | Prioridad |
|----|-------------|-----------|
| RF38 | Reporte de ventas por período (admin) | Media |
| RF39 | Gestión de clientes registrados (admin) | Media |
| RF40 | Dashboard con resumen de pedidos, stock y ventas (admin) | Media |

---

## 5. Requerimientos No Funcionales

| ID | Categoría | Descripción | Criterio |
|----|-----------|-------------|----------|
| RNF01 | Rendimiento | Respuesta ≤ 3 segundos | 95% de solicitudes |
| RNF02 | Rendimiento | Soportar 50 usuarios concurrentes | Prueba de carga |
| RNF03 | Seguridad | Contraseñas cifradas con BCrypt | Sin texto plano en BD |
| RNF04 | Seguridad | Autenticación con JWT | Token con expiración |
| RNF05 | Seguridad | Endpoints admin protegidos por rol | Cliente no accede a /admin |
| RNF06 | Seguridad | Conexión BD con SQL Server Authentication | Credenciales por variable de entorno, sin texto plano en código |
| RNF07 | Usabilidad | Diseño responsive | Probado en 375px, 768px, 1280px |
| RNF08 | Usabilidad | Catálogo visual con imágenes | Cada producto con al menos 1 imagen |
| RNF09 | Usabilidad | Flujo de compra intuitivo | Sin capacitación previa |
| RNF10 | Disponibilidad | Ejecución estable en entorno local | Backend y BD accesibles en localhost |
| RNF11 | Despliegue | Frontend local | Vite dev server en localhost:5173 |
| RNF12 | Despliegue | Backend local | Spring Boot embebido en localhost:8080 |
| RNF13 | Despliegue | BD local SQL Server | Conexión estable en localhost:1433 |
| RNF14 | Calidad | Cobertura de pruebas ≥ 90% | Reporte JaCoCo — alcanzado: 92.7% líneas |
| RNF15 | Mantenibilidad | Arquitectura MVC por capas | Revisión en code review |
| RNF16 | Versionado | Código en GitHub | Rama main |

---

## 6. Historias de Usuario por Sprint

### Sprint 1 — Cimientos (Semanas 1-2) — 17 puntos
| ID | Historia | Puntos |
|----|----------|--------|
| HU-01 | Como cliente quiero registrarme para poder realizar pedidos | 3 |
| HU-02 | Como cliente quiero iniciar sesión para acceder a mi cuenta | 2 |
| HU-03 | Como admin quiero iniciar sesión para gestionar el sistema | 2 |
| HU-21 | Como cliente quiero contactar por WhatsApp con un clic | 1 |
| HU-22 | Como cliente quiero ver la información de contacto de la tienda | 1 |
| — | Configuración Spring Boot + JWT + BD + GitHub | 8 |

### Sprint 2 — Catálogo (Semanas 3-4) — 30 puntos
| ID | Historia | Puntos |
|----|----------|--------|
| HU-05 | Como cliente quiero buscar por marca, modelo, generación y motor | 5 |
| HU-06 | Como cliente quiero buscar por nombre o código del repuesto | 3 |
| HU-07 | Como cliente quiero filtrar por categoría | 3 |
| HU-08 | Como cliente quiero ver la ficha completa de un producto | 3 |
| HU-13 | Como admin quiero crear, editar y eliminar productos | 5 |
| HU-14 | Como admin quiero subir imágenes a los productos | 3 |
| HU-15 | Como admin quiero gestionar compatibilidades | 3 |
| HU-16 | Como admin quiero gestionar el catálogo automotriz | 5 |

### Sprint 3 — Pedidos (Semanas 5-6) — 19 puntos
| ID | Historia | Puntos |
|----|----------|--------|
| HU-09 | Como cliente quiero agregar productos al carrito | 3 |
| HU-10 | Como cliente quiero gestionar mi carrito | 3 |
| HU-11 | Como cliente quiero realizar un pedido con delivery | 5 |
| HU-12 | Como cliente quiero consultar el estado de mis pedidos | 3 |
| HU-17 | Como admin quiero gestionar y actualizar el estado de pedidos | 5 |

### Sprint 4 — Cierre (Semanas 7-8) — 18 puntos
| ID | Historia | Puntos |
|----|----------|--------|
| ~~HU-04~~ | ~~Como cliente quiero recuperar mi contraseña si la olvido~~ | ~~3~~ — **Fuera de alcance v1.0** |
| HU-18 | Como admin quiero configurar zonas y tarifas de delivery | 3 |
| HU-19 | Como admin quiero ver un dashboard con métricas del negocio | 5 |
| HU-20 | Como admin quiero generar reportes de ventas por período | 3 |
| — | Configuración SQL Server local + pruebas finales + cobertura JaCoCo | 7 |

---

## 7. Arquitectura del Sistema

### Stack tecnológico por capa
```
[Navegador]
    │  HTTP
    ▼
[React + Tailwind — localhost:5173 (Vite dev server)]
    │  REST API (JSON)
    ▼
[Spring Boot — localhost:8080]
    │  JPA/Hibernate
    ▼
[SQL Server — localhost:1433 (pacifico_db)]
```

### Estructura de paquetes — Backend
```
com.pacifico.repuestos
├── controller/     # Recibe peticiones HTTP, delega a Service
├── service/        # Lógica de negocio
├── repository/     # Acceso a datos (Spring Data JPA)
├── model/          # Entidades JPA (mapeo BD)
├── dto/            # Objetos de transferencia (request/response)
│   ├── request/
│   └── response/
├── security/       # JWT, filtros, configuración Spring Security
└── exception/      # Manejo global de errores
```

### Estructura de carpetas — Frontend
```
src/
├── pages/          # Vistas principales y admin
├── components/     # Componentes reutilizables
│   ├── layout/
│   ├── catalogo/
│   ├── carrito/
│   └── common/
├── context/        # AuthContext, CarritoContext
├── services/       # Llamadas a la API
└── utils/          # axiosConfig, constants
```

---

## 8. Modelo de Base de Datos

### Tablas del sistema (15 tablas)

| Tabla | Descripción |
|-------|-------------|
| usuarios | Usuarios del sistema con rol CLIENTE o ADMIN |
| clientes | Datos extendidos del cliente (teléfono, dirección, DNI) |
| marcas | Marcas de vehículos (Toyota, Nissan, etc.) |
| modelos | Modelos por marca (Hilux, Corolla, etc.) |
| generaciones | Generaciones por modelo con años |
| motores | Códigos de motor por generación (2GD-FTV, etc.) |
| categorias | Categorías de productos |
| productos | Repuestos con precio, descripción y medidas |
| inventario | Stock actual y mínimo por producto |
| imagenes_producto | Imágenes asociadas a cada producto |
| compatibilidades | Relación producto ↔ motor (N:M) |
| zonas_delivery | Zonas de envío con tarifas |
| pedidos | Pedidos con estado y totales |
| detalle_pedido | Líneas de cada pedido (producto, cantidad, precio) |
| envios | Datos de envío y seguimiento por pedido |

### Relaciones clave
```
usuarios (1) ──── (1) clientes
clientes (1) ──── (N) pedidos
pedidos  (1) ──── (N) detalle_pedido
pedidos  (1) ──── (1) envios
detalle_pedido (N) ──── (1) productos
productos (1) ──── (N) imagenes_producto
productos (1) ──── (N) compatibilidades
productos (N) ──── (1) categorias
productos (1) ──── (1) inventario
compatibilidades (N) ──── (1) motores
motores (N) ──── (1) generaciones
generaciones (N) ──── (1) modelos
modelos (N) ──── (1) marcas
pedidos (N) ──── (1) zonas_delivery
```

### Datos de catálogo (seed real)
**Total: 187 productos** (162 nuevos + 25 originales)

| Categoría | Productos |
|-----------|-----------|
| Suspensión | 72 |
| Reparación de Motor | 63 |
| Dirección | 23 |
| Lubricantes | 14 |
| Sistema Eléctrico | 9 |
| Frenos | 4 |
| Accesorios | 2 |

### Scripts SQL
- `database/01_ddl_create.sql` — Creación de tablas, índices y triggers
- `database/02_seed_data.sql` — Datos semilla (marcas, modelos, motores, productos)

---

## 9. Diseño de API REST

**Base URL:** `http://localhost:8080/api`  
**Formato:** JSON  
**Autenticación:** `Authorization: Bearer <JWT>`

### Endpoints por módulo

#### Auth — `/api/auth`
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| POST | /register | No | Registrar cliente |
| POST | /login | No | Iniciar sesión |
| GET | /health | No | Healthcheck del servicio de autenticación |

#### Productos — `/api/productos`
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| GET | / | No | Listar con filtros opcionales |
| GET | /{id} | No | Ver detalle |
| GET | /buscar?q= | No | Buscar por nombre/código |
| GET | /destacados | No | Listar productos destacados |
| POST | / | ADMIN | Crear producto |
| PUT | /{id} | ADMIN | Editar producto |
| DELETE | /{id} | ADMIN | Eliminar producto |
| POST | /{id}/imagenes | ADMIN | Subir imagen |
| DELETE | /imagenes/{imagenId} | ADMIN | Eliminar imagen |
| POST | /{id}/compatibilidades | ADMIN | Agregar compatibilidad producto-motor |
| DELETE | /{id}/compatibilidades | ADMIN | Eliminar compatibilidad producto-motor |

#### Categorías — `/api/categorias`
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| GET | /api/categorias | No | Listar categorías |
| POST | /api/categorias | ADMIN | Crear categoría |
| PUT | /api/categorias/{id} | ADMIN | Editar categoría |
| DELETE | /api/categorias/{id} | ADMIN | Eliminar categoría |

#### Catálogo — `/api/marcas`, `/api/modelos`, `/api/generaciones`, `/api/motores`
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| GET | /api/marcas | No | Listar marcas |
| POST | /api/marcas | ADMIN | Crear marca |
| PUT | /api/marcas/{id} | ADMIN | Editar marca |
| DELETE | /api/marcas/{id} | ADMIN | Eliminar marca |
| GET | /api/modelos?marcaId= | No | Modelos por marca |
| GET | /api/generaciones?modeloId= | No | Generaciones por modelo |
| GET | /api/motores?generacionId= | No | Motores por generación |

#### Clientes — `/api/clientes`
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| GET | /api/clientes | ADMIN | Listar clientes registrados |

#### Delivery — `/api/delivery`
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| GET | /api/delivery/zonas | No | Listar zonas de delivery con tarifas |
| GET | /api/delivery/zonas/{tipo} | No | Obtener zona por tipo (LOCAL / INTERPROVINCIAL) |

#### Pedidos — `/api/pedidos`
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| POST | / | CLIENTE | Crear pedido |
| GET | /mis-pedidos | CLIENTE | Historial del cliente |
| GET | /{id} | CLIENTE/ADMIN | Detalle del pedido |
| GET | / | ADMIN | Todos los pedidos |
| PUT | /{id}/estado | ADMIN | Actualizar estado |

#### Reportes — `/api/reportes`
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| GET | /dashboard | ADMIN | Métricas del dashboard |
| GET | /ventas?desde=&hasta= | ADMIN | Reporte por período |

### Códigos de respuesta HTTP
| Código | Significado |
|--------|-------------|
| 200 | OK — operación exitosa |
| 201 | Created — recurso creado |
| 400 | Bad Request — datos inválidos |
| 401 | Unauthorized — token ausente o inválido |
| 403 | Forbidden — rol insuficiente |
| 404 | Not Found — recurso no encontrado |
| 500 | Internal Server Error |

---

## 10. Estructura del Proyecto

### Repositorio GitHub
```
pacifico-repuestos/
├── frontend/
│   ├── src/
│   │   ├── pages/
│   │   ├── components/
│   │   ├── context/
│   │   ├── services/
│   │   └── utils/
│   ├── public/
│   ├── index.html
│   ├── vite.config.js
│   ├── tailwind.config.js
│   └── package.json
│
├── backend/
│   └── src/
│       ├── main/
│       │   ├── java/com/pacifico/repuestos/
│       │   │   ├── controller/
│       │   │   ├── service/
│       │   │   ├── repository/
│       │   │   ├── model/
│       │   │   ├── dto/
│       │   │   ├── security/
│       │   │   └── exception/
│       │   └── resources/
│       │       └── application.properties
│       └── test/
│           └── java/com/pacifico/repuestos/
│               ├── service/        # Pruebas unitarias
│               └── controller/     # Pruebas de integración
│       pom.xml
│
├── database/
│   ├── 01_ddl_create.sql
│   └── 02_seed_data.sql
│
├── docs/
│   ├── ESPECIFICACION.md           ← Este archivo (fuente de verdad)
│   ├── requerimientos.md
│   ├── historias-de-usuario.md
│   ├── product-backlog-sprints.md
│   ├── casos-de-uso.md
│   └── arquitectura.md
│
└── README.md
```

### Ramas Git
| Rama | Uso |
|------|-----|
| `main` | Única rama utilizada — código estable, todos los commits del proyecto |

---

## 11. Seguridad

### Flujo de autenticación JWT
```
1. Cliente envía POST /api/auth/login {correo, password}
2. AuthService valida credenciales con BCrypt
3. JwtUtil genera token JWT con: userId, correo, rol, expiración
4. Backend devuelve: { token, tipo: "Bearer", rol }
5. Frontend almacena el token en localStorage y lo gestiona mediante AuthContext
6. Cada petición protegida incluye: Authorization: Bearer <token>
7. JwtFilter valida el token antes de procesar la petición
```

### Reglas de acceso
| Ruta | Acceso |
|------|--------|
| `GET /api/productos/**` | Público |
| `GET /api/marcas/**`, `/api/modelos/**` | Público |
| `POST /api/auth/**` | Público |
| `POST /api/pedidos` | CLIENTE autenticado |
| `GET /api/pedidos/mis-pedidos` | CLIENTE autenticado |
| `POST/PUT/DELETE /api/productos/**` | ADMIN |
| `GET/PUT /api/pedidos/**` | ADMIN |
| `/api/reportes/**` | ADMIN |

### Dependencias de seguridad (pom.xml)
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-api</artifactId>
  <version>0.11.5</version>
</dependency>
```

---

## 12. Despliegue

> El proyecto se ejecuta de forma **local** (sin despliegue en la nube). Vercel/Render/Railway
> quedan fuera de alcance de la v1.0; ver [Restricciones y Decisiones Técnicas](#15-restricciones-y-decisiones-técnicas).

### Frontend — Local (Vite dev server)
```
Comando dev:    npm run dev
URL:            http://localhost:5173
Variable:       VITE_API_URL=http://localhost:8080/api
```

### Backend — Local (Spring Boot embebido)
```
Runtime:        Java 17
Comando build:  mvn clean package
Comando run:    mvn spring-boot:run
URL:            http://localhost:8080
Variables:
  DATABASE_URL=jdbc:sqlserver://localhost:1433;databaseName=pacifico_db;encrypt=true;trustServerCertificate=true
  DATABASE_USERNAME=sa
  DATABASE_PASSWORD=****
  JWT_SECRET=<clave-256-bits>
  JWT_EXPIRATION=86400000
```

### Base de Datos — SQL Server local
```
Motor:      Microsoft SQL Server
Host:       localhost
Puerto:     1433
BD:         pacifico_db
Auth:       SQL Server Authentication (usuario sa)
Scripts:    Ejecutar 01_ddl_create.sql → 02_seed_data.sql
```

---

## 13. Pruebas

### Resultado actual
| Métrica | Resultado |
|---------|-----------|
| Pruebas unitarias JUnit 5 | 125 — todas pasan (0 fallos) |
| Herramienta de cobertura | JaCoCo 0.8.11 |
| Cobertura de líneas | **92.7%** (509/549) — supera el mínimo de 90% |
| Gate `jacoco:check` | Cumplido (`mvn verify` → BUILD SUCCESS) |

### Estrategia de pruebas
| Tipo | Herramienta | Cobertura mínima | Ubicación |
|------|-------------|-----------------|-----------|
| Unitarias | JUnit 5 + Mockito | 90% (LINE) | `src/test/java/.../service/` |
| Integración | JUnit 5 + MockMvc | Flujos críticos | `src/test/java/.../controller/` |
| Cobertura | JaCoCo | ≥ 90% líneas | Reporte en `/target/site/jacoco` |

### Services a probar (prioridad)
1. `AuthService` — registro, login, validaciones
2. `ProductoService` — CRUD, búsqueda, filtros, stock e inventario (la lógica de inventario está integrada dentro de ProductoService, no en un servicio separado)
3. `PedidoService` — creación, estado, stock
4. `CatalogoService` — marcas, modelos, generaciones, motores
5. `ReporteService` — métricas, dashboard

### Configuración JaCoCo (pom.xml)
```xml
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.11</version>
  <configuration>
    <excludes>
      <exclude>**/model/**</exclude>
      <exclude>**/dto/**</exclude>
      <exclude>**/exception/**</exclude>
      <exclude>**/config/**</exclude>
      <exclude>**/*Application*</exclude>
      <exclude>**/security/SecurityConfig.class</exclude>
    </excludes>
  </configuration>
  <executions>
    <execution>
      <goals><goal>prepare-agent</goal></goals>
    </execution>
    <execution>
      <id>report</id>
      <phase>test</phase>
      <goals><goal>report</goal></goals>
    </execution>
    <execution>
      <id>check</id>
      <goals><goal>check</goal></goals>
      <configuration>
        <rules>
          <rule>
            <limits>
              <limit>
                <counter>LINE</counter>
                <value>COVEREDRATIO</value>
                <minimum>0.90</minimum>
              </limit>
            </limits>
          </rule>
        </rules>
      </configuration>
    </execution>
  </executions>
</plugin>
```

> Los paquetes `model`, `dto`, `exception`, `config` y la clase `*Application*` se excluyen del
> cálculo de cobertura por ser POJOs/DTOs generados con Lombok sin lógica de negocio propia.

---

## 14. Product Backlog y Sprints

| Sprint | Objetivo | Semanas | Puntos |
|--------|----------|---------|--------|
| Sprint 1 | Autenticación + estructura base | 1-2 | 17 |
| Sprint 2 | Catálogo + búsqueda + admin productos | 3-4 | 30 |
| Sprint 3 | Carrito + pedidos + admin pedidos | 5-6 | 19 |
| Sprint 4 | Delivery + reportes + despliegue | 7-8 | 18 |
| **Total** | | **8 semanas** | **84 pts** |

### Definición de Terminado (DoD) global
- Código implementado y funcional.
- Pruebas unitarias escritas con cobertura ≥ 90% (JaCoCo).
- Código fusionado a `develop` sin conflictos.
- Funcionalidad revisada contra los criterios de aceptación de la historia.
- Documentación actualizada si hubo cambios de diseño.

---

## 15. Restricciones y Decisiones Técnicas

| Decisión | Justificación |
|----------|--------------|
| Java 17 + Spring Boot 3.x | LTS estable, ecosistema maduro, soporte JPA/Security integrado |
| React 18 + Vite | Rendimiento en desarrollo, ecosistema amplio, ideal para SPA |
| Tailwind CSS | Desarrollo rápido de UI, diseño consistente sin CSS personalizado |
| SQL Server (reemplaza a PostgreSQL) | BD relacional disponible en el entorno de desarrollo, integración nativa con el driver `mssql-jdbc` |
| JWT stateless | Escala horizontalmente, sin necesidad de sesiones en servidor |
| Ejecución local (sin Vercel/Render/Railway) | Simplifica el alcance para la entrega v1.0; evita costos y configuración de servicios cloud |
| Exclusión de `model`/`dto`/`exception`/`config` en JaCoCo | Son clases de datos (POJOs con Lombok) sin lógica de negocio; se mide cobertura real sobre `service`/`controller`/`security` |
| Sin pago en línea v1.0 | Reduce complejidad; el pago se coordina por WhatsApp/efectivo |
| Sin IA operativa | El sistema usa IA solo como herramienta de desarrollo (flujo ADIPD + SDD con Kiro) |

---

> **IMPORTANTE:** Este documento debe mantenerse actualizado durante todo el proyecto.  
> Cualquier cambio en requerimientos, arquitectura o diseño de BD debe reflejarse aquí  
> antes de modificar el código.

---

*Fuente de verdad generada en la Etapa D (Diseño) del flujo ADIPD*  
*Proyecto: Pacífico Repuestos | Metodología: Scrum | Herramienta de apoyo: IA (Claude)*
