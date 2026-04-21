package com.umc.model.usuario;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umc.model.conta.Contato;

import java.util.UUID;

public class Usuario {
    private UUID id;
    private String nome;
    private String cpf;
    private Contato contato;
    private String cargo;
    private Double salario;

    public Usuario() {}

    @JsonCreator
    public Usuario(
            @JsonProperty("id") UUID id,
            @JsonProperty("nome") String nome,
            @JsonProperty("cpf") String cpf,
            @JsonProperty("contato") Contato contato,
            @JsonProperty("cargo") String cargo,
            @JsonProperty("salario") Double salario
    ) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.contato = contato;
        this.cargo = cargo;
        this.salario = salario;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public Contato getContato() { return contato; }
    public void setContato(Contato contato) { this.contato = contato; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public Double getSalario() { return salario; }
    public void setSalario(Double salario) { this.salario = salario; }
}
