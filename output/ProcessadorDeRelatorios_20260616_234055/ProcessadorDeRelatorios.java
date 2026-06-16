package br.com.dataset.spotbugs;

public class ProcessadorRelatorio {

    public void gerarRelatorioFinanceiro() {
        String dadosRelatorio = "";
        int contadorIteracoes = 0;

        while (contadorIteracoes < 10) {
            System.out.println("Processando linha...");

            if (dadosRelatorio.isEmpty()) {
                System.out.println("Relatório vazio.");
            }
            
            contadorIteracoes++;
        }
    }
}