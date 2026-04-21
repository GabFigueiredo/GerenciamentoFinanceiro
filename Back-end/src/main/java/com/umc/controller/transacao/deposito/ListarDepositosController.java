package com.umc.controller.transacao.deposito;

import com.umc.useCases.transacao.deposito.ListarDepositosUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transacoes/depositos")
public class ListarDepositosController {

    private final ListarDepositosUseCase listarDepositosUseCase;

    public ListarDepositosController(ListarDepositosUseCase listarDepositosUseCase) {
        this.listarDepositosUseCase = listarDepositosUseCase;
    }

    @GetMapping
    public ResponseEntity<List<DepositoResponse>> listar(
            @RequestParam(required = false) String contaId) throws Exception {
        List<DepositoResponse> response = listarDepositosUseCase.execute(contaId)
                .stream()
                .map(d -> new DepositoResponse(
                        d.getId(),
                        d.getConta().getId().toString(),
                        d.getValor().getValor(),
                        d.getValor().getMoeda().name(),
                        d.getOrigem(),
                        d.getReceita().name(),
                        d.getData(),
                        d.getDescricao()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    public record DepositoResponse(
            String id,
            String contaId,
            Double valor,
            String moeda,
            String origem,
            String receita,
            LocalDate data,
            String descricao
    ) {}
}
