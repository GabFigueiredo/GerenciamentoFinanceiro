package com.umc.controller.usuario;

import com.umc.model.usuario.Usuario;
import com.umc.useCases.usuario.AtualizarUsuarioUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class AtualizarUsuarioController {

    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;

    public AtualizarUsuarioController(AtualizarUsuarioUseCase atualizarUsuarioUseCase) {
        this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable String id,
            @RequestBody UsuarioRequest request) throws Exception {
        Usuario usuario = atualizarUsuarioUseCase.execute(new AtualizarUsuarioUseCase.Command(
                id,
                request.nome(),
                request.cpf(),
                request.celular(),
                request.telefone(),
                request.email(),
                request.senha(),
                request.cargo(),
                request.salario()
        ));

        return ResponseEntity.ok(toResponse(usuario));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId().toString(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getContato() != null ? usuario.getContato().getCelular() : null,
                usuario.getContato() != null ? usuario.getContato().getTelefone() : null,
                usuario.getCargo(),
                usuario.getSalario()
        );
    }

    public record UsuarioRequest(
            String nome,
            String cpf,
            String email,
            String senha,
            String celular,
            String telefone,
            String cargo,
            Double salario
    ) {}

    public record UsuarioResponse(
            String id,
            String nome,
            String cpf,
            String email,
            String senha,
            String celular,
            String telefone,
            String cargo,
            Double salario
    ) {}
}
