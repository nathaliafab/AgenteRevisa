import com.ficticio.pmd.ClienteApiExterno;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrCodeTest {

    @Test
    public void testBuscarDadosParceiroFalhaConexao() {
        // Injetando um IP inválido para simular a falha de rede
        ClienteApiExterno cliente = new ClienteApiExterno("192.0.2.1");
        
        assertThrows(RuntimeException.class, () -> {
            cliente.buscarDadosParceiro("teste");
        }, "Deveria lançar RuntimeException devido a erro de conexão");
    }
}