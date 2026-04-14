package com.umc.dao;

import com.umc.config.DatabaseConfig;
import com.umc.enums.Moeda;
import com.umc.enums.Receita;
import com.umc.model.Conta;
import com.umc.model.Dinheiro;
import com.umc.model.Transacao.Deposito;
import com.umc.repositories.DepositoRepository;
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
public class DepositoDAO implements DepositoRepository {

    private final DatabaseConfig databaseConfig;
    private final ContaDAO contaDAO;

    public DepositoDAO(DatabaseConfig databaseConfig, ContaDAO contaDAO) {
        this.databaseConfig = databaseConfig;
        this.contaDAO = contaDAO;
    }

    @Override
    public void save(Deposito deposito) throws SQLException {
        String id = deposito.getId() != null ? deposito.getId() : UUID.randomUUID().toString();
        deposito.setId(id);

        String transacaoSql = """
                INSERT INTO transacao (id, conta_id, valor, moeda, data, descricao, data_criacao)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        String depositoSql = """
                INSERT INTO deposito (id, origem, receita)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement transacaoStmt = conn.prepareStatement(transacaoSql);
             PreparedStatement depositoStmt = conn.prepareStatement(depositoSql)) {

            transacaoStmt.setString(1, id);
            transacaoStmt.setString(2, deposito.getConta().getId().toString());
            transacaoStmt.setDouble(3, deposito.getValor().getValor());
            transacaoStmt.setString(4, deposito.getValor().getMoeda().name());
            transacaoStmt.setDate(5, Date.valueOf(deposito.getData()));
            transacaoStmt.setString(6, deposito.getDescricao());
            transacaoStmt.setDate(7, Date.valueOf(deposito.getDataCriacao()));
            transacaoStmt.executeUpdate();

            depositoStmt.setString(1, id);
            depositoStmt.setString(2, deposito.getOrigem());
            depositoStmt.setString(3, deposito.getReceita().name());
            depositoStmt.executeUpdate();
        }
    }

    @Override
    public Optional<Deposito> findById(String id) throws SQLException {
        String sql = """
                SELECT t.*, d.origem, d.receita
                FROM transacao t
                INNER JOIN deposito d ON d.id = t.id
                WHERE t.id = ?
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapDeposito(rs));
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Deposito> findAll() throws SQLException {
        String sql = """
                SELECT t.*, d.origem, d.receita
                FROM transacao t
                INNER JOIN deposito d ON d.id = t.id
                """;
        List<Deposito> depositos = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                depositos.add(mapDeposito(rs));
            }
        }

        return depositos;
    }

    @Override
    public List<Deposito> findByContaId(String contaId) throws SQLException {
        String sql = """
                SELECT t.*, d.origem, d.receita
                FROM transacao t
                INNER JOIN deposito d ON d.id = t.id
                WHERE t.conta_id = ?
                """;
        List<Deposito> depositos = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                depositos.add(mapDeposito(rs));
            }
        }

        return depositos;
    }

    @Override
    public void update(Deposito deposito) throws SQLException {
        String transacaoSql = """
                UPDATE transacao
                SET conta_id = ?, valor = ?, moeda = ?, data = ?, descricao = ?, data_criacao = ?
                WHERE id = ?
                """;

        String depositoSql = """
                UPDATE deposito
                SET origem = ?, receita = ?
                WHERE id = ?
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement transacaoStmt = conn.prepareStatement(transacaoSql);
             PreparedStatement depositoStmt = conn.prepareStatement(depositoSql)) {

            transacaoStmt.setString(1, deposito.getConta().getId().toString());
            transacaoStmt.setDouble(2, deposito.getValor().getValor());
            transacaoStmt.setString(3, deposito.getValor().getMoeda().name());
            transacaoStmt.setDate(4, Date.valueOf(deposito.getData()));
            transacaoStmt.setString(5, deposito.getDescricao());
            transacaoStmt.setDate(6, Date.valueOf(deposito.getDataCriacao()));
            transacaoStmt.setString(7, deposito.getId());
            transacaoStmt.executeUpdate();

            depositoStmt.setString(1, deposito.getOrigem());
            depositoStmt.setString(2, deposito.getReceita().name());
            depositoStmt.setString(3, deposito.getId());
            depositoStmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String deleteDepositoSql = "DELETE FROM deposito WHERE id = ?";
        String deleteTransacaoSql = "DELETE FROM transacao WHERE id = ?";

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement depositoStmt = conn.prepareStatement(deleteDepositoSql);
             PreparedStatement transacaoStmt = conn.prepareStatement(deleteTransacaoSql)) {

            depositoStmt.setString(1, id);
            depositoStmt.executeUpdate();

            transacaoStmt.setString(1, id);
            transacaoStmt.executeUpdate();
        }
    }

    private Deposito mapDeposito(ResultSet rs) throws SQLException {
        Conta conta = contaDAO.findById(rs.getString("conta_id"))
                .orElseThrow(() -> new SQLException("Conta nao encontrada para este deposito"));

        Dinheiro valor = new Dinheiro(
                Moeda.valueOf(rs.getString("moeda")),
                rs.getDouble("valor")
        );

        return new Deposito(
                rs.getString("id"),
                conta,
                valor,
                rs.getDate("data").toLocalDate(),
                rs.getString("descricao"),
                rs.getDate("data_criacao").toLocalDate(),
                rs.getString("origem"),
                Receita.valueOf(rs.getString("receita"))
        );
    }
}
