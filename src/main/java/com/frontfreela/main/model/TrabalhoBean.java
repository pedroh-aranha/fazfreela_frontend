package com.frontfreela.main.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TrabalhoBean {

    private Long id;
    private String titulo;
    private String descricao;
    private String endereco;
    private String cidade;
    private String estado;
    
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataServico;
    
    private BigDecimal valor;
    private Integer horasJornada;
    private String habilidadesExigidas;
    private String status = "ABERTO";
    private UsuarioBean contratante;
    private Integer vagas = 1;
    private String modalidade = "PRESENCIAL";

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

    public Integer getHorasJornada() {
        return horasJornada;
    }

    public void setHorasJornada(Integer horasJornada) {
        this.horasJornada = horasJornada;
    }

    public UsuarioBean getContratante() {
        return contratante;
    }

    public void setContratante(UsuarioBean contratante) {
        this.contratante = contratante;
    }

    public String getHabilidadesExigidas() {
        return habilidadesExigidas;
    }

    public void setHabilidadesExigidas(String habilidadesExigidas) {
        this.habilidadesExigidas = habilidadesExigidas;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Integer getVagas() {
        return vagas;
    }

    public void setVagas(Integer vagas) {
        this.vagas = vagas;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public BigDecimal getValorTotal() {
        if (valor != null && vagas != null) {
            return valor.multiply(BigDecimal.valueOf(vagas));
        }
        return valor;
    }
}
