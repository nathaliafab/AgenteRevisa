package br.com.dataset.spotbugs;

import java.util.Random;

public class CalculadoraEstatistica {

    private Random random = new Random();

    public boolean verificarMargemErro(double valorA, double valorB) {
        return valorA == valorB;
    }

    public int gerarIdLoteAleatorio(int valorMaximo) {
        return (int) (random.nextDouble() * valorMaximo);
    }
}