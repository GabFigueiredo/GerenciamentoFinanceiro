package com.umc.model.conta;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Contato {
    private String celular;
    private String telefone;

    public Contato() {}

    @JsonCreator
    public Contato(
            @JsonProperty("celular") String celular,
            @JsonProperty("telefone") String telefone
    ) {
        this.celular = celular;
        this.telefone = telefone;
    }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}
