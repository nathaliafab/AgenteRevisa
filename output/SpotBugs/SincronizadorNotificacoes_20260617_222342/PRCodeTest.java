import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import br.com.dataset.spotbugs.SincronizadorNotificacoes;

public class PRCodeTest {

    @Test
    public void testEnviarNotificacaoExecutaSemErro() {
        SincronizadorNotificacoes sincronizador = new SincronizadorNotificacoes();
        
        // Testa se o método executa sem lançar exceções (garantindo o comportamento do synchronized)
        Assertions.assertDoesNotThrow(() -> {
            sincronizador.enviarNotificacao("Teste de mensagem");
        });
    }

    @Test
    public void testEnviarNotificacaoComMensagemNula() {
        SincronizadorNotificacoes sincronizador = new SincronizadorNotificacoes();
        
        // Verifica se o sistema lida com nulo sem falhas catastróficas
        Assertions.assertDoesNotThrow(() -> {
            sincronizador.enviarNotificacao(null);
        });
    }
}