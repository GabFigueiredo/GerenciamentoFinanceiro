package com.umc.repositories;

import com.umc.model.Despesa.Despesa;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DespesaRepository {
    void save(Despesa despesa) throws SQLException;
    Optional<Despesa> findById(String id) throws SQLException;
    List<Despesa> findAll() throws SQLException;
    void update(Despesa despesa) throws SQLException;
    void delete(String id) throws SQLException;
}
