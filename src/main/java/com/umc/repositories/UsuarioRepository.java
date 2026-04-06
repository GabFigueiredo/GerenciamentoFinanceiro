package com.umc.repositories;

import com.umc.model.Usuario;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    void save(Usuario usuario) throws SQLException;
    Optional<Usuario> findById(String id) throws SQLException;
    List<Usuario> findAll() throws SQLException;
    void update(Usuario usuario) throws SQLException;
    void delete(String id) throws SQLException;
}