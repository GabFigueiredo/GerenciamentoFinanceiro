package com.umc.useCases.conta;

import com.umc.model.conta.Conta;
import com.umc.model.conta.ContaRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ListarContasUseCase {

    private final ContaRepository contaRepository;

    public ListarContasUseCase(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public List<Conta> execute(String usuarioId) throws SQLException {
        if (usuarioId != null && !usuarioId.isBlank())
            return contaRepository.findByUsuarioId(usuarioId);

        return contaRepository.findAll();
    }
}