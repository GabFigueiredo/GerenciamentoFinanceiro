package com.umc.model;

import java.time.LocalDate;
import java.util.UUID;

public class Meta {
    private UUID id;
    private String nome;
    private Dinheiro valorObjetivo;
    private String cargo;
    private LocalDate dataInicio;
    private LocalDate dataDeConclusao;

    public Meta() {}

    public Meta(UUID id, String nome, Dinheiro valorObjetivo, String cargo,
                LocalDate dataInicio, LocalDate dataDeConclusao) {
        this.id = id;
        this.nome = nome;
        this.valorObjetivo = valorObjetivo;
        this.cargo = cargo;
        this.dataInicio = dataInicio;
        this.dataDeConclusao = dataDeConclusao;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Dinheiro getValorObjetivo() { return valorObjetivo; }
    public void setValorObjetivo(Dinheiro valorObjetivo) { this.valorObjetivo = valorObjetivo; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataDeConclusao() { return dataDeConclusao; }
    public void setDataDeConclusao(LocalDate dataDeConclusao) { this.dataDeConclusao = dataDeConclusao; }
}