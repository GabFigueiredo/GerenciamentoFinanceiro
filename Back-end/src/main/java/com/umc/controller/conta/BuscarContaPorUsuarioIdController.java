package com.umc.controller.conta;

import com.umc.model.conta.Conta;
import com.umc.useCases.conta.BuscarContaPorUsuarioIdUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class BuscarContaPorUsuarioIdController {

    private final BuscarContaPorUsuarioIdUseCase buscarContaPorUsuarioIdUseCase;

    public BuscarContaPorUsuarioIdController(BuscarContaPorUsuarioIdUseCase buscarContaPorUsuarioIdUseCase) {
        this.buscarContaPorUsuarioIdUseCase = buscarContaPorUsuarioIdUseCase;
    }

    @GetMapping("/{usuarioId}/contas")
    public ResponseEntity<List<ContaResponse>> buscar(@PathVariable String usuarioId) throws Exception {
        List<ContaResponse> response = buscarContaPorUsuarioIdUseCase.execute(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    private ContaResponse toResponse(Conta conta) {
        return new ContaResponse(
                conta.getId().toString(),
                conta.getUsuario().getId().toString(),
                conta.getMoeda().name(),
                conta.getSaldoAtual().getValor(),
                conta.getDespesaMensal().getValor(),
                conta.getLimiteGastoMensal().getValor(),
                conta.getDescricao(),
                conta.getDataCriacao(),
                conta.getDataAtualizacao()
        );
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
