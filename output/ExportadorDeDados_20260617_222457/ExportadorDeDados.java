package br.com.dataset.spotbugs;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Classe responsavel pela exportacao de dados com foco em seguranca.
 */
public class ExportadorDados implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String nomeArquivo;
  private final transient InputStream fluxoDados;

  public ExportadorDados(String nomeArquivo, InputStream fluxoDados) {
    this.nomeArquivo = Objects.requireNonNull(nomeArquivo, "Nome do arquivo nao pode ser nulo");
    this.fluxoDados = fluxoDados;
  }

  /**
   * Limpa e exporta os dados de forma segura, prevenindo Path Traversal.
   *
   * @param conteudo Conteudo para processamento
   * @throws IOException Caso ocorra erro de I/O
   */
  public void limparEExportar(String conteudo) throws IOException {
    Objects.requireNonNull(conteudo, "Conteudo nao pode ser nulo");

    Path caminhoBase = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
    Path caminhoDestino = caminhoBase.resolve(nomeArquivo).normalize();

    if (!caminhoDestino.startsWith(caminhoBase)) {
      throw new SecurityException("Tentativa de acesso a caminho invalido: " + nomeArquivo);
    }

    if (Files.exists(caminhoDestino)) {
      Files.delete(caminhoDestino);
    }

    if (fluxoDados != null) {
      // O uso de try-with-resources garante o fechamento do stream
      try (InputStream inputStream = fluxoDados) {
        byte[] buffer = inputStream.readAllBytes();
        // Logica de processamento do buffer omitida por brevidade
      }
    }
  }
}