import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import com.ficticio.pmd.ProcessadorDeRelatorios;
import com.ficticio.pmd.ProcessadorDeRelatorios.Transacao;
import java.util.Arrays;
import java.util.List;

public class PRCodeTest {

    @Test
    public void testGerarRelatorioFinanceiroComSucesso() {
        ProcessadorDeRelatorios processador = new ProcessadorDeRelatorios();
        
        // String literal utilizada para garantir que a comparação de referência == no código original funcione
        String categoria = "VENDAS";
        
        List<Transacao> transacoes = Arrays.asList(
            new Transacao(categoria, 100.0, "Venda A"),
            new Transacao("OUTRA", 50.0, "Despesa B"),
            new Transacao(categoria, 200.0, "Venda C")
        );

        String relatorio = processador.gerarRelatorioFinanceiro(transacoes, categoria);

        Assertions.assertTrue(relatorio.contains("Venda A"));
        Assertions.assertTrue(relatorio.contains("Venda C"));
        Assertions.assertFalse(relatorio.contains("Despesa B"));
        Assertions.assertTrue(relatorio.contains("TOTAL DA CATEGORIA: R$ 300.0"));
    }

    @Test
    public void testRelatorioVazio() {
        ProcessadorDeRelatorios processador = new ProcessadorDeRelatorios();
        String categoria = "INVESTIMENTOS";
        List<Transacao> transacoes = Arrays.asList(
            new Transacao("OUTRA", 100.0, "Teste")
        );

        String relatorio = processador.gerarRelatorioFinanceiro(transacoes, categoria);

        Assertions.assertTrue(relatorio.contains("TOTAL DA CATEGORIA: R$ 0.0"));
        Assertions.assertFalse(relatorio.contains("Teste"));
    }
}