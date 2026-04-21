package com.umc.useCases.transacao.deposito;

import com.umc.model.transacao.Deposito.Deposito;
import com.umc.model.transacao.Deposito.DepositoRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Service
public class BuscarDepositoPorIdUseCase {

    private final DepositoRepository depositoRepository;

    public BuscarDepositoPorIdUseCase(DepositoRepository depositoRepository) {
        this.depositoRepository = depositoRepository;
    }

    public Deposito execute(String id) throws SQLException {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id e obrigatorio");

        return depositoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Deposito nao encontrado"));
    }
}
