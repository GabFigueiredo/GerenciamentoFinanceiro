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
import java.util.UUID;

@Service
public class CriarMetaUseCase {

    private final MetaRepository metaRepository;
    private final ContaRepository contaRepository;

    public CriarMetaUseCase(MetaRepository metaRepository, ContaRepository contaRepository) {
        this.metaRepository = metaRepository;
        this.contaRepository = contaRepository;
    }

    public Meta execute(Command command) throws SQLException {
        validar(command);

        Conta conta = contaRepository.findById(command.contaId())
                .orElseThrow(() -> new NoSuchElementException("Conta nao encontrada"));

        LocalDate dataInicio = command.dataInicio() != null ? command.dataInicio() : LocalDate.now();
        validarDatas(dataInicio, command.dataDeConclusao());

        Meta meta = new Meta(
                UUID.randomUUID(),
                command.nome(),
                new Dinheiro(conta.getMoeda(), command.valorObjetivo()),
                command.cargo(),
                dataInicio,
                command.dataDeConclusao()
        );

        metaRepository.save(command.contaId(), meta);
        return meta;
    }

    private void validar(Command command) {
        if (command.contaId() == null || command.contaId().isBlank())
            throw new IllegalArgumentException("contaId e obrigatorio");
        if (command.nome() == null || command.nome().isBlank())
            throw new IllegalArgumentException("nome e obrigatorio");
        if (command.valorObjetivo() == null || command.valorObjetivo() <= 0)
            throw new IllegalArgumentException("valorObjetivo deve ser maior que zero");
    }

    private void validarDatas(LocalDate dataInicio, LocalDate dataDeConclusao) {
        if (dataDeConclusao != null && dataDeConclusao.isBefore(dataInicio))
            throw new IllegalArgumentException("dataDeConclusao nao pode ser anterior a dataInicio");
    }

    public record Command(
            String contaId,
            String nome,
            Double valorObjetivo,
            String cargo,
            LocalDate dataInicio,
            LocalDate dataDeConclusao
    ) {}
}
