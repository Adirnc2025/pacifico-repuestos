-- ============================================================
-- PACÍFICO REPUESTOS - Datos semilla (seed)
-- PostgreSQL
-- ============================================================

-- Usuario administrador
INSERT INTO usuarios (nombre, correo, password, rol) VALUES
('Administrador', 'admin@pacificorepuestos.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2', 'ADMIN');
-- password: admin123 (BCrypt hash)

-- Usuario cliente demo
INSERT INTO usuarios (nombre, correo, password, rol) VALUES
('Cliente Demo', 'cliente@demo.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2', 'CLIENTE');

INSERT INTO clientes (usuario_id, telefono, direccion, dni) VALUES
(2, '966123456', 'Jr. Lima 123, Ayacucho', '12345678');

-- Marcas
INSERT INTO marcas (nombre) VALUES
('Toyota'), ('Nissan'), ('Hyundai'), ('Kia'), ('Chevrolet'),
('Ford'), ('Mitsubishi'), ('Suzuki'), ('Honda'), ('Mazda');

-- Modelos Toyota
INSERT INTO modelos (nombre, marca_id) VALUES
('Hilux', 1), ('Corolla', 1), ('RAV4', 1),
('Yaris', 1), ('Fortuner', 1), ('Land Cruiser', 1);

-- Modelos Nissan
INSERT INTO modelos (nombre, marca_id) VALUES
('Frontier', 2), ('Sentra', 2), ('X-Trail', 2), ('Versa', 2);

-- Modelos Hyundai
INSERT INTO modelos (nombre, marca_id) VALUES
('Tucson', 3), ('Santa Fe', 3), ('Accent', 3), ('Elantra', 3);

-- Generaciones Toyota Hilux
INSERT INTO generaciones (nombre, anio_inicio, anio_fin, modelo_id) VALUES
('Hilux N80 (8va gen)', 2015, 2023, 1),
('Hilux N70 (7ma gen)', 2004, 2015, 1);

-- Generaciones Toyota Corolla
INSERT INTO generaciones (nombre, anio_inicio, anio_fin, modelo_id) VALUES
('Corolla E210 (12va gen)', 2018, 2023, 2),
('Corolla E170 (11va gen)', 2012, 2018, 2);

-- Motores
INSERT INTO motores (codigo, descripcion, cilindrada, generacion_id) VALUES
('2GD-FTV', 'Motor diesel turbo 4 cilindros', '2.4L', 1),
('1GD-FTV', 'Motor diesel turbo 4 cilindros', '2.8L', 1),
('1KD-FTV', 'Motor diesel turbo 4 cilindros', '3.0L', 2),
('2ZR-FE',  'Motor gasolina 4 cilindros DOHC', '1.8L', 3),
('2ZR-FAE', 'Motor gasolina Valvematic', '1.8L', 4);

-- Categorías
INSERT INTO categorias (nombre, descripcion) VALUES
('Reparación de Motor', 'Empaques, pistones, anillos, camisas y componentes internos del motor'),
('Suspensión', 'Amortiguadores, resortes, rotulas, bases y componentes de suspensión'),
('Frenos', 'Pastillas, discos, tambores, cilindros y componentes del sistema de frenos'),
('Dirección', 'Terminales, barras de dirección, cajas y componentes del sistema de dirección'),
('Sistema Eléctrico', 'Alternadores, arrancadores, sensores y componentes eléctricos'),
('Lubricantes', 'Aceites de motor, caja y diferencial, grasas y fluidos'),
('Accesorios', 'Accesorios y complementos automotrices');

-- Productos
INSERT INTO productos (codigo, nombre, descripcion, precio, medidas, destacado, categoria_id) VALUES
('EMP-001', 'Empaque de Motor Toyota 2GD-FTV',
 'Juego completo de empaques de motor para Toyota Hilux 2.4 diesel. Incluye todos los retenes y juntas.',
 280.00, 'Estándar', true, 1),

('EMP-002', 'Empaque de Culata Toyota 1KD-FTV',
 'Empaque de culata reforzado para Toyota Hilux 3.0 diesel. Material multicapa de alta resistencia.',
 95.00, 'Estándar', true, 1),

('PIS-001', 'Kit de Pistones Toyota 2GD-FTV STD',
 'Juego de 4 pistones estándar con anillos para Toyota Hilux 2.4 diesel.',
 620.00, 'STD', false, 1),

('ANI-001', 'Anillos de Motor Toyota 2GD-FTV STD',
 'Juego completo de anillos para 4 pistones. Medida estándar.',
 180.00, 'STD 0.25 0.50', false, 1),

('MEB-001', 'Metal de Biela Toyota 2GD-FTV STD',
 'Juego de metales de biela para Toyota Hilux 2.4 diesel. Medida estándar.',
 145.00, 'STD 0.25 0.50', false, 1),

('MEB-002', 'Metal de Bancada Toyota 2GD-FTV STD',
 'Juego de metales de bancada para Toyota Hilux 2.4 diesel.',
 160.00, 'STD 0.25 0.50', false, 1),

('AMO-001', 'Amortiguador Delantero Toyota Hilux',
 'Amortiguador delantero de gas para Toyota Hilux. Marca de calidad OEM.',
 220.00, 'Delantero', true, 2),

('PAS-001', 'Pastillas de Freno Toyota Hilux Delantera',
 'Juego de pastillas de freno delanteras para Toyota Hilux. Baja generación de polvo.',
 85.00, 'Delantera', false, 3);

-- Inventario
INSERT INTO inventario (producto_id, stock, stock_minimo) VALUES
(1, 12, 3), (2, 8, 3), (3, 5, 2), (4, 20, 5),
(5, 15, 5), (6, 15, 5), (7, 10, 3), (8, 18, 5);

-- Compatibilidades
INSERT INTO compatibilidades (producto_id, motor_id) VALUES
(1, 1),  -- Empaque Motor -> 2GD-FTV
(2, 3),  -- Empaque Culata -> 1KD-FTV
(3, 1),  -- Pistones -> 2GD-FTV
(4, 1),  -- Anillos -> 2GD-FTV
(5, 1),  -- Metal Biela -> 2GD-FTV
(6, 1),  -- Metal Bancada -> 2GD-FTV
(7, 1),  -- Amortiguador -> 2GD-FTV
(7, 2),  -- Amortiguador -> 1GD-FTV (también compatible)
(8, 1),  -- Pastillas -> 2GD-FTV
(8, 2);  -- Pastillas -> 1GD-FTV

-- Zonas de delivery
INSERT INTO zonas_delivery (nombre, tipo, tarifa) VALUES
('Ayacucho ciudad',    'LOCAL', 5.00),
('Ayacucho provincia', 'LOCAL', 10.00),
('Lima',          'INTERPROVINCIAL', 30.00),
('Ica',           'INTERPROVINCIAL', 25.00),
('Cusco',         'INTERPROVINCIAL', 35.00),
('Huancavelica',  'INTERPROVINCIAL', 20.00);

SELECT 'Datos semilla insertados correctamente.' AS resultado;
