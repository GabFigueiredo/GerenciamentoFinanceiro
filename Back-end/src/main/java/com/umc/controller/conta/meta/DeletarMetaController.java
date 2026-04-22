package com.umc.controller.conta.meta;

import com.umc.useCases.conta.meta.DeletarMetaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contas/{contaId}/metas")
public class DeletarMetaController {

    private final DeletarMetaUseCase deletarMetaUseCase;

    public DeletarMetaController(DeletarMetaUseCase deletarMetaUseCase) {
        this.deletarMetaUseCase = deletarMetaUseCase;
    }

    @DeleteMapping("/{metaId}")
    public ResponseEntity<Void> deletar(
            @PathVariable String contaId,
            @PathVariable String metaId) throws Exception {
        deletarMetaUseCase.execute(contaId, metaId);
        return ResponseEntity.noContent().build();
    }
}
