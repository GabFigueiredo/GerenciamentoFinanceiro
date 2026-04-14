package com.umc.repositories;

import com.umc.model.Despesa.DespesaRecorrente;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DespesaRecorrenteRepository {
    void save(DespesaRecorrente despesaRecorrente) throws SQLException;
    Optional<DespesaRecorrente> findById(String id) throws SQLException;
    List<DespesaRecorrente> findAll() throws SQLException;
    List<DespesaRecorrente> findAtivas() throws SQLException;
    void update(DespesaRecorrente despesaRecorrente) throws SQLException;
    void delete(String id) throws SQLException;
}
