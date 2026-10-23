package com.fitness.dao;

import com.fitness.model.Usuario;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

/**
 * DAO = Data Access Object (acesso ao banco)
 *
 * Somente SQL e conversao ResultSet -> {@link Usuario}.
 * Quem decide "quando" chamar cada metodo e o Service / Controller.
 */
public class UsuarioDAO extends MysqlDAO {

    public UsuarioDAO() {
        super();
    }

    public Usuario buscarPorLoginESenha(String email, String senha) {
        String sql =
                "SELECT id, nome, email, tipo, senha_hash, criado_em, atualizado_em "
                        + "FROM usuarios "
                        + "WHERE email = ?";

        Usuario usuario = null;

        try (ResultSet rs = super.executar(sql, email)) {
            if (rs.next()) {
                usuario = this.mapearUsuario(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuario.", e);
        }

        if (usuario != null && usuario.getSenha() != null
            && BCrypt.checkpw(senha, usuario.getSenha())) {
            return usuario;
        }
        return null;
    }


    public boolean registrar(String nome, String email, String senha) {
        String sql = "INSERT INTO usuarios (nome, email, senha_hash, tipo) "
                        + "VALUES (?, ?, ?, ?)";
        String tipo = "user";
        String senha_hash = BCrypt.hashpw(senha, BCrypt.gensalt());

        try {
            int linhasAfetadas = super.executarUpdate(sql, nome, email, senha_hash, tipo);
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar usuário.", e);
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setTipo(rs.getString("tipo"));
        usuario.setSenha(rs.getString("senha_hash"));
        usuario.setCriadoEm(rs.getObject("criado_em", LocalDateTime.class));
        usuario.setAtualizadoEm(rs.getObject("atualizado_em", LocalDateTime.class));

        return usuario;
    }

    public List<Usuario> listarTodos() {
        String sql =
                "SELECT id, nome, email, tipo, senha_hash, criado_em, atualizado_em "
                        + "FROM usuarios "
                        + "ORDER BY nome";
        List<Usuario> lista = new ArrayList<>();
        try (ResultSet rs = super.executar(sql)) {
            while (rs.next()) {
                lista.add(this.mapearUsuario(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuarios.", e);
        }
        return lista;
    }

    public Usuario buscarPorLogin(String email) {
        String sql =
            "SELECT id, nome, email, senha_hash, tipo, criado_em, atualizado_em "
                        + "FROM usuarios "
                        + "WHERE email = ?";
        try (ResultSet rs = super.executar(sql, email)) {
            if (rs.next()) {
                return this.mapearUsuario(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar por login.", e);
        }
        return null;
    }

    public Usuario buscarPorId(Long id) {
        String sql =
            "SELECT id, nome, email, senha_hash, tipo, criado_em, atualizado_em "
                        + "FROM usuarios "
                        + "WHERE id = ?";
        try (ResultSet rs = super.executar(sql, id)) {
            if (rs.next()) {
                return this.mapearUsuario(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar por id.", e);
        }
        return null;
    }

    public boolean verificarUsuarioUnico(String email) {
        String sql = "SELECT COUNT(*) "
               + "FROM usuarios "
               + "WHERE email = ?";
               
        try (ResultSet rs = super.executar(sql, email)) {
            if (rs.next()) {
                int quantidade = rs.getInt(1);
                return quantidade == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar se usuário é único.", e);
        }
        return false;
    }


    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, email, senha_hash, tipo) VALUES (?, ?, ?, ?)";
        String senhaCriptografada = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
        try {
            super.executarUpdate(
                    sql,
                    usuario.getNome(),
                    usuario.getEmail(),
                    senhaCriptografada,
                    usuario.getTipo());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir.", e);
        }
    }

    public void alterar(Usuario usuario) {
        try {
            if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
                String sql = "UPDATE usuarios SET nome = ?, email = ?, tipo = ? WHERE id = ?";
                super.executarUpdate(sql, usuario.getNome(), usuario.getEmail(),
                        usuario.getTipo(), usuario.getId());
                return;
            }

            String sql = "UPDATE usuarios SET nome = ?, email = ?, tipo = ?, senha_hash = ? WHERE id = ?";
            String senhaCriptografada = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
            super.executarUpdate(sql, usuario.getNome(), usuario.getEmail(),
                    usuario.getTipo(), senhaCriptografada, usuario.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar.", e);
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try {
            super.executarUpdate(sql, id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar.", e);
        }
    }
}
