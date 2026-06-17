package com.example.checkstyle;

public class VerificadorFinanceiro {

    public void calcular(double base, double taxa) {
        double resultado = base * taxa / 100;
        System.out.println("Resultado: " + resultado);
    }

    public boolean verificar(String entrada) {
        return entrada.contains("@");
    }

    public void executar(String codigo, double valor) {
        // processa operacao
    }
}
