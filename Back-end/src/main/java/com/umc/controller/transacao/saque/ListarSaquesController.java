package com.umc.controller.transacao.saque;

import com.umc.useCases.transacao.saque.ListarSaquesUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transacoes/saques")
public class ListarSaquesController {

    private final ListarSaquesUseCase listarSaquesUseCase;

    public ListarSaquesController(ListarSaquesUseCase listarSaquesUseCase) {
        this.listarSaquesUseCase = listarSaquesUseCase;
    }

    @GetMapping
    public ResponseEntity<List<SaqueResponse>> listar(
            @RequestParam(required = false) String contaId) throws Exception {
        List<SaqueResponse> response = listarSaquesUseCase.execute(contaId)
                .stream()
                .map(s -> new SaqueResponse(
                        s.getId(),
                        s.getConta().getId().toString(),
                        s.getValor().getValor(),
                        s.getValor().getMoeda().name(),
                        s.getDestino(),
                        s.getDespesa().getCategoriaDespesa().name(),
                        s.getDespesa().getFormaPagamento().name(),
                        s.getData(),
                        s.getDescricao(),
                        s.getDespesa().getObservacao()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    public record SaqueResponse(
            String id,
            String contaId,
            Double valor,
            String moeda,
            String destino,
            String categoria,
            String formaPagamento,
            LocalDate data,
            String descricao,
            String observacao
    ) {}
}