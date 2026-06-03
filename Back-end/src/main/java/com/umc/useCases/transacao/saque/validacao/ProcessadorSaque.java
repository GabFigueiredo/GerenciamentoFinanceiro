package com.umc.useCases.transacao.saque.validacao;

import com.umc.model.conta.Conta;
import com.umc.model.transacao.Saque.Saque;

public interface ProcessadorSaque {
    Saque processar(Saque saque, Conta conta);
}
