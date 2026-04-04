package com.umc.model;

import com.umc.enums.Moeda;

public class Dinheiro {
    private Moeda moeda;
    private Double valor;

    public Dinheiro() {}

    public Dinheiro(Moeda moeda, Double valor) {
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

    // Conversion rates are hardcoded for simplicity — ideally fetch from an API
    public void converterPara(Moeda novaMoeda) {
        if (this.moeda.equals(novaMoeda)) return;

        double emBRL = switch (this.moeda) {
            case BRL -> this.valor;
            case USD -> this.valor * 5.00;
            case EUR -> this.valor * 5.50;
            case GBP -> this.valor * 6.30;
        };

        this.valor = switch (novaMoeda) {
            case BRL -> emBRL;
            case USD -> emBRL / 5.00;
            case EUR -> emBRL / 5.50;
            case GBP -> emBRL / 6.30;
        };

        this.moeda = novaMoeda;
    }

    public Moeda getMoeda() { return moeda; }
    public void setMoeda(Moeda moeda) { this.moeda = moeda; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
}