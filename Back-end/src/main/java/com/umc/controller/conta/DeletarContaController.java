package com.umc.controller.conta;

import com.umc.useCases.conta.DeletarContaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contas")
public class DeletarContaController {

    private final DeletarContaUseCase deletarContaUseCase;

    public DeletarContaController(DeletarContaUseCase deletarContaUseCase) {
        this.deletarContaUseCase = deletarContaUseCase;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) throws Exception {
        deletarContaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}