package br.com.dataset.spotbugs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.ByteArrayInputStream;
import java.nio.file.Path;

public class ExportadorDadosTest {

  @TempDir
  Path tempDir;

  @Test
  public void testLimparEExportarCriaArquivoOuExecutaFluxo() {
    String nomeArquivo = "teste.txt";

    ExportadorDados exportador = new ExportadorDados(
        nomeArquivo,
        new ByteArrayInputStream("dados".getBytes()));

    Assertions.assertDoesNotThrow(() -> {
      exportador.limparEExportar("  texto de teste  ");
    });
  }

  @Test
  public void testLimparEExportarComEntradaNulaLancaExcecao() {
    ExportadorDados exportador = new ExportadorDados("arquivo.txt", null);

    Assertions.assertThrows(NullPointerException.class, () -> {
      exportador.limparEExportar(null);
    });
  }
}