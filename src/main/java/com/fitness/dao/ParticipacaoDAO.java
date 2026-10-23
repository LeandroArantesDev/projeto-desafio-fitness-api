package com.fitness.dao;

import com.fitness.model.Participacao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * DAO = Data Access Object (acesso ao banco)
 *
 * Somente SQL e conversao ResultSet -> {@link Participacao}.
 * Quem decide "quando" chamar cada metodo e o Service / Controller.
 */
public class ParticipacaoDAO extends MysqlDAO {

    public ParticipacaoDAO() {
        super();
    }

    private Participacao mapearParticipacao(ResultSet rs) throws SQLException {
        Participacao participacao = new Participacao();
        participacao.setId(rs.getLong("id"));
        participacao.setUsuarioId(rs.getLong("usuario_id"));
        participacao.setDesafioId(rs.getLong("desafio_id"));
        participacao.setStatus(rs.getString("status"));
        participacao.setConcluidoEm(rs.getObject("concluido_em", LocalDateTime.class));
        participacao.setCriadoEm(rs.getObject("criado_em", LocalDateTime.class));
        participacao.setAtualizado_em(rs.getObject("atualizado_em", LocalDateTime.class));

        return participacao;
    }

    public Participacao buscarPorId(Long id) {
        String sql = "SELECT id, usuario_id, desafio_id, status, concluido_em, criado_em, atualizado_em "
                + "FROM participacoes_desafio "
                + "WHERE id = ?";
        try (ResultSet rs = super.executar(sql, id)) {
            if (rs.next()) {
                return this.mapearParticipacao(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar participacao por id.", e);
        }
        return null;
    }

    public Participacao buscarPorUsuarioEDesafio(Long usuarioId, Long desafioId) {
        String sql = "SELECT id, usuario_id, desafio_id, status, concluido_em, criado_em, atualizado_em "
                + "FROM participacoes_desafio "
                + "WHERE usuario_id = ? AND desafio_id = ?";
        try (ResultSet rs = super.executar(sql, usuarioId, desafioId)) {
            if (rs.next()) {
                return this.mapearParticipacao(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar participacao.", e);
        }
        return null;
    }

    public boolean existeParticipacao(Long usuarioId, Long desafioId) {
        String sql = "SELECT id FROM participacoes_desafio WHERE usuario_id = ? AND desafio_id = ?";
        try (ResultSet rs = super.executar(sql, usuarioId, desafioId)) {
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar participacao.", e);
        }
    }

    public void inserir(Participacao participacao) {
        String sql = "INSERT INTO participacoes_desafio (usuario_id, desafio_id) VALUES (?, ?)";
        try {
            super.executarUpdate(
                    sql,
                    participacao.getUsuarioId(),
                    participacao.getDesafioId());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir participacao.", e);
        }
    }
}
