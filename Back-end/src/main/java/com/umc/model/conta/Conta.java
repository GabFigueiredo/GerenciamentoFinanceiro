package com.umc.model.conta;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umc.model.enums.Moeda;
import com.umc.model.Dinheiro;
import com.umc.model.usuario.Usuario;

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
        this(
                builder.id,
                builder.usuario,
                builder.saldoAtual,
                builder.moeda,
                builder.despesaMensal,
                builder.metas,
                builder.limiteGastoMensal,
                builder.descricao,
                builder.dataCriacao,
                builder.dataAtualizacao
        );
    }

    @JsonCreator
    public Conta(
            @JsonProperty("id") UUID id,
            @JsonProperty("usuario") Usuario usuario,
            @JsonProperty("saldoAtual") Dinheiro saldoAtual,
            @JsonProperty("moeda") Moeda moeda,
            @JsonProperty("despesaMensal") Dinheiro despesaMensal,
            @JsonProperty("metas") List<Meta> metas,
            @JsonProperty("limiteGastoMensal") Dinheiro limiteGastoMensal,
            @JsonProperty("descricao") String descricao,
            @JsonProperty("dataCriacao") LocalDate dataCriacao,
            @JsonProperty("dataAtualizacao") LocalDate dataAtualizacao
    ) {
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
