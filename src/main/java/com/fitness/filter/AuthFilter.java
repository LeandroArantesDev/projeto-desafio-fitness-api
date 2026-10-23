package com.fitness.filter;

import com.fitness.model.Usuario;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/home", "/usuarios", "/usuarios/*", "/perfis", "/perfis/*", "/desafios", "/desafios/*", "/progresso", "/progresso/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        Usuario usuarioLogado = null;
        if (session != null) {
            usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        }

        if (usuarioLogado == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Verificar a url que usuário está tentando acessar
        String requestURI = req.getRequestURI();

        // Se ele tentar acessar /usuarios ou /perfis, temos que validar se é ADMIN
        if (requestURI.contains("/usuarios")) {
            // Se for área de admin, mas o tipo dele for USER (ou diferente de ADMIN), bloqueia!
            if (!"admin".equalsIgnoreCase(usuarioLogado.getTipo())) {
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
