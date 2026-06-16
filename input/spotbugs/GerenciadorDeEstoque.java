package br.com.dataset.spotbugs;

import java.util.HashMap;
import java.util.Map;

public class GerenciadorEstoque {

    private Map<Long, String> estoque = new HashMap<>();

    public void inicializarEstoque() {
        estoque.put(101L, "Notebook Dell");
        estoque.put(102L, "Teclado Mecânico");
    }

    public String buscarProduto(String idTexto) {
        return estoque.get(idTexto); 
    }

    public boolean verificarConsistencia() {
        return estoque.containsAll(estoque); 
    }
}