package br.com.dataset.spotbugs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConversorFormato {

    /**
     * Transforma uma colecao de dados em uma lista segura.
     * Segue o Google Java Style e evita casts inseguros.
     */
    public List<Object> transformarParaLista(Collection<Object> dados) {
        if (dados == null) {
            return new ArrayList<>();
        }
        
        return new ArrayList<>(dados);
    }
}