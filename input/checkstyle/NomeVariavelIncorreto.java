package com.example.checkstyle;

public class ApuradorValores {

    public void apurar() {
        double Valor = 5000.0;
        double Indice = 0.15;
        double Total_Apurado = Valor * (1 - Indice);
        System.out.println("Apurado: " + Total_Apurado);
    }

    public void executar() {
        String Codigo = "ABC";
        int Contagem = 100;
        boolean Esta_Ativo = true;
        System.out.println(Codigo + " possui " + Contagem + " registros");
    }
}
