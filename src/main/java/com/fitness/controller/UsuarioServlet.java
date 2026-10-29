package com.fitness.controller;

import com.fitness.model.Usuario;
import com.fitness.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controller de Usuario.
 * Ponte entre rota, service e view — sem regra de negocio.
 */
@WebServlet("/api/usuarios/*")
public class UsuarioServlet extends BaseServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    // ==========================================
    // GET: Listar todos ou buscar por ID
    // ==========================================

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            Long id = this.extrairId(req);
        
            if (id == null) {
                List<Usuario> lista = this.usuarioService.listar();
                for (Usuario u : lista) {
                    u.setSenha(null);
                }
            
            this.responderJson(resp, 200, lista);
            return;
            }

            Usuario usuario = this.usuarioService.buscarPorId(id);

            if (usuario == null) {
                this.responderErro(resp, 404, "Usuário não encontrado");
                return;
            }

            usuario.setSenha(null);
            this.responderJson(resp, 200, usuario);
        } catch (NumberFormatException e) {
            this.responderErro(resp, 400, "O ID fornecido na URL é inválido");
        }
    }

    // ==========================================
    // POST: Criar um novo usuário
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
            this.usuarioService.salvar(usuario);

            usuario.setSenha(null);
            this.responderJson(resp, 201, usuario);

        } catch (IllegalArgumentException e) {
            this.responderErro(resp, 400, e.getMessage());
        } catch (Exception e) {
            this.responderErro(resp, 500, "Erro interno ao cadastrar usuário.");
        }
    }

    // ==========================================
    // PUT: Atualizar usuário existente
    // ==========================================

    @Override 
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Long id = this.extrairId(req);
            if (id == null) {
                this.responderErro(resp, 400, "Informe o ID do usuário na URL.");
                return;
            }

            Usuario usuario = this.lerJson(req, Usuario.class);

            if(usuario == null) {
                this.responderErro(resp, 400, "Dados do usuário não fornecidos.");
                return;
            }

            usuario.setId(id);
            this.usuarioService.salvar(usuario);

            usuario.setSenha(null);
            this.responderJson(resp, 200, usuario);

        }  catch (NumberFormatException e) {
            this.responderErro(resp, 400, "O ID fornecido na URL é inválido.");
        } catch (IllegalArgumentException e) {
            int status = e.getMessage().contains("nao encontrado") ? 404 : 400;
            this.responderErro(resp, status, e.getMessage());
        } catch (Exception e) {
            this.responderErro(resp, 500, "Erro interno ao atualizar usuário.");
        }
    }


    // ==========================================
    // DELETE: Excluir usuário
    // ==========================================


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Long id = this.extrairId(req);  

            if (id == null) {
                this.responderErro(resp, 400, "Informe o ID do usuário na URL");
                return;
            }

            this.usuarioService.deletar(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (NumberFormatException e) {
            this.responderErro(resp, 400, "O ID fornecido na URL é inválido.");
        } catch (IllegalArgumentException e ) {
            int status = e.getMessage().contains("nao encontrado") ? 404 : 400;
            this.responderErro(resp, status, e.getMessage());
        } catch (Exception e) {
            this.responderErro(resp, 500, "Erro interno ao excluir usuário");
        }
    }
}
