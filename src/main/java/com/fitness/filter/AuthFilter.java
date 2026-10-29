package com.fitness.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fitness.config.JwtUtil;
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

@WebFilter("/api/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI();

        if (path.endsWith("/api/login")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"erro\": \"Token não fornecido ou formato inválido. Use: Bearer <token>\"}");
            return;
        }

        String token = authHeader.substring(7);

        try {
            DecodedJWT decoded = JwtUtil.validarToken(token);

            if (path.contains("/api/usuarios")) {
                String tipo = decoded.getClaim("tipo").asString();
                if (!"admin".equalsIgnoreCase(tipo)) {
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.getWriter().write("{\"erro\": \"Acesso negado: apenas administradores podem acessar este recurso.\"}");
                    return;
                }
            }

            req.setAttribute("UsuarioLogadoId", decoded.getClaim("userId").asLong());

            chain.doFilter(request, response);
        } catch (Exception e) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            resp.getWriter().write("{\"erro\": \"Token inválido ou expirado.\"}");
        }
    }
}
