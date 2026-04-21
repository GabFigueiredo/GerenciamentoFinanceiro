package com.umc.useCases.transacao;

import com.umc.ExchangeAPI.ExchangeAPIAbstraction;
import com.umc.model.enums.Moeda;
import com.umc.model.enums.Receita;
import com.umc.model.conta.Conta;
import com.umc.model.conta.ContaRepository;
import com.umc.model.Dinheiro;
import com.umc.model.transacao.Deposito.Deposito;
import com.umc.model.transacao.Deposito.DepositoRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class RealizarDepositoUseCase {

    private final ContaRepository contaRepository;
    private final DepositoRepository depositoRepository;
    private final ExchangeAPIAbstraction exchangeAPI;

    public RealizarDepositoUseCase(ContaRepository contaRepository,
                                   DepositoRepository depositoRepository,
                                   ExchangeAPIAbstraction exchangeAPI) {
        this.contaRepository = contaRepository;
        this.depositoRepository = depositoRepository;
        this.exchangeAPI = exchangeAPI;
    }

    public Deposito execute(Command command) throws SQLException {
        validar(command);

        Conta conta = contaRepository.findById(command.contaId())
                .orElseThrow(() -> new NoSuchElementException("Conta nao encontrada"));

        Dinheiro valorDepositado = new Dinheiro(command.moeda(), command.valor());
        Dinheiro valorNaMoedaDaConta = new Dinheiro(conta.getMoeda());

        if (!valorDepositado.getMoeda().equals(conta.getMoeda())) {
            exchangeAPI.converter(valorDepositado, valorNaMoedaDaConta);
        } else {
            valorNaMoedaDaConta.setValor(valorDepositado.getValor());
        }

        conta.getSaldoAtual().somar(valorNaMoedaDaConta);
        contaRepository.save(conta);

        LocalDate hoje = LocalDate.now();

        Deposito deposito = new Deposito(
                UUID.randomUUID().toString(),
                conta,
                valorDepositado,
                hoje,
                command.descricao(),
                hoje,
                command.origem(),
                Receita.valueOf(command.receita().toUpperCase())
        );

        depositoRepository.save(deposito);
        return deposito;
    }

    private void validar(Command command) {
        if (command.contaId() == null || command.contaId().isBlank())
            throw new IllegalArgumentException("contaId e obrigatorio");
        if (command.valor() == null || command.valor() <= 0)
            throw new IllegalArgumentException("valor deve ser maior que zero");
        if (command.moeda() == null)
            throw new IllegalArgumentException("moeda e obrigatoria");
        if (command.origem() == null || command.origem().isBlank())
            throw new IllegalArgumentException("origem e obrigatoria");
        if (command.receita() == null || command.receita().isBlank())
            throw new IllegalArgumentException("receita e obrigatoria");
    }

    public record Command(
            String contaId,
            Double valor,
            Moeda moeda,
            String origem,
            String receita,
            String descricao
    ) {}
}