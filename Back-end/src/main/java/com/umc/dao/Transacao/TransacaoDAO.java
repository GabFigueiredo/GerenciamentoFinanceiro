package com.umc.dao.Transacao;

import com.umc.config.DatabaseConfig;
import com.umc.dao.ContaDAO;
import com.umc.model.enums.Moeda;
import com.umc.model.conta.Conta;
import com.umc.model.Dinheiro;
import com.umc.model.transacao.Transacao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract class TransacaoDAO {

    protected final DatabaseConfig databaseConfig;
    protected final ContaDAO contaDAO;

    protected TransacaoDAO(DatabaseConfig databaseConfig, ContaDAO contaDAO) {
        this.databaseConfig = databaseConfig;
        this.contaDAO = contaDAO;
    }

    protected void salvarTransacaoBase(Transacao transacao, Connection conn) throws SQLException {
        String sql = """
                INSERT INTO transacao (id, conta_id, valor, moeda, data, descricao, data_criacao)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transacao.getId());
            stmt.setString(2, transacao.getConta().getId().toString());
            stmt.setDouble(3, transacao.getValor().getValor());
            stmt.setString(4, transacao.getValor().getMoeda().name());
            stmt.setDate(5, Date.valueOf(transacao.getData()));
            stmt.setString(6, transacao.getDescricao());
            stmt.setDate(7, Date.valueOf(transacao.getDataCriacao()));
            stmt.executeUpdate();
        }
    }

    protected void deletarTransacaoBase(String id, Connection conn) throws SQLException {
        String sql = "DELETE FROM transacao WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    protected Conta mapConta(ResultSet rs) throws SQLException {
        return contaDAO.findById(rs.getString("conta_id"))
                .orElseThrow(() -> new SQLException("Conta nao encontrada para esta transacao"));
    }

    protected Dinheiro mapValor(ResultSet rs) throws SQLException {
        return new Dinheiro(
                Moeda.valueOf(rs.getString("moeda")),
                rs.getDouble("valor")
        );
    }
}