package br.com.dataset.spotbugs;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class AutenticadorSimples {

  private final String tokenSecreto = System.getenv().getOrDefault("APP_TOKEN", "TOKEN_SUPER_SECRETO_123");

  public boolean validarAcesso(String tokenUsuario, String caminhoConfig) {
    if (tokenUsuario != null && MessageDigest.isEqual(tokenUsuario.getBytes(StandardCharsets.UTF_8), 
                                                     tokenSecreto.getBytes(StandardCharsets.UTF_8))) {
      System.out.println("Token validado com sucesso.");
    } else {
      return false;
    }

    Path path = Paths.get(caminhoConfig);
    if (Files.exists(path)) {
      try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
        return reader.read() != -1;
      } catch (IOException e) {
        System.err.println("Erro ao ler configuracao: " + e.getMessage());
      }
    }

    return false;
  }
}