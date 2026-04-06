package com.umc.model.Despesa;

import com.umc.model.Despesa.Despesa;
import com.umc.model.Frequencia;
import com.umc.model.CategoriaDespesa;
import com.umc.model.FormaPagamento;
import com.umc.model.StatusDespesa;


import java.time.LocalDate;

// Arrow in diagram = DespesaRecorrente extends Despesa
public class DespesaRecorrente extends Despesa {
    private Frequencia frequencia;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean ativa;
    private Integer diaDeCobranca;

    public DespesaRecorrente() {}

    public DespesaRecorrente(String id, String descricao, Double valor, LocalDate data,
                             com.umc.model.CategoriaDespesa categoriaDespesa, StatusDespesa status,
                             FormaPagamento formaPagamento, String observacao,
                             Frequencia frequencia, LocalDate dataInicio, LocalDate dataFim,
                             boolean ativa, Integer diaDeCobranca) {
        super(id, descricao, valor, data, categoriaDespesa, status, formaPagamento, observacao);
        this.frequencia = frequencia;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.ativa = ativa;
        this.diaDeCobranca = diaDeCobranca;
    }

    public Frequencia getFrequencia() { return frequencia; }
    public void setFrequencia(Frequencia frequencia) { this.frequencia = frequencia; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }

    public Integer getDiaDeCobranca() { return diaDeCobranca; }
    public void setDiaDeCobranca(Integer diaDeCobranca) { this.diaDeCobranca = diaDeCobranca; }
}