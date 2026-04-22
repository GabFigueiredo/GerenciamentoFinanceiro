package com.umc.controller.usuario;

import com.umc.model.usuario.Usuario;
import com.umc.useCases.usuario.BuscarUsuarioPorIdUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class BuscarUsuarioPorIdController {

    private final BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;

    public BuscarUsuarioPorIdController(BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase) {
        this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(toResponse(buscarUsuarioPorIdUseCase.execute(id)));
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
