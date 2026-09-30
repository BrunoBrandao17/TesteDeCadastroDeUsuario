package service;

import org.example.usuario.ValidarSenha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidacaoSenhaServiceTest {

    private ValidarSenha service;

    @BeforeEach
    void setUp() {
        service = new ValidarSenha();
    }

    @Test
    void deveAceitarSenhaValida() {
        String senha = "Java@123456"; // 10 caracteres (letras, número e especial)
        boolean resultado = service.validarSenha(senha);
        assertTrue(resultado, "A senha válida deveria ser aceite.");
    }
}