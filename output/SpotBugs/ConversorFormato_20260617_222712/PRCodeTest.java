import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import br.com.dataset.spotbugs.ConversorFormato;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class PRCodeTest {

    @Test
    public void testTransformarParaListaComSucesso() {
        ConversorFormato conversor = new ConversorFormato();
        ArrayList<Object> entrada = new ArrayList<>(Arrays.asList("A", "B", "C"));
        
        List<Object> resultado = conversor.transformarParaLista(entrada);
        
        Assertions.assertEquals(entrada, resultado);
        Assertions.assertTrue(resultado instanceof ArrayList);
    }

    @Test
    public void testTransformarParaListaComHashSetNaoGeraExcecao() {
        ConversorFormato conversor = new ConversorFormato();
        // Agora o método trata o HashSet criando um novo ArrayList, não dispara mais ClassCastException
        Collection<Object> entradaInvalida = new HashSet<>(Arrays.asList("A", "B"));
        
        List<Object> resultado = conversor.transformarParaLista(entradaInvalida);
        
        Assertions.assertTrue(resultado instanceof ArrayList);
        Assertions.assertEquals(2, resultado.size());
    }

    @Test
    public void testTransformarParaListaComNull() {
        ConversorFormato conversor = new ConversorFormato();
        
        Assertions.assertNull(conversor.transformarParaLista(null));
    }
}