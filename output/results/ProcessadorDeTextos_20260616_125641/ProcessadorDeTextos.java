package com.usermanagement;

import java.util.Locale;

public class ProcessadorDeTextos {

  public String formatarNome(String nome) {
    if (nome == null || nome.isEmpty()) {
      return "NOME VAZIO";
    }

    String nomeFormatado = nome.trim().toUpperCase(Locale.ROOT);

    if (nomeFormatado.isEmpty()) {
      return "NOME VAZIO";
    }

    return nomeFormatado;
  }
}