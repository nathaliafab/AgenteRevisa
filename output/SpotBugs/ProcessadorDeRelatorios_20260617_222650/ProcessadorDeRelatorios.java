package br.com.dataset.spotbugs;

public class ProcessadorRelatorio {

    public void gerarRelatorioFinanceiro() {
        String DadosRelatorio = "";
        int ContadorIteracoes = 0;

        while (ContadorIteracoes < 10) {
            System.out.println("Processando linha...");
            
            if (DadosRelatorio.isEmpty()) { 
                System.out.println("Relatório vazio.");
            }
            ContadorIteracoes++;
        }
    }
}