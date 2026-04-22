package com.umc.controller.transacao.saque;

import com.umc.model.transacao.Saque.Saque;
import com.umc.model.enums.Moeda;
import com.umc.useCases.transacao.saque.RealizarSaqueUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/transacoes/saques")
public class RealizarSaqueController {

    private final RealizarSaqueUseCase realizarSaqueUseCase;

    public RealizarSaqueController(RealizarSaqueUseCase realizarSaqueUseCase) {
        this.realizarSaqueUseCase = realizarSaqueUseCase;
    }

    @PostMapping
    public ResponseEntity<SaqueResponse> sacar(@RequestBody SaqueRequest request) throws Exception {
        Saque saque = realizarSaqueUseCase.execute(new RealizarSaqueUseCase.Command(
                request.contaId(),
                request.valor(),
                Moeda.valueOf(request.moeda().toUpperCase()),
                request.destino(),
                request.formaPagamento(),
                request.categoria(),
                request.descricao(),
                request.observacao()
        ));

        return ResponseEntity.status(HttpStatus.CREATED).body(new SaqueResponse(
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

    public record SaqueRequest(
            String contaId,
            Double valor,
            String moeda,
            String destino,
            String formaPagamento,
            String categoria,
            String descricao,
            String observacao
    ) {}

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