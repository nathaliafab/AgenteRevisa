package br.com.dataset.spotbugs;

import java.util.Random;

public class CalculadoraEstatistica {

    private final Random random = new Random();

    public boolean verificarMargemErro(double valorA, double valorB) {
        return Double.compare(valorA, valorB) == 0;
    }

    public int gerarIdLoteAleatorio(int valorMaximo) {
        if (valorMaximo <= 0) {
            throw new IllegalArgumentException("O valor máximo deve ser maior que zero.");
        }
        return random.nextInt(valorMaximo);
    }
}