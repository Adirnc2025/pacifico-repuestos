# HISTORIAS_USUARIO.md — Pacífico Repuestos
# Historias de Usuario con Criterios de Aceptación (formato WHEN/SHALL)

**Proyecto:** Pacífico Repuestos  
**Versión:** 1.0.0  
**Fecha:** Julio 2026  
**Fuente de verdad:** [ESPECIFICACION.md](./ESPECIFICACION.md)  
**Módulos cubiertos:** Autenticación JWT · Catálogo y Búsqueda por Compatibilidad · Carrito y Pedidos

---

## ÍNDICE

1. [Módulo Autenticación con JWT](#módulo-1-autenticación-con-jwt)
   - [HU-01 — Registro de cliente](#hu-01--registro-de-cliente)
   - [HU-02 — Inicio de sesión del cliente](#hu-02--inicio-de-sesión-del-cliente)
   - [HU-03 — Inicio de sesión del administrador](#hu-03--inicio-de-sesión-del-administrador)
   - [HU-04 — Recuperación de contraseña](#hu-04--recuperación-de-contraseña)
2. [Módulo Catálogo con Búsqueda por Compatibilidad](#módulo-2-catálogo-con-búsqueda-por-compatibilidad-de-vehículo)
   - [HU-05 — Búsqueda por vehículo](#hu-05--búsqueda-por-vehículo-marca-modelo-generación-motor)
   - [HU-06 — Búsqueda por nombre o código](#hu-06--búsqueda-por-nombre-o-código-de-repuesto)
   - [HU-07 — Filtrado por categoría](#hu-07--filtrado-por-categoría)
   - [HU-08 — Ficha de detalle de producto](#hu-08--ficha-de-detalle-de-producto)
3. [Módulo Carrito de Compras y Pedidos](#módulo-3-carrito-de-compras-y-gestión-de-pedidos)
   - [HU-09 — Agregar productos al carrito](#hu-09--agregar-productos-al-carrito)
   - [HU-10 — Gestión del carrito](#hu-10--gestión-del-carrito)
   - [HU-11 — Realizar pedido con delivery](#hu-11--realizar-pedido-con-delivery)
   - [HU-12 — Consulta de estado e historial de pedidos](#hu-12--consulta-de-estado-e-historial-de-pedidos)
   - [HU-17 — Gestión de pedidos por el administrador](#hu-17--gestión-de-pedidos-por-el-administrador)

---

## MÓDULO 1: AUTENTICACIÓN CON JWT

> **Requerimientos funcionales relacionados:** RF01, RF02, RF03, RF04, RF05, RF06  
> **Requerimientos no funcionales relacionados:** RNF03 (BCrypt), RNF04 (JWT), RNF05 (roles)

---

### HU-01 — Registro de cliente

**Como** cliente nuevo  
**Quiero** registrarme en la plataforma  
**Para** poder realizar pedidos y acceder a mi historial de compras

| # | WHEN (Cuando) | SHALL (El sistema debe) |
|---|---------------|------------------------|
| CA-01.1 | Cuando el cliente completa el formulario con nombre, correo, contraseña y teléfono válidos y envía el registro | El sistema SHALL crear la cuenta, cifrar la contraseña con BCrypt y devolver HTTP 201 con los datos del usuario registrado |
| CA-01.2 | Cuando el cliente intenta registrarse con un correo que ya existe en el sistema | El sistema SHALL rechazar el registro y devolver HTTP 400 con el mensaje "El correo ya está registrado" |
| CA-01.3 | Cuando el cliente envía el formulario con campos obligatorios vacíos (nombre, correo o contraseña) | El sistema SHALL devolver HTTP 400 indicando los campos faltantes sin crear ningún registro |
| CA-01.4 | Cuando el cliente ingresa un correo con formato inválido (sin @, sin dominio) | El sistema SHALL devolver HTTP 400 con el mensaje "Formato de correo inválido" |
| CA-01.5 | Cuando el registro es exitoso | El sistema SHALL asignar automáticamente el rol CLIENTE al nuevo usuario |
| CA-01.6 | Cuando el registro es exitoso | El sistema SHALL almacenar la contraseña cifrada con BCrypt, nunca en texto plano en la base de datos |

---

### HU-02 — Inicio de sesión del cliente

**Como** cliente registrado  
**Quiero** iniciar sesión con mi correo y contraseña  
**Para** acceder a mi cuenta y realizar compras

| # | WHEN (Cuando) | SHALL (El sistema debe) |
|---|---------------|------------------------|
| CA-02.1 | Cuando el cliente envía credenciales correctas (correo + contraseña) al endpoint `POST /api/auth/login` | El sistema SHALL devolver HTTP 200 con un token JWT válido, el tipo "Bearer" y el rol del usuario |
| CA-02.2 | Cuando el cliente envía una contraseña incorrecta para un correo existente | El sistema SHALL devolver HTTP 401 con el mensaje "Credenciales inválidas" sin revelar cuál campo es incorrecto |
| CA-02.3 | Cuando el cliente envía un correo que no existe en el sistema | El sistema SHALL devolver HTTP 401 con el mensaje "Credenciales inválidas" |
| CA-02.4 | Cuando el cliente obtiene el token JWT | El sistema SHALL incluir en el payload: userId, correo, rol y tiempo de expiración (86400000 ms / 24 horas) |
| CA-02.5 | Cuando el token JWT expira | El sistema SHALL rechazar las peticiones protegidas devolviendo HTTP 401 |
| CA-02.6 | Cuando el cliente accede a un endpoint protegido sin incluir el header `Authorization: Bearer <token>` | El sistema SHALL devolver HTTP 401 sin procesar la petición |

---

### HU-03 — Inicio de sesión del administrador

**Como** administrador  
**Quiero** iniciar sesión con mis credenciales de rol ADMIN  
**Para** acceder al panel de gestión del sistema

| # | WHEN (Cuando) | SHALL (El sistema debe) |
|---|---------------|------------------------|
| CA-03.1 | Cuando el administrador inicia sesión con credenciales válidas de rol ADMIN | El sistema SHALL devolver HTTP 200 con un token JWT que incluya el rol "ADMIN" |
| CA-03.2 | Cuando un usuario con rol CLIENTE intenta acceder a rutas `POST/PUT/DELETE /api/productos/**` | El sistema SHALL devolver HTTP 403 Forbidden sin ejecutar la operación |
| CA-03.3 | Cuando un usuario con rol CLIENTE intenta acceder a `/api/reportes/**` | El sistema SHALL devolver HTTP 403 Forbidden |
| CA-03.4 | Cuando el administrador accede con su token ADMIN a cualquier endpoint protegido | El sistema SHALL procesar la petición y devolver HTTP 200 con la información solicitada |

---

### HU-04 — Recuperación de contraseña

**Como** cliente  
**Quiero** recuperar mi contraseña si la olvido  
**Para** recuperar el acceso a mi cuenta sin perder mis datos

| # | WHEN (Cuando) | SHALL (El sistema debe) |
|---|---------------|------------------------|
| CA-04.1 | Cuando el cliente solicita recuperación con un correo registrado en `POST /api/auth/recuperar` | El sistema SHALL enviar un enlace de restablecimiento al correo y devolver HTTP 200 |
| CA-04.2 | Cuando el cliente solicita recuperación con un correo que no existe | El sistema SHALL devolver HTTP 200 sin revelar si el correo existe (por seguridad) |
| CA-04.3 | Cuando el cliente usa el enlace de recuperación dentro de los 30 minutos de vigencia | El sistema SHALL permitir ingresar y confirmar una nueva contraseña |
| CA-04.4 | Cuando el cliente usa el enlace de recuperación después de 30 minutos | El sistema SHALL rechazar la solicitud con HTTP 400 indicando que el enlace ha expirado |
| CA-04.5 | Cuando el cliente restablece exitosamente la contraseña | El sistema SHALL cifrar la nueva contraseña con BCrypt e invalidar el enlace de recuperación usado |

---

## MÓDULO 2: CATÁLOGO CON BÚSQUEDA POR COMPATIBILIDAD DE VEHÍCULO

> **Requerimientos funcionales relacionados:** RF07, RF08, RF09, RF10, RF15, RF16, RF17, RF18, RF19  
> **Requerimientos no funcionales relacionados:** RNF07 (responsive), RNF08 (imágenes), RNF09 (flujo intuitivo)

---

### HU-05 — Búsqueda por vehículo (marca, modelo, generación, motor)

**Como** cliente  
**Quiero** buscar repuestos filtrando por la marca, modelo, generación y motor de mi vehículo  
**Para** encontrar únicamente los repuestos compatibles con mi auto

| # | WHEN (Cuando) | SHALL (El sistema debe) |
|---|---------------|------------------------|
| CA-05.1 | Cuando el cliente accede al catálogo | El sistema SHALL mostrar la lista de marcas de vehículos disponibles obtenida de `GET /api/marcas` |
| CA-05.2 | Cuando el cliente selecciona una marca | El sistema SHALL cargar dinámicamente los modelos de esa marca desde `GET /api/modelos?marcaId={id}` |
| CA-05.3 | Cuando el cliente selecciona un modelo | El sistema SHALL cargar dinámicamente las generaciones de ese modelo desde `GET /api/generaciones?modeloId={id}` |
| CA-05.4 | Cuando el cliente selecciona una generación | El sistema SHALL cargar dinámicamente los motores de esa generación desde `GET /api/motores?generacionId={id}` |
| CA-05.5 | Cuando el cliente completa la selección hasta el motor y aplica el filtro | El sistema SHALL mostrar únicamente los productos compatibles con esa combinación, consultando la tabla `compatibilidades` |
| CA-05.6 | Cuando la combinación seleccionada no tiene productos compatibles | El sistema SHALL mostrar el mensaje "No se encontraron repuestos para este vehículo" sin errores |
| CA-05.7 | Cuando el cliente selecciona solo marca sin completar los demás filtros | El sistema SHALL mantener deshabilitados los selectores dependientes hasta que se complete la selección en cascada |

---

### HU-06 — Búsqueda por nombre o código de repuesto

**Como** cliente  
**Quiero** buscar un repuesto por su nombre o código  
**Para** encontrarlo rápidamente sin navegar por el catálogo completo

| # | WHEN (Cuando) | SHALL (El sistema debe) |
|---|---------------|------------------------|
| CA-06.1 | Cuando el cliente ingresa 3 o más caracteres en el campo de búsqueda | El sistema SHALL consultar `GET /api/productos/buscar?q={término}` y mostrar sugerencias en tiempo real |
| CA-06.2 | Cuando el cliente ingresa menos de 3 caracteres | El sistema SHALL no ejecutar la búsqueda ni mostrar sugerencias |
| CA-06.3 | Cuando no existen productos que coincidan con el término buscado | El sistema SHALL mostrar el mensaje "No se encontraron productos para tu búsqueda" |
| CA-06.4 | Cuando el cliente selecciona una sugerencia del listado | El sistema SHALL redirigir al detalle del producto seleccionado |

---

### HU-07 — Filtrado por categoría

**Como** cliente  
**Quiero** filtrar los productos por categoría  
**Para** ver solo el tipo de repuesto que necesito

| # | WHEN (Cuando) | SHALL (El sistema debe) |
|---|---------------|------------------------|
| CA-07.1 | Cuando el cliente accede al catálogo | El sistema SHALL mostrar las categorías disponibles (Suspensión, Motor, Dirección, Frenos, etc.) como opciones de filtro |
| CA-07.2 | Cuando el cliente selecciona una categoría | El sistema SHALL filtrar y mostrar solo los productos pertenecientes a esa categoría |
| CA-07.3 | Cuando el cliente combina filtro de categoría con filtro de vehículo | El sistema SHALL aplicar ambos filtros simultáneamente y mostrar los productos que cumplan ambas condiciones |
| CA-07.4 | Cuando el cliente limpia el filtro de categoría | El sistema SHALL mostrar todos los productos del catálogo sin restricción de categoría |

---

### HU-08 — Ficha de detalle de producto

**Como** cliente  
**Quiero** ver la ficha completa de un repuesto  
**Para** conocer sus características, compatibilidades e imágenes antes de comprarlo

| # | WHEN (Cuando) | SHALL (El sistema debe) |
|---|---------------|------------------------|
| CA-08.1 | Cuando el cliente hace clic en un producto del catálogo | El sistema SHALL mostrar la ficha con: nombre, descripción, precio, stock disponible, categoría e imágenes |
| CA-08.2 | Cuando el producto tiene asociadas compatibilidades de vehículo | El sistema SHALL listar todos los vehículos compatibles (marca, modelo, generación, motor) |
| CA-08.3 | Cuando el producto tiene stock igual a 0 | El sistema SHALL mostrar la etiqueta "Sin stock" y deshabilitar el botón "Agregar al carrito" |
| CA-08.4 | Cuando el producto tiene al menos una imagen registrada | El sistema SHALL mostrar las imágenes del producto con navegación entre ellas |
| CA-08.5 | Cuando se solicita un producto con un ID inexistente | El sistema SHALL devolver HTTP 404 con el mensaje "Producto no encontrado" |

---

## MÓDULO 3: CARRITO DE COMPRAS Y GESTIÓN DE PEDIDOS

> **Requerimientos funcionales relacionados:** RF25, RF26, RF27, RF28, RF29, RF30, RF31, RF32, RF33  
> **Requerimientos no funcionales relacionados:** RNF01 (respuesta ≤ 3s), RNF04 (JWT), RNF09 (flujo intuitivo)

---

### HU-09 — Agregar productos al carrito

**Como** cliente  
**Quiero** agregar repuestos al carrito de compras  
**Para** acumularlos antes de realizar el pedido

| # | WHEN (Cuando) | SHALL (El sistema debe) |
|---|---------------|------------------------|
| CA-09.1 | Cuando el cliente hace clic en "Agregar al carrito" en un producto con stock disponible | El sistema SHALL agregar el producto al CarritoContext y mostrar la cantidad actualizada en el ícono del carrito |
| CA-09.2 | Cuando el cliente agrega el mismo producto más de una vez | El sistema SHALL incrementar la cantidad en el carrito sin duplicar la línea del producto |
| CA-09.3 | Cuando el cliente intenta agregar una cantidad que supera el stock disponible | El sistema SHALL limitar la cantidad al stock disponible y mostrar el aviso "Stock máximo disponible: {n}" |
| CA-09.4 | Cuando el cliente no ha iniciado sesión e intenta agregar al carrito | El sistema SHALL redirigir al formulario de login antes de agregar el producto |
| CA-09.5 | Cuando el cliente agrega un producto exitosamente | El sistema SHALL mostrar una confirmación visual (toast/notificación) indicando que el producto fue agregado |

---

### HU-10 — Gestión del carrito

**Como** cliente  
**Quiero** ver, modificar y vaciar mi carrito  
**Para** ajustar mi compra antes de confirmar el pedido

| # | WHEN (Cuando) | SHALL (El sistema debe) |
|---|---------------|------------------------|
| CA-10.1 | Cuando el cliente abre el carrito | El sistema SHALL mostrar todos los productos agregados con nombre, imagen, cantidad, precio unitario y subtotal por línea |
| CA-10.2 | Cuando el cliente aumenta la cantidad de un producto en el carrito | El sistema SHALL actualizar el subtotal y el total general en tiempo real |
| CA-10.3 | Cuando el cliente reduce la cantidad de un producto a cero | El sistema SHALL eliminar automáticamente esa línea del carrito |
| CA-10.4 | Cuando el cliente hace clic en "Eliminar" en una línea del carrito | El sistema SHALL remover ese producto del carrito y recalcular el total |
| CA-10.5 | Cuando el carrito está vacío | El sistema SHALL mostrar el mensaje "Tu carrito está vacío" con un enlace para ir al catálogo |
| CA-10.6 | Cuando el cliente visualiza el carrito | El sistema SHALL mostrar el total general calculado como la suma de todos los subtotales |

---

### HU-11 — Realizar pedido con delivery

**Como** cliente  
**Quiero** confirmar mi pedido indicando la dirección y tipo de delivery  
**Para** recibir los repuestos que compré

| # | WHEN (Cuando) | SHALL (El sistema debe) |
|---|---------------|------------------------|
| CA-11.1 | Cuando el cliente procede al checkout con al menos un producto en el carrito | El sistema SHALL mostrar el formulario de pedido con los campos: dirección de entrega y tipo de delivery (local Ayacucho / interprovincial) |
| CA-11.2 | Cuando el cliente selecciona una zona de delivery | El sistema SHALL mostrar el costo de envío correspondiente a esa zona |
| CA-11.3 | Cuando el cliente confirma el pedido con todos los datos completos | El sistema SHALL crear el pedido en `POST /api/pedidos`, devolver HTTP 201 con el ID del pedido, descontar el stock de cada producto y vaciar el carrito |
| CA-11.4 | Cuando el pedido se crea exitosamente | El sistema SHALL registrar el estado inicial del pedido como "PENDIENTE" |
| CA-11.5 | Cuando el cliente intenta confirmar el pedido con el campo dirección vacío | El sistema SHALL bloquear el envío y mostrar "La dirección de entrega es obligatoria" |
| CA-11.6 | Cuando el stock de un producto se agota durante el proceso de confirmación | El sistema SHALL notificar al cliente que el producto ya no está disponible y no crear el pedido |
| CA-11.7 | Cuando el pedido es creado | El sistema SHALL registrar el detalle completo (producto, cantidad, precio unitario al momento de la compra) en la tabla `detalle_pedido` |

---

### HU-12 — Consulta de estado e historial de pedidos

**Como** cliente  
**Quiero** consultar el estado y el historial de todos mis pedidos  
**Para** saber en qué etapa está mi compra

| # | WHEN (Cuando) | SHALL (El sistema debe) |
|---|---------------|------------------------|
| CA-12.1 | Cuando el cliente accede a "Mis Pedidos" con sesión activa | El sistema SHALL consultar `GET /api/pedidos/mis-pedidos` y listar solo los pedidos del cliente autenticado |
| CA-12.2 | Cuando el cliente visualiza la lista de pedidos | El sistema SHALL mostrar por cada pedido: número, fecha, total y estado actual (PENDIENTE, EN_PROCESO, ENVIADO, ENTREGADO, CANCELADO) |
| CA-12.3 | Cuando el cliente hace clic en un pedido de su historial | El sistema SHALL mostrar el detalle completo: productos, cantidades, precios, dirección de entrega y datos del envío |
| CA-12.4 | Cuando un cliente intenta acceder al detalle de un pedido que no le pertenece | El sistema SHALL devolver HTTP 403 Forbidden |
| CA-12.5 | Cuando el cliente no tiene pedidos registrados | El sistema SHALL mostrar el mensaje "Aún no tienes pedidos" con un enlace al catálogo |

---

### HU-17 — Gestión de pedidos por el administrador

**Como** administrador  
**Quiero** ver todos los pedidos y actualizar su estado  
**Para** gestionar el flujo operativo de despacho y entrega

| # | WHEN (Cuando) | SHALL (El sistema debe) |
|---|---------------|------------------------|
| CA-17.1 | Cuando el administrador accede a la gestión de pedidos | El sistema SHALL consultar `GET /api/pedidos` (ADMIN) y listar todos los pedidos con cliente, fecha, total y estado |
| CA-17.2 | Cuando el administrador actualiza el estado de un pedido vía `PUT /api/pedidos/{id}/estado` | El sistema SHALL persistir el nuevo estado y devolver HTTP 200 con el pedido actualizado |
| CA-17.3 | Cuando el administrador intenta asignar un estado inválido no contemplado en el sistema | El sistema SHALL devolver HTTP 400 indicando los estados válidos permitidos |
| CA-17.4 | Cuando el estado de un pedido cambia | El sistema SHALL registrar el cambio con su fecha/hora para trazabilidad |
| CA-17.5 | Cuando un usuario con rol CLIENTE intenta acceder a `GET /api/pedidos` (listado general de admin) | El sistema SHALL devolver HTTP 403 Forbidden sin revelar información de otros clientes |

---

## TRAZABILIDAD

| Historia | Requerimientos Funcionales | Requerimientos No Funcionales | Sprint |
|----------|---------------------------|-------------------------------|--------|
| HU-01 | RF01 | RNF03, RNF04 | Sprint 1 |
| HU-02 | RF02 | RNF03, RNF04 | Sprint 1 |
| HU-03 | RF03 | RNF04, RNF05 | Sprint 1 |
| HU-04 | RF06 | RNF03 | Sprint 4 |
| HU-05 | RF07, RF08, RF09, RF10, RF16 | RNF07, RNF09 | Sprint 2 |
| HU-06 | RF18 | RNF01, RNF09 | Sprint 2 |
| HU-07 | RF16 | RNF07, RNF09 | Sprint 2 |
| HU-08 | RF15, RF17 | RNF07, RNF08 | Sprint 2 |
| HU-09 | RF25 | RNF01, RNF09 | Sprint 3 |
| HU-10 | RF26 | RNF01, RNF09 | Sprint 3 |
| HU-11 | RF27, RF31, RF32, RF33 | RNF01, RNF04 | Sprint 3 |
| HU-12 | RF28 | RNF04, RNF05 | Sprint 3 |
| HU-17 | RF30 | RNF04, RNF05 | Sprint 3 |

---

*Documento generado a partir de [ESPECIFICACION.md](./ESPECIFICACION.md)*  
*Proyecto: Pacífico Repuestos | Metodología: Scrum | Julio 2026*
