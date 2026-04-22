package com.umc.controller.conta.meta;

import com.umc.model.conta.Meta;
import com.umc.useCases.conta.meta.AtualizarMetaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/contas/{contaId}/metas")
public class AtualizarMetaController {

    private final AtualizarMetaUseCase atualizarMetaUseCase;

    public AtualizarMetaController(AtualizarMetaUseCase atualizarMetaUseCase) {
        this.atualizarMetaUseCase = atualizarMetaUseCase;
    }

    @PatchMapping("/{metaId}")
    public ResponseEntity<MetaResponse> atualizar(
            @PathVariable String contaId,
            @PathVariable String metaId,
            @RequestBody MetaRequest request) throws Exception {
        Meta meta = atualizarMetaUseCase.execute(new AtualizarMetaUseCase.Command(
                contaId,
                metaId,
                request.nome(),
                request.valorObjetivo(),
                request.cargo(),
                request.dataInicio(),
                request.dataDeConclusao()
        ));

        return ResponseEntity.ok(toResponse(contaId, meta));
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

    public record MetaRequest(
            String nome,
            Double valorObjetivo,
            String cargo,
            LocalDate dataInicio,
            LocalDate dataDeConclusao
    ) {}

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
