# Requerimientos del Sistema — Pacífico Repuestos

**Proyecto:** Pacífico Repuestos — Sistema Web de Gestión y Comercialización de Repuestos Automotrices  
**Versión:** 1.0  
**Fecha:** 2025  
**Ubicación:** Ayacucho, Perú  
**Metodología:** Scrum  
**Elaborado con apoyo de:** IA (Claude) — flujo ADIPD  

---

## 1. Requerimientos Funcionales

### Módulo 1 — Autenticación y Usuarios

| ID   | Requerimiento | Actor | Prioridad |
|------|--------------|-------|-----------|
| RF01 | El sistema debe permitir al cliente registrarse con nombre, correo, contraseña y teléfono. | Cliente | Alta |
| RF02 | El sistema debe permitir al cliente iniciar sesión con correo y contraseña. | Cliente | Alta |
| RF03 | El sistema debe permitir al administrador iniciar sesión con credenciales especiales. | Administrador | Alta |
| RF04 | El sistema debe permitir cerrar sesión a cualquier usuario autenticado. | Cliente / Admin | Alta |
| RF05 | El sistema debe enviar confirmación de registro al correo del cliente. | Cliente | Media |
| RF06 | El sistema debe permitir recuperar contraseña mediante enlace al correo. | Cliente | Media |

---

### Módulo 2 — Catálogo Automotriz

| ID   | Requerimiento | Actor | Prioridad |
|------|--------------|-------|-----------|
| RF07 | El sistema debe mostrar un catálogo de marcas de vehículos disponibles. | Cliente | Alta |
| RF08 | El sistema debe mostrar los modelos asociados a cada marca. | Cliente | Alta |
| RF09 | El sistema debe mostrar las generaciones asociadas a cada modelo. | Cliente | Alta |
| RF10 | El sistema debe mostrar los motores asociados a cada generación. | Cliente | Alta |
| RF11 | El sistema debe permitir al administrador crear, editar y eliminar marcas. | Administrador | Alta |
| RF12 | El sistema debe permitir al administrador crear, editar y eliminar modelos. | Administrador | Alta |
| RF13 | El sistema debe permitir al administrador crear, editar y eliminar generaciones. | Administrador | Alta |
| RF14 | El sistema debe permitir al administrador crear, editar y eliminar motores. | Administrador | Alta |

---

### Módulo 3 — Productos

| ID   | Requerimiento | Actor | Prioridad |
|------|--------------|-------|-----------|
| RF15 | El sistema debe mostrar productos con nombre, descripción, precio, stock e imágenes. | Cliente | Alta |
| RF16 | El sistema debe permitir filtrar productos por marca, modelo, generación, motor y categoría. | Cliente | Alta |
| RF17 | El sistema debe mostrar las compatibilidades de cada producto con vehículos y motores. | Cliente | Alta |
| RF18 | El sistema debe permitir buscar productos mediante barra de búsqueda por nombre o código. | Cliente | Alta |
| RF19 | El sistema debe mostrar productos destacados en la página principal. | Cliente | Media |
| RF20 | El sistema debe permitir al administrador crear, editar y eliminar productos. | Administrador | Alta |
| RF21 | El sistema debe permitir al administrador asociar compatibilidades a cada producto. | Administrador | Alta |
| RF22 | El sistema debe permitir al administrador subir y gestionar imágenes de productos. | Administrador | Alta |
| RF23 | El sistema debe permitir al administrador gestionar el stock de cada producto. | Administrador | Alta |
| RF24 | El sistema debe permitir al administrador gestionar categorías de productos. | Administrador | Alta |

---

### Módulo 4 — Carrito y Pedidos

| ID   | Requerimiento | Actor | Prioridad |
|------|--------------|-------|-----------|
| RF25 | El sistema debe permitir al cliente agregar productos al carrito de compras. | Cliente | Alta |
| RF26 | El sistema debe permitir al cliente modificar cantidades o eliminar productos del carrito. | Cliente | Alta |
| RF27 | El sistema debe permitir al cliente realizar un pedido desde el carrito. | Cliente | Alta |
| RF28 | El sistema debe permitir al cliente consultar el estado de su pedido. | Cliente | Alta |
| RF29 | El sistema debe notificar al cliente el cambio de estado de su pedido. | Cliente | Media |
| RF30 | El sistema debe permitir al administrador gestionar y actualizar el estado de pedidos. | Administrador | Alta |
| RF31 | El sistema debe registrar el detalle completo de cada pedido (productos, cantidades, precios). | Sistema | Alta |

---

### Módulo 5 — Delivery

| ID   | Requerimiento | Actor | Prioridad |
|------|--------------|-------|-----------|
| RF32 | El sistema debe ofrecer opciones de delivery local (Ayacucho) e interprovincial. | Cliente | Alta |
| RF33 | El sistema debe permitir al cliente ingresar la dirección de entrega al realizar el pedido. | Cliente | Alta |
| RF34 | El sistema debe permitir al administrador gestionar zonas y tarifas de delivery. | Administrador | Media |
| RF35 | El sistema debe mostrar el costo de delivery según la zona seleccionada. | Cliente | Media |

---

### Módulo 6 — Contacto y Comunicación

| ID   | Requerimiento | Actor | Prioridad |
|------|--------------|-------|-----------|
| RF36 | El sistema debe mostrar un botón de contacto directo por WhatsApp. | Cliente | Alta |
| RF37 | El sistema debe mostrar información de contacto, ubicación y horarios en el footer. | Cliente | Alta |

---

### Módulo 7 — Reportes y Gestión

| ID   | Requerimiento | Actor | Prioridad |
|------|--------------|-------|-----------|
| RF38 | El sistema debe permitir al administrador generar reportes de ventas por período. | Administrador | Media |
| RF39 | El sistema debe permitir al administrador gestionar la lista de clientes registrados. | Administrador | Media |
| RF40 | El sistema debe mostrar un dashboard con resumen de pedidos, stock y ventas. | Administrador | Media |

---

## 2. Requerimientos No Funcionales

### RNF — Rendimiento

| ID    | Requerimiento | Criterio de aceptación |
|-------|--------------|------------------------|
| RNF01 | El sistema debe responder a las peticiones del usuario en menos de 3 segundos bajo condiciones normales. | Tiempo de respuesta ≤ 3s en el 95% de las solicitudes. |
| RNF02 | El sistema debe soportar al menos 50 usuarios concurrentes sin degradación perceptible. | Pruebas de carga con 50 usuarios simultáneos. |

---

### RNF — Seguridad

| ID    | Requerimiento | Criterio de aceptación |
|-------|--------------|------------------------|
| RNF03 | El sistema debe cifrar las contraseñas de usuarios usando BCrypt. | Ninguna contraseña se almacena en texto plano. |
| RNF04 | El sistema debe usar JWT para la autenticación y autorización de sesiones. | Tokens con expiración configurable. |
| RNF05 | El sistema debe proteger los endpoints de administración con roles y permisos. | Un cliente no puede acceder a rutas de administrador. |
| RNF06 | El sistema debe usar HTTPS en todos los entornos de producción. | Certificado SSL activo en Vercel y Render. |

---

### RNF — Usabilidad

| ID    | Requerimiento | Criterio de aceptación |
|-------|--------------|------------------------|
| RNF07 | El sistema debe ser responsive y adaptarse a dispositivos móviles, tablets y escritorio. | Diseño probado en resoluciones 375px, 768px y 1280px. |
| RNF08 | El catálogo debe ser visual, mostrando imágenes de productos de forma clara. | Cada producto muestra al menos una imagen. |
| RNF09 | El sistema debe ser intuitivo y no requerir capacitación para el cliente. | Flujo de compra completado sin asistencia en pruebas de usabilidad. |

---

### RNF — Disponibilidad y Despliegue

| ID    | Requerimiento | Criterio de aceptación |
|-------|--------------|------------------------|
| RNF10 | El sistema debe estar disponible el 99% del tiempo (excluyendo mantenimiento programado). | Monitoreo de uptime en Render y Vercel. |
| RNF11 | El frontend debe desplegarse en Vercel. | URL pública accesible desde internet. |
| RNF12 | El backend debe desplegarse en Render. | API REST accesible públicamente. |
| RNF13 | La base de datos debe estar en Railway PostgreSQL. | Conexión estable desde el backend en Render. |

---

### RNF — Mantenibilidad y Calidad

| ID    | Requerimiento | Criterio de aceptación |
|-------|--------------|------------------------|
| RNF14 | El código del backend debe tener una cobertura de pruebas unitarias mínima del 90%. | Reporte JaCoCo con cobertura ≥ 90%. |
| RNF15 | El sistema debe seguir el patrón de arquitectura MVC con capas bien definidas (Controller, Service, Repository, DTO). | Revisión de arquitectura en code review. |
| RNF16 | El código fuente completo debe estar versionado en GitHub con estructura definida. | Repositorio público/privado con ramas main y develop. |

---

## 3. Restricciones del Sistema

- Lenguaje backend: Java con Spring Boot.
- Lenguaje frontend: JavaScript con React y Tailwind CSS.
- Base de datos: PostgreSQL exclusivamente.
- IDE de desarrollo: Visual Studio Code.
- El sistema NO incorpora inteligencia artificial como funcionalidad operativa.
- El pago en línea no está incluido en el alcance v1.0 (se registra el pedido, el pago se coordina externamente).

---

## 4. Actores del Sistema

| Actor | Descripción |
|-------|-------------|
| Cliente | Usuario registrado que navega el catálogo, realiza pedidos y consulta su historial. |
| Administrador | Usuario con acceso total al panel de gestión: productos, pedidos, reportes y configuración. |
| Sistema | Procesos automáticos: notificaciones, actualización de stock, generación de reportes. |

---

*Documento generado en la Etapa A (Análisis) del flujo ADIPD — Proyecto Pacífico Repuestos*  
*Metodología: Scrum | Herramienta de apoyo: IA (Claude)*
