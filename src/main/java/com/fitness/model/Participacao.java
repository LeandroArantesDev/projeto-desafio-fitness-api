package com.fitness.model;

import java.time.LocalDateTime;

public class Participacao {

    private Long id;
    private Long usuario_id;
    private Long desafio_id;
    private String status;
    private LocalDateTime concluido_em;
    private LocalDateTime criado_em;
    private LocalDateTime atualizado_em;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuario_id;
    }

    public void setUsuarioId(Long usuario_id) {
        this.usuario_id = usuario_id;
    }

    public Long getDesafioId() {
        return desafio_id;
    }

    public void setDesafioId(Long desafio_id) {
        this.desafio_id = desafio_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getConcluidoEm() {
        return concluido_em;
    }

    public void setConcluidoEm(LocalDateTime concluido_em) {
        this.concluido_em = concluido_em;
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
