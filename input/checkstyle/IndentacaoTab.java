package com.example.checkstyle;

public class ValidadorDados {

	public void executar() {
		int entrada = 10;
		int saida = 20;
		if (entrada < saida) {
			System.out.println("Condicao atendida");
			int total = entrada + saida;
			System.out.println("Total: " + total);
		}
	}
}
