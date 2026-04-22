package com.umc.useCases.conta.meta;

import com.umc.model.Dinheiro;
import com.umc.model.conta.Conta;
import com.umc.model.conta.ContaRepository;
import com.umc.model.conta.Meta;
import com.umc.model.conta.MetaRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
public class AtualizarMetaUseCase {

    private final MetaRepository metaRepository;
    private final ContaRepository contaRepository;

    public AtualizarMetaUseCase(MetaRepository metaRepository, ContaRepository contaRepository) {
        this.metaRepository = metaRepository;
        this.contaRepository = contaRepository;
    }

    public Meta execute(Command command) throws SQLException {
        if (command.contaId() == null || command.contaId().isBlank())
            throw new IllegalArgumentException("contaId e obrigatorio");
        if (command.metaId() == null || command.metaId().isBlank())
            throw new IllegalArgumentException("metaId e obrigatorio");
        if (command.nome() != null && command.nome().isBlank())
            throw new IllegalArgumentException("nome e obrigatorio");
        if (command.valorObjetivo() != null && command.valorObjetivo() <= 0)
            throw new IllegalArgumentException("valorObjetivo deve ser maior que zero");

        Conta conta = contaRepository.findById(command.contaId())
                .orElseThrow(() -> new NoSuchElementException("Conta nao encontrada"));

        Meta atual = metaRepository.findById(command.contaId(), command.metaId())
                .orElseThrow(() -> new NoSuchElementException("Meta nao encontrada"));

        LocalDate dataInicio = command.dataInicio() != null ? command.dataInicio() : atual.getDataInicio();
        LocalDate dataDeConclusao = command.dataDeConclusao() != null
                ? command.dataDeConclusao()
                : atual.getDataDeConclusao();

        validarDatas(dataInicio, dataDeConclusao);

        Meta atualizada = new Meta(
                atual.getId(),
                command.nome() != null ? command.nome() : atual.getNome(),
                command.valorObjetivo() != null
                        ? new Dinheiro(conta.getMoeda(), command.valorObjetivo())
                        : atual.getValorObjetivo(),
                command.cargo() != null ? command.cargo() : atual.getCargo(),
                dataInicio,
                dataDeConclusao
        );

        metaRepository.update(command.contaId(), atualizada);
        return atualizada;
    }

    private void validarDatas(LocalDate dataInicio, LocalDate dataDeConclusao) {
        if (dataDeConclusao != null && dataDeConclusao.isBefore(dataInicio))
            throw new IllegalArgumentException("dataDeConclusao nao pode ser anterior a dataInicio");
    }

    public record Command(
            String contaId,
            String metaId,
            String nome,
            Double valorObjetivo,
            String cargo,
            LocalDate dataInicio,
            LocalDate dataDeConclusao
    ) {}
}
