package com.umc.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umc.model.enums.Moeda;

public class Dinheiro {
    private Moeda moeda;
    private Double valor;

    public Dinheiro() {}

    public Dinheiro(Moeda moeda) {
        this.moeda = moeda;
    }

    @JsonCreator
    public Dinheiro(
            @JsonProperty("moeda") Moeda moeda,
            @JsonProperty("valor") Double valor
    ) {
        this.moeda = moeda;
        this.valor = valor;
    }

    public void somar(Dinheiro outro) {
        if (!this.moeda.equals(outro.getMoeda())) {
            throw new IllegalArgumentException("Moedas diferentes, converta antes de somar.");
        }
        this.valor += outro.getValor();
    }

    public void subtrair(Dinheiro outro) {
        if (!this.moeda.equals(outro.getMoeda())) {
            throw new IllegalArgumentException("Moedas diferentes, converta antes de subtrair.");
        }
        this.valor -= outro.getValor();
    }

    public Moeda getMoeda() { return moeda; }
    public void setMoeda(Moeda moeda) { this.moeda = moeda; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
}
