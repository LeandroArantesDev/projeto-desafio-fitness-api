package com.fitness.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Base dos controllers.
 * So helpers de rota/view: ler parametro, encaminhar JSP e redirecionar.
 * Regra de negocio fica no Service.
 */
public abstract class BaseServlet extends HttpServlet {

    // le a acao da URL (?acao=...); sem valor, assume "listar"
    protected String acao(HttpServletRequest req) {
        String acao = req.getParameter("acao");
        if (acao == null || acao.isBlank()) {
            return "listar";
        }
        return acao;
    }

    // le parametro como texto puro, sem conversao
    protected String param(HttpServletRequest req, String nome) {
        return req.getParameter(nome);
    }

    // converte parametro para Long; vazio ou invalido vira null
    protected Long paramLong(HttpServletRequest req, String nome) {
        String valor = req.getParameter(nome);
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return Long.valueOf(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // converte parametro para Double; vazio ou invalido vira null
    protected Double paramDouble(HttpServletRequest req, String nome) {
        String valor = req.getParameter(nome);
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return Double.valueOf(valor);
        } catch (NumberFormatException e) {https://github.com/LeandroArantesDev/projeto-desafio-fitness
            return null;
        }
    }

    // converte parametro data (yyyy-MM-dd) para LocalDateTime; vazio ou invalido vira null
    protected LocalDateTime paramData(HttpServletRequest req, String nome) {
        String valor = req.getParameter(nome);
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(valor).atStartOfDay();
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // encaminha para um JSP mantendo os dados setados no request
    protected void forward(HttpServletRequest req, HttpServletResponse resp, String jsp)
            throws ServletException, IOException {
        req.getRequestDispatcher(jsp).forward(req, resp);
    }

    // redireciona para outra rota (nova requisicao, dados do request se perdem)
    protected void redirect(HttpServletRequest req, HttpServletResponse resp, String caminho)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + caminho);
    }
}
