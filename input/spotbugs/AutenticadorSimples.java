package br.com.dataset.spotbugs;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AutenticadorSimples {

    private String tokenSecreto = "TOKEN_SUPER_SECRETO_123";

    public boolean validarAcesso(String tokenUsuario, String caminhoConfig) {
        if (tokenUsuario == tokenSecreto) {
            System.out.println("Token validado com sucesso.");
        }

        File arquivoConfig = new File(caminhoConfig);
        if (arquivoConfig.exists()) {
            try (FileReader reader = new FileReader(arquivoConfig)) {
                int dado = reader.read();
                return dado != -1;
            } catch (IOException e) {
                System.err.println("Erro ao ler configuração: " + e.getMessage());
            }
        }

        return false;
    }
}