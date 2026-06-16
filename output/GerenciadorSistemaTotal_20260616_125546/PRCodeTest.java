import com.ficticio.pmd.GerenciadorSistemaTotal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

public class PRCodeTest {

    private GerenciadorSistemaTotal gerenciador;

    @BeforeEach
    public void setUp() {
        gerenciador = new GerenciadorSistemaTotal();
    }

    @Test
    public void testCadastrarNovoUsuario() {
        Assertions.assertDoesNotThrow(() -> gerenciador.cadastrarNovoUsuario("Joao", "j@e.com", "123"));
    }

    @Test
    public void testProcessarFaturamentoPagamentoSucesso() {
        Assertions.assertDoesNotThrow(() -> gerenciador.processarFaturamentoMensal(100.0));
    }

    @Test
    public void testEmitirNotaFiscalEletronica() {
        String resultado = gerenciador.emitirNotaFiscalEletronica();
        
        Assertions.assertNotNull(resultado);
        Assertions.assertTrue(resultado.contains("emitida com sucesso"));
        Assertions.assertTrue(resultado.startsWith("Nota"));
    }

    @Test
    public void testConfigurarEEnviarEmailExecucao() {
        Assertions.assertDoesNotThrow(() -> {
            gerenciador.configurarEEnviarEmail("teste@teste.com", "Assunto");
        });
    }
}