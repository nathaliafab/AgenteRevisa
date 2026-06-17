import com.ficticio.pmd.ComunicadorRabbitMQ;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class PRCodeTest {

    @Test
    public void testPublicarMensagemDeveLancarExcecaoQuandoConexaoFalhar() {
        ComunicadorRabbitMQ comunicador = new ComunicadorRabbitMQ();
        
        Assertions.assertThrows(ComunicadorRabbitMQ.MensagemNaoEnviadaException.class, () -> {
            comunicador.publicarMensagem("fila-teste", "conteudo-teste");
        });
    }

    @Test
    public void testMensagemDaExcecaoDeveConterNomeDaFila() {
        ComunicadorRabbitMQ comunicador = new ComunicadorRabbitMQ();
        String fila = "fila-exemplo";
        
        ComunicadorRabbitMQ.MensagemNaoEnviadaException exception = Assertions.assertThrows(
            ComunicadorRabbitMQ.MensagemNaoEnviadaException.class, 
            () -> comunicador.publicarMensagem(fila, "teste")
        );
        
        Assertions.assertTrue(exception.getMessage().contains(fila));
    }
}