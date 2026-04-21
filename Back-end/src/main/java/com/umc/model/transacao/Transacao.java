package com.umc.model.transacao;

import com.umc.model.conta.Conta;
import com.umc.model.Dinheiro;

import java.time.LocalDate;

public class Transacao {
    private String id;
    private Conta conta;
    private Dinheiro valor;
    private LocalDate data;
    private String descricao;
    private LocalDate dataCriacao;

    public Transacao() {}

    public Transacao(String id, Conta conta, Dinheiro valor,
                     LocalDate data, String descricao, LocalDate dataCriacao) {
        this.id = id;
        this.conta = conta;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Conta getConta() { return conta; }
    public void setConta(Conta conta) { this.conta = conta; }

    public Dinheiro getValor() { return valor; }
    public void setValor(Dinheiro valor) { this.valor = valor; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }
}