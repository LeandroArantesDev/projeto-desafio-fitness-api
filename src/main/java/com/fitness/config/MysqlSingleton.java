package com.fitness.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlSingleton {

    // Config
    private static final String URL = "jdbc:mysql://mysql:3306/projeto_desafios_fitness?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "pdf_user";
    private static final String PASSWORD = "pdf123";

    // Estado do Singleton
    private static MysqlSingleton instance;
    private Connection conexao;

    // Construtor
    private MysqlSingleton() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL nao encontrado no projeto.", e);
        }
    }

    // Ponto de acesso
    public static synchronized MysqlSingleton getInstance() {
        if (instance == null) {
            instance = new MysqlSingleton();
        }
        return instance;
    }

    // Abrir/reaproveitar conexão
    private Connection obterConexao() throws SQLException {
        if (this.conexao == null || this.conexao.isClosed()) {
            this.conexao = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return this.conexao;
    }

    // Executar SELECT
    public ResultSet executar(String sql, Object... parametros) throws SQLException {
        Connection conn = this.obterConexao();
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < parametros.length; i++) {
            ps.setObject(i + 1, parametros[i]);
        }
        return ps.executeQuery();
    }

    // Executar INSERT/UPDATE/DELETE
    public int executarUpdate(String sql, Object... parametros) throws SQLException {
        Connection conn = this.obterConexao();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) {
                ps.setObject(i + 1, parametros[i]);
            }
            return ps.executeUpdate();
        }
    }
}
