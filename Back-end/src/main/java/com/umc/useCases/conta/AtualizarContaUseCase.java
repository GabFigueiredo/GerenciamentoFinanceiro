package com.umc.useCases.conta;

import com.umc.model.conta.Conta;
import com.umc.model.conta.ContaRepository;
import com.umc.model.Dinheiro;
import com.umc.model.usuario.Usuario;
import com.umc.model.enums.Moeda;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
public class AtualizarContaUseCase {

    private final ContaRepository contaRepository;

    public AtualizarContaUseCase(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public Conta execute(Command command) throws SQLException {
        if (command.id() == null || command.id().isBlank())
            throw new IllegalArgumentException("id e obrigatorio");

        Conta atual = contaRepository.findById(command.id())
                .orElseThrow(() -> new NoSuchElementException("Conta nao encontrada"));

        Moeda moeda = atual.getMoeda();
        Usuario usuario = atual.getUsuario();

        Conta atualizada = Conta.builder(usuario, moeda)
                .id(atual.getId())
                .saldoAtual(atual.getSaldoAtual())
                .despesaMensal(atual.getDespesaMensal())
                .metas(atual.getMetas())
                .limiteGastoMensal(command.limiteGastoMensal() != null
                        ? new Dinheiro(moeda, command.limiteGastoMensal())
                        : atual.getLimiteGastoMensal())
                .descricao(command.descricao() != null
                        ? command.descricao()
                        : atual.getDescricao())
                .dataCriacao(atual.getDataCriacao())
                .dataAtualizacao(LocalDate.now())
                .build();

        contaRepository.update(atualizada);
        return atualizada;
    }

    public record Command(
            String id,
            Double limiteGastoMensal,
            String descricao
    ) {}
}
