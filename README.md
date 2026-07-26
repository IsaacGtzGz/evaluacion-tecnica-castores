# Prueba técnica de Almacén
Aplicación web para administrar inventario, registrar entradas y controlar salidas de material. Está desarrollada con Java 21, Spring Boot, Thymeleaf y MySQL, siguiendo un diseño MVC y separando claramente los permisos de administrador y almacenista.

## Datos del proyecto
- IDE utilizado: Visual Studio Code.
- Lenguaje de programación: Java 21.
- Framework principal: Spring Boot 4.1.0.
- DBMS utilizado: MySQL 8.0.1

## Arquitectura MVC

El proyecto sigue el patrón MVC:

- Model: entidades JPA y lógica de persistencia.
- View: vistas Thymeleaf dentro de [src/main/resources/templates](src/main/resources/templates).
- Controller: controladores web dentro de [src/main/java/com/castores/almacen/controllers](src/main/java/com/castores/almacen/controllers).

## Funcionalidad implementada
- Inicio de sesión por usuario y sesión activa.
- Módulo de inventario para administrador.
- Registro de nuevos productos con cantidad inicial en 0.
- Aumento de inventario mediante entradas de material.
- Baja lógica de productos sin eliminar registros.
- Reactivación de productos dados de baja.
- Visualización de productos activos e inactivos.
- Módulo independiente para salida de productos.
- Historial de movimientos con filtro por tipo.
- Registro del usuario que realizó cada movimiento.
- Registro de fecha y hora en cada movimiento.

## Roles y permisos
| Permiso                               | Administrador | Almacenista |
| ------------------------------------- | ------------- | ----------- |
| Ver módulo inventario                 | Sí            | Sí          |
| Agregar nuevos productos              | Sí            | No          |
| Aumentar inventario                   | Sí            | No          |
| Dar de baja / reactivar un producto   | Sí            | No          |
| Ver módulo de salida de productos     | No            | Sí          |
| Sacar inventario del almacén          | No            | Sí          |
| Ver módulo histórico                  | Sí            | No          |

## Flujos principales

### Administrador
Accede a `/inventario` y puede:

- Ver el catálogo completo de productos.
- Registrar productos nuevos.
- Dar entrada de material.
- Dar de baja o reactivar productos.
- Consultar el historial de movimientos en `/historico`.

### Almacenista
Accede a `/salida` y puede:

- Ver solo productos activos.
- Registrar salidas de material.
- Trabajar en un módulo separado del inventario administrativo.

## Rutas principales
- `/` inicio de sesión.
- `/inventario` catálogo y administración de inventario.
- `/salida` salida de productos.
- `/historico` historial de movimientos.
- `/logout` cierre de sesión.

## Estructura de base de datos
El proyecto usa una base MySQL llamada `almacen_castores`. La conexión se configura en [src/main/resources/application.properties](src/main/resources/application.properties).

Valores por defecto:
- Host: `localhost`
- Puerto: `3306`
- Base de datos: `almacen_castores`
- Usuario: `root`
- Contraseña: `root`

## Scripts SQL
Los scripts solicitados por la evaluación están en [SCRIPTS](SCRIPTS):
- [01_DDL_almacen.sql](SCRIPTS/01_DDL_almacen.sql)
- [02_DML_almacen.sql](SCRIPTS/02_DML_almacen.sql)

También se incluyen los entregables de apoyo:
- [Diagrama Relacional.png](SCRIPTS/Diagrama%20Relacional.png)
- [IGG_CONOCIMIENTOS_SQL.pdf](SCRIPTS/IGG_CONOCIMIENTOS_SQL.pdf)

## Pasos para ejecutar la aplicación
1. Abrir MySQL para crear la base de datos `almacen_castores`
2. Ejecutar primero [01_DDL_almacen.sql](SCRIPTS/01_DDL_almacen.sql).
3. Ejecutar después [02_DML_almacen.sql](SCRIPTS/02_DML_almacen.sql).
4. Verificar las credenciales de conexión en [src/main/resources/application.properties](src/main/resources/application.properties).

5. Levantar la aplicación desde VS Code abriendo [src/main/java/com/castores/almacen/AlmacenApplication.java](src/main/java/com/castores/almacen/AlmacenApplication.java) y usando la opción Run Java.

   También puedes arrancarla desde la terminal, desde la raíz del proyecto, con:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

6. Abrir la aplicación en el navegador en http://localhost:8080.


## Estructura del proyecto
- [src/main/java/com/castores/almacen/controllers](src/main/java/com/castores/almacen/controllers): capa Controller del patrón MVC.
- [src/main/java/com/castores/almacen/models](src/main/java/com/castores/almacen/models): capa Model del patrón MVC.
- [src/main/java/com/castores/almacen/repositories](src/main/java/com/castores/almacen/repositories): acceso a datos.
- [src/main/java/com/castores/almacen/services](src/main/java/com/castores/almacen/services): lógica de negocio.
- [src/main/resources/templates](src/main/resources/templates): capa View del patrón MVC.
- [SCRIPTS](SCRIPTS): scripts y documentos de apoyo de la evaluación.
