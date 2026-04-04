package com.umc.model;

import java.util.UUID;

public class Usuario {
    private UUID id;
    private String nome;
    private String cpf;
    private Contato contato;
    private String cargo;
    private Double salario;

    public Usuario() {}

    public Usuario(UUID id, String nome, String cpf, Contato contato, String cargo, Double salario) {
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