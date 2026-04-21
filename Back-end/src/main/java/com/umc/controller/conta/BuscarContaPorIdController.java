package com.umc.controller.conta;

import com.umc.model.conta.Conta;
import com.umc.useCases.conta.BuscarContaPorIdUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/contas")
public class BuscarContaPorIdController {

    private final BuscarContaPorIdUseCase buscarContaPorIdUseCase;

    public BuscarContaPorIdController(BuscarContaPorIdUseCase buscarContaPorIdUseCase) {
        this.buscarContaPorIdUseCase = buscarContaPorIdUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponse> buscarPorId(@PathVariable String id) throws Exception {
        Conta conta = buscarContaPorIdUseCase.execute(id);

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
