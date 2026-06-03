package com.umc.useCases.transacao.saque.validacao;

import com.umc.model.conta.Conta;
import com.umc.model.transacao.Saque.Saque;

public abstract class ValidacaoSaqueDecorator implements ProcessadorSaque {
    protected final ProcessadorSaque processadorAnterior;

    public ValidacaoSaqueDecorator(ProcessadorSaque processadorAnterior) {
        this.processadorAnterior = processadorAnterior;
    }

    @Override
    public abstract Saque processar(Saque saque, Conta conta);
}
