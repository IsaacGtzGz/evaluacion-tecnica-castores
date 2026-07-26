package com.castores.almacen.controllers;

import com.castores.almacen.models.Producto;
import com.castores.almacen.models.Usuario;
import com.castores.almacen.services.AlmacenService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AlmacenController {

    @Autowired
    private AlmacenService almacenService;

    @GetMapping("/inventario")
    public String verInventario(HttpSession session, Model model) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
        if (user == null)
            return "redirect:/";

        if (user.getIdRol() == 1) {
            model.addAttribute("productos", almacenService.obtenerTodosProductos());
        } else {
            model.addAttribute("productos", almacenService.obtenerProductosActivos());
        }
        model.addAttribute("usuario", user);
        return "inventario";
    }

    @PostMapping("/producto/agregar")
    public String agregarProducto(@RequestParam String nombre, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
        if (user == null || user.getIdRol() != 1)
            return "redirect:/inventario";

        almacenService.registrarProducto(nombre);
        return "redirect:/inventario";
    }

    @PostMapping("/producto/entrada")
    public String entradaInventario(@RequestParam Integer idProducto, @RequestParam Integer cantidad,
            HttpSession session, Model model) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
        if (user == null || user.getIdRol() != 1)
            return "redirect:/inventario";

        try {
            almacenService.entradaProducto(idProducto, cantidad, user.getIdUsuario());
        } catch (IllegalArgumentException e) {
            session.setAttribute("errorMensaje", e.getMessage());
        }
        return "redirect:/inventario";
    }

    @PostMapping("/producto/salida")
    public String salidaInventario(@RequestParam Integer idProducto, @RequestParam Integer cantidad,
            HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
        if (user == null || user.getIdRol() != 2)
            return "redirect:/inventario";

        try {
            almacenService.salidaProducto(idProducto, cantidad, user.getIdUsuario());
        } catch (IllegalArgumentException e) {
            session.setAttribute("errorMensaje", e.getMessage());
        }
        return "redirect:/inventario";
    }

    @PostMapping("/producto/estatus")
    public String cambiarEstatus(@RequestParam Integer idProducto, @RequestParam Integer estatus, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
        if (user == null || user.getIdRol() != 1)
            return "redirect:/inventario";

        almacenService.cambiarEstatusProducto(idProducto, estatus);
        return "redirect:/inventario";
    }

    @GetMapping("/historico")
    public String verHistorico(@RequestParam(required = false, defaultValue = "TODOS") String filtro,
            HttpSession session, Model model) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
        if (user == null || user.getIdRol() != 1)
            return "redirect:/inventario";

        model.addAttribute("movimientos", almacenService.filtrarHistorial(filtro));
        model.addAttribute("filtroActual", filtro);
        model.addAttribute("usuario", user);
        return "historico";
    }
}