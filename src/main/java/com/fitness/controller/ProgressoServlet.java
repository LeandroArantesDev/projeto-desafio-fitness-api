package com.fitness.controller;

import com.fitness.model.Desafio;
import com.fitness.model.Participacao;
import com.fitness.model.Progresso;
import com.fitness.model.Usuario;
import com.fitness.service.DesafioService;
import com.fitness.service.ParticipacaoService;
import com.fitness.service.ProgressoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/progresso")
public class ProgressoServlet extends BaseServlet {

    private static final String FORM = "/WEB-INF/jsp/progresso/form.jsp";
    private static final String LISTA_DESAFIOS = "/WEB-INF/jsp/desafios/lista.jsp";

    private final ProgressoService progressoService = new ProgressoService();
    private final ParticipacaoService participacaoService = new ParticipacaoService();
    private final DesafioService desafioService = new DesafioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuarioLogado = this.usuarioLogado(req);
        Long desafioId = this.paramLong(req, "desafioId");

        Participacao participacao = this.participacaoService.buscarPorUsuarioEDesafio(
                usuarioLogado.getId(), desafioId);

        if (participacao == null) {
            this.redirect(req, resp, "/desafios");
            return;
        }

        Desafio desafio = this.desafioService.buscarPorId(desafioId);

        req.setAttribute("desafio", desafio);
        req.setAttribute("historico", this.progressoService.listarPorParticipacao(participacao.getId()));
        req.setAttribute("totalRegistrado", this.progressoService.totalRegistrado(participacao.getId()));

        req.setAttribute("participacao", participacao);
        this.forward(req, resp, FORM);
    }

    private Usuario usuarioLogado(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (Usuario) session.getAttribute("usuarioLogado");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        Progresso progresso = this.fromRequest(req);

        try {
            this.progressoService.registrar(progresso);
            req.setAttribute("sucesso", "Progresso registrado com sucesso.");
            Usuario usuarioLogado = this.usuarioLogado(req);
            req.setAttribute("desafiosParticipando", this.desafioService.listarParticipando(usuarioLogado.getId()));
            req.setAttribute("desafiosCriados", this.desafioService.listarCriadosPor(usuarioLogado.getId()));
            this.forward(req, resp, LISTA_DESAFIOS);
        } catch (IllegalArgumentException e) {
            Participacao participacao = this.participacaoService.buscarPorId(progresso.getParticipacaoId());

            req.setAttribute("erro", e.getMessage());
            req.setAttribute("participacao", participacao);
            req.setAttribute("desafio", this.desafioService.buscarPorId(participacao.getDesafioId()));
            req.setAttribute("historico", this.progressoService.listarPorParticipacao(participacao.getId()));
            req.setAttribute("totalRegistrado", this.progressoService.totalRegistrado(participacao.getId()));
            this.forward(req, resp, FORM);
        }
    }

    private Progresso fromRequest(HttpServletRequest req) {
        Progresso progresso = new Progresso();
        progresso.setParticipacaoId(this.paramLong(req, "participacaoId"));
        progresso.setValorRegistrado(this.paramDouble(req, "valorRegistrado"));
        progresso.setObservacao(this.param(req, "observacao"));
        progresso.setDataRegistro(this.paramData(req, "dataRegistro"));
        return progresso;
    }

}
