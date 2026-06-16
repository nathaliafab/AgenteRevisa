package com.ficticio.pmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SincronizadorDeArquivos {

    private final String diretorioLocal;
    private final String urlNuvem;

    public SincronizadorDeArquivos(String diretorioLocal, String urlNuvem) {
        this.diretorioLocal = diretorioLocal;
        this.urlNuvem = urlNuvem;
    }

    public void sincronizar() {
        System.out.println("Iniciando a sincronização do diretório: " + diretorioLocal);

        File pasta = new File(diretorioLocal);
        File[] arquivos = pasta.listFiles();

        if (arquivos == null) {
            System.out.println("Aviso: O diretório especificado é inválido ou está vazio.");
            return;
        }

        for (File arquivo : arquivos) {
            if (arquivo.isFile()) {
                try {
                    processarEEnviar(arquivo);
                    
                    System.out.println("Arquivo enviado com sucesso para a nuvem: " + arquivo.getName());
                    
                } catch (IOException e) { }
            }
        }

        System.out.println("Sincronização concluída com sucesso.");
    }

    private void processarEEnviar(File arquivo) throws IOException {
        if (arquivo.getName().contains("erro")) {
            throw new IOException("Erro simulado de leitura de disco.");
        }

        try (FileInputStream fis = new FileInputStream(arquivo)) {
            byte[] buffer = new byte[1024];
            int bytesLidos = fis.read(buffer);
        }
    }
}
