package com.ficticio.pmd;

/**
 * Classe responsável pelo cálculo de impostos.
 */
public class CalculadoraDeImpostos {

    public double calcularImpostoTotal(double valorProduto, String tipoEstado) {
        double impostoBase = valorProduto * 0.10;
        double impostoAdicional;

        if ("SP".equalsIgnoreCase(tipoEstado)) {
            impostoAdicional = valorProduto * 0.08;
        } else if ("RJ".equalsIgnoreCase(tipoEstado)) {
            impostoAdicional = valorProduto * 0.12;
        } else {
            impostoAdicional = valorProduto * 0.05;
        }

        return valorProduto + impostoBase + impostoAdicional;
    }
}