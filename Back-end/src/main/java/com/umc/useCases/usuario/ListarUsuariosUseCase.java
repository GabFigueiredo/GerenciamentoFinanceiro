package com.umc.useCases.usuario;

import com.umc.model.usuario.Usuario;
import com.umc.model.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ListarUsuariosUseCase {

    private final UsuarioRepository usuarioRepository;

    public ListarUsuariosUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> execute() throws SQLException {
        return usuarioRepository.findAll();
    }
}
