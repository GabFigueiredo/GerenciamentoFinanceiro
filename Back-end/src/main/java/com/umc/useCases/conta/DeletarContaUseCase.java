package com.umc.useCases.conta;

import com.umc.model.conta.ContaRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Service
public class DeletarContaUseCase {

    private final ContaRepository contaRepository;

    public DeletarContaUseCase(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public void execute(String id) throws SQLException {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id e obrigatorio");

        contaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Conta nao encontrada"));

        contaRepository.delete(id);
    }
}
