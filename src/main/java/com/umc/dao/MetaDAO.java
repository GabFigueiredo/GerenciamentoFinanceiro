package com.umc.dao;

import com.umc.config.DatabaseConfig;
import com.umc.enums.Moeda;
import com.umc.model.Dinheiro;
import com.umc.model.Meta;
import com.umc.repositories.MetaRepository;
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
public class MetaDAO implements MetaRepository {

    private final DatabaseConfig databaseConfig;

    public MetaDAO(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public void save(String contaId, Meta meta) throws SQLException {
        String sql = """
                INSERT INTO meta (id, conta_id, nome, valor_objetivo, moeda, cargo, data_inicio, data_conclusao)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, contaId);
            stmt.setString(3, meta.getNome());
            stmt.setDouble(4, meta.getValorObjetivo().getValor());
            stmt.setString(5, meta.getValorObjetivo().getMoeda().name());
            stmt.setString(6, meta.getCargo());
            stmt.setDate(7, Date.valueOf(meta.getDataInicio()));
            stmt.setDate(8, Date.valueOf(meta.getDataDeConclusao()));

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Meta> findById(String id) throws SQLException {
        String sql = "SELECT * FROM meta WHERE id = ?";

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
    public List<Meta> findAll() throws SQLException {
        String sql = "SELECT * FROM meta";
        List<Meta> metas = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                metas.add(mapRow(rs));
            }
        }

        return metas;
    }

    @Override
    public List<Meta> findByContaId(String contaId) throws SQLException {
        String sql = "SELECT * FROM meta WHERE conta_id = ?";
        List<Meta> metas = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                metas.add(mapRow(rs));
            }
        }

        return metas;
    }

    @Override
    public void update(Meta meta) throws SQLException {
        String sql = """
                UPDATE meta
                SET nome = ?, valor_objetivo = ?, moeda = ?, cargo = ?, data_inicio = ?, data_conclusao = ?
                WHERE id = ?
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, meta.getNome());
            stmt.setDouble(2, meta.getValorObjetivo().getValor());
            stmt.setString(3, meta.getValorObjetivo().getMoeda().name());
            stmt.setString(4, meta.getCargo());
            stmt.setDate(5, Date.valueOf(meta.getDataInicio()));
            stmt.setDate(6, Date.valueOf(meta.getDataDeConclusao()));
            stmt.setString(7, meta.getId().toString());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM meta WHERE id = ?";

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private Meta mapRow(ResultSet rs) throws SQLException {
        Moeda moeda = Moeda.valueOf(rs.getString("moeda"));
        Dinheiro valorObjetivo = new Dinheiro(moeda, rs.getDouble("valor_objetivo"));

        return new Meta(
                UUID.fromString(rs.getString("id")),
                rs.getString("nome"),
                valorObjetivo,
                rs.getString("cargo"),
                rs.getDate("data_inicio").toLocalDate(),
                rs.getDate("data_conclusao").toLocalDate()
        );
    }
}
