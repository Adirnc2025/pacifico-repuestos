# Casos de Uso — Pacífico Repuestos

**Proyecto:** Pacífico Repuestos  
**Versión:** 1.0  
**Metodología:** Scrum  
**Etapa:** A — Análisis  

---

## 1. Actores del Sistema

| Actor | Tipo | Descripción |
|-------|------|-------------|
| Cliente | Principal | Usuario registrado que navega, busca y compra repuestos. |
| Administrador | Principal | Usuario con acceso total al panel de gestión del sistema. |
| Sistema | Secundario | Ejecuta procesos automáticos: descuento de stock, notificaciones, validaciones. |

---

## 2. Diagrama General de Casos de Uso (texto estructurado)

```
Sistema: Pacífico Repuestos
│
├── Actor: Cliente
│   ├── CU-01  Registrarse
│   ├── CU-02  Iniciar sesión
│   ├── CU-03  Recuperar contraseña
│   ├── CU-04  Buscar repuestos por vehículo
│   ├── CU-05  Buscar repuestos por nombre o código
│   ├── CU-06  Filtrar productos por categoría
│   ├── CU-07  Ver detalle de producto
│   ├── CU-08  Agregar producto al carrito
│   ├── CU-09  Gestionar carrito
│   ├── CU-10  Realizar pedido
│   ├── CU-11  Consultar estado del pedido
│   └── CU-12  Contactar por WhatsApp
│
└── Actor: Administrador
    ├── CU-13  Iniciar sesión como administrador
    ├── CU-14  Gestionar productos
    ├── CU-15  Gestionar imágenes de productos
    ├── CU-16  Gestionar compatibilidades
    ├── CU-17  Gestionar catálogo automotriz
    ├── CU-18  Gestionar categorías
    ├── CU-19  Gestionar pedidos
    ├── CU-20  Gestionar clientes
    ├── CU-21  Gestionar zonas de delivery
    ├── CU-22  Ver dashboard
    └── CU-23  Generar reporte de ventas
```

---

## 3. Especificación de Casos de Uso

---

### CU-01 — Registrarse

| Campo | Detalle |
|-------|---------|
| **ID** | CU-01 |
| **Nombre** | Registrarse |
| **Actor principal** | Cliente |
| **Precondición** | El cliente no tiene una cuenta registrada en el sistema. |
| **Postcondición** | El cliente queda registrado y puede iniciar sesión. |

**Flujo principal:**
1. El cliente accede a la página de registro.
2. El sistema muestra el formulario con campos: nombre, correo, contraseña, teléfono.
3. El cliente completa el formulario y hace clic en "Registrarse".
4. El sistema valida que el correo no esté registrado previamente.
5. El sistema valida el formato del correo y la longitud de la contraseña (mínimo 8 caracteres).
6. El sistema cifra la contraseña con BCrypt y guarda el registro.
7. El sistema muestra mensaje de éxito y redirige al login.

**Flujos alternativos:**
- 4a. El correo ya está registrado → el sistema muestra el mensaje "Este correo ya tiene una cuenta".
- 5a. El correo tiene formato inválido → el sistema resalta el campo con error.
- 5b. La contraseña tiene menos de 8 caracteres → el sistema muestra requisito mínimo.

**Referencia:** RF01, HU-01

---

### CU-02 — Iniciar sesión (Cliente)

| Campo | Detalle |
|-------|---------|
| **ID** | CU-02 |
| **Nombre** | Iniciar sesión |
| **Actor principal** | Cliente |
| **Precondición** | El cliente tiene una cuenta registrada. |
| **Postcondición** | El cliente obtiene un token JWT y accede a su cuenta. |

**Flujo principal:**
1. El cliente accede a la página de login.
2. El sistema muestra el formulario con campos: correo y contraseña.
3. El cliente ingresa sus credenciales y hace clic en "Ingresar".
4. El sistema valida las credenciales contra la base de datos.
5. El sistema genera un token JWT y lo devuelve al frontend.
6. El frontend almacena el token y redirige a la página principal.

**Flujos alternativos:**
- 4a. Las credenciales son incorrectas → el sistema muestra "Correo o contraseña incorrectos".
- 4b. El usuario no existe → mismo mensaje genérico (no revelar si el correo existe).

**Referencia:** RF02, HU-02

---

### CU-03 — Recuperar contraseña

| Campo | Detalle |
|-------|---------|
| **ID** | CU-03 |
| **Nombre** | Recuperar contraseña |
| **Actor principal** | Cliente |
| **Precondición** | El cliente tiene una cuenta registrada con correo válido. |
| **Postcondición** | El cliente puede iniciar sesión con su nueva contraseña. |

**Flujo principal:**
1. El cliente hace clic en "¿Olvidaste tu contraseña?" en el login.
2. El sistema muestra campo para ingresar el correo registrado.
3. El cliente ingresa su correo y confirma.
4. El sistema genera un token temporal (expira en 30 minutos) y envía enlace al correo.
5. El cliente accede al enlace y el sistema muestra formulario de nueva contraseña.
6. El cliente ingresa y confirma la nueva contraseña.
7. El sistema valida, cifra y actualiza la contraseña. Invalida el token.
8. El sistema redirige al login con mensaje de éxito.

**Flujos alternativos:**
- 4a. El correo no existe → el sistema muestra mensaje genérico sin confirmar si existe.
- 5a. El token expiró → el sistema muestra "El enlace ha expirado" y ofrece solicitar uno nuevo.

**Referencia:** RF06, HU-04

---

### CU-04 — Buscar repuestos por vehículo

| Campo | Detalle |
|-------|---------|
| **ID** | CU-04 |
| **Nombre** | Buscar repuestos por vehículo |
| **Actor principal** | Cliente |
| **Precondición** | Existen productos y compatibilidades registradas en el sistema. |
| **Postcondición** | El sistema muestra la lista de productos compatibles con el vehículo seleccionado. |

**Flujo principal:**
1. El cliente accede al buscador por vehículo (en la página principal o en la sección Productos).
2. El sistema muestra selectores encadenados: Marca → Modelo → Generación → Motor.
3. El cliente selecciona la Marca. El sistema carga los modelos de esa marca.
4. El cliente selecciona el Modelo. El sistema carga las generaciones.
5. El cliente selecciona la Generación. El sistema carga los motores.
6. El cliente selecciona el Motor.
7. El sistema consulta los productos compatibles con ese motor y los muestra en grilla.

**Flujos alternativos:**
- 7a. No hay productos para ese motor → el sistema muestra "No encontramos repuestos para este vehículo. Contáctanos por WhatsApp."
- 3a. No hay modelos para la marca → el sistema deshabilita los selectores siguientes.

**Referencia:** RF16, HU-05

---

### CU-05 — Buscar repuestos por nombre o código

| Campo | Detalle |
|-------|---------|
| **ID** | CU-05 |
| **Nombre** | Buscar repuestos por nombre o código |
| **Actor principal** | Cliente |
| **Precondición** | El cliente escribe al menos 3 caracteres en la barra de búsqueda. |
| **Postcondición** | El sistema muestra los productos que coinciden con el término buscado. |

**Flujo principal:**
1. El cliente escribe en la barra de búsqueda del header.
2. A partir del tercer carácter, el sistema consulta productos por nombre o código.
3. El sistema muestra sugerencias en un desplegable.
4. El cliente selecciona una sugerencia o presiona Enter.
5. El sistema redirige a la página de resultados con los productos filtrados.

**Flujos alternativos:**
- 5a. No hay resultados → el sistema muestra "No encontramos productos con ese nombre".

**Referencia:** RF18, HU-06

---

### CU-07 — Ver detalle de producto

| Campo | Detalle |
|-------|---------|
| **ID** | CU-07 |
| **Nombre** | Ver detalle de producto |
| **Actor principal** | Cliente |
| **Precondición** | El producto existe y está activo en el catálogo. |
| **Postcondición** | El cliente visualiza toda la información del producto. |

**Flujo principal:**
1. El cliente hace clic en un producto desde el listado o resultados de búsqueda.
2. El sistema carga la ficha del producto con:
   - Galería de imágenes (principal + miniaturas).
   - Nombre, descripción, precio en S/, medidas.
   - Indicador de stock (disponible / agotado).
   - Tabla de compatibilidades: Marca / Modelo / Generación / Motor.
   - Categoría.
3. El cliente puede seleccionar cantidad y agregar al carrito.
4. El cliente puede contactar por WhatsApp desde la ficha.

**Flujos alternativos:**
- 2a. El producto está agotado → el botón "Agregar al carrito" se deshabilita y muestra "Sin stock".
- 2b. El producto no tiene imagen → se muestra imagen genérica de repuesto.

**Referencia:** RF15, RF17, HU-08

---

### CU-08 — Agregar producto al carrito

| Campo | Detalle |
|-------|---------|
| **ID** | CU-08 |
| **Nombre** | Agregar producto al carrito |
| **Actor principal** | Cliente |
| **Precondición** | El producto tiene stock disponible. |
| **Postcondición** | El producto queda en el carrito con la cantidad indicada. |

**Flujo principal:**
1. El cliente selecciona la cantidad deseada (mínimo 1).
2. El cliente hace clic en "Agregar al carrito".
3. El sistema verifica que la cantidad no supere el stock disponible.
4. El sistema agrega el producto al carrito (almacenado en estado del frontend).
5. El ícono del carrito en el header actualiza el contador.
6. El sistema muestra notificación: "Producto agregado al carrito".

**Flujos alternativos:**
- 3a. La cantidad supera el stock → el sistema muestra "Solo hay X unidades disponibles".
- 3b. El producto ya estaba en el carrito → se incrementa la cantidad.

**Referencia:** RF25, HU-09

---

### CU-10 — Realizar pedido

| Campo | Detalle |
|-------|---------|
| **ID** | CU-10 |
| **Nombre** | Realizar pedido |
| **Actor principal** | Cliente |
| **Precondición** | El cliente está autenticado y tiene al menos un producto en el carrito. |
| **Postcondición** | El pedido queda registrado con estado "Pendiente" y el stock se descuenta. |

**Flujo principal:**
1. El cliente accede al carrito y revisa los productos.
2. El cliente hace clic en "Proceder al pedido".
3. El sistema verifica que el cliente esté autenticado.
4. El sistema muestra el formulario de checkout:
   - Dirección de entrega.
   - Tipo de delivery: Recojo en tienda / Local (Ayacucho) / Interprovincial.
   - Resumen del pedido con subtotal, costo de delivery y total.
5. El cliente confirma el pedido.
6. El sistema valida el stock de cada producto.
7. El sistema registra el pedido y el detalle en la base de datos.
8. El sistema descuenta el stock de cada producto.
9. El sistema muestra la confirmación con el número de pedido.
10. El carrito queda vacío.

**Flujos alternativos:**
- 3a. El cliente no está autenticado → el sistema redirige al login con mensaje "Inicia sesión para continuar".
- 6a. Algún producto quedó sin stock → el sistema informa qué producto no tiene stock y pide actualizar el carrito.

**Referencia:** RF27, RF32, RF33, HU-11

---

### CU-11 — Consultar estado del pedido

| Campo | Detalle |
|-------|---------|
| **ID** | CU-11 |
| **Nombre** | Consultar estado del pedido |
| **Actor principal** | Cliente |
| **Precondición** | El cliente está autenticado y tiene pedidos registrados. |
| **Postcondición** | El cliente visualiza el estado actual y el detalle de sus pedidos. |

**Flujo principal:**
1. El cliente accede a "Mis pedidos" desde su perfil.
2. El sistema muestra la lista de pedidos con: número, fecha, total y estado actual.
3. El cliente hace clic en un pedido para ver el detalle.
4. El sistema muestra: productos, cantidades, precios, dirección de entrega, tipo de delivery y estado.

**Estados posibles:** Pendiente → Confirmado → En preparación → Enviado → Entregado / Cancelado

**Referencia:** RF28, HU-12

---

### CU-14 — Gestionar productos (Administrador)

| Campo | Detalle |
|-------|---------|
| **ID** | CU-14 |
| **Nombre** | Gestionar productos |
| **Actor principal** | Administrador |
| **Precondición** | El administrador está autenticado con rol ADMIN. |
| **Postcondición** | El catálogo de productos queda actualizado. |

**Flujo principal — Crear producto:**
1. El administrador accede al panel de administración → Productos → Nuevo.
2. El sistema muestra el formulario: nombre, descripción, precio, stock, categoría, medidas.
3. El administrador completa el formulario y guarda.
4. El sistema valida los campos obligatorios y crea el producto.
5. El sistema muestra confirmación y el nuevo producto aparece en el listado.

**Flujo principal — Editar producto:**
1. El administrador selecciona un producto del listado y hace clic en "Editar".
2. El sistema carga el formulario con los datos actuales.
3. El administrador modifica los campos necesarios y guarda.
4. El sistema actualiza el producto y muestra confirmación.

**Flujo principal — Eliminar producto:**
1. El administrador hace clic en "Eliminar" sobre un producto.
2. El sistema solicita confirmación: "¿Estás seguro?".
3. El administrador confirma.
4. El sistema verifica que el producto no tenga pedidos activos.
5. El sistema elimina el producto (o lo marca como inactivo).

**Flujos alternativos:**
- 4 (eliminar). El producto tiene pedidos activos → el sistema muestra "No se puede eliminar, tiene pedidos activos."

**Referencia:** RF20, HU-13

---

### CU-19 — Gestionar pedidos (Administrador)

| Campo | Detalle |
|-------|---------|
| **ID** | CU-19 |
| **Nombre** | Gestionar pedidos |
| **Actor principal** | Administrador |
| **Precondición** | El administrador está autenticado con rol ADMIN. |
| **Postcondición** | El estado del pedido queda actualizado y el cliente es notificado. |

**Flujo principal:**
1. El administrador accede al panel → Pedidos.
2. El sistema muestra la lista de pedidos con filtros: estado, fecha, cliente.
3. El administrador selecciona un pedido y hace clic en "Ver detalle".
4. El sistema muestra el detalle completo: cliente, productos, dirección, tipo de delivery, total.
5. El administrador cambia el estado del pedido (ej: Pendiente → Confirmado).
6. El sistema guarda el nuevo estado y registra la fecha del cambio.

**Referencia:** RF30, HU-17

---

### CU-22 — Ver dashboard (Administrador)

| Campo | Detalle |
|-------|---------|
| **ID** | CU-22 |
| **Nombre** | Ver dashboard de administración |
| **Actor principal** | Administrador |
| **Precondición** | El administrador está autenticado. |
| **Postcondición** | El administrador visualiza las métricas clave del negocio. |

**Flujo principal:**
1. El administrador inicia sesión y accede al panel de administración.
2. El sistema muestra el dashboard con:
   - Total de pedidos del día.
   - Pedidos por estado (gráfico o contadores).
   - Productos con stock bajo (menos de 5 unidades).
   - Ingresos del mes actual.
3. El administrador puede navegar a cualquier sección desde el dashboard.

**Referencia:** RF40, HU-19

---

## 4. Resumen de Casos de Uso

| ID | Nombre | Actor | Sprint | RF relacionado |
|----|--------|-------|--------|----------------|
| CU-01 | Registrarse | Cliente | 1 | RF01 |
| CU-02 | Iniciar sesión | Cliente | 1 | RF02 |
| CU-03 | Recuperar contraseña | Cliente | 4 | RF06 |
| CU-04 | Buscar por vehículo | Cliente | 2 | RF16 |
| CU-05 | Buscar por nombre/código | Cliente | 2 | RF18 |
| CU-06 | Filtrar por categoría | Cliente | 2 | RF07 |
| CU-07 | Ver detalle de producto | Cliente | 2 | RF15, RF17 |
| CU-08 | Agregar al carrito | Cliente | 3 | RF25 |
| CU-09 | Gestionar carrito | Cliente | 3 | RF26 |
| CU-10 | Realizar pedido | Cliente | 3 | RF27, RF32 |
| CU-11 | Consultar estado pedido | Cliente | 3 | RF28 |
| CU-12 | Contactar por WhatsApp | Cliente | 1 | RF36 |
| CU-13 | Login administrador | Administrador | 1 | RF03 |
| CU-14 | Gestionar productos | Administrador | 2 | RF20 |
| CU-15 | Gestionar imágenes | Administrador | 2 | RF22 |
| CU-16 | Gestionar compatibilidades | Administrador | 2 | RF21 |
| CU-17 | Gestionar catálogo automotriz | Administrador | 2 | RF11–RF14 |
| CU-18 | Gestionar categorías | Administrador | 2 | RF24 |
| CU-19 | Gestionar pedidos | Administrador | 3 | RF30 |
| CU-20 | Gestionar clientes | Administrador | 4 | RF39 |
| CU-21 | Gestionar zonas de delivery | Administrador | 4 | RF34 |
| CU-22 | Ver dashboard | Administrador | 4 | RF40 |
| CU-23 | Generar reporte de ventas | Administrador | 4 | RF38 |

---

*Documento generado en la Etapa A (Análisis) del flujo ADIPD — Proyecto Pacífico Repuestos*  
*Metodología: Scrum | Herramienta de apoyo: IA (Claude)*
