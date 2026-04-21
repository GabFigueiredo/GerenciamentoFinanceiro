package com.umc.useCases.transacao.saque;

import com.umc.model.transacao.Saque.Saque;
import com.umc.model.transacao.Saque.SaqueRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ListarSaquesUseCase {

    private final SaqueRepository saqueRepository;

    public ListarSaquesUseCase(SaqueRepository saqueRepository) {
        this.saqueRepository = saqueRepository;
    }

    public List<Saque> execute(String contaId) throws SQLException {
        if (contaId != null && !contaId.isBlank())
            return saqueRepository.findByContaId(contaId);

        return saqueRepository.findAll();
    }
}