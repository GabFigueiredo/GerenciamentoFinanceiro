package com.umc.controller.conta.meta;

import com.umc.model.conta.Meta;
import com.umc.useCases.conta.meta.BuscarMetaPorIdUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/contas/{contaId}/metas")
public class BuscarMetaPorIdController {

    private final BuscarMetaPorIdUseCase buscarMetaPorIdUseCase;

    public BuscarMetaPorIdController(BuscarMetaPorIdUseCase buscarMetaPorIdUseCase) {
        this.buscarMetaPorIdUseCase = buscarMetaPorIdUseCase;
    }

    @GetMapping("/{metaId}")
    public ResponseEntity<MetaResponse> buscarPorId(
            @PathVariable String contaId,
            @PathVariable String metaId) throws Exception {
        return ResponseEntity.ok(toResponse(contaId, buscarMetaPorIdUseCase.execute(contaId, metaId)));
    }

    private MetaResponse toResponse(String contaId, Meta meta) {
        return new MetaResponse(
                meta.getId().toString(),
                contaId,
                meta.getNome(),
                meta.getValorObjetivo().getValor(),
                meta.getValorObjetivo().getMoeda().name(),
                meta.getCargo(),
                meta.getDataInicio(),
                meta.getDataDeConclusao()
        );
    }

    public record MetaResponse(
            String id,
            String contaId,
            String nome,
            Double valorObjetivo,
            String moeda,
            String cargo,
            LocalDate dataInicio,
            LocalDate dataDeConclusao
    ) {}
}
