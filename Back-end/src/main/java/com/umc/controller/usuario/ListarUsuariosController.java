package com.umc.controller.usuario;

import com.umc.useCases.usuario.ListarUsuariosUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class ListarUsuariosController {

    private final ListarUsuariosUseCase listarUsuariosUseCase;

    public ListarUsuariosController(ListarUsuariosUseCase listarUsuariosUseCase) {
        this.listarUsuariosUseCase = listarUsuariosUseCase;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() throws Exception {
        List<UsuarioResponse> response = listarUsuariosUseCase.execute()
                .stream()
                .map(usuario -> new UsuarioResponse(
                        usuario.getId().toString(),
                        usuario.getNome(),
                        usuario.getCpf(),
                        usuario.getEmail(),
                        usuario.getSenha(),
                        usuario.getContato() != null ? usuario.getContato().getCelular() : null,
                        usuario.getContato() != null ? usuario.getContato().getTelefone() : null,
                        usuario.getCargo(),
                        usuario.getSalario()
                ))
                .toList();

        return ResponseEntity.ok(response);
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
