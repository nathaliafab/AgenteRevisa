import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.Assertions;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class PRCodeTest {

    @Test
    public void testValidarAcessoComTokenCorretoEArquivoExistente(@TempDir Path tempDir) throws IOException {
        br.com.dataset.spotbugs.AutenticadorSimples autenticador = new br.com.dataset.spotbugs.AutenticadorSimples();
        File arquivo = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(arquivo)) {
            writer.write("dados");
        }

        String token = "TOKEN_SUPER_SECRETO_123";
        boolean resultado = autenticador.validarAcesso(token, arquivo.getAbsolutePath());
        
        Assertions.assertTrue(resultado, "Deveria retornar true com token correto e arquivo não vazio");
    }

    @Test
    public void testValidarAcessoComTokenIncorreto(@TempDir Path tempDir) throws IOException {
        br.com.dataset.spotbugs.AutenticadorSimples autenticador = new br.com.dataset.spotbugs.AutenticadorSimples();
        File arquivo = tempDir.resolve("config.txt").toFile();
        arquivo.createNewFile();

        boolean resultado = autenticador.validarAcesso("TOKEN_ERRADO", arquivo.getAbsolutePath());
        
        Assertions.assertFalse(resultado, "Deveria retornar false se o arquivo estiver vazio, mesmo com token correto ou incorreto");
    }

    @Test
    public void testValidarAcessoArquivoInexistente() {
        br.com.dataset.spotbugs.AutenticadorSimples autenticador = new br.com.dataset.spotbugs.AutenticadorSimples();
        
        boolean resultado = autenticador.validarAcesso("TOKEN_SUPER_SECRETO_123", "caminho/inexistente.txt");
        
        Assertions.assertFalse(resultado, "Deveria retornar false se o arquivo não existir");
    }
}