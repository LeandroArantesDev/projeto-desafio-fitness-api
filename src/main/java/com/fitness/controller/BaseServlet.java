package com.fitness.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

/**
 * Base dos controllers.
 * So helpers de API: ler JSON/parametro, extrair id e responder JSON.
 * Regra de negocio fica no Service.
 */
public abstract class BaseServlet extends HttpServlet {

    // 1. Instância compartilhada do Gson com suporte a LocalDateTime
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class,
                    (JsonSerializer<LocalDateTime>) (data, tipo,
                            contexto) -> data != null ? new JsonPrimitive(data.toString()) : null)
            // le "2026-09-01" (Postman) ou "2026-09-01T00:00" (front) do JSON
            .registerTypeAdapter(LocalDateTime.class,
                    (JsonDeserializer<LocalDateTime>) (json, tipo, contexto) -> {
                        String valor = json.getAsString();
                        return valor.length() == 10
                                ? LocalDate.parse(valor).atStartOfDay()
                                : LocalDateTime.parse(valor);
                    })
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

    // 6. Id do usuario logado, colocado no request pelo AuthFilter a partir do
    // token
    protected Long usuarioLogadoId(HttpServletRequest req) {
        return (Long) req.getAttribute("UsuarioLogadoId");
    }

    // 7. Converte parametro da URL (?nome=valor) para Long; vazio ou invalido vira
    // null
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
}
