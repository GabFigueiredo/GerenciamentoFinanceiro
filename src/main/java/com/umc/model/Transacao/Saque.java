package com.umc.model.Transacao;

import com.umc.enums.FormaPagamento;
import com.umc.model.Conta;
import com.umc.model.Despesa.Despesa;
import com.umc.model.Dinheiro;

import java.time.LocalDate;

// Arrow in diagram = Saque extends Transacao
public class Saque extends Transacao {
    private String id;
    private String destino;
    private FormaPagamento formaPagamento;
    private Despesa despesa;

    public Saque() {}

    public Saque(String id, Conta conta, Dinheiro valor, LocalDate data,
                 String descricao, LocalDate dataCriacao,
                 String saqueId, String destino,
                 FormaPagamento formaPagamento, Despesa despesa) {
        super(id, conta, valor, data, descricao, dataCriacao);
        this.id = saqueId;
        this.destino = destino;
        this.formaPagamento = formaPagamento;
        this.despesa = despesa;
    }

    public String getSaqueId() { return id; }
    public void setSaqueId(String id) { this.id = id; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }

    public Despesa getDespesa() { return despesa; }
    public void setDespesa(Despesa despesa) { this.despesa = despesa; }
}