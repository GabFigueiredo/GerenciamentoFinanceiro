package com.umc.useCases.transacao.saque.validacao;

import com.umc.model.conta.Conta;
import com.umc.model.transacao.Saque.Saque;

public class ValidacaoSaldoDecorator extends ValidacaoSaqueDecorator {

    public ValidacaoSaldoDecorator(ProcessadorSaque processadorAnterior) {
        super(processadorAnterior);
    }

    @Override
    public Saque processar(Saque saque, Conta conta) {
        if (saque.getValor().getValor() > conta.getSaldoAtual().getValor()) {
            throw new IllegalStateException(
                    "Saldo insuficiente. Disponivel: " + conta.getSaldoAtual().getValor()
            );
        }

        return processadorAnterior.processar(saque, conta);
    }
}
