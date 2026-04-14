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

    private Conta(Builder builder) {
        this.id = builder.id;
        this.usuario = builder.usuario;
        this.saldoAtual = builder.saldoAtual;
        this.moeda = builder.moeda;
        this.despesaMensal = builder.despesaMensal;
        this.metas = builder.metas;
        this.limiteGastoMensal = builder.limiteGastoMensal;
        this.descricao = builder.descricao;
        this.dataCriacao = builder.dataCriacao;
        this.dataAtualizacao = builder.dataAtualizacao;
    }

    public static Builder builder(Usuario usuario, Moeda moeda) {
        return new Builder(usuario, moeda);
    }

    // Getters only (recommended when using Builder)
    public UUID getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public Dinheiro getSaldoAtual() { return saldoAtual; }
    public Moeda getMoeda() { return moeda; }
    public Dinheiro getDespesaMensal() { return despesaMensal; }
    public List<Meta> getMetas() { return metas; }
    public Dinheiro getLimiteGastoMensal() { return limiteGastoMensal; }
    public String getDescricao() { return descricao; }
    public LocalDate getDataCriacao() { return dataCriacao; }
    public LocalDate getDataAtualizacao() { return dataAtualizacao; }

    public static class Builder {
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

        public Builder(Usuario usuario, Moeda moeda) {
            this.usuario = usuario;
            this.moeda = moeda;
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder saldoAtual(Dinheiro saldoAtual) {
            this.saldoAtual = saldoAtual;
            return this;
        }

        public Builder despesaMensal(Dinheiro despesaMensal) {
            this.despesaMensal = despesaMensal;
            return this;
        }

        public Builder metas(List<Meta> metas) {
            this.metas = metas;
            return this;
        }

        public Builder limiteGastoMensal(Dinheiro limiteGastoMensal) {
            this.limiteGastoMensal = limiteGastoMensal;
            return this;
        }

        public Builder descricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public Builder dataCriacao(LocalDate dataCriacao) {
            this.dataCriacao = dataCriacao;
            return this;
        }

        public Builder dataAtualizacao(LocalDate dataAtualizacao) {
            this.dataAtualizacao = dataAtualizacao;
            return this;
        }

        public Conta build() {
            // ✅ Validation
            if (usuario == null) {
                throw new IllegalStateException("Usuario é obrigatório!");
            }

            if (moeda == null) {
                throw new IllegalStateException("Moeda é obrigatória!");
            }

            if (dataCriacao == null) {
                dataCriacao = LocalDate.now();
            }

            if (dataAtualizacao == null) {
                dataAtualizacao = LocalDate.now();
            }

            return new Conta(this);
        }
    }
}