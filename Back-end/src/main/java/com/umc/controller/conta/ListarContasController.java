package com.umc.controller.conta;

import com.umc.useCases.conta.ListarContasUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/contas")
public class ListarContasController {

    private final ListarContasUseCase listarContasUseCase;

    public ListarContasController(ListarContasUseCase listarContasUseCase) {
        this.listarContasUseCase = listarContasUseCase;
    }

    @GetMapping
    public ResponseEntity<List<ContaResponse>> listar(
            @RequestParam(required = false) String usuarioId) throws Exception {
        List<ContaResponse> response = listarContasUseCase.execute(usuarioId)
                .stream()
                .map(c -> new ContaResponse(
                        c.getId().toString(),
                        c.getUsuario().getId().toString(),
                        c.getMoeda().name(),
                        c.getSaldoAtual().getValor(),
                        c.getDespesaMensal().getValor(),
                        c.getLimiteGastoMensal().getValor(),
                        c.getDescricao(),
                        c.getDataCriacao(),
                        c.getDataAtualizacao()
                ))
                .toList();

        return ResponseEntity.ok(response);
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
