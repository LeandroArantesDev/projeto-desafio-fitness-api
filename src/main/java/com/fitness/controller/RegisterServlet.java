package com.fitness.controller;

import com.fitness.model.Usuario;
import com.fitness.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller de Registro.
 * Ponte entre rota, service e resposta JSON — sem regra de negocio.
 */
@WebServlet("/api/register/*")
public class RegisterServlet extends BaseServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    // ==========================================
    // POST: Criar um novo usuário por fora do site
    // ==========================================

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Usuario usuario = this.lerJson(req, Usuario.class);

            if (usuario == null) {
                this.responderErro(resp, 400, "Dados do usuário não fornecidos.");
                return;
            }

            usuario.setId(null);
            usuario.setTipo("user");
            this.usuarioService.salvar(usuario);

            usuario.setSenha(null);
            this.responderJson(resp, 201, usuario);

        } catch (IllegalArgumentException e) {
            this.responderErro(resp, 400, e.getMessage());
        } catch (Exception e) {
            this.responderErro(resp, 500, "Erro interno ao cadastrar usuário.");
        }
    }
}
