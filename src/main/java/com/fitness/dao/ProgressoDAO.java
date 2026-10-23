package com.fitness.dao;

import com.fitness.model.Progresso;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO = Data Access Object (acesso ao banco)
 *
 * Somente SQL e conversao ResultSet -> {@link Progresso}.
 * Quem decide "quando" chamar cada metodo e o Service / Controller.
 */
public class ProgressoDAO extends MysqlDAO {

    public ProgressoDAO() {
        super();
    }

    private Progresso mapearProgresso(ResultSet rs) throws SQLException {
        Progresso progresso = new Progresso();
        progresso.setId(rs.getLong("id"));
        progresso.setParticipacaoId(rs.getLong("participacao_id"));
        progresso.setValorRegistrado(rs.getDouble("valor_registrado"));
        progresso.setObservacao(rs.getString("observacao"));
        progresso.setDataRegistro(rs.getObject("data_registro", LocalDateTime.class));
        progresso.setCriadoEm(rs.getObject("criado_em", LocalDateTime.class));
        progresso.setAtualizado_em(rs.getObject("atualizado_em", LocalDateTime.class));

        return progresso;
    }

    public void inserir(Progresso progresso) {
        String sql = "INSERT INTO progresso (participacao_id, valor_registrado, observacao, data_registro) VALUES (?, ?, ?, ?)";
        try {
            super.executarUpdate(
                    sql,
                    progresso.getParticipacaoId(),
                    progresso.getValorRegistrado(),
                    progresso.getObservacao(),
                    progresso.getDataRegistro());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir progresso.", e);
        }
    }

    public List<Progresso> listarPorParticipacao(Long participacaoId) {
        String sql = "SELECT id, participacao_id, valor_registrado, observacao, data_registro, criado_em, atualizado_em "
                + "FROM progresso "
                + "WHERE participacao_id = ?";
        List<Progresso> lista = new ArrayList<>();
        try (ResultSet rs = super.executar(sql, participacaoId)) {
            while (rs.next()) {
                lista.add(this.mapearProgresso(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar progresso por participação.", e);
        }
        return lista;
    }

}
