package com.example.checkstyle;

public class ConfiguracoesSistema {

    public static final double taxa_juros = 3.14159;
    public static final int Limite_tentativas = 5;
    public static final String formato_Padrao = "UTF-8";
    public static final long Tempo_espera = 30000;
    public static final int CodigoSistema = 1001;

    public void executar() {
        double resultado = 2 * taxa_juros * 5;
        System.out.println("Resultado: " + resultado);
    }
}
