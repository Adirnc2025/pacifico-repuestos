# Product Backlog y Planificación de Sprints — Pacífico Repuestos

**Proyecto:** Pacífico Repuestos  
**Versión:** 1.0  
**Metodología:** Scrum  
**Etapa:** A — Análisis  
**Duración de sprint:** 2 semanas  
**Total de sprints:** 4  
**Duración total estimada:** 8 semanas  

---

## 1. Product Backlog Completo

El backlog está ordenado por prioridad (MoSCoW) y sprint asignado.

| ID | Historia de Usuario | Épica | Prioridad MoSCoW | Puntos | Sprint |
|----|-------------------|-------|-----------------|--------|--------|
| HU-01 | Registro de cliente | Autenticación | Must Have | 3 | 1 |
| HU-02 | Inicio de sesión cliente | Autenticación | Must Have | 2 | 1 |
| HU-03 | Inicio de sesión administrador | Autenticación | Must Have | 2 | 1 |
| HU-21 | Contacto por WhatsApp | Contacto | Must Have | 1 | 1 |
| HU-22 | Info contacto y ubicación | Contacto | Must Have | 1 | 1 |
| HU-05 | Buscar repuestos por vehículo | Catálogo | Must Have | 5 | 2 |
| HU-06 | Buscar por nombre o código | Catálogo | Must Have | 3 | 2 |
| HU-07 | Filtrar por categoría | Catálogo | Must Have | 3 | 2 |
| HU-08 | Ver detalle de producto | Catálogo | Must Have | 3 | 2 |
| HU-13 | Gestionar productos (admin) | Admin Productos | Must Have | 5 | 2 |
| HU-14 | Gestionar imágenes (admin) | Admin Productos | Must Have | 3 | 2 |
| HU-15 | Gestionar compatibilidades (admin) | Admin Productos | Must Have | 3 | 2 |
| HU-16 | Gestionar catálogo automotriz (admin) | Admin Catálogo | Must Have | 5 | 2 |
| HU-09 | Agregar producto al carrito | Pedidos | Must Have | 3 | 3 |
| HU-10 | Gestionar carrito | Pedidos | Must Have | 3 | 3 |
| HU-11 | Realizar pedido | Pedidos | Must Have | 5 | 3 |
| HU-12 | Consultar estado del pedido | Pedidos | Must Have | 3 | 3 |
| HU-17 | Gestionar pedidos (admin) | Admin Pedidos | Must Have | 5 | 3 |
| HU-04 | Recuperación de contraseña | Autenticación | Should Have | 3 | 4 |
| HU-18 | Gestionar zonas de delivery | Delivery | Should Have | 3 | 4 |
| HU-19 | Dashboard de administración | Reportes | Should Have | 5 | 4 |
| HU-20 | Reporte de ventas | Reportes | Should Have | 3 | 4 |

**Total puntos:** 71  

---

## 2. Épicas del Proyecto

| Épica | Descripción | Historias | Puntos totales |
|-------|-------------|-----------|----------------|
| E1 — Autenticación | Registro, login y recuperación de acceso | HU-01, HU-02, HU-03, HU-04 | 10 |
| E2 — Catálogo y Búsqueda | Búsqueda y visualización de productos | HU-05, HU-06, HU-07, HU-08 | 14 |
| E3 — Pedidos y Carrito | Flujo de compra completo | HU-09, HU-10, HU-11, HU-12 | 14 |
| E4 — Admin Productos | Gestión de catálogo por administrador | HU-13, HU-14, HU-15, HU-16 | 16 |
| E5 — Admin Pedidos/Delivery | Gestión operativa de pedidos | HU-17, HU-18 | 8 |
| E6 — Reportes | Dashboard y reportes de ventas | HU-19, HU-20 | 8 |
| E7 — Contacto | WhatsApp e información de tienda | HU-21, HU-22 | 2 |

---

## 3. Planificación de Sprints

---

### SPRINT 1 — Cimientos del sistema
**Período:** Semanas 1 – 2  
**Objetivo del sprint:** Tener el proyecto configurado, la base de datos creada, el sistema de autenticación funcionando y la estructura base del frontend desplegada.  
**Capacidad:** 18 puntos  

#### Backlog del Sprint 1

| ID | Historia | Puntos | Responsable |
|----|----------|--------|-------------|
| HU-01 | Registro de cliente | 3 | Full Stack |
| HU-02 | Inicio de sesión cliente | 2 | Full Stack |
| HU-03 | Inicio de sesión administrador | 2 | Full Stack |
| HU-21 | Contacto por WhatsApp | 1 | Frontend |
| HU-22 | Info contacto y ubicación | 1 | Frontend |
| — | Configuración proyecto Spring Boot | 2 | Backend |
| — | Configuración proyecto React + Tailwind | 2 | Frontend |
| — | Scripts SQL y modelo de BD PostgreSQL | 3 | BD |
| — | Configuración repositorio GitHub | 1 | DevOps |

**Total puntos Sprint 1:** 17 puntos

#### Tareas técnicas Sprint 1

**Backend:**
- Crear proyecto Spring Boot con dependencias (Spring Security, JWT, JPA, PostgreSQL)
- Configurar conexión a PostgreSQL en Railway
- Implementar entidades: Usuario, Cliente, Rol
- Implementar AuthController: `/api/auth/register`, `/api/auth/login`
- Configurar JWT (generación y validación de tokens)
- Implementar BCrypt para cifrado de contraseñas
- Pruebas unitarias: AuthService (cobertura ≥ 90%)

**Frontend:**
- Crear proyecto React con Vite + Tailwind CSS
- Configurar rutas con React Router
- Implementar página de inicio (estructura base)
- Implementar formulario de registro
- Implementar formulario de login
- Implementar header con logo, búsqueda y carrito
- Implementar footer con contacto, WhatsApp y ubicación
- Botón flotante de WhatsApp

**Base de Datos:**
- Ejecutar scripts DDL completos
- Crear tablas: usuarios, clientes, roles, marcas, modelos, generaciones, motores, categorias, productos, compatibilidades, pedidos, detalle_pedido, imagenes_producto, envios, inventario
- Insertar datos semilla (marcas y modelos iniciales)

**DevOps:**
- Crear repositorio GitHub con estructura: `/frontend`, `/backend`, `/database`, `/docs`, `/tests`
- Crear ramas: `main`, `develop`, `feature/sprint-1`
- Configurar `.gitignore` para Java y Node

#### Definición de Terminado (DoD) Sprint 1
- [ ] API de autenticación funciona y devuelve JWT válido
- [ ] Cliente puede registrarse e iniciar sesión desde el frontend
- [ ] Base de datos creada en Railway con todas las tablas
- [ ] Repositorio GitHub configurado con estructura correcta
- [ ] Footer con WhatsApp activo
- [ ] Pruebas unitarias de autenticación con cobertura ≥ 90%
- [ ] Código en rama `develop` sin conflictos

---

### SPRINT 2 — Catálogo y gestión de productos
**Período:** Semanas 3 – 4  
**Objetivo del sprint:** Tener el catálogo de productos funcional con búsqueda por vehículo, filtros, ficha de producto y el panel de administración para gestionar productos, imágenes, compatibilidades y catálogo automotriz.  
**Capacidad:** 27 puntos  

#### Backlog del Sprint 2

| ID | Historia | Puntos | Responsable |
|----|----------|--------|-------------|
| HU-05 | Buscar repuestos por vehículo | 5 | Full Stack |
| HU-06 | Buscar por nombre o código | 3 | Full Stack |
| HU-07 | Filtrar por categoría | 3 | Frontend |
| HU-08 | Ver detalle de producto | 3 | Full Stack |
| HU-13 | Gestionar productos (admin) | 5 | Full Stack |
| HU-14 | Gestionar imágenes (admin) | 3 | Full Stack |
| HU-15 | Gestionar compatibilidades (admin) | 3 | Full Stack |
| HU-16 | Gestionar catálogo automotriz (admin) | 5 | Full Stack |

**Total puntos Sprint 2:** 30 puntos

#### Tareas técnicas Sprint 2

**Backend:**
- Implementar entidades y repositorios: Marca, Modelo, Generacion, Motor, Categoria, Producto, Compatibilidad, ImagenProducto
- Implementar ProductoController con endpoints:
  - `GET /api/productos` (con filtros: marca, modelo, generacion, motor, categoria, búsqueda)
  - `GET /api/productos/{id}`
  - `POST /api/productos` (admin)
  - `PUT /api/productos/{id}` (admin)
  - `DELETE /api/productos/{id}` (admin)
- Implementar CatalogoController: CRUD para marcas, modelos, generaciones, motores
- Implementar CompatibilidadController: asociar/desasociar motores a productos
- Implementar ImagenController: subida y eliminación de imágenes
- Pruebas unitarias: ProductoService, CatalogoService (cobertura ≥ 90%)

**Frontend:**
- Implementar página de listado de productos con grilla visual
- Implementar buscador encadenado: Marca → Modelo → Generación → Motor
- Implementar barra de búsqueda con sugerencias
- Implementar filtros laterales por categoría
- Implementar ficha de producto con galería de imágenes y tabla de compatibilidades
- Implementar panel de administración — sección Productos (CRUD)
- Implementar subida de imágenes en el panel admin
- Implementar panel admin — sección Catálogo (marcas, modelos, generaciones, motores)

#### Definición de Terminado (DoD) Sprint 2
- [ ] Buscador por vehículo funciona con filtros encadenados
- [ ] Ficha de producto muestra imágenes y compatibilidades
- [ ] Administrador puede crear, editar y eliminar productos
- [ ] Administrador puede subir imágenes de productos
- [ ] Catálogo automotriz gestionable desde panel admin
- [ ] Pruebas unitarias de ProductoService y CatalogoService con cobertura ≥ 90%
- [ ] Código revisado y fusionado a `develop`

---

### SPRINT 3 — Carrito, pedidos y gestión operativa
**Período:** Semanas 5 – 6  
**Objetivo del sprint:** Implementar el flujo completo de compra: carrito, pedido, seguimiento de estado y gestión de pedidos por el administrador.  
**Capacidad:** 22 puntos  

#### Backlog del Sprint 3

| ID | Historia | Puntos | Responsable |
|----|----------|--------|-------------|
| HU-09 | Agregar producto al carrito | 3 | Full Stack |
| HU-10 | Gestionar carrito | 3 | Frontend |
| HU-11 | Realizar pedido | 5 | Full Stack |
| HU-12 | Consultar estado del pedido | 3 | Full Stack |
| HU-17 | Gestionar pedidos (admin) | 5 | Full Stack |

**Total puntos Sprint 3:** 19 puntos

#### Tareas técnicas Sprint 3

**Backend:**
- Implementar entidades: Pedido, DetallePedido, Envio
- Implementar PedidoController:
  - `POST /api/pedidos` — crear pedido
  - `GET /api/pedidos/cliente/{id}` — historial del cliente
  - `GET /api/pedidos/{id}` — detalle del pedido
  - `PUT /api/pedidos/{id}/estado` (admin) — actualizar estado
  - `GET /api/pedidos` (admin) — todos los pedidos con filtros
- Lógica de descuento de stock al confirmar pedido
- Validación de stock disponible antes de confirmar
- Pruebas unitarias: PedidoService (cobertura ≥ 90%)

**Frontend:**
- Implementar componente Carrito (sidebar o página)
- Implementar página de checkout con formulario de dirección y tipo de delivery
- Implementar página de confirmación de pedido con número de orden
- Implementar página "Mis pedidos" con historial y estados
- Implementar panel admin — sección Pedidos con filtros y cambio de estado

#### Definición de Terminado (DoD) Sprint 3
- [ ] Cliente puede agregar productos al carrito y modificarlos
- [ ] Flujo de pedido completo: carrito → checkout → confirmación
- [ ] Stock se descuenta al confirmar pedido
- [ ] Cliente puede ver historial y estado de sus pedidos
- [ ] Administrador puede gestionar y actualizar estado de pedidos
- [ ] Pruebas unitarias de PedidoService con cobertura ≥ 90%
- [ ] Código fusionado a `develop`

---

### SPRINT 4 — Delivery, reportes, despliegue y cierre
**Período:** Semanas 7 – 8  
**Objetivo del sprint:** Implementar gestión de delivery, dashboard de administración, reportes de ventas, y desplegar el sistema completo en producción (Vercel + Render + Railway).  
**Capacidad:** 18 puntos  

#### Backlog del Sprint 4

| ID | Historia | Puntos | Responsable |
|----|----------|--------|-------------|
| HU-04 | Recuperación de contraseña | 3 | Full Stack |
| HU-18 | Gestionar zonas de delivery | 3 | Full Stack |
| HU-19 | Dashboard de administración | 5 | Full Stack |
| HU-20 | Reporte de ventas | 3 | Full Stack |
| — | Despliegue frontend en Vercel | 2 | DevOps |
| — | Despliegue backend en Render | 2 | DevOps |
| — | Pruebas de integración y ajustes finales | 3 | QA |

**Total puntos Sprint 4:** 21 puntos

#### Tareas técnicas Sprint 4

**Backend:**
- Implementar entidades: ZonaDelivery, TarifaEnvio
- Implementar DeliveryController: CRUD zonas y tarifas
- Implementar ReporteController:
  - `GET /api/reportes/ventas?desde=&hasta=` — ventas por rango de fechas
  - `GET /api/reportes/dashboard` — métricas resumen
- Implementar recuperación de contraseña (token temporal por correo)
- Generar reporte JaCoCo final con cobertura global ≥ 90%

**Frontend:**
- Implementar página de recuperación de contraseña
- Implementar panel admin — sección Delivery (zonas y tarifas)
- Implementar Dashboard con métricas: pedidos del día, stock bajo, ingresos del mes
- Implementar página de reportes con filtro de fechas y tabla de ventas
- Ajustes responsive finales en todo el sistema

**DevOps / Despliegue:**
- Configurar variables de entorno en Render (backend)
- Configurar variables de entorno en Vercel (frontend)
- Verificar conexión Railway PostgreSQL desde Render
- Prueba de humo end-to-end en producción
- Documentar URL de producción en README

#### Definición de Terminado (DoD) Sprint 4
- [ ] Sistema completo desplegado en Vercel + Render + Railway
- [ ] URL de producción accesible y funcional
- [ ] Dashboard de administración muestra métricas reales
- [ ] Reporte de ventas por rango de fechas funcional
- [ ] Zonas de delivery configurables
- [ ] Reporte JaCoCo con cobertura global ≥ 90%
- [ ] Código fusionado a `main` con tag de versión `v1.0.0`
- [ ] README actualizado con URL de producción e instrucciones

---

## 4. Velocidad y Burndown Estimado

| Sprint | Puntos planificados | Semana fin |
|--------|-------------------|------------|
| Sprint 1 | 17 pts | Semana 2 |
| Sprint 2 | 30 pts | Semana 4 |
| Sprint 3 | 19 pts | Semana 6 |
| Sprint 4 | 21 pts | Semana 8 |
| **Total** | **87 pts** | **8 semanas** |

---

## 5. Roles Scrum del Proyecto

| Rol | Responsabilidad |
|-----|----------------|
| Product Owner | Define prioridades del backlog, acepta las historias completadas |
| Scrum Master | Guía el proceso, elimina impedimentos, facilita ceremonias |
| Development Team | Diseña, desarrolla, prueba y despliega el sistema |

---

## 6. Ceremonias Scrum por Sprint

| Ceremonia | Frecuencia | Duración estimada |
|-----------|-----------|-------------------|
| Sprint Planning | Inicio de cada sprint | 2 horas |
| Daily Standup | Cada día hábil | 15 minutos |
| Sprint Review | Fin de cada sprint | 1 hora |
| Sprint Retrospective | Fin de cada sprint | 30 minutos |

---

## 7. Estructura del Repositorio GitHub

```
pacifico-repuestos/
├── frontend/               # Proyecto React + Tailwind CSS
│   ├── src/
│   ├── public/
│   └── package.json
├── backend/                # Proyecto Spring Boot
│   ├── src/
│   │   ├── main/java/
│   │   └── test/java/
│   └── pom.xml
├── database/               # Scripts SQL
│   ├── 01_ddl_create.sql
│   ├── 02_seed_data.sql
│   └── README.md
├── docs/                   # Documentación del proyecto
│   ├── requerimientos.md
│   ├── historias-de-usuario.md
│   ├── product-backlog-sprints.md
│   ├── casos-de-uso.md
│   ├── arquitectura.md
│   ├── ESPECIFICACION.md
│   └── diagramas/
└── README.md
```

---

*Documento generado en la Etapa A (Análisis) del flujo ADIPD — Proyecto Pacífico Repuestos*  
*Metodología: Scrum | Herramienta de apoyo: IA (Claude)*
