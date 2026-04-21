package com.umc.model.conta;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ContaRepository {
    void save(Conta conta) throws SQLException;
    Optional<Conta> findById(String id) throws SQLException;
    List<Conta> findAll() throws SQLException;
    List<Conta> findByUsuarioId(String usuarioId) throws SQLException;
    void update(Conta conta) throws SQLException;
    void delete(String id) throws SQLException;
}