package com.umc.controller.usuario;

import com.umc.useCases.usuario.DeletarUsuarioUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class DeletarUsuarioController {

    private final DeletarUsuarioUseCase deletarUsuarioUseCase;

    public DeletarUsuarioController(DeletarUsuarioUseCase deletarUsuarioUseCase) {
        this.deletarUsuarioUseCase = deletarUsuarioUseCase;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) throws Exception {
        deletarUsuarioUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
