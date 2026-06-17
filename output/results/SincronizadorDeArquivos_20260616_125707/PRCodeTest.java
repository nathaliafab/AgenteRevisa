import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class PRCodeTest {

    @TempDir
    Path tempDir;

    @Test
    void testSincronizacaoComDiretorioInexistente() {
        SincronizadorDeArquivos sincronizador = new SincronizadorDeArquivos("caminho/invalido/inexistente", "url");
        assertDoesNotThrow(sincronizador::sincronizar);
    }

    @Test
    void testSincronizacaoComSucesso() throws IOException {
        File arquivo = tempDir.resolve("teste.txt").toFile();
        Files.writeString(arquivo.toPath(), "conteudo de teste");

        SincronizadorDeArquivos sincronizador = new SincronizadorDeArquivos(tempDir.toString(), "url");
        
        assertDoesNotThrow(sincronizador::sincronizar);
        assertTrue(arquivo.exists());
    }

    @Test
    void testSincronizacaoComArquivoDeErro() throws IOException {
        File arquivoErro = tempDir.resolve("arquivo_erro.txt").toFile();
        Files.writeString(arquivoErro.toPath(), "deve falhar");
        
        File arquivoNormal = tempDir.resolve("normal.txt").toFile();
        Files.writeString(arquivoNormal.toPath(), "deve processar");

        SincronizadorDeArquivos sincronizador = new SincronizadorDeArquivos(tempDir.toString(), "url");

        assertDoesNotThrow(sincronizador::sincronizar);
    }
}