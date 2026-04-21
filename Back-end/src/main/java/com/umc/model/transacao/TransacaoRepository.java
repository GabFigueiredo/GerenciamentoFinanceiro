package com.umc.model.transacao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TransacaoRepository {
    void save(Transacao transacao) throws SQLException;
    Optional<Transacao> findById(String id) throws SQLException;
    List<Transacao> findAll() throws SQLException;
    List<Transacao> findByContaId(String contaId) throws SQLException;
    void update(Transacao transacao) throws SQLException;
    void delete(String id) throws SQLException;
}
