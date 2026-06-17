package br.com.dataset.spotbugs;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * GerenciadorEstoque responsible for handling inventory operations.
 */
public class GerenciadorEstoque {

    private final Map<Long, String> estoque = new HashMap<>();

    public void inicializarEstoque() {
        estoque.put(101L, "Notebook Dell");
        estoque.put(102L, "Teclado Mecânico");
    }

    /**
     * Finds a product by its ID string representation.
     */
    public String buscarProduto(String idTexto) {
        try {
            Long id = Long.valueOf(idTexto);
            return estoque.get(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Checks if the stock contains data.
     */
    public boolean verificarConsistencia() {
        return estoque != null && !estoque.isEmpty();
    }
}