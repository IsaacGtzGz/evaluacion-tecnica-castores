/* ==========================================================================
   AUTOR: Isaac Gutiérrez Gómez
   EVALUACIÓN: Evaluación Técnica - Almacén 
   ========================================================================== */

USE almacen_castores;

-- ROLES
INSERT IGNORE INTO roles (idRol, nombreRol) VALUES 
(1, 'Administrador'),
(2, 'Almacenista');

-- USUARIOS
INSERT IGNORE INTO usuarios (idUsuario, nombre, correo, contrasena, idRol, estatus) VALUES 
(1, 'Administrador', 'admin@castores.com', 'admin123', 1, 1),
(2, 'Almacenista', 'almacen@castores.com', 'almacen123', 2, 1);

-- PRODUCTOS
INSERT IGNORE INTO productos (idProducto, nombre, cantidad, estatus, ultimoUsuario) VALUES 
(1, 'Balatas Delanteras', 15, 1, 1);

-- HISTÓRICO
INSERT IGNORE INTO historico_movimientos (idMovimiento, idProducto, idUsuario, tipoMovimiento, cantidad, fechaHora) VALUES 
(1, 1, 1, 'ENTRADA', 15, NOW());