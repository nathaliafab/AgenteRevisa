package br.com.dataset.spotbugs;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Classe responsável pela exportação segura de dados.
 */
public class ExportadorDados implements Serializable {

  private static final long serialVersionUID = 1L;
  private static final Path BASE_DIRECTORY = Paths.get("/var/data/exports").toAbsolutePath();

  private final String nomeArquivo;
  private final transient InputStream fluxoDados;

  public ExportadorDados(String nomeArquivo, InputStream fluxoDados) {
    this.nomeArquivo = nomeArquivo;
    this.fluxoDados = fluxoDados;
  }

  /**
   * Realiza a limpeza e exportação segura dos dados.
   *
   * @param texto o conteúdo a ser processado
   * @throws IOException se ocorrer erro na escrita do arquivo
   */
  public void limparEExportar(String texto) throws IOException {
    if (texto == null) {
      return;
    }

    Path caminhoDestino = BASE_DIRECTORY.resolve(nomeArquivo).normalize();

    // Verificação de segurança contra Path Traversal
    if (!caminhoDestino.startsWith(BASE_DIRECTORY)) {
      throw new SecurityException("Tentativa de acesso a caminho inválido: " + nomeArquivo);
    }

    // Processamento e escrita segura
    if (fluxoDados != null) {
      Files.copy(fluxoDados, caminhoDestino, StandardCopyOption.REPLACE_EXISTING);
    }
  }
}