package com.fitness.controller;

import com.fitness.service.DesafioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller da Home.
 * Ponte entre rota, service e resposta JSON — sem regra de negocio.
 */
@WebServlet("/api/home")
public class HomeServlet extends BaseServlet {

    private final DesafioService desafioService = new DesafioService();

    // ==========================================
    // GET: Desafios com participacao e progresso do usuario logado
    // ==========================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        this.responderJson(resp, 200, this.desafioService.listarComProgresso(this.usuarioLogadoId(req)));
    }
}
