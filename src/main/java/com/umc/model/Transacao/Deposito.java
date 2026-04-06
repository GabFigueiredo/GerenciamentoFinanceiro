package com.umc.model.Transacao;

import com.umc.enums.Receita;
import com.umc.model.Conta;
import com.umc.model.Dinheiro;

import java.time.LocalDate;

public class Deposito extends Transacao {
    private String origem;
    private Receita receita;

    public Deposito() {}

    public Deposito(String id, Conta conta, Dinheiro valor, LocalDate data,
                    String descricao, LocalDate dataCriacao,
                    String origem, Receita receita) {
        super(id, conta, valor, data, descricao, dataCriacao);
        this.origem = origem;
        this.receita = receita;
    }

    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }

    public Receita getReceita() { return receita; }
    public void setReceita(Receita receita) { this.receita = receita; }
}