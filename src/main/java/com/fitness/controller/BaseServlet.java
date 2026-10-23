package com.fitness.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

/**
 * Base dos controllers.
 * So helpers de rota/view: ler parametro, encaminhar JSP e redirecionar.
 * Regra de negocio fica no Service.
 */
public abstract class BaseServlet extends HttpServlet {

    // 1. Instância compartilhada do Gson com suporte a LocalDateTime
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, 
                (JsonSerializer<LocalDateTime>) (data, tipo, contexto) -> 
                    data != null ? new JsonPrimitive(data.toString()) : null)
            .create();

    // 2. Método para responder qualquer objeto em formato JSON
    protected void responderJson(HttpServletResponse resp, int status, Object dados) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);

        if (dados != null) {
            resp.getWriter().write(this.gson.toJson(dados));
        }
    }

    // 3. Método para responder mensagens de erro padronizadas: {"erro": "mensagem"}
    protected void responderErro(HttpServletResponse resp, int status, String mensagem) throws IOException {
        this.responderJson(resp, status, Map.of("erro", mensagem));
    }

    // 4. Método para ler o corpo (Body) da requisição e converter em objeto Java
    protected <T> T lerJson(HttpServletRequest req, Class<T> classe) throws IOException {
        return this.gson.fromJson(req.getReader(), classe);
    }

    // 5. Método para extrair o ID numérico da URL (/10 -> 10L)
    protected Long extrairId(HttpServletRequest req) {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            return null; // Não foi passado ID (é listagem geral)
        }
        // Remove a barra inicial "/" e tenta converter em número
        return Long.parseLong(path.substring(1));
    }

    //
    //
    // A PARTIR DAQUI É O CONTROLLER ANTIGO - RETIRAR DEPOIS AS FUNÇÕES
    //
    //

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
        } catch (NumberFormatException e) {
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
