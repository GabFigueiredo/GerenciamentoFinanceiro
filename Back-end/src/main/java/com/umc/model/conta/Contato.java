package com.umc.model.conta;

public class Contato {
    private String celular;
    private String telefone;
    private String email;

    public Contato() {}

    @com.fasterxml.jackson.annotation.JsonCreator
    public Contato(
            @com.fasterxml.jackson.annotation.JsonProperty("celular") String celular,
            @com.fasterxml.jackson.annotation.JsonProperty("telefone") String telefone,
            @com.fasterxml.jackson.annotation.JsonProperty("email") String email
    ) {
        this.celular = celular;
        this.telefone = telefone;
        this.email = email;
    }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
