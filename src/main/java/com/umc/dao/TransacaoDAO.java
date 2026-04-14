package com.umc.dao;

import com.umc.config.DatabaseConfig;
import com.umc.enums.Moeda;
import com.umc.model.Conta;
import com.umc.model.Dinheiro;
import com.umc.model.Transacao.Transacao;
import com.umc.repositories.TransacaoRepository;
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
public class TransacaoDAO implements TransacaoRepository {

    protected final DatabaseConfig databaseConfig;
    protected final ContaDAO contaDAO;

    public TransacaoDAO(DatabaseConfig databaseConfig, ContaDAO contaDAO) {
        this.databaseConfig = databaseConfig;
        this.contaDAO = contaDAO;
    }

    @Override
    public void save(Transacao transacao) throws SQLException {
        String id = transacao.getId() != null ? transacao.getId() : UUID.randomUUID().toString();
        transacao.setId(id);

        String sql = """
                INSERT INTO transacao (id, conta_id, valor, moeda, data, descricao, data_criacao)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.setString(2, transacao.getConta().getId().toString());
            stmt.setDouble(3, transacao.getValor().getValor());
            stmt.setString(4, transacao.getValor().getMoeda().name());
            stmt.setDate(5, Date.valueOf(transacao.getData()));
            stmt.setString(6, transacao.getDescricao());
            stmt.setDate(7, Date.valueOf(transacao.getDataCriacao()));

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Transacao> findById(String id) throws SQLException {
        String sql = "SELECT * FROM transacao WHERE id = ?";

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Transacao> findAll() throws SQLException {
        String sql = "SELECT * FROM transacao";
        List<Transacao> transacoes = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                transacoes.add(mapRow(rs));
            }
        }

        return transacoes;
    }

    @Override
    public List<Transacao> findByContaId(String contaId) throws SQLException {
        String sql = "SELECT * FROM transacao WHERE conta_id = ?";
        List<Transacao> transacoes = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transacoes.add(mapRow(rs));
            }
        }

        return transacoes;
    }

    @Override
    public void update(Transacao transacao) throws SQLException {
        String sql = """
                UPDATE transacao
                SET conta_id = ?, valor = ?, moeda = ?, data = ?, descricao = ?, data_criacao = ?
                WHERE id = ?
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transacao.getConta().getId().toString());
            stmt.setDouble(2, transacao.getValor().getValor());
            stmt.setString(3, transacao.getValor().getMoeda().name());
            stmt.setDate(4, Date.valueOf(transacao.getData()));
            stmt.setString(5, transacao.getDescricao());
            stmt.setDate(6, Date.valueOf(transacao.getDataCriacao()));
            stmt.setString(7, transacao.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM transacao WHERE id = ?";

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    protected Transacao mapRow(ResultSet rs) throws SQLException {
        Conta conta = contaDAO.findById(rs.getString("conta_id"))
                .orElseThrow(() -> new SQLException("Conta não encontrada para esta transação"));

        Dinheiro valor = new Dinheiro(
                Moeda.valueOf(rs.getString("moeda")),
                rs.getDouble("valor")
        );

        return new Transacao(
                rs.getString("id"),
                conta,
                valor,
                rs.getDate("data").toLocalDate(),
                rs.getString("descricao"),
                rs.getDate("data_criacao").toLocalDate()
        );
    }
}
