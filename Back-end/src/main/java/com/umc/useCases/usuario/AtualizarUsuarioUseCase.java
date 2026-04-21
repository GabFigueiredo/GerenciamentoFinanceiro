package com.umc.useCases.usuario;

import com.umc.model.conta.Contato;
import com.umc.model.usuario.Usuario;
import com.umc.model.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Service
public class AtualizarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public AtualizarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario execute(Command command) throws SQLException {
        if (command.id() == null || command.id().isBlank())
            throw new IllegalArgumentException("id e obrigatorio");

        Usuario atual = usuarioRepository.findById(command.id())
                .orElseThrow(() -> new NoSuchElementException("Usuario nao encontrado"));

        Contato contatoAtualizado = new Contato(
                command.celular() != null ? command.celular() : atual.getContato().getCelular(),
                command.telefone() != null ? command.telefone() : atual.getContato().getTelefone(),
                command.email() != null ? command.email() : atual.getContato().getEmail()
        );

        atual.setNome(command.nome() != null ? command.nome() : atual.getNome());
        atual.setCargo(command.cargo() != null ? command.cargo() : atual.getCargo());
        atual.setSalario(command.salario() != null ? command.salario() : atual.getSalario());
        atual.setContato(contatoAtualizado);

        usuarioRepository.update(atual);
        return atual;
    }

    public record Command(
            String id,
            String nome,
            String celular,
            String telefone,
            String email,
            String cargo,
            Double salario
    ) {}
}
