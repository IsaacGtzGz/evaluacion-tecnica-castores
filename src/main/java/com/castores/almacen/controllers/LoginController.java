package com.castores.almacen.controllers;

import com.castores.almacen.models.Usuario;
import com.castores.almacen.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String loginPage(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario != null) {
            return usuario.getIdRol() == 2 ? "redirect:/salida" : "redirect:/inventario";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String correo, @RequestParam String contrasena, HttpSession session,
            Model model) {
        Usuario usuario = usuarioService.login(correo, contrasena);
        if (usuario != null) {
            session.setAttribute("usuarioLogueado", usuario);
            return usuario.getIdRol() == 2 ? "redirect:/salida" : "redirect:/inventario";
        }
        model.addAttribute("error", "Correo o contraseña incorrectos, o usuario inactivo.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}