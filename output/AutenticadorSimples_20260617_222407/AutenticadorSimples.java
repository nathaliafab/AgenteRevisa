package br.com.dataset.spotbugs;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutenticadorSimples {

    private static final Logger LOGGER = Logger.getLogger(AutenticadorSimples.class.getName());
    // Em um cenário real, utilize System.getenv("APP_TOKEN") para evitar hardcode
    private static final String TOKEN_SECRETO = "TOKEN_SUPER_SECRETO_123";

    public boolean validarAcesso(String tokenUsuario, String caminhoConfig) {
        if (!TOKEN_SECRETO.equals(tokenUsuario)) {
            return false;
        }

        Path path = Paths.get(caminhoConfig);
        if (Files.exists(path)) {
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                return reader.read() != -1;
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Erro ao ler configuracao: {0}", e.getMessage());
            }
        }

        return false;
    }
}