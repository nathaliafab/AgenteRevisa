package br.com.dataset.spotbugs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Testes unitários para CalculadoraEstatistica.
 * Removido o modificador 'public' da classe para evitar conflito de compilação 
 * em ambientes onde o arquivo de teste não segue a estrutura nome-classe.
 */
class PrCodeTest {

    private final CalculadoraEstatistica calculadora = new CalculadoraEstatistica();

    @Test
    void testVerificarMargemErro() {
        Assertions.assertTrue(calculadora.verificarMargemErro(10.0, 10.0), "Deveria retornar true para valores iguais");
        Assertions.assertFalse(calculadora.verificarMargemErro(10.0, 10.1), "Deveria retornar false para valores diferentes");
    }

    @Test
    void testGerarIdLoteAleatorio() {
        int maximo = 100;
        int resultado = calculadora.gerarIdLoteAleatorio(maximo);
        
        Assertions.assertTrue(resultado >= 0, "O ID deve ser maior ou igual a 0");
        Assertions.assertTrue(resultado < maximo, "O ID deve ser menor que o valor máximo");
    }
}