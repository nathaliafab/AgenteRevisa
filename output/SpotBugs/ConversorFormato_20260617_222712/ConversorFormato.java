package br.com.dataset.spotbugs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConversorFormato {

  /**
   * Transforma uma Collection em ArrayList.
   * Se a entrada já for ArrayList, retorna a instância original para performance.
   * Caso contrário, cria uma nova lista para evitar ClassCastException.
   */
  public List<Object> transformarParaLista(Collection<Object> dados) {
    if (dados == null) {
      return null;
    }

    if (dados instanceof ArrayList) {
      return (ArrayList<Object>) dados;
    }

    return new ArrayList<>(dados);
  }
}