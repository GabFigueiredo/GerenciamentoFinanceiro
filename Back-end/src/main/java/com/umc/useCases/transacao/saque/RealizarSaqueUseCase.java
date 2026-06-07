package com.umc.useCases.transacao.saque;

import com.umc.ExchangeAPI.ExchangeAPIAbstraction;
import com.umc.model.enums.CategoriaDespesa;
import com.umc.model.enums.FormaPagamento;
import com.umc.model.enums.Moeda;
import com.umc.model.conta.Conta;
import com.umc.model.conta.ContaRepository;
import com.umc.model.transacao.Saque.Despesa;
import com.umc.model.Dinheiro;
import com.umc.model.transacao.Saque.Saque;
import com.umc.model.transacao.Saque.SaqueRepository;
import com.umc.useCases.transacao.saque.validacao.ProcessadorSaque;
import com.umc.useCases.transacao.saque.validacao.SaqueExecutor;
import com.umc.useCases.transacao.saque.validacao.ValidacaoLimiteMensalDecorator;
import com.umc.useCases.transacao.saque.validacao.ValidacaoSaldoDecorator;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class RealizarSaqueUseCase {

    private final ContaRepository contaRepository;
    private final SaqueRepository saqueRepository;
    private final ExchangeAPIAbstraction exchangeAPI;

    public RealizarSaqueUseCase(ContaRepository contaRepository,
                                SaqueRepository saqueRepository,
                                ExchangeAPIAbstraction exchangeAPI) {
        this.contaRepository = contaRepository;
        this.saqueRepository = saqueRepository;
        this.exchangeAPI = exchangeAPI;
    }

    public Saque execute(Command command) throws SQLException {
        validar(command);

        Conta conta = contaRepository.findById(command.contaId())
                .orElseThrow(() -> new NoSuchElementException("Conta nao encontrada"));

        Dinheiro valorSolicitado = new Dinheiro(command.moeda(), command.valor());
        Dinheiro valorNaMoedaDaConta = new Dinheiro(conta.getMoeda());

        if (!valorSolicitado.getMoeda().equals(conta.getMoeda())) {
            exchangeAPI.converter(valorSolicitado, valorNaMoedaDaConta);
        } else {
            valorNaMoedaDaConta.setValor(valorSolicitado.getValor());
        }

        LocalDate hoje = LocalDate.now();
        Despesa despesa = new Despesa(
                UUID.randomUUID().toString(),
                command.descricao(),
                valorNaMoedaDaConta.getValor(),
                hoje,
                CategoriaDespesa.valueOf(command.categoria().toUpperCase()),
                FormaPagamento.valueOf(command.formaPagamento().toUpperCase()),
                command.observacao()
        );

        Saque saque = new Saque(
                UUID.randomUUID().toString(),
                conta,
                valorSolicitado,
                hoje,
                command.descricao(),
                hoje,
                command.destino(),
                despesa
        );

        // Decorator
        ProcessadorSaque processador =
                new ValidacaoSaldoDecorator(
                new ValidacaoLimiteMensalDecorator(
                        new SaqueExecutor()
                )
        );
        processador.processar(saque, conta);

        conta.getSaldoAtual().subtrair(valorNaMoedaDaConta);
        conta.getDespesaMensal().somar(valorNaMoedaDaConta);
        conta = Conta.builder(conta.getUsuario(), conta.getMoeda())
                .id(conta.getId())
                .saldoAtual(conta.getSaldoAtual())
                .despesaMensal(conta.getDespesaMensal())
                .metas(conta.getMetas())
                .limiteGastoMensal(conta.getLimiteGastoMensal())
                .descricao(conta.getDescricao())
                .dataCriacao(conta.getDataCriacao())
                .dataAtualizacao(LocalDate.now())
                .build();
        contaRepository.update(conta);

        saqueRepository.save(saque);
        return saque;
    }

    private void validar(Command command) {
        if (command.contaId() == null || command.contaId().isBlank())
            throw new IllegalArgumentException("contaId e obrigatorio");
        if (command.valor() == null || command.valor() <= 0)
            throw new IllegalArgumentException("valor deve ser maior que zero");
        if (command.moeda() == null)
            throw new IllegalArgumentException("moeda e obrigatoria");
        if (command.formaPagamento() == null || command.formaPagamento().isBlank())
            throw new IllegalArgumentException("formaPagamento e obrigatorio");
        if (command.categoria() == null || command.categoria().isBlank())
            throw new IllegalArgumentException("categoria e obrigatoria");
        if (command.destino() == null || command.destino().isBlank())
            throw new IllegalArgumentException("destino e obrigatorio");
    }

    public record Command(
            String contaId,
            Double valor,
            Moeda moeda,
            String destino,
            String formaPagamento,
            String categoria,
            String descricao,
            String observacao
    ) {}
}
