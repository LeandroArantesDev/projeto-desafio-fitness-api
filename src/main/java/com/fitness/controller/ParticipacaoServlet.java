package com.fitness.controller;

import com.fitness.model.Participacao;
import com.fitness.service.ParticipacaoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller de Participacao.
 * Ponte entre rota, service e resposta JSON — sem regra de negocio.
 */
@WebServlet("/api/participacoes/*")
public class ParticipacaoServlet extends BaseServlet {

    private final ParticipacaoService participacaoService = new ParticipacaoService();

    // ==========================================
    // POST: Usuario logado passa a participar de um desafio
    // ==========================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Participacao dados = this.lerJson(req, Participacao.class);

            if (dados == null) {
                this.responderErro(resp, 400, "Dados da participação não fornecidos.");
                return;
            }

            Long usuarioId = this.usuarioLogadoId(req);
            this.participacaoService.participar(usuarioId, dados.getDesafioId());

            // devolve a participacao criada (com id, usado no POST /api/progressos)
            this.responderJson(resp, 201,
                    this.participacaoService.buscarPorUsuarioEDesafio(usuarioId, dados.getDesafioId()));

        } catch (IllegalArgumentException e) {
            this.responderErro(resp, 400, e.getMessage());
        } catch (Exception e) {
            this.responderErro(resp, 500, "Erro interno ao participar do desafio.");
        }
    }
}
