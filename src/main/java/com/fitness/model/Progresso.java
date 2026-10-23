package com.fitness.model;

import java.time.LocalDateTime;

public class Progresso {

    private Long id;
    private Long participacao_id;
    private Double valor_registrado;
    private String observacao;
    private LocalDateTime data_registro;
    private LocalDateTime criado_em;
    private LocalDateTime atualizado_em;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParticipacaoId() {
        return participacao_id;
    }

    public void setParticipacaoId(Long participacao_id) {
        this.participacao_id = participacao_id;
    }

    public Double getValorRegistrado() {
        return valor_registrado;
    }

    public void setValorRegistrado(Double valor_registrado) {
        this.valor_registrado = valor_registrado;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDateTime getDataRegistro() {
        return data_registro;
    }

    public void setDataRegistro(LocalDateTime data_registro) {
        this.data_registro = data_registro;
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
}
