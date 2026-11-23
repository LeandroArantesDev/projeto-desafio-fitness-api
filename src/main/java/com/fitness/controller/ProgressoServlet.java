package com.fitness.controller;

import com.fitness.model.Participacao;
import com.fitness.model.Progresso;
import com.fitness.service.ParticipacaoService;
import com.fitness.service.ProgressoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Controller de Progresso.
 * Ponte entre rota, service e resposta JSON — sem regra de negocio.
 */
@WebServlet("/api/progressos/*")
public class ProgressoServlet extends BaseServlet {

    private final ProgressoService progressoService = new ProgressoService();
    private final ParticipacaoService participacaoService = new ParticipacaoService();

    // ==========================================
    // GET: Historico e total do usuario logado em um desafio (?desafioId=1)
    // ==========================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long desafioId = this.paramLong(req, "desafioId");

        if (desafioId == null) {
            this.responderErro(resp, 400, "Informe o desafioId na URL. Ex: /api/progressos?desafioId=1");
            return;
        }

        Participacao participacao = this.participacaoService.buscarPorUsuarioEDesafio(
                this.usuarioLogadoId(req), desafioId);

        if (participacao == null) {
            this.responderErro(resp, 404, "Você não participa deste desafio.");
            return;
        }

        this.responderJson(resp, 200, Map.of(
                "participacao", participacao,
                "historico", this.progressoService.listarPorParticipacao(participacao.getId()),
                "total_registrado", this.progressoService.totalRegistrado(participacao.getId())));
    }

    // ==========================================
    // POST: Registrar um novo progresso
    // ==========================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Progresso progresso = this.lerJson(req, Progresso.class);

            if (progresso == null) {
                this.responderErro(resp, 400, "Dados do progresso não fornecidos.");
                return;
            }

            progresso.setId(null);
            this.progressoService.registrar(progresso);

            this.responderJson(resp, 201, progresso);

        } catch (IllegalArgumentException e) {
            this.responderErro(resp, 400, e.getMessage());
        } catch (Exception e) {
            this.responderErro(resp, 500, "Erro interno ao registrar progresso.");
        }
    }
}
