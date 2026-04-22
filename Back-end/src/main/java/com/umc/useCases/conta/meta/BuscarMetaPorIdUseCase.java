package com.umc.useCases.conta.meta;

import com.umc.model.conta.ContaRepository;
import com.umc.model.conta.Meta;
import com.umc.model.conta.MetaRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Service
public class BuscarMetaPorIdUseCase {

    private final MetaRepository metaRepository;
    private final ContaRepository contaRepository;

    public BuscarMetaPorIdUseCase(MetaRepository metaRepository, ContaRepository contaRepository) {
        this.metaRepository = metaRepository;
        this.contaRepository = contaRepository;
    }

    public Meta execute(String contaId, String metaId) throws SQLException {
        if (contaId == null || contaId.isBlank())
            throw new IllegalArgumentException("contaId e obrigatorio");
        if (metaId == null || metaId.isBlank())
            throw new IllegalArgumentException("metaId e obrigatorio");

        contaRepository.findById(contaId)
                .orElseThrow(() -> new NoSuchElementException("Conta nao encontrada"));

        return metaRepository.findById(contaId, metaId)
                .orElseThrow(() -> new NoSuchElementException("Meta nao encontrada"));
    }
}
