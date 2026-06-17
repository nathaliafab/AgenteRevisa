import br.com.dataset.spotbugs.ProcessadorRelatorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class PRCodeTest {

    @Test
    public void testGerarRelatorioFinanceiroExecutesSuccessfully() {
        ProcessadorRelatorio processador = new ProcessadorRelatorio();

        // O teste agora espera que o método execute sem exceções, 
        // visto que a lógica de negócio foi corrigida para evitar o NullPointerException
        // e o loop infinito.
        Assertions.assertDoesNotThrow(() -> {
            processador.gerarRelatorioFinanceiro();
        });
    }
}