package com.example.checkstyle;

/**
 * Classe responsável pelo gerenciamento e registro de entidades de usuário.
 */
public class RegistradorEntidade {

  /**
   * Registra uma nova entidade com base nos dados fornecidos.
   *
   * @param nome o nome da entidade
   * @param sobrenome o sobrenome da entidade
   */
  public void registrar(String nome, String sobrenome) {
    System.out.println("Registrando: " + nome + " " + sobrenome);
  }

  /**
   * Calcula um valor baseado em parâmetros de entrada.
   *
   * @param valorA operando a
   * @param valorB operando b
   * @param valorC operando c
   * @param valorD operando d
   * @param valorE operando e
   * @param valorF operando f
   * @param fator multiplicador g
   * @return resultado do cálculo
   */
  public double calcular(
      double valorA,
      double valorB,
      double valorC,
      double valorD,
      double valorE,
      double valorF,
      int fator) {
    return valorA * fator;
  }
}