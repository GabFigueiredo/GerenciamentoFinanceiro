package com.umc.model.transacao.Saque;

import com.umc.model.conta.Conta;
import com.umc.model.Dinheiro;
import com.umc.model.transacao.Transacao;

import java.time.LocalDate;

public class Saque extends Transacao {
    private String destino;
    private Despesa despesa;

    public Saque() {}

    public Saque(String id, Conta conta, Dinheiro valor, LocalDate data,
                 String descricao, LocalDate dataCriacao,
                 String destino, Despesa despesa) {
        super(id, conta, valor, data, descricao, dataCriacao);
        this.destino = destino;
        this.despesa = despesa;
    }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public Despesa getDespesa() { return despesa; }
    public void setDespesa(Despesa despesa) { this.despesa = despesa; }
}