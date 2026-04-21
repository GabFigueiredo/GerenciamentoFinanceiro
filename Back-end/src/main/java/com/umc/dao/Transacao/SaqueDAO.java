package com.umc.dao.Transacao;

import com.umc.config.DatabaseConfig;
import com.umc.dao.ContaDAO;
import com.umc.model.enums.CategoriaDespesa;
import com.umc.model.enums.FormaPagamento;
import com.umc.model.transacao.Saque.Despesa;
import com.umc.model.transacao.Saque.Saque;
import com.umc.model.transacao.Saque.SaqueRepository;
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
public class SaqueDAO extends TransacaoDAO implements SaqueRepository {

    public SaqueDAO(DatabaseConfig databaseConfig, ContaDAO contaDAO) {
        super(databaseConfig, contaDAO);
    }

    @Override
    public void save(Saque saque) throws SQLException {
        if (saque.getId() == null) saque.setId(UUID.randomUUID().toString());

        String sql = """
                INSERT INTO saque (id, conta_id, valor, moeda, data, descricao, data_criacao,
                                   destino, categoria_despesa, forma_pagamento, observacao)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, saque.getId());
            stmt.setString(2, saque.getConta().getId().toString());
            stmt.setDouble(3, saque.getValor().getValor());
            stmt.setString(4, saque.getValor().getMoeda().name());
            stmt.setDate(5, Date.valueOf(saque.getData()));
            stmt.setString(6, saque.getDescricao());
            stmt.setDate(7, Date.valueOf(saque.getDataCriacao()));
            stmt.setString(8, saque.getDestino());
            stmt.setString(9, saque.getDespesa().getCategoriaDespesa().name());
            stmt.setString(10, saque.getDespesa().getFormaPagamento().name());
            stmt.setString(11, saque.getDespesa().getObservacao());

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Saque> findById(String id) throws SQLException {
        String sql = "SELECT * FROM saque WHERE id = ?";

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return Optional.of(mapRow(rs));
        }

        return Optional.empty();
    }

    @Override
    public List<Saque> findAll() throws SQLException {
        String sql = "SELECT * FROM saque";
        List<Saque> saques = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) saques.add(mapRow(rs));
        }

        return saques;
    }

    @Override
    public List<Saque> findByContaId(String contaId) throws SQLException {
        String sql = "SELECT * FROM saque WHERE conta_id = ?";
        List<Saque> saques = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) saques.add(mapRow(rs));
        }

        return saques;
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM saque WHERE id = ?";

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private Saque mapRow(ResultSet rs) throws SQLException {
        Despesa despesa = new Despesa(
                null,
                rs.getString("descricao"),
                rs.getDouble("valor"),
                rs.getDate("data").toLocalDate(),
                CategoriaDespesa.valueOf(rs.getString("categoria_despesa")),
                FormaPagamento.valueOf(rs.getString("forma_pagamento")),
                rs.getString("observacao")
        );

        return new Saque(
                rs.getString("id"),
                mapConta(rs),
                mapValor(rs),
                rs.getDate("data").toLocalDate(),
                rs.getString("descricao"),
                rs.getDate("data_criacao").toLocalDate(),
                rs.getString("destino"),
                despesa
        );
    }
}