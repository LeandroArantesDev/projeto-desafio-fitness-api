package com.fitness.controller;

import com.fitness.config.JwtUtil;
import com.fitness.model.Usuario;
import com.fitness.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

import org.eclipse.tags.shaded.org.apache.bcel.generic.RETURN;

/**
 * Controller de Login.
 * Ponte entre rota (/login), {@link UsuarioService} e a view.
 * Sem regra de negocio: so chama o service e encaminha o resultado.
 */
@WebServlet("/api/login")
public class LoginServlet extends BaseServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Map<?, ?> dados = this.lerJson(req, Map.class);
            if (dados == null) {
                this.responderErro(resp, 400, "Informe e-mail e senha.");
            }

            String email = (String) dados.get("email");
            String senha = (String) dados.get("senha");

            Usuario usuario = this.usuarioService.logar(email, senha);

            String token = JwtUtil.gerarToken(usuario);

            this.responderJson(resp, 200, Map.of(
                "token", token,
                "nome", usuario.getNome(),
                "tipo", usuario.getTipo()
            ));

        } catch (IllegalArgumentException e) {
            this.responderErro(resp, 401, e.getMessage());
        } catch (Exception e) {
            this.responderJson(resp, 500, "Erro interno ao realizar login.");
        } 

        return;
    }
}
