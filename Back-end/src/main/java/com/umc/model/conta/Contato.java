package com.umc.model.conta;

public class Contato {
    private String celular;
    private String telefone;

    public Contato() {}

    @com.fasterxml.jackson.annotation.JsonCreator
    public Contato(
            @com.fasterxml.jackson.annotation.JsonProperty("celular") String celular,
            @com.fasterxml.jackson.annotation.JsonProperty("telefone") String telefone
    ) {
        this.celular = celular;
        this.telefone = telefone;
    }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}
