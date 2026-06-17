package br.com.dataset.spotbugs;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Classe responsável por cálculos estatísticos básicos.
 */
public class CalculadoraEstatistica {

  private final Random random = new SecureRandom();

  public boolean verificarMargemErro(double valorA, double valorB) {
    return Double.compare(valorA, valorB) == 0;
  }

  public int gerarIdLoteAleatorio(int valorMaximo) {
    if (valorMaximo <= 0) {
      throw new IllegalArgumentException("O valor maximo deve ser positivo.");
    }
    return random.nextInt(valorMaximo);
  }
}