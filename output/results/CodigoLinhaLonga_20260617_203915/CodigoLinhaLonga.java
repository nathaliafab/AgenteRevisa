package com.example.checkstyle;

/**
 * Classe responsável pelo processamento e geração de relatórios de usuários.
 */
public class GeradorRelatorio {

  /**
   * Processa os dados de entrada e exibe no console.
   */
  public void processarEntrada(
      String campo1, String campo2, String campo3, String campo4, String campo5) {
    String resultado = "Entrada: " + campo1 + " " + campo2 + " Contato: " + campo3 
        + " Endereco: " + campo4 + " Telefone: " + campo5;
    System.out.println(resultado);
  }

  /**
   * Constrói uma string de saída consolidada com base em múltiplos valores numéricos.
   */
  public String construirSaida(int v1, int v2, int v3, int v4, int v5, int v6) {
    return "Saida consolidada com multiplos valores: v1=" + v1 + ", v2=" + v2 
        + ", v3=" + v3 + ", v4=" + v4 + ", v5=" + v5 + ", v6=" + v6;
  }
}