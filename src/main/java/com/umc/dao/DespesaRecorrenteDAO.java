package com.umc.dao;

import com.umc.config.DatabaseConfig;
import com.umc.enums.CategoriaDespesa;
import com.umc.enums.FormaPagamento;
import com.umc.enums.Frequencia;
import com.umc.enums.StatusDespesa;
import com.umc.model.Despesa.DespesaRecorrente;
import com.umc.repositories.DespesaRecorrenteRepository;
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
public class DespesaRecorrenteDAO implements DespesaRecorrenteRepository {

    private final DatabaseConfig databaseConfig;

    public DespesaRecorrenteDAO(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public void save(DespesaRecorrente despesaRecorrente) throws SQLException {
        String sql = """
                INSERT INTO despesa_recorrente (
                    id, descricao, valor, data, categoria_despesa, status, forma_pagamento,
                    observacao, frequencia, data_inicio, data_fim, ativa, dia_cobranca
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, despesaRecorrente.getDescricao());
            stmt.setDouble(3, despesaRecorrente.getValor());
            stmt.setDate(4, Date.valueOf(despesaRecorrente.getData()));
            stmt.setString(5, despesaRecorrente.getCategoriaDespesa().name());
            stmt.setString(6, despesaRecorrente.getStatus().name());
            stmt.setString(7, despesaRecorrente.getFormaPagamento().name());
            stmt.setString(8, despesaRecorrente.getObservacao());
            stmt.setString(9, despesaRecorrente.getFrequencia().name());
            stmt.setDate(10, Date.valueOf(despesaRecorrente.getDataInicio()));
            stmt.setDate(11, Date.valueOf(despesaRecorrente.getDataFim()));
            stmt.setBoolean(12, despesaRecorrente.isAtiva());
            stmt.setInt(13, despesaRecorrente.getDiaDeCobranca());

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<DespesaRecorrente> findById(String id) throws SQLException {
        String sql = "SELECT * FROM despesa_recorrente WHERE id = ?";

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
    public List<DespesaRecorrente> findAll() throws SQLException {
        String sql = "SELECT * FROM despesa_recorrente";
        List<DespesaRecorrente> despesas = new ArrayList<>();

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
    public List<DespesaRecorrente> findAtivas() throws SQLException {
        String sql = "SELECT * FROM despesa_recorrente WHERE ativa = true";
        List<DespesaRecorrente> despesas = new ArrayList<>();

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
    public void update(DespesaRecorrente despesaRecorrente) throws SQLException {
        String sql = """
                UPDATE despesa_recorrente
                SET descricao = ?, valor = ?, data = ?, categoria_despesa = ?, status = ?, forma_pagamento = ?,
                    observacao = ?, frequencia = ?, data_inicio = ?, data_fim = ?, ativa = ?, dia_cobranca = ?
                WHERE id = ?
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, despesaRecorrente.getDescricao());
            stmt.setDouble(2, despesaRecorrente.getValor());
            stmt.setDate(3, Date.valueOf(despesaRecorrente.getData()));
            stmt.setString(4, despesaRecorrente.getCategoriaDespesa().name());
            stmt.setString(5, despesaRecorrente.getStatus().name());
            stmt.setString(6, despesaRecorrente.getFormaPagamento().name());
            stmt.setString(7, despesaRecorrente.getObservacao());
            stmt.setString(8, despesaRecorrente.getFrequencia().name());
            stmt.setDate(9, Date.valueOf(despesaRecorrente.getDataInicio()));
            stmt.setDate(10, Date.valueOf(despesaRecorrente.getDataFim()));
            stmt.setBoolean(11, despesaRecorrente.isAtiva());
            stmt.setInt(12, despesaRecorrente.getDiaDeCobranca());
            stmt.setString(13, despesaRecorrente.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM despesa_recorrente WHERE id = ?";

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private DespesaRecorrente mapRow(ResultSet rs) throws SQLException {
        return new DespesaRecorrente(
                rs.getString("id"),
                rs.getString("descricao"),
                rs.getDouble("valor"),
                rs.getDate("data").toLocalDate(),
                CategoriaDespesa.valueOf(rs.getString("categoria_despesa")),
                StatusDespesa.valueOf(rs.getString("status")),
                FormaPagamento.valueOf(rs.getString("forma_pagamento")),
                rs.getString("observacao"),
                Frequencia.valueOf(rs.getString("frequencia")),
                rs.getDate("data_inicio").toLocalDate(),
                rs.getDate("data_fim").toLocalDate(),
                rs.getBoolean("ativa"),
                rs.getInt("dia_cobranca")
        );
    }
}
