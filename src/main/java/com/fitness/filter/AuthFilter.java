package com.fitness.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fitness.config.JwtUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebFilter("/api/*")
public class AuthFilter implements Filter {

    private static final List<String> ROTAS_PUBLICAS = List.of(
            "/api/login",
            "/api/register"
    );

    private static final List<String> ROTAS_PRIVADAS = List.of(
            "/api/usuarios"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI();

        if (this.contemRota(path, ROTAS_PUBLICAS)) {
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

            if (this.contemRota(path, ROTAS_PRIVADAS)) {
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

    private boolean contemRota(String path, List<String> rotas) {
            for (String rota : rotas) {
                if (path.contains(rota)) {
                    return true;
            }
            }
        return false;
    }
}
