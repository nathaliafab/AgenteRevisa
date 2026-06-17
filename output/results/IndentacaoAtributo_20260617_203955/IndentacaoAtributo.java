package com.example.checkstyle;

/**
 * Classe responsável pelas configurações do sistema.
 */
public class ConfiguracoesSistema {

  public static final double TAXA_JUROS = 3.14159;
  public static final int LIMITE_TENTATIVAS = 5;
  public static final String FORMATO_PADRAO = "UTF-8";
  public static final long TEMPO_ESPERA = 30000;
  public static final int CODIGO_SISTEMA = 1001;

  public void executar() {
    double resultado = 2 * TAXA_JUROS * 5;
    System.out.println("Resultado: " + resultado);
  }
}