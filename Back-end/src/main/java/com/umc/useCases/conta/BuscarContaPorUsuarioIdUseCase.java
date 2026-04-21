package com.umc.useCases.conta;

import com.umc.model.conta.Conta;
import com.umc.model.conta.ContaRepository;
import com.umc.model.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BuscarContaPorUsuarioIdUseCase {

    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;

    public BuscarContaPorUsuarioIdUseCase(ContaRepository contaRepository,
                                          UsuarioRepository usuarioRepository) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Conta> execute(String usuarioId) throws SQLException {
        if (usuarioId == null || usuarioId.isBlank())
            throw new IllegalArgumentException("usuarioId e obrigatorio");

        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuario nao encontrado"));

        return contaRepository.findByUsuarioId(usuarioId);
    }
}
