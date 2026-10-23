package com.fitness.model;

import java.time.LocalDateTime;

public class Usuario {

    private Long id;
    private String nome;
    private String email;
    private String tipo;
    private String senha_hash;
    private String status;
    private LocalDateTime criado_em;
    private LocalDateTime atualizado_em;

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSenha() {
        return senha_hash;
    }

    public void setSenha(String senha_hash) {
        this.senha_hash = senha_hash;
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

    public LocalDateTime getAtualizadoEm() {
        return atualizado_em;
    }

    public void setAtualizadoEm(LocalDateTime atualizado_em) {
        this.atualizado_em = atualizado_em;
    }
}
