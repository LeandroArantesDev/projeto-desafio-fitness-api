package com.fitness.service;

import com.fitness.dao.DesafioDAO;
import com.fitness.dao.ParticipacaoDAO;
import com.fitness.model.Participacao;

/**
 * SERVICE de Participacao — regras de negocio ficam aqui.
 *
 * Controller so chama estes metodos e decide a view.
 * DAO so executa SQL.
 */
public class ParticipacaoService {

    private final ParticipacaoDAO participacaoDAO;
    private final DesafioDAO desafioDAO;

    public ParticipacaoService() {
        this.participacaoDAO = new ParticipacaoDAO();
        this.desafioDAO = new DesafioDAO();
    }

    /**
     * Regra de participacao:
     * - desafio e usuario obrigatorios
     * - desafio precisa existir
     * - usuario nao pode participar do mesmo desafio duas vezes
     */
    public void participar(Long usuarioId, Long desafioId) {
        if (usuarioId == null || desafioId == null) {
            throw new IllegalArgumentException("Usuario e desafio sao obrigatorios.");
        }

        if (this.desafioDAO.buscarPorId(desafioId) == null) {
            throw new IllegalArgumentException("Desafio nao encontrado.");
        }

        if (this.participacaoDAO.existeParticipacao(usuarioId, desafioId)) {
            throw new IllegalArgumentException("Voce ja participa deste desafio.");
        }

        Participacao participacao = new Participacao();
        participacao.setUsuarioId(usuarioId);
        participacao.setDesafioId(desafioId);
        this.participacaoDAO.inserir(participacao);
    }

    public Participacao buscarPorUsuarioEDesafio(Long usuarioId, Long desafioId) {
        return this.participacaoDAO.buscarPorUsuarioEDesafio(usuarioId, desafioId);
    }

    public Participacao buscarPorId(Long id) {
        return this.participacaoDAO.buscarPorId(id);
    }

}
