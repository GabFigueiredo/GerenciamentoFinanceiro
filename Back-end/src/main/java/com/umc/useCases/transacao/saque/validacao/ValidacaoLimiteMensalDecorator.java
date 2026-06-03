package com.umc.useCases.transacao.saque.validacao;

import com.umc.model.conta.Conta;
import com.umc.model.transacao.Saque.Saque;

public class ValidacaoLimiteMensalDecorator extends ValidacaoSaqueDecorator {

    public ValidacaoLimiteMensalDecorator(ProcessadorSaque processadorAnterior) {
        super(processadorAnterior);
    }

    @Override
    public Saque processar(Saque saque, Conta conta) {
        double projecao = conta.getDespesaMensal().getValor() + saque.getValor().getValor();
        if (projecao > conta.getLimiteGastoMensal().getValor()) {
            throw new IllegalStateException(
                    "Limite mensal atingido. Limite: " + conta.getLimiteGastoMensal().getValor()
            );
        }

        return processadorAnterior.processar(saque, conta);
    }
}
