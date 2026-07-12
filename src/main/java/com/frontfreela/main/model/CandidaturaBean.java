/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontfreela.main.model;

import java.time.LocalDate;

public class CandidaturaBean {

    private Long id;
    private TrabalhoBean trabalho;
    private UsuarioBean trabalhador;
    private LocalDate dataCandidatura;
    private String status = "PENDENTE";
    private Double matchScore;

    public CandidaturaBean() {
    }

    public CandidaturaBean(Long id, TrabalhoBean trabalho, UsuarioBean trabalhador, LocalDate dataCandidatura, String status) {
        this.id = id;
        this.trabalho = trabalho;
        this.trabalhador = trabalhador;
        this.dataCandidatura = dataCandidatura;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrabalhoBean getTrabalho() {
        return trabalho;
    }

    public void setTrabalho(TrabalhoBean trabalho) {
        this.trabalho = trabalho;
    }

    public UsuarioBean getTrabalhador() {
        return trabalhador;
    }

    public void setTrabalhador(UsuarioBean trabalhador) {
        this.trabalhador = trabalhador;
    }

    public LocalDate getDataCandidatura() {
        return dataCandidatura;
    }

    public void setDataCandidatura(LocalDate dataCandidatura) {
        this.dataCandidatura = dataCandidatura;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Double matchScore) {
        this.matchScore = matchScore;
    }
}
