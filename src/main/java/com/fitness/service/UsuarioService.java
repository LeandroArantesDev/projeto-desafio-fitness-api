package com.fitness.service;

import com.fitness.dao.UsuarioDAO;
import com.fitness.model.Usuario;

import java.util.List;

/**
 * SERVICE de Usuario — regras de negocio ficam aqui.
 *
 * Controller so chama estes metodos e decide a view.
 * DAO so executa SQL.
 */
public class UsuarioService {

    private static final int SENHA_MINIMA = 6;

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Regra de autenticacao:
     * - login e senha obrigatorios
     * - so libera acesso se existir usuario com esse login/senha
     */
    public Usuario logar(String email, String senha) {
        email = this.normalizar(email);
        senha = this.normalizar(senha);

        if (email == null || senha == null) {
            throw new IllegalArgumentException("Informe e-mail e senha.");
        }

        Usuario usuario = this.usuarioDAO.buscarPorLoginESenha(email, senha);
        if (usuario == null) {
            throw new IllegalArgumentException("E-mail ou senha invalidos.");
        }
        return usuario;
    }

    public Usuario registrar(String nome, String email, String senha) {
        nome = this.normalizar(nome);
        email = this.normalizar(email);
        senha = this.normalizar(senha);

        if (nome == null || email == null || senha == null) {
            throw new IllegalArgumentException("Informe nome, e-mail e senha.");
        }

        if (!this.usuarioDAO.verificarUsuarioUnico(email)) {
            throw new IllegalArgumentException("Já existe um usuário registrado com esse e-mail.");
        }

        if (senha.length() < 8) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 8 caracteres.");
        }

        if (!this.usuarioDAO.registrar(nome, email, senha)) {
            throw new IllegalArgumentException("Erro ao registrar usuário.");
        }

        Usuario usuario = this.usuarioDAO.buscarPorLoginESenha(email, senha);
        if (usuario == null) {
            throw new IllegalArgumentException("Erro ao buscar usuário.");
        }
        return usuario;
    }

    public List<Usuario> listar() {
        return this.usuarioDAO.listarTodos();
    }

    public Usuario buscarPorId(Long id) {
        if (id == null) {
            return null;
        }
        return this.usuarioDAO.buscarPorId(id);
    }

    /**
     * Regra de salvamento:
     * - sem id  -> cadastro novo
     * - com id  -> alteracao (usuario precisa existir)
     */
    public void salvar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario e obrigatorio.");
        }

        this.prepararDados(usuario);
        this.validarCamposObrigatorios(usuario);
        if (usuario.getId() == null || usuario.getSenha() != null) {
            this.validarSenha(usuario.getSenha());
        }
        this.validarLoginUnico(usuario);

        if (usuario.getId() == null) {
            this.usuarioDAO.inserir(usuario);
            return;
        }

        if (this.usuarioDAO.buscarPorId(usuario.getId()) == null) {
            throw new IllegalArgumentException("Usuario nao encontrado para alteracao.");
        }
        this.usuarioDAO.alterar(usuario);
    }

    /**
     * Regra de exclusao:
     * - id obrigatorio
     * - usuario precisa existir
     */
    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id e obrigatorio para excluir.");
        }
        if (this.usuarioDAO.buscarPorId(id) == null) {
            throw new IllegalArgumentException("Usuario nao encontrado.");
        }
        try {
            this.usuarioDAO.deletar(id);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
                throw new IllegalArgumentException("Nao e possivel excluir: usuario possui desafios vinculados.");
            }
            throw e;
        }
    }

    private void prepararDados(Usuario usuario) {
        usuario.setNome(this.normalizar(usuario.getNome()));
        usuario.setEmail(this.normalizar(usuario.getEmail()));
        usuario.setSenha(this.normalizar(usuario.getSenha()));
    }

    private void validarCamposObrigatorios(Usuario usuario) {
        if (usuario.getNome() == null) {
            throw new IllegalArgumentException("Nome e obrigatorio.");
        }
        if (usuario.getEmail() == null) {
            throw new IllegalArgumentException("E-mail e obrigatorio.");
        }
        if (usuario.getId() == null && usuario.getSenha() == null) {
            throw new IllegalArgumentException("Senha e obrigatoria.");
        }
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < SENHA_MINIMA) {
            throw new IllegalArgumentException("Senha deve ter no minimo " + SENHA_MINIMA + " caracteres.");
        }
    }

    private void validarLoginUnico(Usuario usuario) {
        Usuario existente = this.usuarioDAO.buscarPorLogin(usuario.getEmail());
        if (existente == null) {
            return;
        }
        // no cadastro, qualquer login repetido e invalido
        if (usuario.getId() == null) {
            throw new IllegalArgumentException("Ja existe um usuario com este login.");
        }
        // na alteracao, so permite se o login for do proprio usuario
        if (!existente.getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("Ja existe um usuario com este login.");
        }
    }

    private String normalizar(String valor) {
        if (valor == null) {
            return null;
        }
        String limpo = valor.trim();
        return limpo.isEmpty() ? null : limpo;
    }
}
