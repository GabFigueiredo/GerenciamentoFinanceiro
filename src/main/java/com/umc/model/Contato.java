package com.umc.model;

public class Contato {
    private String celular;
    private String telefone;
    private String email;

    public Contato() {}

    public Contato(String celular, String telefone, String email) {
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