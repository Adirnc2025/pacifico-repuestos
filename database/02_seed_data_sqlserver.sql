-- ============================================================
-- PACÍFICO REPUESTOS — Datos semilla (seed)
-- SQL Server
-- ============================================================

-- Usuario administrador
INSERT INTO usuarios (nombre, correo, password, rol) VALUES
(N'Administrador', N'admin@pacificorepuestos.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2', N'ADMIN');
-- password: admin123 (BCrypt hash)

-- Usuario cliente demo
INSERT INTO usuarios (nombre, correo, password, rol) VALUES
(N'Cliente Demo', N'cliente@demo.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2', N'CLIENTE');

INSERT INTO clientes (usuario_id, telefono, direccion, dni) VALUES
(2, N'966123456', N'Jr. Lima 123, Ayacucho', N'12345678');

-- Marcas
INSERT INTO marcas (nombre) VALUES
(N'Toyota'), (N'Nissan'), (N'Hyundai'), (N'Kia'), (N'Chevrolet'),
(N'Ford'), (N'Mitsubishi'), (N'Suzuki'), (N'Honda'), (N'Mazda');

-- Modelos Toyota
INSERT INTO modelos (nombre, marca_id) VALUES
(N'Hilux', 1), (N'Corolla', 1), (N'RAV4', 1),
(N'Yaris', 1), (N'Fortuner', 1), (N'Land Cruiser', 1);

-- Modelos Nissan
INSERT INTO modelos (nombre, marca_id) VALUES
(N'Frontier', 2), (N'Sentra', 2), (N'X-Trail', 2), (N'Versa', 2);

-- Modelos Hyundai
INSERT INTO modelos (nombre, marca_id) VALUES
(N'Tucson', 3), (N'Santa Fe', 3), (N'Accent', 3), (N'Elantra', 3);

-- Generaciones Toyota Hilux
INSERT INTO generaciones (nombre, anio_inicio, anio_fin, modelo_id) VALUES
(N'Hilux N80 (8va gen)', 2015, 2023, 1),
(N'Hilux N70 (7ma gen)', 2004, 2015, 1);

-- Generaciones Toyota Corolla
INSERT INTO generaciones (nombre, anio_inicio, anio_fin, modelo_id) VALUES
(N'Corolla E210 (12va gen)', 2018, 2023, 2),
(N'Corolla E170 (11va gen)', 2012, 2018, 2);

-- Motores
INSERT INTO motores (codigo, descripcion, cilindrada, generacion_id) VALUES
(N'2GD-FTV', N'Motor diesel turbo 4 cilindros', N'2.4L', 1),
(N'1GD-FTV', N'Motor diesel turbo 4 cilindros', N'2.8L', 1),
(N'1KD-FTV', N'Motor diesel turbo 4 cilindros', N'3.0L', 2),
(N'2ZR-FE',  N'Motor gasolina 4 cilindros DOHC', N'1.8L', 3),
(N'2ZR-FAE', N'Motor gasolina Valvematic', N'1.8L', 4);

-- Categorías
INSERT INTO categorias (nombre, descripcion) VALUES
(N'Reparación de Motor', N'Empaques, pistones, anillos, camisas y componentes internos del motor'),
(N'Suspensión', N'Amortiguadores, resortes, rotulas, bases y componentes de suspensión'),
(N'Frenos', N'Pastillas, discos, tambores, cilindros y componentes del sistema de frenos'),
(N'Dirección', N'Terminales, barras de dirección, cajas y componentes del sistema de dirección'),
(N'Sistema Eléctrico', N'Alternadores, arrancadores, sensores y componentes eléctricos'),
(N'Lubricantes', N'Aceites de motor, caja y diferencial, grasas y fluidos'),
(N'Accesorios', N'Accesorios y complementos automotrices');

-- Productos
INSERT INTO productos (codigo, nombre, descripcion, precio, medidas, destacado, categoria_id) VALUES
(N'EMP-001', N'Empaque de Motor Toyota 2GD-FTV',
 N'Juego completo de empaques de motor para Toyota Hilux 2.4 diesel. Incluye todos los retenes y juntas.',
 280.00, N'Estándar', 1, 1),

(N'EMP-002', N'Empaque de Culata Toyota 1KD-FTV',
 N'Empaque de culata reforzado para Toyota Hilux 3.0 diesel. Material multicapa de alta resistencia.',
 95.00, N'Estándar', 1, 1),

(N'PIS-001', N'Kit de Pistones Toyota 2GD-FTV STD',
 N'Juego de 4 pistones estándar con anillos para Toyota Hilux 2.4 diesel.',
 620.00, N'STD', 0, 1),

(N'ANI-001', N'Anillos de Motor Toyota 2GD-FTV STD',
 N'Juego completo de anillos para 4 pistones. Medida estándar.',
 180.00, N'STD 0.25 0.50', 0, 1),

(N'MEB-001', N'Metal de Biela Toyota 2GD-FTV STD',
 N'Juego de metales de biela para Toyota Hilux 2.4 diesel. Medida estándar.',
 145.00, N'STD 0.25 0.50', 0, 1),

(N'MEB-002', N'Metal de Bancada Toyota 2GD-FTV STD',
 N'Juego de metales de bancada para Toyota Hilux 2.4 diesel.',
 160.00, N'STD 0.25 0.50', 0, 1),

(N'AMO-001', N'Amortiguador Delantero Toyota Hilux',
 N'Amortiguador delantero de gas para Toyota Hilux. Marca de calidad OEM.',
 220.00, N'Delantero', 1, 2),

(N'PAS-001', N'Pastillas de Freno Toyota Hilux Delantera',
 N'Juego de pastillas de freno delanteras para Toyota Hilux. Baja generación de polvo.',
 85.00, N'Delantera', 0, 3);

-- Inventario
INSERT INTO inventario (producto_id, stock, stock_minimo) VALUES
(1, 12, 3), (2, 8, 3), (3, 5, 2), (4, 20, 5),
(5, 15, 5), (6, 15, 5), (7, 10, 3), (8, 18, 5);

-- Compatibilidades
INSERT INTO compatibilidades (producto_id, motor_id) VALUES
(1, 1),  -- Empaque Motor → 2GD-FTV
(2, 3),  -- Empaque Culata → 1KD-FTV
(3, 1),  -- Pistones → 2GD-FTV
(4, 1),  -- Anillos → 2GD-FTV
(5, 1),  -- Metal Biela → 2GD-FTV
(6, 1),  -- Metal Bancada → 2GD-FTV
(7, 1),  -- Amortiguador → 2GD-FTV
(7, 2),  -- Amortiguador → 1GD-FTV (también compatible)
(8, 1),  -- Pastillas → 2GD-FTV
(8, 2);  -- Pastillas → 1GD-FTV

-- Zonas de delivery
INSERT INTO zonas_delivery (nombre, tipo, tarifa) VALUES
(N'Ayacucho ciudad',    N'LOCAL', 5.00),
(N'Ayacucho provincia', N'LOCAL', 10.00),
(N'Lima',          N'INTERPROVINCIAL', 30.00),
(N'Ica',           N'INTERPROVINCIAL', 25.00),
(N'Cusco',         N'INTERPROVINCIAL', 35.00),
(N'Huancavelica',  N'INTERPROVINCIAL', 20.00);

SELECT 'Datos semilla insertados correctamente.' AS resultado;
GO
