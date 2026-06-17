import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import br.com.dataset.spotbugs.GerenciadorEstoque;

public class PRCodeTest {

    private GerenciadorEstoque gerenciador;

    @BeforeEach
    public void setup() {
        gerenciador = new GerenciadorEstoque();
    }

    @Test
    public void testInicializarEstoque() {
        gerenciador.inicializarEstoque();
        // Agora o método processa a conversão de String para Long corretamente
        Assertions.assertEquals("Notebook Dell", gerenciador.buscarProduto("101"));
    }

    @Test
    public void testBuscarProdutoInexistente() {
        Assertions.assertNull(gerenciador.buscarProduto("999"));
    }

    @Test
    public void testVerificarConsistencia() {
        gerenciador.inicializarEstoque();
        // Agora o método retorna um booleano válido em vez de lançar exceção
        Assertions.assertTrue(gerenciador.verificarConsistencia());
    }
}