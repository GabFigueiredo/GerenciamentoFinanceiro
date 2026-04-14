package com.umc.dao;

import com.umc.config.DatabaseConfig;
import com.umc.enums.FormaPagamento;
import com.umc.enums.Moeda;
import com.umc.model.Conta;
import com.umc.model.Despesa.Despesa;
import com.umc.model.Dinheiro;
import com.umc.model.Transacao.Saque;
import com.umc.repositories.SaqueRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SaqueDAO implements SaqueRepository {

    private final DatabaseConfig databaseConfig;
    private final ContaDAO contaDAO;
    private final DespesaDAO despesaDAO;

    public SaqueDAO(DatabaseConfig databaseConfig, ContaDAO contaDAO, DespesaDAO despesaDAO) {
        this.databaseConfig = databaseConfig;
        this.contaDAO = contaDAO;
        this.despesaDAO = despesaDAO;
    }

    @Override
    public void save(Saque saque) throws SQLException {
        String id = saque.getId() != null ? saque.getId() : UUID.randomUUID().toString();
        saque.setId(id);
        saque.setSaqueId(id);

        String transacaoSql = """
                INSERT INTO transacao (id, conta_id, valor, moeda, data, descricao, data_criacao)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        String saqueSql = """
                INSERT INTO saque (id, destino, forma_pagamento, despesa_id)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement transacaoStmt = conn.prepareStatement(transacaoSql);
             PreparedStatement saqueStmt = conn.prepareStatement(saqueSql)) {

            transacaoStmt.setString(1, id);
            transacaoStmt.setString(2, saque.getConta().getId().toString());
            transacaoStmt.setDouble(3, saque.getValor().getValor());
            transacaoStmt.setString(4, saque.getValor().getMoeda().name());
            transacaoStmt.setDate(5, Date.valueOf(saque.getData()));
            transacaoStmt.setString(6, saque.getDescricao());
            transacaoStmt.setDate(7, Date.valueOf(saque.getDataCriacao()));
            transacaoStmt.executeUpdate();

            saqueStmt.setString(1, id);
            saqueStmt.setString(2, saque.getDestino());
            saqueStmt.setString(3, saque.getFormaPagamento().name());
            saqueStmt.setString(4, saque.getDespesa() != null ? saque.getDespesa().getId() : null);
            saqueStmt.executeUpdate();
        }
    }

    @Override
    public Optional<Saque> findById(String id) throws SQLException {
        String sql = """
                SELECT t.*, s.destino, s.forma_pagamento, s.despesa_id
                FROM transacao t
                INNER JOIN saque s ON s.id = t.id
                WHERE t.id = ?
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapSaque(rs));
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Saque> findAll() throws SQLException {
        String sql = """
                SELECT t.*, s.destino, s.forma_pagamento, s.despesa_id
                FROM transacao t
                INNER JOIN saque s ON s.id = t.id
                """;
        List<Saque> saques = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                saques.add(mapSaque(rs));
            }
        }

        return saques;
    }

    @Override
    public List<Saque> findByContaId(String contaId) throws SQLException {
        String sql = """
                SELECT t.*, s.destino, s.forma_pagamento, s.despesa_id
                FROM transacao t
                INNER JOIN saque s ON s.id = t.id
                WHERE t.conta_id = ?
                """;
        List<Saque> saques = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                saques.add(mapSaque(rs));
            }
        }

        return saques;
    }

    @Override
    public void update(Saque saque) throws SQLException {
        String transacaoSql = """
                UPDATE transacao
                SET conta_id = ?, valor = ?, moeda = ?, data = ?, descricao = ?, data_criacao = ?
                WHERE id = ?
                """;

        String saqueSql = """
                UPDATE saque
                SET destino = ?, forma_pagamento = ?, despesa_id = ?
                WHERE id = ?
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement transacaoStmt = conn.prepareStatement(transacaoSql);
             PreparedStatement saqueStmt = conn.prepareStatement(saqueSql)) {

            transacaoStmt.setString(1, saque.getConta().getId().toString());
            transacaoStmt.setDouble(2, saque.getValor().getValor());
            transacaoStmt.setString(3, saque.getValor().getMoeda().name());
            transacaoStmt.setDate(4, Date.valueOf(saque.getData()));
            transacaoStmt.setString(5, saque.getDescricao());
            transacaoStmt.setDate(6, Date.valueOf(saque.getDataCriacao()));
            transacaoStmt.setString(7, saque.getId());
            transacaoStmt.executeUpdate();

            saqueStmt.setString(1, saque.getDestino());
            saqueStmt.setString(2, saque.getFormaPagamento().name());
            saqueStmt.setString(3, saque.getDespesa() != null ? saque.getDespesa().getId() : null);
            saqueStmt.setString(4, saque.getId());
            saqueStmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String deleteSaqueSql = "DELETE FROM saque WHERE id = ?";
        String deleteTransacaoSql = "DELETE FROM transacao WHERE id = ?";

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement saqueStmt = conn.prepareStatement(deleteSaqueSql);
             PreparedStatement transacaoStmt = conn.prepareStatement(deleteTransacaoSql)) {

            saqueStmt.setString(1, id);
            saqueStmt.executeUpdate();

            transacaoStmt.setString(1, id);
            transacaoStmt.executeUpdate();
        }
    }

    private Saque mapSaque(ResultSet rs) throws SQLException {
        Conta conta = contaDAO.findById(rs.getString("conta_id"))
                .orElseThrow(() -> new SQLException("Conta nao encontrada para este saque"));

        Dinheiro valor = new Dinheiro(
                Moeda.valueOf(rs.getString("moeda")),
                rs.getDouble("valor")
        );

        String despesaId = rs.getString("despesa_id");
        Despesa despesa = despesaId != null ? despesaDAO.findById(despesaId).orElse(null) : null;

        return new Saque(
                rs.getString("id"),
                conta,
                valor,
                rs.getDate("data").toLocalDate(),
                rs.getString("descricao"),
                rs.getDate("data_criacao").toLocalDate(),
                rs.getString("id"),
                rs.getString("destino"),
                FormaPagamento.valueOf(rs.getString("forma_pagamento")),
                despesa
        );
    }
}
