package com.umc.useCases;

import com.umc.enums.Moeda;
import com.umc.model.Conta;
import com.umc.model.Dinheiro;
import com.umc.model.Usuario;
import com.umc.repositories.ContaRepository;
import com.umc.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class CriarContaUseCase {

    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;

    public CriarContaUseCase(ContaRepository contaRepository, UsuarioRepository usuarioRepository) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Conta execute(Command command) throws SQLException {
        validar(command);

        Usuario usuario = usuarioRepository.findById(command.usuarioId())
                .orElseThrow(() -> new NoSuchElementException("Usuario nao encontrado"));

        LocalDate hoje = LocalDate.now();
        UUID contaId = UUID.randomUUID();
        Moeda moeda = Moeda.valueOf(command.moeda().toUpperCase());

        Conta conta = Conta.builder(usuario, moeda)
                .id(contaId)
                .saldoAtual(new Dinheiro(moeda, command.saldoAtual()))
                .despesaMensal(new Dinheiro(moeda, command.despesaMensal()))
                .metas(List.of())
                .limiteGastoMensal(new Dinheiro(moeda, command.limiteGastoMensal()))
                .descricao(command.descricao())
                .dataCriacao(hoje)
                .dataAtualizacao(hoje)
                .build();

        contaRepository.save(conta);
        return conta;
    }

    private void validar(Command command) {
        if (command.usuarioId() == null || command.usuarioId().isBlank()) {
            throw new IllegalArgumentException("usuarioId e obrigatorio");
        }

        if (command.moeda() == null || command.moeda().isBlank()) {
            throw new IllegalArgumentException("moeda e obrigatoria");
        }

        if (command.saldoAtual() == null) {
            throw new IllegalArgumentException("saldoAtual e obrigatorio");
        }

        if (command.despesaMensal() == null) {
            throw new IllegalArgumentException("despesaMensal e obrigatoria");
        }

        if (command.limiteGastoMensal() == null) {
            throw new IllegalArgumentException("limiteGastoMensal e obrigatorio");
        }
    }

    public record Command(
            String usuarioId,
            String moeda,
            Double saldoAtual,
            Double despesaMensal,
            Double limiteGastoMensal,
            String descricao
    ) {
    }
}
