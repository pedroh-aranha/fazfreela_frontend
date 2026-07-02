/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontfreela.main.model;

/**
 *
 * @author pedro
 */
public class UsuarioBean {
    
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String cidade;
    private String estado;
    private Double notaMedia;
    private String habilidades;
    private java.math.BigDecimal pretensaoValorHora;

    public UsuarioBean() {
    }

    public UsuarioBean(Long id, String nome, String email, String senha, String cidade, String estado, Double notaMedia) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cidade = cidade;
        this.estado = estado;
        this.notaMedia = notaMedia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getNotaMedia() {
        return notaMedia;
    }

    public void setNotaMedia(Double notaMedia) {
        this.notaMedia = notaMedia;
    }

    public String getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(String habilidades) {
        this.habilidades = habilidades;
    }

    public java.math.BigDecimal getPretensaoValorHora() {
        return pretensaoValorHora;
    }

    public void setPretensaoValorHora(java.math.BigDecimal pretensaoValorHora) {
        this.pretensaoValorHora = pretensaoValorHora;
    }
}
