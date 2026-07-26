package com.castores.almacen.services;

import com.castores.almacen.models.Usuario;
import com.castores.almacen.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario login(String correo, String contrasena) {
        Optional<Usuario> userOpt = usuarioRepository.findByCorreo(correo);
        if (userOpt.isPresent()) {
            Usuario user = userOpt.get();
            if (user.getContrasena().equals(contrasena) && user.getEstatus() == 1) {
                return user;
            }
        }
        return null;
    }
}