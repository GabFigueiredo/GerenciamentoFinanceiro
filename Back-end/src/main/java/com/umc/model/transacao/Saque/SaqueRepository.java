package com.umc.model.transacao.Saque;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface SaqueRepository {
    void save(Saque saque) throws SQLException;
    Optional<Saque> findById(String id) throws SQLException;
    List<Saque> findAll() throws SQLException;
    List<Saque> findByContaId(String contaId) throws SQLException;
    void delete(String id) throws SQLException;
}
