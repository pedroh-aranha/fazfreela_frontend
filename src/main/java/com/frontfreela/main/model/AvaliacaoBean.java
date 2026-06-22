/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontfreela.main.model;

public class AvaliacaoBean {

    private Long id;
    private Integer nota;
    private String comentario;
    private TrabalhoBean trabalho;
    private UsuarioBean avaliador;
    private UsuarioBean avaliado;

    public AvaliacaoBean() {
    }

    public AvaliacaoBean(Long id, Integer nota, String comentario, TrabalhoBean trabalho, UsuarioBean avaliador, UsuarioBean avaliado) {
        this.id = id;
        this.nota = nota;
        this.comentario = comentario;
        this.trabalho = trabalho;
        this.avaliador = avaliador;
        this.avaliado = avaliado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public TrabalhoBean getTrabalho() {
        return trabalho;
    }

    public void setTrabalho(TrabalhoBean trabalho) {
        this.trabalho = trabalho;
    }

    public UsuarioBean getAvaliador() {
        return avaliador;
    }

    public void setAvaliador(UsuarioBean avaliador) {
        this.avaliador = avaliador;
    }

    public UsuarioBean getAvaliado() {
        return avaliado;
    }

    public void setAvaliado(UsuarioBean avaliado) {
        this.avaliado = avaliado;
    }
}
