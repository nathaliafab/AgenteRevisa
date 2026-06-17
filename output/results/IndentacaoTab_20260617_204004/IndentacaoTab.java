package com.example.checkstyle;

/**
 * ValidadorDados realiza verificações básicas de dados de entrada.
 */
public class ValidadorDados {

  /**
   * Executa a lógica de validação comparando entrada e saída.
   */
  public void executar() {
    int entrada = 10;
    int saida = 20;
    if (entrada < saida) {
      System.out.println("Condicao atendida");
      int total = entrada + saida;
      System.out.println("Total: " + total);
    }
  }
}