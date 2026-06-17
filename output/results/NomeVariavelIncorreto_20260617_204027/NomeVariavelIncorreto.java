package com.example.checkstyle;

/**
 * Classe responsável pelo processamento e apuração de valores.
 */
public class ApuradorValores {

  /**
   * Realiza a apuração de valores com base em um índice.
   */
  public void apurar() {
    double valor = 5000.0;
    double indice = 0.15;
    double totalApurado = valor * (1 - indice);
    System.out.println("Apurado: " + totalApurado);
  }

  /**
   * Executa a rotina de verificação de registros.
   */
  public void executar() {
    String codigo = "ABC";
    int contagem = 100;
    boolean estaAtivo = true;
    System.out.println(codigo + " possui " + contagem + " registros");
  }
}