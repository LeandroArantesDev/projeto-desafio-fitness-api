package com.fitness.controller;

import com.fitness.dao.ParticipacaoDAO;
import com.fitness.model.Desafio;
import com.fitness.model.Participacao;
import com.fitness.model.Usuario;
import com.fitness.service.DesafioService;
import com.fitness.service.ProgressoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends BaseServlet {

    private static final String VIEW = "/WEB-INF/jsp/home.jsp";

    private final DesafioService desafioService = new DesafioService();
    private final ParticipacaoDAO participacaoDAO = new ParticipacaoDAO();
    private final ProgressoService progressoService = new ProgressoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario usuario = (Usuario) req.getSession(false).getAttribute("usuarioLogado");
        var desafios = this.desafioService.listar();

        for (Desafio desafio : desafios) {
            Participacao participacao = this.participacaoDAO.buscarPorUsuarioEDesafio(
                usuario.getId(), desafio.getId());
            desafio.setParticipando(participacao != null);

            double progresso = participacao == null
                ? 0.0
                : this.progressoService.totalRegistrado(participacao.getId());
            desafio.setProgressoAtual(progresso);

            double percentual = desafio.getMetaTotal() == null || desafio.getMetaTotal() <= 0
                ? 0.0
                : (progresso / desafio.getMetaTotal()) * 100.0;
            desafio.setProgressoPercentual(Math.min(100.0, percentual));
        }
        
        req.setAttribute("desafios", desafios);
        this.forward(req, resp, VIEW);
    }
}
