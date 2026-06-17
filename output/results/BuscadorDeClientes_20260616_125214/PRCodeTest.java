import com.ficticio.pmd.BuscadorDeClientes;
import com.ficticio.pmd.BuscadorDeClientes.Cliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class PRCodeTest {

    private final BuscadorDeClientes buscador = new BuscadorDeClientes();

    @Test
    void testBuscarClientesPorNomeComNomeValido() {
        List<Cliente> resultado = buscador.buscarClientesPorNome("Maria");
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Maria Silva", resultado.get(0).getNome());
    }

    @Test
    void testBuscarClientesPorNomeComNomeInexistente() {
        List<Cliente> resultado = buscador.buscarClientesPorNome("Inexistente");
        Assertions.assertNotNull(resultado);
        Assertions.assertTrue(resultado.isEmpty());
    }

    @Test
    void testBuscarClientesPorNomeComEntradaInvalida() {
        Assertions.assertTrue(buscador.buscarClientesPorNome(null).isEmpty());
        Assertions.assertTrue(buscador.buscarClientesPorNome("").isEmpty());
        Assertions.assertTrue(buscador.buscarClientesPorNome("   ").isEmpty());
    }

    @Test
    void testBuscarArrayDeClientesComNomeValido() {
        Cliente[] resultado = buscador.buscarArrayDeClientes("Jose");
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.length);
        Assertions.assertEquals("Jose Santos", resultado[0].getNome());
    }

    @Test
    void testBuscarArrayDeClientesComNomeInexistente() {
        Cliente[] resultado = buscador.buscarArrayDeClientes("Joao");
        Assertions.assertEquals(0, resultado.length);
    }

    @Test
    void testBuscarArrayDeClientesComEntradaInvalida() {
        Assertions.assertEquals(0, buscador.buscarArrayDeClientes(null).length);
        Assertions.assertEquals(0, buscador.buscarArrayDeClientes("").length);
        Assertions.assertEquals(0, buscador.buscarArrayDeClientes("   ").length);
    }
}