package com.umc.controller.conta;

import com.umc.model.conta.Conta;
import com.umc.useCases.conta.AtualizarContaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/contas")
public class AtualizarContaController {

    private final AtualizarContaUseCase atualizarContaUseCase;

    public AtualizarContaController(AtualizarContaUseCase atualizarContaUseCase) {
        this.atualizarContaUseCase = atualizarContaUseCase;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ContaResponse> atualizar(
            @PathVariable String id,
            @RequestBody ContaRequest request) throws Exception {
        Conta conta = atualizarContaUseCase.execute(new AtualizarContaUseCase.Command(
                id,
                request.limiteGastoMensal(),
                request.descricao()
        ));

        return ResponseEntity.ok(new ContaResponse(
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
