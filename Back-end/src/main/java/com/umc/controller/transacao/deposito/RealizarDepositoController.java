package com.umc.controller.transacao.deposito;

import com.umc.model.transacao.Deposito.Deposito;
import com.umc.model.enums.Moeda;
import com.umc.useCases.transacao.deposito.RealizarDepositoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/transacoes/depositos")
public class RealizarDepositoController {

    private final RealizarDepositoUseCase realizarDepositoUseCase;

    public RealizarDepositoController(RealizarDepositoUseCase realizarDepositoUseCase) {
        this.realizarDepositoUseCase = realizarDepositoUseCase;
    }

    @PostMapping
    public ResponseEntity<DepositoResponse> depositar(@RequestBody DepositoRequest request) throws Exception {
        Deposito deposito = realizarDepositoUseCase.execute(new RealizarDepositoUseCase.Command(
                request.contaId(),
                request.valor(),
                Moeda.valueOf(request.moeda().toUpperCase()),
                request.origem(),
                request.receita(),
                request.descricao()
        ));

        return ResponseEntity.status(HttpStatus.CREATED).body(new DepositoResponse(
                deposito.getId(),
                deposito.getConta().getId().toString(),
                deposito.getValor().getValor(),
                deposito.getValor().getMoeda().name(),
                deposito.getOrigem(),
                deposito.getReceita().name(),
                deposito.getData(),
                deposito.getDescricao()
        ));
    }

    public record DepositoRequest(
            String contaId,
            Double valor,
            String moeda,
            String origem,
            String receita,
            String descricao
    ) {}

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
