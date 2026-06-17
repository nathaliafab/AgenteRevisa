package com.example.checkstyle;

/**
 * Classe responsável por avaliar condições de usuários.
 */
public class AvaliadorCondicoes {

  /**
   * Avalia se o usuário atende aos requisitos necessários.
   *
   * @param idade idade do usuário
   * @param status status do usuário
   * @param saldo saldo do usuário
   * @param ativo indicador de atividade
   * @param categoria categoria do usuário
   */
  public void avaliar(int idade, String status, double saldo, boolean ativo, String categoria) {
    if (idade < 18) {
      return;
    }

    if (!"ativo".equals(status)) {
      return;
    }

    if (saldo <= 10000) {
      return;
    }

    if (!ativo) {
      return;
    }

    if ("A".equals(categoria) || "B".equals(categoria)) {
      System.out.println("Condicao satisfeita");
    }
  }
}