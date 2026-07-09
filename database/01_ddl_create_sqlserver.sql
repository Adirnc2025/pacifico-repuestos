-- ============================================================
-- PACÍFICO REPUESTOS — Script DDL
-- Base de datos: SQL Server
-- Versión: 1.0
-- Etapa: D — Diseño
-- ============================================================

-- Crear base de datos (ejecutar solo si no existe)
-- CREATE DATABASE pacifico_db;
-- GO
-- USE pacifico_db;
-- GO

-- ============================================================
-- Eliminar tablas si existen (orden inverso por FK)
-- ============================================================
IF OBJECT_ID('dbo.envios', 'U') IS NOT NULL DROP TABLE dbo.envios;
IF OBJECT_ID('dbo.detalle_pedido', 'U') IS NOT NULL DROP TABLE dbo.detalle_pedido;
IF OBJECT_ID('dbo.pedidos', 'U') IS NOT NULL DROP TABLE dbo.pedidos;
IF OBJECT_ID('dbo.zonas_delivery', 'U') IS NOT NULL DROP TABLE dbo.zonas_delivery;
IF OBJECT_ID('dbo.compatibilidades', 'U') IS NOT NULL DROP TABLE dbo.compatibilidades;
IF OBJECT_ID('dbo.imagenes_producto', 'U') IS NOT NULL DROP TABLE dbo.imagenes_producto;
IF OBJECT_ID('dbo.inventario', 'U') IS NOT NULL DROP TABLE dbo.inventario;
IF OBJECT_ID('dbo.productos', 'U') IS NOT NULL DROP TABLE dbo.productos;
IF OBJECT_ID('dbo.categorias', 'U') IS NOT NULL DROP TABLE dbo.categorias;
IF OBJECT_ID('dbo.motores', 'U') IS NOT NULL DROP TABLE dbo.motores;
IF OBJECT_ID('dbo.generaciones', 'U') IS NOT NULL DROP TABLE dbo.generaciones;
IF OBJECT_ID('dbo.modelos', 'U') IS NOT NULL DROP TABLE dbo.modelos;
IF OBJECT_ID('dbo.marcas', 'U') IS NOT NULL DROP TABLE dbo.marcas;
IF OBJECT_ID('dbo.clientes', 'U') IS NOT NULL DROP TABLE dbo.clientes;
IF OBJECT_ID('dbo.usuarios', 'U') IS NOT NULL DROP TABLE dbo.usuarios;
GO

-- ============================================================
-- TABLA: usuarios
-- ============================================================
CREATE TABLE usuarios (
    id              BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre          NVARCHAR(100) NOT NULL,
    correo          NVARCHAR(150) NOT NULL UNIQUE,
    password        NVARCHAR(255) NOT NULL,
    rol             NVARCHAR(20) NOT NULL CHECK (rol IN ('CLIENTE','ADMIN')),
    activo          BIT NOT NULL DEFAULT 1,
    fecha_registro  DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);
GO

-- ============================================================
-- TABLA: clientes
-- ============================================================
CREATE TABLE clientes (
    id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    usuario_id  BIGINT NOT NULL UNIQUE
                  REFERENCES usuarios(id) ON DELETE CASCADE,
    telefono    NVARCHAR(20),
    direccion   NVARCHAR(255),
    dni         NVARCHAR(15)
);
GO

-- ============================================================
-- TABLA: marcas
-- ============================================================
CREATE TABLE marcas (
    id        BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre    NVARCHAR(100) NOT NULL UNIQUE,
    logo_url  NVARCHAR(500),
    activo    BIT NOT NULL DEFAULT 1
);
GO

-- ============================================================
-- TABLA: modelos
-- ============================================================
CREATE TABLE modelos (
    id        BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre    NVARCHAR(100) NOT NULL,
    marca_id  BIGINT NOT NULL REFERENCES marcas(id),
    activo    BIT NOT NULL DEFAULT 1,
    CONSTRAINT uq_modelo_marca UNIQUE(nombre, marca_id)
);
GO

-- ============================================================
-- TABLA: generaciones
-- ============================================================
CREATE TABLE generaciones (
    id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre      NVARCHAR(100) NOT NULL,
    anio_inicio INT,
    anio_fin    INT,
    modelo_id   BIGINT NOT NULL REFERENCES modelos(id),
    CONSTRAINT uq_generacion_modelo UNIQUE(nombre, modelo_id)
);
GO

-- ============================================================
-- TABLA: motores
-- ============================================================
CREATE TABLE motores (
    id              BIGINT IDENTITY(1,1) PRIMARY KEY,
    codigo          NVARCHAR(50) NOT NULL UNIQUE,
    descripcion     NVARCHAR(200),
    cilindrada      NVARCHAR(20),
    generacion_id   BIGINT NOT NULL REFERENCES generaciones(id)
);
GO

-- ============================================================
-- TABLA: categorias
-- ============================================================
CREATE TABLE categorias (
    id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre      NVARCHAR(100) NOT NULL UNIQUE,
    descripcion NVARCHAR(300),
    imagen_url  NVARCHAR(500),
    activo      BIT NOT NULL DEFAULT 1
);
GO

-- ============================================================
-- TABLA: productos
-- ============================================================
CREATE TABLE productos (
    id              BIGINT IDENTITY(1,1) PRIMARY KEY,
    codigo          NVARCHAR(50) NOT NULL UNIQUE,
    nombre          NVARCHAR(200) NOT NULL,
    descripcion     NVARCHAR(MAX),
    precio          DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
    medidas         NVARCHAR(100),
    destacado       BIT NOT NULL DEFAULT 0,
    activo          BIT NOT NULL DEFAULT 1,
    categoria_id    BIGINT NULL REFERENCES categorias(id) ON DELETE SET NULL,
    fecha_creacion  DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);
GO

-- ============================================================
-- TABLA: inventario
-- ============================================================
CREATE TABLE inventario (
    id                      BIGINT IDENTITY(1,1) PRIMARY KEY,
    producto_id             BIGINT NOT NULL UNIQUE
                              REFERENCES productos(id) ON DELETE CASCADE,
    stock                   INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    stock_minimo            INT DEFAULT 5,
    ultima_actualizacion    DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);
GO

-- ============================================================
-- TABLA: imagenes_producto
-- ============================================================
CREATE TABLE imagenes_producto (
    id           BIGINT IDENTITY(1,1) PRIMARY KEY,
    producto_id  BIGINT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
    url          NVARCHAR(500) NOT NULL,
    es_principal BIT NOT NULL DEFAULT 0,
    orden        INT DEFAULT 0
);
GO

-- ============================================================
-- TABLA: compatibilidades
-- ============================================================
CREATE TABLE compatibilidades (
    id           BIGINT IDENTITY(1,1) PRIMARY KEY,
    producto_id  BIGINT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
    motor_id     BIGINT NOT NULL REFERENCES motores(id) ON DELETE CASCADE,
    observacion  NVARCHAR(300),
    CONSTRAINT uq_compatibilidad UNIQUE(producto_id, motor_id)
);
GO

-- ============================================================
-- TABLA: zonas_delivery
-- ============================================================
CREATE TABLE zonas_delivery (
    id      BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre  NVARCHAR(100) NOT NULL,
    tipo    NVARCHAR(20) NOT NULL CHECK (tipo IN ('LOCAL','INTERPROVINCIAL')),
    tarifa  DECIMAL(8,2) NOT NULL CHECK (tarifa >= 0),
    activo  BIT NOT NULL DEFAULT 1
);
GO

-- ============================================================
-- TABLA: pedidos
-- ============================================================
CREATE TABLE pedidos (
    id                  BIGINT IDENTITY(1,1) PRIMARY KEY,
    numero_pedido       NVARCHAR(20) NOT NULL UNIQUE,
    cliente_id          BIGINT NOT NULL REFERENCES clientes(id),
    estado              NVARCHAR(30) NOT NULL DEFAULT 'PENDIENTE'
                            CHECK (estado IN ('PENDIENTE','CONFIRMADO','EN_PREPARACION','ENVIADO','ENTREGADO','CANCELADO')),
    subtotal            DECIMAL(10,2) NOT NULL,
    costo_delivery      DECIMAL(8,2) DEFAULT 0,
    total               DECIMAL(10,2) NOT NULL,
    tipo_delivery       NVARCHAR(20) NOT NULL CHECK (tipo_delivery IN ('RECOJO','LOCAL','INTERPROVINCIAL')),
    direccion_entrega   NVARCHAR(300),
    zona_id             BIGINT NULL REFERENCES zonas_delivery(id) ON DELETE SET NULL,
    observacion         NVARCHAR(MAX),
    fecha_pedido        DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    fecha_actualizacion DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);
GO

-- ============================================================
-- TABLA: detalle_pedido
-- ============================================================
CREATE TABLE detalle_pedido (
    id               BIGINT IDENTITY(1,1) PRIMARY KEY,
    pedido_id        BIGINT NOT NULL REFERENCES pedidos(id) ON DELETE CASCADE,
    producto_id      BIGINT NOT NULL REFERENCES productos(id),
    cantidad         INT NOT NULL CHECK (cantidad > 0),
    precio_unitario  DECIMAL(10,2) NOT NULL,
    subtotal         DECIMAL(10,2) NOT NULL
);
GO

-- ============================================================
-- TABLA: envios
-- ============================================================
CREATE TABLE envios (
    id                      BIGINT IDENTITY(1,1) PRIMARY KEY,
    pedido_id               BIGINT NOT NULL UNIQUE
                              REFERENCES pedidos(id) ON DELETE CASCADE,
    transportista           NVARCHAR(100),
    numero_guia             NVARCHAR(100),
    fecha_envio             DATETIME2,
    fecha_entrega_estimada  DATE,
    fecha_entrega_real      DATETIME2,
    estado                  NVARCHAR(30)
);
GO

-- ============================================================
-- ÍNDICES para optimizar consultas frecuentes
-- ============================================================
CREATE INDEX idx_productos_categoria ON productos(categoria_id);
CREATE INDEX idx_productos_activo    ON productos(activo);
CREATE INDEX idx_productos_nombre    ON productos(nombre);
CREATE INDEX idx_productos_codigo    ON productos(codigo);
CREATE INDEX idx_modelos_marca       ON modelos(marca_id);
CREATE INDEX idx_generaciones_modelo ON generaciones(modelo_id);
CREATE INDEX idx_motores_generacion  ON motores(generacion_id);
CREATE INDEX idx_compat_producto     ON compatibilidades(producto_id);
CREATE INDEX idx_compat_motor        ON compatibilidades(motor_id);
CREATE INDEX idx_pedidos_cliente     ON pedidos(cliente_id);
CREATE INDEX idx_pedidos_estado      ON pedidos(estado);
CREATE INDEX idx_pedidos_fecha       ON pedidos(fecha_pedido);
CREATE INDEX idx_detalle_pedido_ped  ON detalle_pedido(pedido_id);
CREATE INDEX idx_imagenes_producto   ON imagenes_producto(producto_id);
GO

-- ============================================================
-- TRIGGER: actualizar fecha_actualizacion en pedidos
-- ============================================================
CREATE TRIGGER trg_pedido_actualizacion
ON pedidos
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE p
    SET fecha_actualizacion = SYSDATETIME()
    FROM pedidos p
    INNER JOIN inserted i ON p.id = i.id;
END;
GO

-- ============================================================
-- TRIGGER: descontar inventario cuando un pedido pasa
-- de PENDIENTE a CONFIRMADO
-- ============================================================
CREATE TRIGGER trg_inventario_pedido
ON pedidos
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE inv
    SET stock = inv.stock - dp.cantidad,
        ultima_actualizacion = SYSDATETIME()
    FROM inventario inv
    INNER JOIN detalle_pedido dp ON dp.producto_id = inv.producto_id
    INNER JOIN inserted i ON dp.pedido_id = i.id
    INNER JOIN deleted  d ON d.id = i.id
    WHERE i.estado = 'CONFIRMADO' AND d.estado = 'PENDIENTE';
END;
GO

-- ============================================================
-- Confirmación
-- ============================================================
SELECT 'Base de datos Pacífico Repuestos creada correctamente.' AS resultado;
GO
