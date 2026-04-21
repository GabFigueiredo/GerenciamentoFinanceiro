package com.umc.controller.transacao.saque;

import com.umc.model.transacao.Saque.Saque;
import com.umc.useCases.transacao.saque.BuscarSaquePorIdUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/transacoes/saques")
public class BuscarSaquePorIdController {

    private final BuscarSaquePorIdUseCase buscarSaquePorIdUseCase;

    public BuscarSaquePorIdController(BuscarSaquePorIdUseCase buscarSaquePorIdUseCase) {
        this.buscarSaquePorIdUseCase = buscarSaquePorIdUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaqueResponse> buscarPorId(@PathVariable String id) throws Exception {
        Saque saque = buscarSaquePorIdUseCase.execute(id);

        return ResponseEntity.ok(new SaqueResponse(
                saque.getId(),
                saque.getConta().getId().toString(),
                saque.getValor().getValor(),
                saque.getValor().getMoeda().name(),
                saque.getDestino(),
                saque.getDespesa().getCategoriaDespesa().name(),
                saque.getDespesa().getFormaPagamento().name(),
                saque.getData(),
                saque.getDescricao(),
                saque.getDespesa().getObservacao()
        ));
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
