package com.umc.useCases.conta;

import com.umc.model.conta.Conta;
import com.umc.model.conta.ContaRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Service
public class BuscarContaPorIdUseCase {

    private final ContaRepository contaRepository;

    public BuscarContaPorIdUseCase(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public Conta execute(String id) throws SQLException {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id e obrigatorio");

        return contaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Conta nao encontrada"));
    }
}