package com.umc.ExchangeAPI;

public class ExchangeResponse {
    private String base;
    private String target;
    private double rate;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}