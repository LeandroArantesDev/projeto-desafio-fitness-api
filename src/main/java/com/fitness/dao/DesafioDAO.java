package com.fitness.dao;

import com.fitness.model.Desafio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO = Data Access Object (acesso ao banco)
 *
 * Somente SQL e conversao ResultSet -> {@link Desafio}.
 * Quem decide "quando" chamar cada metodo e o Service / Controller.
 */
public class DesafioDAO extends MysqlDAO {

    public DesafioDAO() {
        super();
    }

    private Desafio mapearDesafio(ResultSet rs) throws SQLException {
        Desafio desafio = new Desafio();
        desafio.setId(rs.getLong("id"));
        desafio.setCriadorId(rs.getLong("criador_id"));
        desafio.setNome(rs.getString("nome"));
        desafio.setDescricao(rs.getString("descricao"));
        desafio.setCategoria(rs.getString("categoria"));
        desafio.setTipoMeta(rs.getString("tipo_meta"));
        desafio.setMetaTotal(rs.getDouble("meta_total"));
        desafio.setUnidadeMedida(rs.getString("unidade_medida"));
        desafio.setDataInicio(rs.getObject("data_inicio", LocalDateTime.class));
        desafio.setDataFim(rs.getObject("data_fim", LocalDateTime.class));
        desafio.setStatus(rs.getString("status"));
        desafio.setCriadoEm(rs.getObject("criado_em", LocalDateTime.class));
        desafio.setAtualizado_em(rs.getObject("atualizado_em", LocalDateTime.class));

        return desafio;
    }

    public List<Desafio> listarTodosDesafios() {
        String sql = "SELECT id, criador_id, nome, descricao, categoria, tipo_meta, meta_total, unidade_medida, data_inicio, data_fim, status, criado_em, atualizado_em "
                + "FROM desafios";
        List<Desafio> lista = new ArrayList<>();
        try (ResultSet rs = super.executar(sql)) {
            while (rs.next()) {
                lista.add(this.mapearDesafio(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar desafios.", e);
        }
        return lista;
    }

    public Desafio buscarPorId(Long id) {
        String sql = "SELECT id, criador_id, nome, descricao, categoria, tipo_meta, meta_total, unidade_medida, data_inicio, data_fim, status, criado_em, atualizado_em "
                + "FROM desafios "
                + "WHERE id = ?";
        try (ResultSet rs = super.executar(sql, id)) {
            if (rs.next()) {
                return this.mapearDesafio(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar desafio por id.", e);
        }
        return null;
    }

    public List<Desafio> listarPorCriador(Long criadorId) {
        String sql = "SELECT id, criador_id, nome, descricao, categoria, tipo_meta, meta_total, unidade_medida, data_inicio, data_fim, status, criado_em, atualizado_em "
                + "FROM desafios "
                + "WHERE criador_id = ?";
        List<Desafio> lista = new ArrayList<>();
        try (ResultSet rs = super.executar(sql, criadorId)) {
            while (rs.next()) {
                lista.add(this.mapearDesafio(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar desafios por criador.", e);
        }
        return lista;
    }

    public List<Desafio> listarPorParticipante(Long usuarioId) {
        String sql = "SELECT d.id, d.criador_id, d.nome, d.descricao, d.categoria, d.tipo_meta, d.meta_total, "
                + "d.unidade_medida, d.data_inicio, d.data_fim, d.status, d.criado_em, d.atualizado_em "
                + "FROM desafios d "
                + "INNER JOIN participacoes_desafio p ON p.desafio_id = d.id "
                + "WHERE p.usuario_id = ?";
        List<Desafio> lista = new ArrayList<>();
        try (ResultSet rs = super.executar(sql, usuarioId)) {
            while (rs.next()) {
                lista.add(this.mapearDesafio(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar desafios por participante.", e);
        }
        return lista;
    }

    public void inserir(Desafio desafio) {
        String sql = "INSERT INTO desafios (criador_id, nome, descricao, categoria, tipo_meta, meta_total, data_inicio, data_fim, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            super.executarUpdate(
                    sql,
                    desafio.getCriadorId(),
                    desafio.getNome(),
                    desafio.getDescricao(),
                    desafio.getCategoria(),
                    desafio.getTipoMeta(),
                    desafio.getMetaTotal(),
                    desafio.getDataInicio(),
                    desafio.getDataFim(),
                    desafio.getStatus());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir.", e);
        }
    }

    public void alterar(Desafio desafio) {
        String sql = "UPDATE desafios SET nome = ?, descricao = ?, categoria = ?, tipo_meta = ?, meta_total = ?, data_inicio = ?, data_fim = ?, status = ? WHERE id = ?";
        try {
            super.executarUpdate(
                    sql,
                    desafio.getNome(),
                    desafio.getDescricao(),
                    desafio.getCategoria(),
                    desafio.getTipoMeta(),
                    desafio.getMetaTotal(),
                    desafio.getDataInicio(),
                    desafio.getDataFim(),
                    desafio.getStatus(),

                    desafio.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar.", e);
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM desafios WHERE id = ?";
        try {
            super.executarUpdate(sql, id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar.", e);
        }
    }
}
