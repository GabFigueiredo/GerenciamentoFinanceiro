package com.umc.controller.transacao.deposito;

import com.umc.model.transacao.Deposito.Deposito;
import com.umc.useCases.transacao.deposito.BuscarDepositoPorIdUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/transacoes/depositos")
public class BuscarDepositoPorIdController {

    private final BuscarDepositoPorIdUseCase buscarDepositoPorIdUseCase;

    public BuscarDepositoPorIdController(BuscarDepositoPorIdUseCase buscarDepositoPorIdUseCase) {
        this.buscarDepositoPorIdUseCase = buscarDepositoPorIdUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepositoResponse> buscarPorId(@PathVariable String id) throws Exception {
        Deposito deposito = buscarDepositoPorIdUseCase.execute(id);

        return ResponseEntity.ok(new DepositoResponse(
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
