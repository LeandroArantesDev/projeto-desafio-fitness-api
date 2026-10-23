package com.fitness.service;

import com.fitness.dao.ParticipacaoDAO;
import com.fitness.dao.ProgressoDAO;
import com.fitness.model.Progresso;

import java.util.List;

public class ProgressoService {

    private final ProgressoDAO progressoDAO;
    private final ParticipacaoDAO participacaoDAO;

    public ProgressoService() {
        this.progressoDAO = new ProgressoDAO();
        this.participacaoDAO = new ParticipacaoDAO();
    }

    public List<Progresso> listarPorParticipacao(Long participacaoId) {
        return this.progressoDAO.listarPorParticipacao(participacaoId);
    }

    public void registrar(Progresso progresso) {
        if (progresso == null) {
            throw new IllegalArgumentException("Progresso e obrigatorio.");
        }

        this.validarCamposObrigatorios(progresso);

        if (this.participacaoDAO.buscarPorId(progresso.getParticipacaoId()) == null) {
            throw new IllegalArgumentException("Participacao nao encontrada.");
        }

        this.progressoDAO.inserir(progresso);
    }

    private void validarCamposObrigatorios(Progresso progresso) {
        if (progresso.getParticipacaoId() == null) {
            throw new IllegalArgumentException("Participacao e obrigatoria.");
        }
        if (progresso.getValorRegistrado() == null || progresso.getValorRegistrado() <= 0) {
            throw new IllegalArgumentException("Valor registrado deve ser maior que zero.");
        }
        if (progresso.getDataRegistro() == null) {
            throw new IllegalArgumentException("Data de registro e obrigatoria.");
        }
    }

    public Double totalRegistrado(Long participacaoId) {
        Double total = 0.0;
        for (Progresso progresso : this.progressoDAO.listarPorParticipacao(participacaoId)) {
            total += progresso.getValorRegistrado();
        }
        return total;
    }
}
