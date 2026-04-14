package com.umc.repositories;

import com.umc.model.Meta;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MetaRepository {
    void save(String contaId, Meta meta) throws SQLException;
    Optional<Meta> findById(String id) throws SQLException;
    List<Meta> findAll() throws SQLException;
    List<Meta> findByContaId(String contaId) throws SQLException;
    void update(Meta meta) throws SQLException;
    void delete(String id) throws SQLException;
}
