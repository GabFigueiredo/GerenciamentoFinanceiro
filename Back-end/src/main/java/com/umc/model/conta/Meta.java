package com.umc.model.conta;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umc.model.Dinheiro;

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

    @JsonCreator
    public Meta(
            @JsonProperty("id") UUID id,
            @JsonProperty("nome") String nome,
            @JsonProperty("valorObjetivo") Dinheiro valorObjetivo,
            @JsonProperty("cargo") String cargo,
            @JsonProperty("dataInicio") LocalDate dataInicio,
            @JsonProperty("dataDeConclusao") LocalDate dataDeConclusao
    ) {
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
