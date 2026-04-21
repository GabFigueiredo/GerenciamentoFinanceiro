package com.umc.useCases.usuario;

import com.umc.model.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Service
public class DeletarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public DeletarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void execute(String id) throws SQLException {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id e obrigatorio");

        usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario nao encontrado"));

        usuarioRepository.delete(id);
    }
}
