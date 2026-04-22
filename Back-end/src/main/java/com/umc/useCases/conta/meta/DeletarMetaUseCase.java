package com.umc.useCases.conta.meta;

import com.umc.model.conta.ContaRepository;
import com.umc.model.conta.MetaRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Service
public class DeletarMetaUseCase {

    private final MetaRepository metaRepository;
    private final ContaRepository contaRepository;

    public DeletarMetaUseCase(MetaRepository metaRepository, ContaRepository contaRepository) {
        this.metaRepository = metaRepository;
        this.contaRepository = contaRepository;
    }

    public void execute(String contaId, String metaId) throws SQLException {
        if (contaId == null || contaId.isBlank())
            throw new IllegalArgumentException("contaId e obrigatorio");
        if (metaId == null || metaId.isBlank())
            throw new IllegalArgumentException("metaId e obrigatorio");

        contaRepository.findById(contaId)
                .orElseThrow(() -> new NoSuchElementException("Conta nao encontrada"));

        metaRepository.findById(contaId, metaId)
                .orElseThrow(() -> new NoSuchElementException("Meta nao encontrada"));

        metaRepository.delete(contaId, metaId);
    }
}
