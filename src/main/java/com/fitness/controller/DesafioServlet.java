package com.fitness.controller;

import com.fitness.model.Desafio;
import com.fitness.service.DesafioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller de Desafio.
 * Ponte entre rota, service e resposta JSON — sem regra de negocio.
 */
@WebServlet("/api/desafios/*")
public class DesafioServlet extends BaseServlet {

    private final DesafioService desafioService = new DesafioService();

    // ==========================================
    // GET: Listar todos ou buscar por ID
    // ==========================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            Long id = this.extrairId(req);

            if (id == null) {
                this.responderJson(resp, 200, this.desafioService.listar());
                return;
            }

            Desafio desafio = this.desafioService.buscarPorId(id);

            if (desafio == null) {
                this.responderErro(resp, 404, "Desafio não encontrado");
                return;
            }

            this.responderJson(resp, 200, desafio);
        } catch (NumberFormatException e) {
            this.responderErro(resp, 400, "O ID fornecido na URL é inválido");
        }
    }

    // ==========================================
    // POST: Criar um novo desafio
    // ==========================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Desafio desafio = this.lerJson(req, Desafio.class);

            if (desafio == null) {
                this.responderErro(resp, 400, "Dados do desafio não fornecidos.");
                return;
            }

            desafio.setId(null);
            desafio.setCriadorId(this.usuarioLogadoId(req));
            this.desafioService.salvar(desafio);

            this.responderJson(resp, 201, desafio);

        } catch (IllegalArgumentException e) {
            this.responderErro(resp, 400, e.getMessage());
        } catch (Exception e) {
            this.responderErro(resp, 500, "Erro interno ao cadastrar desafio.");
        }
    }

    // ==========================================
    // PUT: Atualizar desafio existente
    // ==========================================

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Long id = this.extrairId(req);
            if (id == null) {
                this.responderErro(resp, 400, "Informe o ID do desafio na URL.");
                return;
            }

            Desafio desafio = this.lerJson(req, Desafio.class);

            if (desafio == null) {
                this.responderErro(resp, 400, "Dados do desafio não fornecidos.");
                return;
            }

            desafio.setId(id);
            this.desafioService.salvar(desafio);

            this.responderJson(resp, 200, desafio);

        } catch (NumberFormatException e) {
            this.responderErro(resp, 400, "O ID fornecido na URL é inválido.");
        } catch (IllegalArgumentException e) {
            int status = e.getMessage().contains("nao encontrado") ? 404 : 400;
            this.responderErro(resp, status, e.getMessage());
        } catch (Exception e) {
            this.responderErro(resp, 500, "Erro interno ao atualizar desafio.");
        }
    }

    // ==========================================
    // DELETE: Excluir desafio
    // ==========================================

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Long id = this.extrairId(req);

            if (id == null) {
                this.responderErro(resp, 400, "Informe o ID do desafio na URL");
                return;
            }

            this.desafioService.deletar(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (NumberFormatException e) {
            this.responderErro(resp, 400, "O ID fornecido na URL é inválido.");
        } catch (IllegalArgumentException e) {
            int status = e.getMessage().contains("nao encontrado") ? 404 : 400;
            this.responderErro(resp, status, e.getMessage());
        } catch (Exception e) {
            this.responderErro(resp, 500, "Erro interno ao excluir desafio.");
        }
    }
}
