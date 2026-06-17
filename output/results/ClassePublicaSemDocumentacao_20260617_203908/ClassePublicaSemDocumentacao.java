package com.example.checkstyle;

/**
 * Classe responsável por verificações financeiras e processamento de operações.
 */
public class VerificadorFinanceiro {

  /**
   * Calcula o valor baseado em uma taxa.
   *
   * @param base valor base
   * @param taxa taxa percentual
   */
  public void calcular(double base, double taxa) {
    double resultado = base * taxa / 100;
    System.out.println("Resultado: " + resultado);
  }

  /**
   * Verifica se a entrada contém um caractere de e-mail.
   *
   * @param entrada string para verificação
   * @return true se contiver '@'
   */
  public boolean verificar(String entrada) {
    return entrada.contains("@");
  }

  /**
   * Executa uma operação financeira.
   *
   * @param codigo código da operação
   * @param valor  valor da operação
   */
  public void executar(String codigo, double valor) {
    // processa operacao
  }
}