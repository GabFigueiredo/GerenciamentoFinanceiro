package com.umc.model.Despesa;

import com.umc.enums.CategoriaDespesa;
import com.umc.enums.StatusDespesa;
import com.umc.enums.FormaPagamento;

import java.time.LocalDate;

public class Despesa {
    private String id;
    private String descricao;
    private Double valor;
    private LocalDate data;
    private CategoriaDespesa categoriaDespesa;
    private StatusDespesa status;
    private FormaPagamento formaPagamento;
    private String observacao;

    public Despesa() {}

    public Despesa(String id, String descricao, Double valor, LocalDate data,
                   CategoriaDespesa categoriaDespesa, StatusDespesa status,
                   FormaPagamento formaPagamento, String observacao) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.categoriaDespesa = categoriaDespesa;
        this.status = status;
        this.formaPagamento = formaPagamento;
        this.observacao = observacao;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public CategoriaDespesa getCategoriaDespesa() { return categoriaDespesa; }
    public void setCategoriaDespesa(CategoriaDespesa categoriaDespesa) { this.categoriaDespesa = categoriaDespesa; }

    public StatusDespesa getStatus() { return status; }
    public void setStatus(StatusDespesa status) { this.status = status; }

    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}