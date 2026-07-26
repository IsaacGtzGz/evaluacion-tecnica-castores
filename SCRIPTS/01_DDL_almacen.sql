/* ==========================================================================
   AUTOR: Isaac Gutiérrez Gómez
   EVALUACIÓN: Evaluación Técnica - Almacén 
   ========================================================================== */

DROP DATABASE IF EXISTS almacen_castores;
CREATE DATABASE almacen_castores;
USE almacen_castores;

-- TABLAS 
CREATE TABLE IF NOT EXISTS roles (
    idRol INT(2) AUTO_INCREMENT PRIMARY KEY,
    nombreRol VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS usuarios (
    idUsuario INT(6) AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(25) NOT NULL,
    idRol INT(2) NOT NULL,
    estatus INT(1) DEFAULT 1,
    FOREIGN KEY (idRol) REFERENCES roles(idRol)
);

CREATE TABLE IF NOT EXISTS productos (
    idProducto INT(6) AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cantidad INT(6) DEFAULT 0,
    estatus INT(1) DEFAULT 1,
    ultimoUsuario INT(6) DEFAULT 1,
    FOREIGN KEY (ultimoUsuario) REFERENCES usuarios(idUsuario)
);

CREATE TABLE IF NOT EXISTS historico_movimientos (
    idMovimiento INT(6) AUTO_INCREMENT PRIMARY KEY,
    idProducto INT(6) NOT NULL,
    idUsuario INT(6) NOT NULL,
    tipoMovimiento VARCHAR(10) NOT NULL,
    cantidad INT(6) NOT NULL,
    fechaHora DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idProducto) REFERENCES productos(idProducto),
    FOREIGN KEY (idUsuario) REFERENCES usuarios(idUsuario)
);

-- TRIGGERS Y STORED PROCEDURES
DELIMITER //
DROP TRIGGER IF EXISTS trg_registro_historico //
CREATE TRIGGER trg_registro_historico 
AFTER UPDATE ON productos
FOR EACH ROW
BEGIN
    DECLARE v_tipo VARCHAR(10);
    DECLARE v_dif INT;
    
    IF OLD.cantidad <> NEW.cantidad THEN
        IF NEW.cantidad > OLD.cantidad THEN
            SET v_tipo = 'ENTRADA';
            SET v_dif = NEW.cantidad - OLD.cantidad;
        ELSE
            SET v_tipo = 'SALIDA';
            SET v_dif = OLD.cantidad - NEW.cantidad;
        END IF;
        
        INSERT INTO historico_movimientos (idProducto, idUsuario, tipoMovimiento, cantidad, fechaHora)
        VALUES (NEW.idProducto, NEW.ultimoUsuario, v_tipo, v_dif, NOW());
    END IF;
END //
DELIMITER ;

DROP VIEW IF EXISTS vw_historico_movimientos;
CREATE VIEW vw_historico_movimientos AS
SELECT hm.idMovimiento,
       hm.idProducto,
       p.nombre AS nombreProducto,
       hm.idUsuario,
       u.nombre AS nombreUsuario,
       hm.tipoMovimiento,
       hm.cantidad,
       hm.fechaHora
FROM historico_movimientos hm
INNER JOIN productos p ON hm.idProducto = p.idProducto
INNER JOIN usuarios u ON hm.idUsuario = u.idUsuario;

DELIMITER //
DROP PROCEDURE IF EXISTS sp_movimiento_inventario //
CREATE PROCEDURE sp_movimiento_inventario(
    IN p_idProducto INT,
    IN p_cantidad INT,
    IN p_tipo VARCHAR(10),
    IN p_idUsuario INT
)
BEGIN
    IF p_tipo = 'ENTRADA' THEN
        UPDATE productos 
        SET cantidad = cantidad + p_cantidad,
            ultimoUsuario = p_idUsuario
        WHERE idProducto = p_idProducto AND estatus = 1;
        
    ELSEIF p_tipo = 'SALIDA' THEN
        UPDATE productos 
        SET cantidad = cantidad - p_cantidad,
            ultimoUsuario = p_idUsuario
        WHERE idProducto = p_idProducto AND cantidad >= p_cantidad AND estatus = 1;
    END IF;
END //
DELIMITER ;