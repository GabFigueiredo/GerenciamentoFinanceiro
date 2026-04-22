package com.umc.useCases.usuario;

import com.umc.model.conta.Contato;
import com.umc.model.usuario.Usuario;
import com.umc.model.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.UUID;

@Service
public class CriarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public CriarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario execute(Command command) throws SQLException {
        validar(command);

        Usuario usuario = new Usuario(
                UUID.randomUUID(),
                command.nome(),
                command.cpf(),
                command.email(),
                command.senha(),
                new Contato(command.celular(), command.telefone()),
                command.cargo(),
                command.salario()
        );

        usuarioRepository.save(usuario);
        return usuario;
    }

    private void validar(Command command) {
        if (command.nome() == null || command.nome().isBlank())
            throw new IllegalArgumentException("nome e obrigatorio");
        if (command.cpf() == null || command.cpf().isBlank())
            throw new IllegalArgumentException("cpf e obrigatorio");
        if (command.email() == null || command.email().isBlank())
            throw new IllegalArgumentException("email e obrigatorio");
        if (command.senha() == null || command.senha().isBlank())
            throw new IllegalArgumentException("senha e obrigatoria");
        if (command.salario() == null || command.salario() < 0)
            throw new IllegalArgumentException("salario invalido");
    }

    public record Command(
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
