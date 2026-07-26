package com.castores.almacen.services;

import com.castores.almacen.models.HistoricoMovimiento;
import com.castores.almacen.models.Producto;
import com.castores.almacen.repositories.HistoricoMovimientoRepository;
import com.castores.almacen.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AlmacenService {

    private final ProductoRepository productoRepository;
    private final HistoricoMovimientoRepository historicoRepository;

    public AlmacenService(ProductoRepository productoRepository, HistoricoMovimientoRepository historicoRepository) {
        this.productoRepository = productoRepository;
        this.historicoRepository = historicoRepository;
    }

    public List<Producto> obtenerTodosProductos() {
        return productoRepository.findAll();
    }

    public List<Producto> obtenerProductosActivos() {
        return productoRepository.findByEstatus(1);
    }

    public Producto registrarProducto(String nombre) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setCantidad(0);
        producto.setEstatus(1);
        return productoRepository.save(producto);
    }

    public void entradaProducto(Integer idProducto, Integer cantidadAgregar, Integer idUsuario) {
        if (cantidadAgregar <= 0) {
            throw new IllegalArgumentException("Error: No puedes disminuir o poner 0 en la entrada de inventario.");
        }
        productoRepository.ejecutarMovimiento(idProducto, cantidadAgregar, "ENTRADA", idUsuario);
    }

    public void salidaProducto(Integer idProducto, Integer cantidadSacar, Integer idUsuario) {
        if (cantidadSacar <= 0) {
            throw new IllegalArgumentException("Error: La cantidad a sacar debe ser mayor a 0.");
        }
        Optional<Producto> prodOpt = productoRepository.findById(idProducto);
        if (prodOpt.isPresent()) {
            Producto prod = prodOpt.get();
            if (cantidadSacar > prod.getCantidad()) {
                throw new IllegalArgumentException(
                        "Error: No se puede sacar una cantidad mayor a la del inventario actual.");
            }
            productoRepository.ejecutarMovimiento(idProducto, cantidadSacar, "SALIDA", idUsuario);
        }
    }

    public void cambiarEstatusProducto(Integer idProducto, Integer nuevoEstatus) {
        Optional<Producto> prodOpt = productoRepository.findById(idProducto);
        if (prodOpt.isPresent()) {
            Producto prod = prodOpt.get();
            prod.setEstatus(nuevoEstatus);
            productoRepository.save(prod);
        }
    }

    public List<HistoricoMovimiento> obtenerHistorial() {
        return historicoRepository.findAll();
    }

    public List<HistoricoMovimiento> filtrarHistorial(String tipo) {
        if (tipo == null || tipo.isEmpty() || tipo.equals("TODOS")) {
            return historicoRepository.findAll();
        }
        return historicoRepository.findByTipoMovimiento(tipo);
    }
}