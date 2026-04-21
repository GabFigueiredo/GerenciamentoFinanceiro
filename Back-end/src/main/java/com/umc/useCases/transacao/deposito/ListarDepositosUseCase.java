package com.umc.useCases.transacao.deposito;

import com.umc.model.transacao.Deposito.Deposito;
import com.umc.model.transacao.Deposito.DepositoRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ListarDepositosUseCase {

    private final DepositoRepository depositoRepository;

    public ListarDepositosUseCase(DepositoRepository depositoRepository) {
        this.depositoRepository = depositoRepository;
    }

    public List<Deposito> execute(String contaId) throws SQLException {
        if (contaId != null && !contaId.isBlank())
            return depositoRepository.findByContaId(contaId);

        return depositoRepository.findAll();
    }
}
