package br.com.dataset.spotbugs;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

public class ExportadorDados implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String nomeArquivo;
    
    private InputStream fluxoDados; 

    public ExportadorDados(String nomeArquivo, InputStream fluxoDados) {
        this.nomeArquivo = nomeArquivo;
        this.fluxoDados = fluxoDados;
    }

    public void limparEExportar(String texto) {
        texto.trim(); 

        File arquivoAntigo = new File(nomeArquivo);

        arquivoAntigo.delete(); 
        
        System.out.println("Exportando dados para: " + nomeArquivo);
    }
}