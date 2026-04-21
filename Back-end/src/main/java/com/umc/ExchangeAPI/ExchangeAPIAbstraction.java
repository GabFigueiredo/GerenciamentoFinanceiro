package com.umc.ExchangeAPI;

import com.umc.model.enums.Moeda;
import com.umc.model.Dinheiro;

public interface ExchangeAPIAbstraction {
    public ExchangeResponse getExchange(Moeda de, Moeda para);
    public void converter(Dinheiro currentAmount, Dinheiro incomeAmount);
}
