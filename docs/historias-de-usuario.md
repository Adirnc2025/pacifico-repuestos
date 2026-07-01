# Historias de Usuario — Pacífico Repuestos

**Proyecto:** Pacífico Repuestos  
**Versión:** 1.0  
**Metodología:** Scrum  
**Etapa:** A — Análisis  

---

## Épica 1 — Autenticación y Gestión de Cuenta

### HU-01 — Registro de cliente
**Como** cliente nuevo,  
**quiero** registrarme en el sistema con mi nombre, correo, contraseña y teléfono,  
**para** poder realizar pedidos y hacer seguimiento de mis compras.

**Criterios de aceptación:**
- El formulario valida que el correo tenga formato válido.
- La contraseña debe tener mínimo 8 caracteres.
- No se permite registrar dos cuentas con el mismo correo.
- Al registrarse exitosamente, el cliente recibe un mensaje de confirmación.

**Prioridad:** Alta | **Estimación:** 3 puntos | **Sprint:** 1

---

### HU-02 — Inicio de sesión
**Como** cliente registrado,  
**quiero** iniciar sesión con mi correo y contraseña,  
**para** acceder a mi carrito, historial de pedidos y datos de cuenta.

**Criterios de aceptación:**
- El sistema muestra mensaje de error si las credenciales son incorrectas.
- La sesión se mantiene activa mediante token JWT.
- El cliente puede cerrar sesión desde cualquier página.

**Prioridad:** Alta | **Estimación:** 2 puntos | **Sprint:** 1

---

### HU-03 — Inicio de sesión del administrador
**Como** administrador,  
**quiero** iniciar sesión con credenciales de administrador,  
**para** acceder al panel de gestión del sistema.

**Criterios de aceptación:**
- El administrador accede a una ruta protegida `/admin`.
- Un cliente autenticado no puede acceder al panel de administración.
- El sistema registra la fecha y hora del último acceso.

**Prioridad:** Alta | **Estimación:** 2 puntos | **Sprint:** 1

---

### HU-04 — Recuperación de contraseña
**Como** cliente,  
**quiero** recuperar mi contraseña si la olvido,  
**para** no perder el acceso a mi cuenta.

**Criterios de aceptación:**
- El sistema envía un enlace de recuperación al correo registrado.
- El enlace expira en 30 minutos.
- La nueva contraseña debe cumplir las reglas de seguridad.

**Prioridad:** Media | **Estimación:** 3 puntos | **Sprint:** 2

---

## Épica 2 — Catálogo y Búsqueda de Repuestos

### HU-05 — Buscar repuestos por vehículo
**Como** cliente,  
**quiero** buscar repuestos seleccionando la marca, modelo, generación y motor de mi vehículo,  
**para** encontrar rápidamente las piezas compatibles con mi auto.

**Criterios de aceptación:**
- El buscador muestra selectores encadenados: marca → modelo → generación → motor.
- Los resultados se filtran automáticamente al seleccionar cada nivel.
- Si no hay productos compatibles, se muestra un mensaje claro.

**Prioridad:** Alta | **Estimación:** 5 puntos | **Sprint:** 2

---

### HU-06 — Buscar repuestos por nombre o código
**Como** cliente,  
**quiero** buscar un repuesto escribiendo su nombre o código en la barra de búsqueda,  
**para** encontrarlo rápidamente sin navegar por categorías.

**Criterios de aceptación:**
- La búsqueda es insensible a mayúsculas y acentos.
- Los resultados aparecen en menos de 2 segundos.
- Se muestran sugerencias mientras el cliente escribe (mínimo 3 caracteres).

**Prioridad:** Alta | **Estimación:** 3 puntos | **Sprint:** 2

---

### HU-07 — Filtrar productos por categoría
**Como** cliente,  
**quiero** filtrar los productos por categoría (frenos, motor, suspensión, etc.),  
**para** ver solo los repuestos del tipo que necesito.

**Criterios de aceptación:**
- Las categorías se muestran como botones o lista lateral.
- Se puede combinar el filtro de categoría con el de vehículo.
- El número de resultados se actualiza al aplicar filtros.

**Prioridad:** Alta | **Estimación:** 3 puntos | **Sprint:** 2

---

### HU-08 — Ver detalle de un producto
**Como** cliente,  
**quiero** ver la ficha completa de un producto con imágenes, precio, stock, medidas y compatibilidades,  
**para** asegurarme de que es el repuesto correcto antes de comprarlo.

**Criterios de aceptación:**
- La página muestra al menos una imagen del producto.
- Se lista la tabla de compatibilidades (marca / modelo / motor).
- Se indica claramente si hay stock disponible.
- El precio se muestra en soles (S/).

**Prioridad:** Alta | **Estimación:** 3 puntos | **Sprint:** 2

---

## Épica 3 — Carrito y Pedidos

### HU-09 — Agregar producto al carrito
**Como** cliente,  
**quiero** agregar un producto al carrito desde su ficha o desde el listado,  
**para** acumular los repuestos que quiero comprar antes de confirmar el pedido.

**Criterios de aceptación:**
- El ícono del carrito en el header muestra la cantidad de productos agregados.
- No se puede agregar una cantidad mayor al stock disponible.
- Se puede agregar el mismo producto varias veces (acumula cantidad).

**Prioridad:** Alta | **Estimación:** 3 puntos | **Sprint:** 3

---

### HU-10 — Gestionar el carrito
**Como** cliente,  
**quiero** ver el contenido de mi carrito, modificar cantidades y eliminar productos,  
**para** revisar y ajustar mi pedido antes de confirmarlo.

**Criterios de aceptación:**
- El carrito muestra imagen, nombre, precio unitario y subtotal por producto.
- Se muestra el total general del carrito.
- Los cambios de cantidad actualizan el total en tiempo real.

**Prioridad:** Alta | **Estimación:** 3 puntos | **Sprint:** 3

---

### HU-11 — Realizar un pedido
**Como** cliente autenticado,  
**quiero** confirmar mi pedido indicando mi dirección de entrega y tipo de delivery,  
**para** recibir los repuestos en mi domicilio o recogerlos en tienda.

**Criterios de aceptación:**
- El cliente debe estar autenticado para confirmar un pedido.
- Se muestran las opciones: recojo en tienda, delivery local, delivery interprovincial.
- Al confirmar, el pedido queda registrado con estado "Pendiente".
- El stock de los productos se descuenta automáticamente.
- El cliente recibe confirmación con el número de pedido.

**Prioridad:** Alta | **Estimación:** 5 puntos | **Sprint:** 3

---

### HU-12 — Consultar estado del pedido
**Como** cliente,  
**quiero** consultar el estado actual de mis pedidos,  
**para** saber si mi pedido está siendo preparado, en camino o entregado.

**Criterios de aceptación:**
- El cliente ve el historial de todos sus pedidos con fecha y estado.
- Los estados posibles son: Pendiente, Confirmado, En preparación, Enviado, Entregado, Cancelado.
- Se muestra el detalle completo de cada pedido (productos, precios, dirección).

**Prioridad:** Alta | **Estimación:** 3 puntos | **Sprint:** 3

---

## Épica 4 — Administración de Productos y Catálogo

### HU-13 — Gestionar productos
**Como** administrador,  
**quiero** crear, editar y eliminar productos con todos sus atributos,  
**para** mantener el catálogo actualizado y preciso.

**Criterios de aceptación:**
- El formulario incluye: nombre, descripción, precio, stock, categoría, medidas.
- Se puede marcar un producto como destacado para la página principal.
- No se puede eliminar un producto que tiene pedidos activos.

**Prioridad:** Alta | **Estimación:** 5 puntos | **Sprint:** 2

---

### HU-14 — Gestionar imágenes de productos
**Como** administrador,  
**quiero** subir, reemplazar y eliminar imágenes de cada producto,  
**para** que el catálogo sea visual y atractivo para los clientes.

**Criterios de aceptación:**
- Se admiten formatos JPG y PNG con tamaño máximo de 2MB por imagen.
- Un producto puede tener múltiples imágenes.
- La primera imagen es la imagen principal del producto.

**Prioridad:** Alta | **Estimación:** 3 puntos | **Sprint:** 2

---

### HU-15 — Gestionar compatibilidades
**Como** administrador,  
**quiero** asociar cada producto con los motores y vehículos con los que es compatible,  
**para** que los clientes puedan encontrar el repuesto correcto para su vehículo.

**Criterios de aceptación:**
- Se pueden asociar múltiples motores a un mismo producto.
- Las compatibilidades se muestran en la ficha del producto.
- Se puede eliminar una compatibilidad sin afectar el producto.

**Prioridad:** Alta | **Estimación:** 3 puntos | **Sprint:** 2

---

### HU-16 — Gestionar catálogo automotriz
**Como** administrador,  
**quiero** gestionar marcas, modelos, generaciones y motores del catálogo,  
**para** mantener actualizada la base de datos de vehículos compatibles.

**Criterios de aceptación:**
- Cada entidad se puede crear, editar y eliminar de forma independiente.
- No se puede eliminar una marca si tiene modelos asociados.
- Los cambios en el catálogo se reflejan de inmediato en el buscador del cliente.

**Prioridad:** Alta | **Estimación:** 5 puntos | **Sprint:** 2

---

## Épica 5 — Gestión de Pedidos y Delivery

### HU-17 — Gestionar pedidos como administrador
**Como** administrador,  
**quiero** ver todos los pedidos, filtrarlos por estado y actualizar su estado,  
**para** gestionar eficientemente el flujo de trabajo de la tienda.

**Criterios de aceptación:**
- Se pueden filtrar pedidos por estado, fecha y cliente.
- El administrador puede cambiar el estado de un pedido.
- Al cambiar el estado, el cliente recibe una notificación.

**Prioridad:** Alta | **Estimación:** 5 puntos | **Sprint:** 3

---

### HU-18 — Gestionar zonas de delivery
**Como** administrador,  
**quiero** configurar las zonas de delivery con sus tarifas,  
**para** que los clientes vean el costo de envío según su ubicación.

**Criterios de aceptación:**
- Se pueden definir zonas: local (Ayacucho) e interprovincial.
- Cada zona tiene una tarifa configurable.
- El costo de delivery se suma automáticamente al total del pedido.

**Prioridad:** Media | **Estimación:** 3 puntos | **Sprint:** 4

---

## Épica 6 — Reportes y Dashboard

### HU-19 — Ver dashboard de administración
**Como** administrador,  
**quiero** ver un panel resumen con métricas clave del negocio,  
**para** tomar decisiones rápidas sobre inventario, ventas y atención.

**Criterios de aceptación:**
- El dashboard muestra: total de pedidos del día, productos con stock bajo, ingresos del mes.
- Los datos se actualizan sin necesidad de recargar la página.
- Se accede desde el menú principal del panel de administración.

**Prioridad:** Media | **Estimación:** 5 puntos | **Sprint:** 4

---

### HU-20 — Generar reporte de ventas
**Como** administrador,  
**quiero** generar un reporte de ventas filtrando por rango de fechas,  
**para** analizar el desempeño del negocio y planificar el inventario.

**Criterios de aceptación:**
- El reporte muestra: fecha, número de pedido, cliente, productos y total.
- Se puede exportar o imprimir el reporte.
- Los totales se agrupan por día, semana o mes.

**Prioridad:** Media | **Estimación:** 3 puntos | **Sprint:** 4

---

## Épica 7 — Comunicación y Contacto

### HU-21 — Contactar por WhatsApp
**Como** cliente,  
**quiero** contactar directamente a la tienda por WhatsApp con un solo clic,  
**para** hacer consultas rápidas sobre disponibilidad o precios.

**Criterios de aceptación:**
- El botón de WhatsApp está visible en todo momento (flotante o en header).
- Al hacer clic, abre WhatsApp con un mensaje predefinido.
- Funciona tanto en móvil como en escritorio.

**Prioridad:** Alta | **Estimación:** 1 punto | **Sprint:** 1

---

### HU-22 — Ver información de contacto y ubicación
**Como** cliente,  
**quiero** ver la dirección, teléfono, horarios y redes sociales de la tienda,  
**para** saber cómo contactarlos o visitarlos físicamente.

**Criterios de aceptación:**
- La información está visible en el footer de todas las páginas.
- El horario de atención se muestra claramente.
- Los íconos de redes sociales enlazan a los perfiles reales de la tienda.

**Prioridad:** Alta | **Estimación:** 1 punto | **Sprint:** 1

---

## Resumen de Historias de Usuario

| ID    | Historia | Épica | Sprint | Puntos | Prioridad |
|-------|----------|-------|--------|--------|-----------|
| HU-01 | Registro de cliente | Autenticación | 1 | 3 | Alta |
| HU-02 | Inicio de sesión | Autenticación | 1 | 2 | Alta |
| HU-03 | Login administrador | Autenticación | 1 | 2 | Alta |
| HU-04 | Recuperación contraseña | Autenticación | 2 | 3 | Media |
| HU-05 | Buscar por vehículo | Catálogo | 2 | 5 | Alta |
| HU-06 | Buscar por nombre/código | Catálogo | 2 | 3 | Alta |
| HU-07 | Filtrar por categoría | Catálogo | 2 | 3 | Alta |
| HU-08 | Ver detalle producto | Catálogo | 2 | 3 | Alta |
| HU-09 | Agregar al carrito | Pedidos | 3 | 3 | Alta |
| HU-10 | Gestionar carrito | Pedidos | 3 | 3 | Alta |
| HU-11 | Realizar pedido | Pedidos | 3 | 5 | Alta |
| HU-12 | Consultar estado pedido | Pedidos | 3 | 3 | Alta |
| HU-13 | Gestionar productos | Admin | 2 | 5 | Alta |
| HU-14 | Gestionar imágenes | Admin | 2 | 3 | Alta |
| HU-15 | Gestionar compatibilidades | Admin | 2 | 3 | Alta |
| HU-16 | Gestionar catálogo automotriz | Admin | 2 | 5 | Alta |
| HU-17 | Gestionar pedidos (admin) | Delivery | 3 | 5 | Alta |
| HU-18 | Gestionar zonas delivery | Delivery | 4 | 3 | Media |
| HU-19 | Dashboard administración | Reportes | 4 | 5 | Media |
| HU-20 | Reporte de ventas | Reportes | 4 | 3 | Media |
| HU-21 | Contacto WhatsApp | Contacto | 1 | 1 | Alta |
| HU-22 | Info contacto y ubicación | Contacto | 1 | 1 | Alta |

**Total de puntos de historia:** 71 puntos  
**Total de historias:** 22 historias de usuario  
**Sprints planificados:** 4

---

*Documento generado en la Etapa A (Análisis) del flujo ADIPD — Proyecto Pacífico Repuestos*  
*Metodología: Scrum | Herramienta de apoyo: IA (Claude)*
