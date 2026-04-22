package com.umc.controller.conta.meta;

import com.umc.model.conta.Meta;
import com.umc.useCases.conta.meta.CriarMetaUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/contas/{contaId}/metas")
public class CriarMetaController {

    private final CriarMetaUseCase criarMetaUseCase;

    public CriarMetaController(CriarMetaUseCase criarMetaUseCase) {
        this.criarMetaUseCase = criarMetaUseCase;
    }

    @PostMapping
    public ResponseEntity<MetaResponse> criar(
            @PathVariable String contaId,
            @RequestBody MetaRequest request) throws Exception {
        Meta meta = criarMetaUseCase.execute(new CriarMetaUseCase.Command(
                contaId,
                request.nome(),
                request.valorObjetivo(),
                request.cargo(),
                request.dataInicio(),
                request.dataDeConclusao()
        ));

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(contaId, meta));
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
