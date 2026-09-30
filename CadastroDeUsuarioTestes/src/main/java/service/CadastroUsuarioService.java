package service;

import org.example.usuario.Usuario;
import org.example.usuario.ValidarSenha;

import java.util.ArrayList;
import java.util.List;

public class CadastroUsuarioService {

    private final ValidarSenha validarSenha;
    private final List<Usuario> usuariosCadastrados = new ArrayList<>();

    public CadastroUsuarioService(ValidarSenha validarSenha) {
        this.validarSenha = validarSenha;
    }

    public boolean cadastrar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }
        if (usuario.getNome() == null || usuario.getNome().isBlank() ||
                usuario.getLogin() == null || usuario.getLogin().isBlank() ||
                usuario.getSenha() == null || usuario.getSenha().isBlank() ||
                usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("Campos obrigatórios não podem ser nulos ou vazios.");
        }

        if (!validarSenha.validarSenha(usuario.getSenha())) {
            throw new IllegalArgumentException("Senha inválida pelas regras do sistema.");
        }

        return usuariosCadastrados.add(usuario);
    }

    // RF05 + RF06 + RF07
    public boolean autenticar(String login, String senha) {
        if (login == null || login.isBlank() || senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Login e senha são obrigatórios.");
        }

        Usuario usuarioEncontrado = null;
        for (Usuario u : usuariosCadastrados) {
            if (u.getLogin().equals(login)) {
                usuarioEncontrado = u;
                break;
            }
        }

        if (usuarioEncontrado == null) {
            throw new RuntimeException("Usuário inexistente.");
        }

        // RF07: Usuário bloqueado não deve conseguir autenticar
        if (usuarioEncontrado.isBloqueado()) {
            throw new RuntimeException("Conta bloqueada por excesso de tentativas incorretas.");
        }

        // Validação da senha
        if (!usuarioEncontrado.getSenha().equals(senha)) {
            usuarioEncontrado.registrarTentativaInvalida(); // Incrementa a contagem
            if (usuarioEncontrado.isBloqueado()) {
                throw new RuntimeException("Conta bloqueada após 3 tentativas incorretas.");
            }
            throw new IllegalArgumentException("Senha incorreta.");
        }

        // Sucesso no login: reseta o contador de tentativas
        usuarioEncontrado.resetarTentativas();
        return true;
    }

    public List<Usuario> getUsuariosCadastrados() {
        return usuariosCadastrados;
    }
}