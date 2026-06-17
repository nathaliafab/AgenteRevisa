import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import com.ficticio.pmd.AnalisadorDeLogs;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

public class PRCodeTest {

    private final AnalisadorDeLogs analisador = new AnalisadorDeLogs();

    @Test
    public void testAnalisarComLogsValidos() {
        List<String> logs = Arrays.asList(
            "[INFO] Sistema iniciado",
            "[ERROR] Falha na conexão",
            "[DEBUG] Teste",
            "[ERROR] Erro de banco de dados"
        );
        List<String> resultado = analisador.analisar(logs);
        
        Assertions.assertEquals(2, resultado.size());
        Assertions.assertTrue(resultado.contains("[ERROR] Falha na conexão"));
        Assertions.assertTrue(resultado.contains("[ERROR] Erro de banco de dados"));
    }

    @Test
    public void testAnalisarComListaNula() {
        List<String> resultado = analisador.analisar(null);
        Assertions.assertNotNull(resultado);
        Assertions.assertTrue(resultado.isEmpty());
    }

    @Test
    public void testAnalisarComListaVazia() {
        List<String> resultado = analisador.analisar(Collections.emptyList());
        Assertions.assertTrue(resultado.isEmpty());
    }

    @Test
    public void testAnalisarIgnoraLinhasSemErro() {
        List<String> logs = Arrays.asList("[INFO] Operação sucesso", null, "");
        List<String> resultado = analisador.analisar(logs);
        Assertions.assertTrue(resultado.isEmpty());
    }
}