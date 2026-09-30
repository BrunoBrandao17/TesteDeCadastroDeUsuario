package service;

import org.example.usuario.Usuario;
import org.example.usuario.ValidarSenha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TratamentoExcecoesTest {

    private CadastroUsuarioService service;

    @BeforeEach
    void setUp() {
        ValidarSenha validator = new ValidarSenha();
        service = new CadastroUsuarioService(validator);

        Usuario usuario = new Usuario("Maria Silva", "maria.silva", "Java@12345", "maria@email.com");
        service.cadastrar(usuario);
    }

    @Test
    @DisplayName("RF06 - Lança IllegalArgumentException para campos vazios")
    void deveLancarExcecaoCamposVazios() {
        assertThrows(IllegalArgumentException.class, () -> service.autenticar("", "Java@12345"));
    }

    @Test
    @DisplayName("RF06 - Lança RuntimeException para usuário inexistente")
    void deveLancarExcecaoUsuarioInexistente() {
        assertThrows(RuntimeException.class, () -> service.autenticar("usuario.fantasma", "Java@12345"));
    }

    @Test
    @DisplayName("RF06 - Lança IllegalArgumentException para senha incorreta")
    void deveLancarExcecaoSenhaIncorreta() {
        assertThrows(IllegalArgumentException.class, () -> service.autenticar("maria.silva", "SenhaErrada123"));
    }
}