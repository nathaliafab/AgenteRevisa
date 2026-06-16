package br.com.dataset.spotbugs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GerenciadorEstoque handles inventory operations.
 */
public class GerenciadorEstoque {

    private final Map<Long, String> estoque = new ConcurrentHashMap<>();

    public void inicializarEstoque() {
        estoque.put(101L, "Notebook Dell");
        estoque.put(102L, "Teclado Mecânico");
    }

    /**
     * Busca um produto pelo ID.
     * @param idTexto ID do produto em formato string.
     * @return Nome do produto ou null se não encontrado.
     */
    public String buscarProduto(String idTexto) {
        try {
            Long id = Long.parseLong(idTexto);
            return estoque.get(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Verifica a integridade do estoque.
     */
    public boolean verificarConsistencia() {
        return estoque != null;
    }
}