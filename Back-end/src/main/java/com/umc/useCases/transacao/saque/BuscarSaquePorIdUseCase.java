package com.umc.useCases.transacao.saque;

import com.umc.model.transacao.Saque.Saque;
import com.umc.model.transacao.Saque.SaqueRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Service
public class BuscarSaquePorIdUseCase {

    private final SaqueRepository saqueRepository;

    public BuscarSaquePorIdUseCase(SaqueRepository saqueRepository) {
        this.saqueRepository = saqueRepository;
    }

    public Saque execute(String id) throws SQLException {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id e obrigatorio");

        return saqueRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Saque nao encontrado"));
    }
}