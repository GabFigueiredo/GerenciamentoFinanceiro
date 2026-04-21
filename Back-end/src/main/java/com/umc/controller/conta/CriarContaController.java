package com.umc.controller.conta;

import com.umc.model.conta.Conta;
import com.umc.useCases.conta.CriarContaUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/contas")
public class CriarContaController {

    private final CriarContaUseCase criarContaUseCase;

    public CriarContaController(CriarContaUseCase criarContaUseCase) {
        this.criarContaUseCase = criarContaUseCase;
    }

    @PostMapping
    public ResponseEntity<ContaResponse> criar(@RequestBody ContaRequest request) throws Exception {
        Conta conta = criarContaUseCase.execute(new CriarContaUseCase.Command(
                request.usuarioId(),
                request.moeda(),
                request.saldoAtual(),
                request.limiteGastoMensal(),
                request.descricao()
        ));

        return ResponseEntity.status(HttpStatus.CREATED).body(new ContaResponse(
                conta.getId().toString(),
                conta.getUsuario().getId().toString(),
                conta.getMoeda().name(),
                conta.getSaldoAtual().getValor(),
                conta.getDespesaMensal().getValor(),
                conta.getLimiteGastoMensal().getValor(),
                conta.getDescricao(),
                conta.getDataCriacao(),
                conta.getDataAtualizacao()
        ));
    }

    public record ContaRequest(
            String usuarioId,
            String moeda,
            Double saldoAtual,
            Double limiteGastoMensal,
            String descricao
    ) {}

    public record ContaResponse(
            String id,
            String usuarioId,
            String moeda,
            Double saldoAtual,
            Double despesaMensal,
            Double limiteGastoMensal,
            String descricao,
            LocalDate dataCriacao,
            LocalDate dataAtualizacao
    ) {}
}