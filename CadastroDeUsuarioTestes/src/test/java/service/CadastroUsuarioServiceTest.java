package service;

import org.example.usuario.NivelAcesso;
import org.example.usuario.Usuario;
import org.example.usuario.ValidarSenha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CadastroUsuarioServiceTest {

    private CadastroUsuarioService service;

    @BeforeEach
    void setUp() {
        ValidarSenha validator = new ValidarSenha();
        service = new CadastroUsuarioService(validator);
    }

    // ==========================================
    // RF01 — CADASTRO DE USUÁRIO
    // ==========================================

    @Test
    @DisplayName("RF01 - Deve cadastrar usuário com todos os campos preenchidos corretamente")
    void deveCadastrarUsuarioComSucesso() {
        Usuario usuario = new Usuario("João Silva", "joao.silva", "Java@12345", "joao@email.com");
        boolean cadastrado = service.cadastrar(usuario);

        assertTrue(cadastrado);
        assertEquals(1, service.getUsuariosCadastrados().size());
    }

    @Test
    @DisplayName("RF01 - Deve rejeitar cadastro quando o usuário for nulo")
    void deveLancarExcecaoQuandoUsuarioForNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(null));
    }

    @ParameterizedTest
    @CsvSource({
            "'', joao.silva, Java@12345, joao@email.com",   // Nome vazio
            "João, '', Java@12345, joao@email.com",         // Login vazio
            "João, joao.silva, '', joao@email.com",         // Senha vazia
            "João, joao.silva, Java@12345, ''"              // Email vazio
    })
    @DisplayName("RF01 - Deve rejeitar cadastro com campos vazios")
    void deveLancarExcecaoQuandoCampoForVazio(String nome, String login, String senha, String email) {
        Usuario usuario = new Usuario(nome, login, senha, email);
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(usuario));
    }

    // ==========================================
    // RF08 — NÍVEIS DE USUÁRIO (ADMIN, GERENTE, CLIENTE)
    // ==========================================

    @Test
    @DisplayName("RF08 - Deve cadastrar usuários com níveis específicos (ADMIN, GERENTE, CLIENTE)")
    void deveCadastrarUsuariosComNiveisEspecificos() {
        Usuario admin = new Usuario("Carlos Admin", "carlos.admin", "Java@12345", "admin@email.com", NivelAcesso.ADMIN);
        Usuario gerente = new Usuario("Ana Gerente", "ana.gerente", "Java@12345", "gerente@email.com", NivelAcesso.GERENTE);
        Usuario cliente = new Usuario("Pedro Cliente", "pedro.cliente", "Java@12345", "cliente@email.com", NivelAcesso.CLIENTE);

        assertTrue(service.cadastrar(admin));
        assertTrue(service.cadastrar(gerente));
        assertTrue(service.cadastrar(cliente));

        assertEquals(NivelAcesso.ADMIN, service.getUsuariosCadastrados().get(0).getNivelAcesso());
        assertEquals(NivelAcesso.GERENTE, service.getUsuariosCadastrados().get(1).getNivelAcesso());
        assertEquals(NivelAcesso.CLIENTE, service.getUsuariosCadastrados().get(2).getNivelAcesso());
    }

    @Test
    @DisplayName("RF08 - Deve atribuir o nível CLIENTE por padrão quando não informado")
    void deveAtribuirNivelClientePorPadrao() {
        Usuario usuarioPadrao = new Usuario("João Silva", "joao.silva", "Java@12345", "joao@email.com");
        service.cadastrar(usuarioPadrao);

        assertEquals(NivelAcesso.CLIENTE, usuarioPadrao.getNivelAcesso());
    }
}