package com.umc.controller.conta.meta;

import com.umc.useCases.conta.meta.ListarMetasUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/contas/{contaId}/metas")
public class ListarMetasController {

    private final ListarMetasUseCase listarMetasUseCase;

    public ListarMetasController(ListarMetasUseCase listarMetasUseCase) {
        this.listarMetasUseCase = listarMetasUseCase;
    }

    @GetMapping
    public ResponseEntity<List<MetaResponse>> listar(@PathVariable String contaId) throws Exception {
        List<MetaResponse> response = listarMetasUseCase.execute(contaId)
                .stream()
                .map(meta -> new MetaResponse(
                        meta.getId().toString(),
                        contaId,
                        meta.getNome(),
                        meta.getValorObjetivo().getValor(),
                        meta.getValorObjetivo().getMoeda().name(),
                        meta.getCargo(),
                        meta.getDataInicio(),
                        meta.getDataDeConclusao()
                ))
                .toList();

        return ResponseEntity.ok(response);
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
