package com.umc.dao.Transacao;

import com.umc.config.DatabaseConfig;
import com.umc.dao.ContaDAO;
import com.umc.model.enums.Receita;
import com.umc.model.transacao.Deposito.Deposito;
import com.umc.model.transacao.Deposito.DepositoRepository;
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
public class DepositoDAO extends TransacaoDAO implements DepositoRepository {

    public DepositoDAO(DatabaseConfig databaseConfig, ContaDAO contaDAO) {
        super(databaseConfig, contaDAO);
    }

    @Override
    public void save(Deposito deposito) throws SQLException {
        if (deposito.getId() == null) deposito.setId(UUID.randomUUID().toString());

        String sql = """
                INSERT INTO deposito (id, conta_id, valor, moeda, data, descricao, data_criacao,
                                      origem, receita)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, deposito.getId());
            stmt.setString(2, deposito.getConta().getId().toString());
            stmt.setDouble(3, deposito.getValor().getValor());
            stmt.setString(4, deposito.getValor().getMoeda().name());
            stmt.setDate(5, Date.valueOf(deposito.getData()));
            stmt.setString(6, deposito.getDescricao());
            stmt.setDate(7, Date.valueOf(deposito.getDataCriacao()));
            stmt.setString(8, deposito.getOrigem());
            stmt.setString(9, deposito.getReceita().name());

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Deposito> findById(String id) throws SQLException {
        String sql = "SELECT * FROM deposito WHERE id = ?";

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return Optional.of(mapRow(rs));
        }

        return Optional.empty();
    }

    @Override
    public List<Deposito> findAll() throws SQLException {
        String sql = "SELECT * FROM deposito";
        List<Deposito> depositos = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) depositos.add(mapRow(rs));
        }

        return depositos;
    }

    @Override
    public List<Deposito> findByContaId(String contaId) throws SQLException {
        String sql = "SELECT * FROM deposito WHERE conta_id = ?";
        List<Deposito> depositos = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) depositos.add(mapRow(rs));
        }

        return depositos;
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM deposito WHERE id = ?";

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private Deposito mapRow(ResultSet rs) throws SQLException {
        return new Deposito(
                rs.getString("id"),
                mapConta(rs),
                mapValor(rs),
                rs.getDate("data").toLocalDate(),
                rs.getString("descricao"),
                rs.getDate("data_criacao").toLocalDate(),
                rs.getString("origem"),
                Receita.valueOf(rs.getString("receita"))
        );
    }
}