/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontfreela.main.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TrabalhoBean {

    private Long id;
    private String titulo;
    private String descricao;
    private String cidade;
    private String estado;
    private LocalDateTime dataServico;
    private BigDecimal valor;
    private String status = "ABERTO";
    private UsuarioBean contratante;

    public TrabalhoBean() {
    }

    public TrabalhoBean(Long id, String titulo, String descricao, String cidade, String estado, LocalDateTime dataServico, BigDecimal valor, String status, UsuarioBean contratante) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.cidade = cidade;
        this.estado = estado;
        this.dataServico = dataServico;
        this.valor = valor;
        this.status = status;
        this.contratante = contratante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public LocalDateTime getDataServico() {
        return dataServico;
    }

    public void setDataServico(LocalDateTime dataServico) {
        this.dataServico = dataServico;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UsuarioBean getContratante() {
        return contratante;
    }

    public void setContratante(UsuarioBean contratante) {
        this.contratante = contratante;
    }
}
