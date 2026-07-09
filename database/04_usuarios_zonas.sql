-- ============================================================
-- PACIFICO REPUESTOS - Usuarios, clientes y zonas de delivery reales
-- Migrados desde SQL Server local (pacifico_db) a PostgreSQL
-- Generado automaticamente a partir de la base de datos real
-- PostgreSQL
-- ============================================================
-- Preserva los IDs originales de SQL Server porque clientes.usuario_id
-- depende de usuarios.id. Reemplaza a los INSERT de usuarios/clientes/
-- zonas_delivery de 02_seed_data.sql (que solo tenia 2 usuarios demo).
--
-- Orden de ejecucion: despues de 01_ddl_create.sql
-- (usuarios antes que clientes por la llave foranea)
-- ============================================================

-- ===== USUARIOS =====
INSERT INTO usuarios (id, nombre, correo, password, rol, activo, fecha_registro) OVERRIDING SYSTEM VALUE VALUES (1, 'Administrador', 'admin@pacificorepuestos.com', '$2a$10$v/uSnovJe.OER3yQC84vtuzPImt4y/CWerao9YjNGRbbfX9D2uCpC', 'ADMIN', true, '2026-06-15 08:30:06.047');
INSERT INTO usuarios (id, nombre, correo, password, rol, activo, fecha_registro) OVERRIDING SYSTEM VALUE VALUES (2, 'Cliente Demo', 'cliente@demo.com', '$2a$10$v/uSnovJe.OER3yQC84vtuzPImt4y/CWerao9YjNGRbbfX9D2uCpC', 'CLIENTE', true, '2026-06-15 08:30:06.057');
INSERT INTO usuarios (id, nombre, correo, password, rol, activo, fecha_registro) OVERRIDING SYSTEM VALUE VALUES (3, 'Adir navarro', 'neutrolee965@gmail.com', '$2a$10$7yl2qikEShi23Nklt9U59.WsVrZlovkAymSplsMb2OHzJYVMnT.aW', 'CLIENTE', true, '2026-06-18 12:43:27.991');
INSERT INTO usuarios (id, nombre, correo, password, rol, activo, fecha_registro) OVERRIDING SYSTEM VALUE VALUES (4, 'Juan Prueba', 'juan.prueba@test.com', '$2a$10$.65PLd2O2CVzUZR6pniV5eFSSvoXFVX5KQDzq2kpmEjvjTSO8bxiG', 'CLIENTE', true, '2026-06-30 19:45:57.378');
-- ===== CLIENTES =====
INSERT INTO clientes (id, usuario_id, telefono, direccion, dni) OVERRIDING SYSTEM VALUE VALUES (1, 2, '966123456', 'Jr. Lima 123, Ayacucho', '12345678');
INSERT INTO clientes (id, usuario_id, telefono, direccion, dni) OVERRIDING SYSTEM VALUE VALUES (2, 3, '998280702', NULL, NULL);
INSERT INTO clientes (id, usuario_id, telefono, direccion, dni) OVERRIDING SYSTEM VALUE VALUES (3, 4, '987654321', NULL, NULL);
-- ===== ZONAS_DELIVERY =====
INSERT INTO zonas_delivery (id, nombre, tipo, tarifa, activo) OVERRIDING SYSTEM VALUE VALUES (1, 'Ayacucho ciudad', 'LOCAL', 5.00, true);
INSERT INTO zonas_delivery (id, nombre, tipo, tarifa, activo) OVERRIDING SYSTEM VALUE VALUES (2, 'Ayacucho provincia', 'LOCAL', 10.00, true);
INSERT INTO zonas_delivery (id, nombre, tipo, tarifa, activo) OVERRIDING SYSTEM VALUE VALUES (3, 'Lima', 'INTERPROVINCIAL', 30.00, true);
INSERT INTO zonas_delivery (id, nombre, tipo, tarifa, activo) OVERRIDING SYSTEM VALUE VALUES (4, 'Ica', 'INTERPROVINCIAL', 25.00, true);
INSERT INTO zonas_delivery (id, nombre, tipo, tarifa, activo) OVERRIDING SYSTEM VALUE VALUES (5, 'Cusco', 'INTERPROVINCIAL', 35.00, true);
INSERT INTO zonas_delivery (id, nombre, tipo, tarifa, activo) OVERRIDING SYSTEM VALUE VALUES (6, 'Huancavelica', 'INTERPROVINCIAL', 20.00, true);

-- ============================================================
-- Reiniciar las secuencias de autoincremento
-- ============================================================
SELECT setval(pg_get_serial_sequence('usuarios','id'), (SELECT COALESCE(MAX(id),1) FROM usuarios));
SELECT setval(pg_get_serial_sequence('clientes','id'), (SELECT COALESCE(MAX(id),1) FROM clientes));
SELECT setval(pg_get_serial_sequence('zonas_delivery','id'), (SELECT COALESCE(MAX(id),1) FROM zonas_delivery));

SELECT 'Usuarios, clientes y zonas de delivery migrados correctamente.' AS resultado;
