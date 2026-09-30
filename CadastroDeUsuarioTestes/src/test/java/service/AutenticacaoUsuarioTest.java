package service;

import org.example.usuario.Usuario;
import org.example.usuario.ValidarSenha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AutenticacaoUsuarioTest {

    private CadastroUsuarioService service;

    @BeforeEach
    void setUp() {
        ValidarSenha validator = new ValidarSenha();
        service = new CadastroUsuarioService(validator);

        Usuario usuario = new Usuario("Maria Silva", "maria.silva", "Java@12345", "maria@email.com");
        service.cadastrar(usuario);
    }

    @Test
    @DisplayName("RF05 — Deve permitir o acesso quando o usuário existe e a senha está correta")
    void devePermitirAcessoComCredenciaisCorretas() {
        boolean autenticado = service.autenticar("maria.silva", "Java@12345");
        assertTrue(autenticado, "O acesso deveria ter sido permitido.");
    }

    @Test
    @DisplayName("RF05/RF06 — Deve lançar exceção quando a senha estiver incorreta")
    void deveImpedirAcessoComSenhaIncorreta() {
        assertThrows(IllegalArgumentException.class, () -> service.autenticar("maria.silva", "SenhaErrada1"));
    }

    @Test
    @DisplayName("RF05/RF06 — Deve lançar exceção quando o usuário não existir")
    void deveImpedirAcessoComUsuarioInexistente() {
        assertThrows(RuntimeException.class, () -> service.autenticar("usuario.fantasma", "Java@12345"));
    }

    @Test
    @DisplayName("RF05/RF06 — Deve lançar exceção quando o login ou senha forem vazios")
    void deveImpedirAcessoComCamposVaziosOuNulos() {
        assertThrows(IllegalArgumentException.class, () -> service.autenticar("", "Java@12345"));
        assertThrows(IllegalArgumentException.class, () -> service.autenticar("maria.silva", ""));
    }

    @Test
    @DisplayName("RF07 — Deve bloquear a conta após 3 tentativas consecutivas de senha incorreta")
    void deveBloquearContaAposTresTentativasIncorretas() {
        // 1ª tentativa incorreta
        assertThrows(IllegalArgumentException.class, () -> service.autenticar("maria.silva", "SenhaErrada1"));

        // 2ª tentativa incorreta
        assertThrows(IllegalArgumentException.class, () -> service.autenticar("maria.silva", "SenhaErrada2"));

        // 3ª tentativa incorreta (deve atingir o limite e lançar a exceção de bloqueio)
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.autenticar("maria.silva", "SenhaErrada3"));
        assertTrue(exception.getMessage().contains("bloqueada"));
    }

    @Test
    @DisplayName("RF07 — Usuário bloqueado não deve conseguir autenticar mesmo digitando a senha correta")
    void usuarioBloqueadoNaoDeveAutenticarMesmoComSenhaCorreta() {
        // Erra a senha 3 vezes para forçar o bloqueio
        for (int i = 0; i < 3; i++) {
            try {
                service.autenticar("maria.silva", "SenhaErrada");
            } catch (Exception ignored) {}
        }

        // Tenta logar com a senha correta ("Java@12345")
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.autenticar("maria.silva", "Java@12345"));
        assertTrue(exception.getMessage().contains("bloqueada"));
    }
}