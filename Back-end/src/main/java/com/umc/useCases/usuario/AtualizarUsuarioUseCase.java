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

        Contato contatoAtual = atual.getContato() != null
                ? atual.getContato()
                : new Contato(null, null);

        Contato contatoAtualizado = new Contato(
                command.celular() != null ? command.celular() : contatoAtual.getCelular(),
                command.telefone() != null ? command.telefone() : contatoAtual.getTelefone()
        );

        if (command.salario() != null && command.salario() < 0)
            throw new IllegalArgumentException("salario invalido");
        if (command.nome() != null && command.nome().isBlank())
            throw new IllegalArgumentException("nome e obrigatorio");
        if (command.cpf() != null && command.cpf().isBlank())
            throw new IllegalArgumentException("cpf e obrigatorio");
        if (command.email() != null && command.email().isBlank())
            throw new IllegalArgumentException("email e obrigatorio");
        if (command.senha() != null && command.senha().isBlank())
            throw new IllegalArgumentException("senha e obrigatoria");

        atual.setNome(command.nome() != null ? command.nome() : atual.getNome());
        atual.setCpf(command.cpf() != null ? command.cpf() : atual.getCpf());
        atual.setEmail(command.email() != null ? command.email() : atual.getEmail());
        atual.setSenha(command.senha() != null ? command.senha() : atual.getSenha());
        atual.setCargo(command.cargo() != null ? command.cargo() : atual.getCargo());
        atual.setSalario(command.salario() != null ? command.salario() : atual.getSalario());
        atual.setContato(contatoAtualizado);

        usuarioRepository.update(atual);
        return atual;
    }

    public record Command(
            String id,
            String nome,
            String cpf,
            String celular,
            String telefone,
            String email,
            String senha,
            String cargo,
            Double salario
    ) {}
}
