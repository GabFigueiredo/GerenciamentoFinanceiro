package com.umc.dao;

import com.umc.config.DatabaseConfig;
import com.umc.enums.Moeda;
import com.umc.model.*;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ContaDAO {

    private final DatabaseConfig databaseConfig;
    private final UsuarioDAO usuarioDAO;

    public ContaDAO(DatabaseConfig databaseConfig, UsuarioDAO usuarioDAO) {
        this.databaseConfig = databaseConfig;
        this.usuarioDAO = usuarioDAO;
    }

    public void save(Conta conta) throws SQLException {
        String sql = """
                INSERT INTO conta (id, usuario_id, moeda, saldo_atual, despesa_mensal,
                                   limite_gasto_mensal, descricao, data_criacao, data_atualizacao)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, conta.getUsuario().getId().toString());
            stmt.setString(3, conta.getMoeda().name());
            stmt.setDouble(4, conta.getSaldoAtual().getValor());
            stmt.setDouble(5, conta.getDespesaMensal().getValor());
            stmt.setDouble(6, conta.getLimiteGastoMensal().getValor());
            stmt.setString(7, conta.getDescricao());
            stmt.setDate(8, Date.valueOf(conta.getDataCriacao()));
            stmt.setDate(9, Date.valueOf(conta.getDataAtualizacao()));

            stmt.executeUpdate();
        }
    }

    public Optional<Conta> findById(String id) throws SQLException {
        String sql = "SELECT * FROM conta WHERE id = ?";

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

    public List<Conta> findAll() throws SQLException {
        String sql = "SELECT * FROM conta";
        List<Conta> contas = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                contas.add(mapRow(rs));
            }
        }
        return contas;
    }

    public List<Conta> findByUsuarioId(String usuarioId) throws SQLException {
        String sql = "SELECT * FROM conta WHERE usuario_id = ?";
        List<Conta> contas = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                contas.add(mapRow(rs));
            }
        }
        return contas;
    }

    public void update(Conta conta) throws SQLException {
        String sql = """
                UPDATE conta
                SET moeda = ?, saldo_atual = ?, despesa_mensal = ?,
                    limite_gasto_mensal = ?, descricao = ?, data_atualizacao = ?
                WHERE id = ?
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, conta.getMoeda().name());
            stmt.setDouble(2, conta.getSaldoAtual().getValor());
            stmt.setDouble(3, conta.getDespesaMensal().getValor());
            stmt.setDouble(4, conta.getLimiteGastoMensal().getValor());
            stmt.setString(5, conta.getDescricao());
            stmt.setDate(6, Date.valueOf(conta.getDataAtualizacao()));
            stmt.setString(7, conta.getId().toString());

            stmt.executeUpdate();
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM conta WHERE id = ?";

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    // Maps a ResultSet row to a Conta object
    private Conta mapRow(ResultSet rs) throws SQLException {
        Moeda moeda = Moeda.valueOf(rs.getString("moeda"));

        Usuario usuario = usuarioDAO.findById(rs.getString("usuario_id"))
                .orElseThrow(() -> new SQLException("Usuário não encontrado para esta conta"));

        Dinheiro saldoAtual = new Dinheiro(moeda, rs.getDouble("saldo_atual"));
        Dinheiro despesaMensal = new Dinheiro(moeda, rs.getDouble("despesa_mensal"));
        Dinheiro limiteGastoMensal = new Dinheiro(moeda, rs.getDouble("limite_gasto_mensal"));

        return new Conta(
                UUID.fromString(rs.getString("id")),
                usuario,
                saldoAtual,
                moeda,
                despesaMensal,
                null, // Metas loaded separately to avoid heavy joins
                limiteGastoMensal,
                rs.getString("descricao"),
                rs.getDate("data_criacao").toLocalDate(),
                rs.getDate("data_atualizacao").toLocalDate()
        );
    }
}