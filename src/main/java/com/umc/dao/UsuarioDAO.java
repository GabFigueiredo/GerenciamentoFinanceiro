package com.umc.dao;

import com.umc.config.DatabaseConfig;
import com.umc.model.Contato;
import com.umc.model.Usuario;
import com.umc.repositories.UsuarioRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UsuarioDAO implements UsuarioRepository {

    private final DatabaseConfig databaseConfig;

    public UsuarioDAO(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public void save(Usuario usuario) throws SQLException {
        String sql = """
                INSERT INTO usuario (id, nome, cpf, cargo, salario,
                                     contato_celular, contato_telefone, contato_email)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getCpf());
            stmt.setString(4, usuario.getCargo());
            stmt.setDouble(5, usuario.getSalario());
            stmt.setString(6, usuario.getContato().getCelular());
            stmt.setString(7, usuario.getContato().getTelefone());
            stmt.setString(8, usuario.getContato().getEmail());

            stmt.executeUpdate();
        }
    }

    public Optional<Usuario> findById(String id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id = ?";

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

    public List<Usuario> findAll() throws SQLException {
        String sql = "SELECT * FROM usuario";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapRow(rs));
            }
        }
        return usuarios;
    }

    public void update(Usuario usuario) throws SQLException {
        String sql = """
                UPDATE usuario
                SET nome = ?, cpf = ?, cargo = ?, salario = ?,
                    contato_celular = ?, contato_telefone = ?, contato_email = ?
                WHERE id = ?
                """;

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getCargo());
            stmt.setDouble(4, usuario.getSalario());
            stmt.setString(5, usuario.getContato().getCelular());
            stmt.setString(6, usuario.getContato().getTelefone());
            stmt.setString(7, usuario.getContato().getEmail());
            stmt.setString(8, usuario.getId().toString());

            stmt.executeUpdate();
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    // Maps a ResultSet row to a Usuario object
    private Usuario mapRow(ResultSet rs) throws SQLException {
        Contato contato = new Contato(
                rs.getString("contato_celular"),
                rs.getString("contato_telefone"),
                rs.getString("contato_email")
        );

        return new Usuario(
                UUID.fromString(rs.getString("id")),
                rs.getString("nome"),
                rs.getString("cpf"),
                contato,
                rs.getString("cargo"),
                rs.getDouble("salario")
        );
    }
}