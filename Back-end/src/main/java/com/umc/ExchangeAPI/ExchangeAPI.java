package com.umc.ExchangeAPI;

import com.umc.model.enums.Moeda;
import com.umc.model.Dinheiro;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExchangeAPI implements ExchangeAPIAbstraction {
    private final WebClient webClient;

    public ExchangeAPI() {
        this.webClient = WebClient.create("https://fxapi.app");
    }

    public ExchangeResponse getExchange(Moeda de, Moeda para) {
        String urlString = "/api/" + de.toString() + "/" + para.toString() + ".json";

        return webClient.get()
                .uri(urlString)
                .retrieve()
                .bodyToMono(ExchangeResponse.class)
                .block(); // bloqueia e retorna resultado
    }

    @Override
    public void converter(Dinheiro origem, Dinheiro destino) {
        double rate = this.getExchange(origem.getMoeda(), destino.getMoeda()).getRate();

        destino.setValor(origem.getValor() * rate);
    }

}
