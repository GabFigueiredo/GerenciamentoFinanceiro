package com.umc.useCases.conta.meta;

import com.umc.model.conta.ContaRepository;
import com.umc.model.conta.Meta;
import com.umc.model.conta.MetaRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ListarMetasUseCase {

    private final MetaRepository metaRepository;
    private final ContaRepository contaRepository;

    public ListarMetasUseCase(MetaRepository metaRepository, ContaRepository contaRepository) {
        this.metaRepository = metaRepository;
        this.contaRepository = contaRepository;
    }

    public List<Meta> execute(String contaId) throws SQLException {
        if (contaId == null || contaId.isBlank())
            throw new IllegalArgumentException("contaId e obrigatorio");

        contaRepository.findById(contaId)
                .orElseThrow(() -> new NoSuchElementException("Conta nao encontrada"));

        return metaRepository.findByContaId(contaId);
    }
}
