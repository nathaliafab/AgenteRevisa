package com.usermanagement.processor;

import java.util.Locale;

/**
 * Processador de textos para gestão de usuários.
 */
public class ProcessadorDeTextos {

  public String formatarNome(String nome) {
    if (nome == null) {
      return "";
    }

    String nomeProcessado = nome.trim().toUpperCase(Locale.ROOT);

    if (nomeProcessado.isEmpty()) {
      return "NOME VAZIO";
    }

    return nomeProcessado;
  }
}