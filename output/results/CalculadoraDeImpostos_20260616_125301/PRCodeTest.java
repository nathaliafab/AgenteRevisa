import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import com.ficticio.pmd.CalculadoraDeImpostos;

public class PRCodeTest {

    @Test
    public void testCalcularImpostoTotalSP() {
        CalculadoraDeImpostos calc = new CalculadoraDeImpostos();
        // Base(100*0.10=10) + AdicionalSP(100*0.08=8) + Valor(100) = 118.0
        double resultado = calc.calcularImpostoTotal(100.0, "SP");
        Assertions.assertEquals(118.0, resultado, 0.001);
    }

    @Test
    public void testCalcularImpostoTotalRJ() {
        CalculadoraDeImpostos calc = new CalculadoraDeImpostos();
        // Base(100*0.10=10) + AdicionalRJ(100*0.12=12) + Valor(100) = 122.0
        double resultado = calc.calcularImpostoTotal(100.0, "RJ");
        Assertions.assertEquals(122.0, resultado, 0.001);
    }

    @Test
    public void testCalcularImpostoTotalOutroEstado() {
        CalculadoraDeImpostos calc = new CalculadoraDeImpostos();
        // Base(100*0.10=10) + AdicionalOutro(100*0.05=5) + Valor(100) = 115.0
        double resultado = calc.calcularImpostoTotal(100.0, "MG");
        Assertions.assertEquals(115.0, resultado, 0.001);
    }

    @Test
    public void testCaseInsensitiveEstado() {
        CalculadoraDeImpostos calc = new CalculadoraDeImpostos();
        double resultadoSpMinusculo = calc.calcularImpostoTotal(100.0, "sp");
        Assertions.assertEquals(118.0, resultadoSpMinusculo, 0.001);
    }
}