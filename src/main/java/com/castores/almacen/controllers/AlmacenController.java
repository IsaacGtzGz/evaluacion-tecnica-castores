package com.castores.almacen.controllers;

import com.castores.almacen.models.Usuario;
import com.castores.almacen.services.AlmacenService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AlmacenController {

    private final AlmacenService almacenService;

    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }

    @GetMapping("/inventario")
    public String verInventario(HttpSession session, Model model) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
        if (user == null)
            return "redirect:/";

        if (user.getIdRol() == 1) {
            model.addAttribute("productos", almacenService.obtenerTodosProductos());
            model.addAttribute("mostrarAcciones", true);
            model.addAttribute("esModuloSalida", false);
            model.addAttribute("tituloModulo", "Catálogo de Productos");
        } else {
            model.addAttribute("productos", almacenService.obtenerProductosActivos());
            model.addAttribute("mostrarAcciones", false);
            model.addAttribute("esModuloSalida", false);
            model.addAttribute("tituloModulo", "Catálogo de Productos");
        }
        model.addAttribute("usuario", user);
        return "inventario";
    }

    @GetMapping("/salida")
    public String verSalida(HttpSession session, Model model) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
        if (user == null || user.getIdRol() != 2)
            return "redirect:/inventario";

        model.addAttribute("productos", almacenService.obtenerProductosActivos());
        model.addAttribute("usuario", user);
        model.addAttribute("mostrarAcciones", true);
        model.addAttribute("esModuloSalida", true);
        model.addAttribute("tituloModulo", "Salida de Productos");
        return "inventario";
    }

    @PostMapping("/producto/agregar")
    public String agregarProducto(@RequestParam String nombre, HttpSession session,
            RedirectAttributes redirectAttributes) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
        if (user == null || user.getIdRol() != 1)
            return "redirect:/inventario";

        try {
            almacenService.registrarProducto(nombre);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMensaje", "Error al registrar: " + e.getMessage());
        }
        return "redirect:/inventario";
    }

    @PostMapping("/producto/entrada")
    public String entradaInventario(@RequestParam Integer idProducto, @RequestParam Integer cantidad,
            HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
        if (user == null || user.getIdRol() != 1)
            return "redirect:/inventario";

        try {
            almacenService.entradaProducto(idProducto, cantidad, user.getIdUsuario());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMensaje", e.getMessage());
        }
        return "redirect:/inventario";
    }

    @PostMapping("/producto/salida")
    public String salidaInventario(@RequestParam Integer idProducto, @RequestParam Integer cantidad,
            HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
        if (user == null || user.getIdRol() != 2)
            return "redirect:/inventario";

        try {
            almacenService.salidaProducto(idProducto, cantidad, user.getIdUsuario());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMensaje", e.getMessage());
        }
        return "redirect:/salida";
    }

    @PostMapping("/producto/estatus")
    public String cambiarEstatus(@RequestParam Integer idProducto, @RequestParam Integer estatus, HttpSession session,
            RedirectAttributes redirectAttributes) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
        if (user == null || user.getIdRol() != 1)
            return "redirect:/inventario";

        try {
            almacenService.cambiarEstatusProducto(idProducto, estatus);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMensaje", e.getMessage());
        }
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