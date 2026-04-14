package com.umc.controller;

import com.umc.model.Conta;
import com.umc.useCases.CriarContaUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/contas")
public class CriarContaController {

    private final CriarContaUseCase criarContaUseCase;

    public CriarContaController(CriarContaUseCase criarContaUseCase) {
        this.criarContaUseCase = criarContaUseCase;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody CriarContaRequest request) {
        try {
            Conta conta = criarContaUseCase.execute(new CriarContaUseCase.Command(
                    request.usuarioId(),
                    request.moeda(),
                    request.saldoAtual(),
                    request.despesaMensal(),
                    request.limiteGastoMensal(),
                    request.descricao()
            ));

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CriarContaResponse(
                            conta.getId().toString(),
                            conta.getUsuario().getId().toString(),
                            conta.getMoeda().name(),
                            conta.getSaldoAtual().getValor(),
                            conta.getDespesaMensal().getValor(),
                            conta.getLimiteGastoMensal().getValor(),
                            conta.getDescricao()
                    ));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao criar conta"));
        }
    }

    public record CriarContaRequest(
            String usuarioId,
            String moeda,
            Double saldoAtual,
            Double despesaMensal,
            Double limiteGastoMensal,
            String descricao
    ) {
    }

    public record CriarContaResponse(
            String id,
            String usuarioId,
            String moeda,
            Double saldoAtual,
            Double despesaMensal,
            Double limiteGastoMensal,
            String descricao
    ) {
    }

    public record ErrorResponse(String message) {
    }
}
