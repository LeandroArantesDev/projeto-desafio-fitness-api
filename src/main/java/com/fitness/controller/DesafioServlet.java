package com.fitness.controller;

import com.fitness.model.Desafio;
import com.fitness.model.Usuario;
import com.fitness.service.DesafioService;
import com.fitness.service.ParticipacaoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller de Desafio.
 * Ponte entre rota, service e view — sem regra de negocio.
 */
@WebServlet("/desafios")
public class DesafioServlet extends BaseServlet {

    private static final String LISTA = "/WEB-INF/jsp/desafios/lista.jsp";
    private static final String FORM = "/WEB-INF/jsp/desafios/form.jsp";
    private final DesafioService desafioService = new DesafioService();
    private final ParticipacaoService participacaoService = new ParticipacaoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        switch (this.acao(req)) {
            case "participar" -> this.participar(req, resp);
            case "novo" -> this.form(req, resp, null);
            case "editar" -> this.form(req, resp, this.desafioService.buscarPorId(this.paramLong(req, "id")));
            case "excluir" -> {
                try {
                    this.desafioService.deletar(this.paramLong(req, "id"));
                } catch (IllegalArgumentException e) {
                    req.setAttribute("erro", e.getMessage());
                    this.listar(req, resp);
                    return;
                }
                req.setAttribute("sucesso", "Desafio excluido com sucesso.");
                this.listar(req, resp);
            }
            default -> this.listar(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        Desafio desafio = this.fromRequest(req);

        try {
            this.desafioService.salvar(desafio);
            req.setAttribute("sucesso", "Desafio salvo com sucesso.");
            this.listar(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
            this.form(req, resp, desafio);
        }
    }

    private void form(HttpServletRequest req, HttpServletResponse resp, Desafio desafio)
            throws ServletException, IOException {

        if ("editar".equals(this.acao(req)) && desafio == null) {
            this.redirect(req, resp, "/desafios");
            return;
        }

        req.setAttribute("desafio", desafio);
        this.forward(req, resp, FORM);
    }

    private Desafio fromRequest(HttpServletRequest req) {
        Desafio desafio = new Desafio();
        desafio.setId(this.paramLong(req, "id"));
        desafio.setCriadorId(this.usuarioLogado(req).getId());
        desafio.setNome(this.param(req, "nome"));
        desafio.setDescricao(this.param(req, "descricao"));
        desafio.setCategoria(this.param(req, "categoria"));
        desafio.setTipoMeta(this.param(req, "tipoMeta"));
        desafio.setMetaTotal(this.paramDouble(req, "metaTotal"));
        desafio.setDataInicio(this.paramData(req, "dataInicio"));
        desafio.setDataFim(this.paramData(req, "dataFim"));
        desafio.setStatus(this.param(req, "status"));
        return desafio;
    }

    private void participar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuarioLogado = this.usuarioLogado(req);
        try {
            this.participacaoService.participar(usuarioLogado.getId(), this.paramLong(req, "id"));
            this.redirect(req, resp, "/home");
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
            this.listar(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuarioLogado = this.usuarioLogado(req);
        req.setAttribute("desafiosParticipando", this.desafioService.listarParticipando(usuarioLogado.getId()));
        req.setAttribute("desafiosCriados", this.desafioService.listarCriadosPor(usuarioLogado.getId()));
        this.forward(req, resp, LISTA);
    }

    // Helper local
    private Usuario usuarioLogado(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (Usuario) session.getAttribute("usuarioLogado");
    }

}
