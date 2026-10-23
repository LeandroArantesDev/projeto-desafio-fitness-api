package com.fitness.model;

import java.time.LocalDateTime;

public class Desafio {

    private Long id;
    private Long criador_id;
    private String nome;
    private String descricao;
    private String categoria;
    private String tipo_meta;
    private Double meta_total;
    private String unidade_medida;
    private LocalDateTime data_inicio;
    private LocalDateTime data_fim;
    private String status;
    private LocalDateTime criado_em;
    private LocalDateTime atualizado_em;
    private boolean participando;
    private Double progressoAtual;
    private Double progressoPercentual;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCriadorId() {
        return criador_id;
    }

    public void setCriadorId(Long criador_id) {
        this.criador_id = criador_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipoMeta() {
        return tipo_meta;
    }

    public void setTipoMeta(String tipo_meta) {
        this.tipo_meta = tipo_meta;
    }

    public Double getMetaTotal() {
        return meta_total;
    }

    public void setMetaTotal(Double meta_total) {
        this.meta_total = meta_total;
    }

    public String getUnidadeMedida() {
        return unidade_medida;
    }

    public void setUnidadeMedida(String unidade_medida) {
        this.unidade_medida = unidade_medida;
    }

    public LocalDateTime getDataInicio() {
        return data_inicio;
    }

    public void setDataInicio(LocalDateTime data_inicio) {
        this.data_inicio = data_inicio;
    }

    public LocalDateTime getDataFim() {
        return data_fim;
    }

    public void setDataFim(LocalDateTime data_fim) {
        this.data_fim = data_fim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCriadoEm() {
        return criado_em;
    }

    public void setCriadoEm(LocalDateTime criado_em) {
        this.criado_em = criado_em;
    }

    public LocalDateTime getAtualizado_em() {
        return atualizado_em;
    }

    public void setAtualizado_em(LocalDateTime atualizado_em) {
        this.atualizado_em = atualizado_em;
    }

    public boolean isParticipando() {
        return participando;
    }

    public void setParticipando(boolean participando) {
        this.participando = participando;
    }

    public Double getProgressoAtual() {
        return progressoAtual;
    }

    public void setProgressoAtual(Double progressoAtual) {
        this.progressoAtual = progressoAtual;
    }

    public Double getProgressoPercentual() {
        return progressoPercentual;
    }

    public void setProgressoPercentual(Double progressoPercentual) {
        this.progressoPercentual = progressoPercentual;
    }
}
