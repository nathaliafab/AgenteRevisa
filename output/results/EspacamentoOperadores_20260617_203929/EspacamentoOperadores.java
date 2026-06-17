package com.example.checkstyle;

/**
 * Classe responsável por realizar operações matemáticas básicas.
 */
public class ComputadorValores {

  /**
   * Realiza cálculos básicos de valores predefinidos.
   */
  public void calcular() {
    int a = 10;
    int b = 20;
    int soma = a + b;
    int produto = a * b;
    int diferenca = a - b;
    boolean resultado = (a > 5) && (b < 30);

    if (a == 10) {
      System.out.println("a vale dez");
    }

    for (int i = 0; i < 10; i++) {
      soma += i;
    }
  }
}