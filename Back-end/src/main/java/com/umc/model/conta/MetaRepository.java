package com.umc.model.conta;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MetaRepository {
    void save(String contaId, Meta meta) throws SQLException;
    Optional<Meta> findById(String contaId, String metaId) throws SQLException;
    List<Meta> findByContaId(String contaId) throws SQLException;
    void update(String contaId, Meta meta) throws SQLException;
    void delete(String contaId, String metaId) throws SQLException;
}
