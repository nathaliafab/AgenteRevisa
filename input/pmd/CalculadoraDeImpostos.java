package com.ficticio.pmd;

public class CalculadoraDeImpostos {

    public double calcularImpostoTotal(double valorProduto, String tipoEstado) {
        double impostoBase = valorProduto * 0.10;
        double impostoAdicional = 0.0;
        double aliquotaCofinsAntiga = 0.075;
        double aliquotaPisDiferenciada = 0.0165;
        double fatorReducaoIcmsRegiao = 0.20;

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
