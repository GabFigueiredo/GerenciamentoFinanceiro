package com.umc.repositories;

import com.umc.model.Transacao.Deposito;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DepositoRepository {
    void save(Deposito deposito) throws SQLException;
    Optional<Deposito> findById(String id) throws SQLException;
    List<Deposito> findAll() throws SQLException;
    List<Deposito> findByContaId(String contaId) throws SQLException;
    void update(Deposito deposito) throws SQLException;
    void delete(String id) throws SQLException;
}
