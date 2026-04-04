package com.umc.model;

import com.umc.enums.Moeda;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Conta {
    private UUID id;
    private Usuario usuario;
    private Dinheiro saldoAtual;
    private Moeda moeda;
    private Dinheiro despesaMensal;
    private List<Meta> metas;
    private Dinheiro limiteGastoMensal;
    private String descricao;
    private LocalDate dataCriacao;
    private LocalDate dataAtualizacao;

    public Conta() {}

    public Conta(UUID id, Usuario usuario, Dinheiro saldoAtual, Moeda moeda,
                 Dinheiro despesaMensal, List<Meta> metas, Dinheiro limiteGastoMensal,
                 String descricao, LocalDate dataCriacao, LocalDate dataAtualizacao) {
        this.id = id;
        this.usuario = usuario;
        this.saldoAtual = saldoAtual;
        this.moeda = moeda;
        this.despesaMensal = despesaMensal;
        this.metas = metas;
        this.limiteGastoMensal = limiteGastoMensal;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Dinheiro getSaldoAtual() { return saldoAtual; }
    public void setSaldoAtual(Dinheiro saldoAtual) { this.saldoAtual = saldoAtual; }

    public Moeda getMoeda() { return moeda; }
    public void setMoeda(Moeda moeda) { this.moeda = moeda; }

    public Dinheiro getDespesaMensal() { return despesaMensal; }
    public void setDespesaMensal(Dinheiro despesaMensal) { this.despesaMensal = despesaMensal; }

    public List<Meta> getMetas() { return metas; }
    public void setMetas(List<Meta> metas) { this.metas = metas; }

    public Dinheiro getLimiteGastoMensal() { return limiteGastoMensal; }
    public void setLimiteGastoMensal(Dinheiro limiteGastoMensal) { this.limiteGastoMensal = limiteGastoMensal; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDate getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDate dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}