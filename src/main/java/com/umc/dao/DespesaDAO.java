package com.umc.dao;

import com.umc.config.DatabaseConfig;
import com.umc.enums.CategoriaDespesa;
import com.umc.enums.FormaPagamento;
import com.umc.enums.StatusDespesa;
import com.umc.model.Despesa.Despesa;
import com.umc.repositories.DespesaRepository;
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
public class DespesaDAO implements DespesaRepository {

    protected final DatabaseConfig databaseConfig;

    public DespesaDAO(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public void save(Despesa despesa) throws SQLException {
        String sql = """
                INSERT INTO despesa (id, descricao, valor, data, categoria_despesa, status, forma_pagamento, observacao)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, despesa.getDescricao());
            stmt.setDouble(3, despesa.getValor());
            stmt.setDate(4, Date.valueOf(despesa.getData()));
            stmt.setString(5, despesa.getCategoriaDespesa().name());
            stmt.setString(6, despesa.getStatus().name());
            stmt.setString(7, despesa.getFormaPagamento().name());
            stmt.setString(8, despesa.getObservacao());

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Despesa> findById(String id) throws SQLException {
        String sql = "SELECT * FROM despesa WHERE id = ?";

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
    public List<Despesa> findAll() throws SQLException {
        String sql = "SELECT * FROM despesa";
        List<Despesa> despesas = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                despesas.add(mapRow(rs));
            }
        }

        return despesas;
    }

    @Override
    public void update(Despesa despesa) throws SQLException {
        String sql = """
                UPDATE despesa
                SET descricao = ?, valor = ?, data = ?, categoria_despesa = ?, status = ?, forma_pagamento = ?, observacao = ?
                WHERE id = ?
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, despesa.getDescricao());
            stmt.setDouble(2, despesa.getValor());
            stmt.setDate(3, Date.valueOf(despesa.getData()));
            stmt.setString(4, despesa.getCategoriaDespesa().name());
            stmt.setString(5, despesa.getStatus().name());
            stmt.setString(6, despesa.getFormaPagamento().name());
            stmt.setString(7, despesa.getObservacao());
            stmt.setString(8, despesa.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM despesa WHERE id = ?";

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    protected Despesa mapRow(ResultSet rs) throws SQLException {
        return new Despesa(
                rs.getString("id"),
                rs.getString("descricao"),
                rs.getDouble("valor"),
                rs.getDate("data").toLocalDate(),
                CategoriaDespesa.valueOf(rs.getString("categoria_despesa")),
                StatusDespesa.valueOf(rs.getString("status")),
                FormaPagamento.valueOf(rs.getString("forma_pagamento")),
                rs.getString("observacao")
        );
    }
}
