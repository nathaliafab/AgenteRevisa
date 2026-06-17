package com.usermanagement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrCodeTest {

  @Test
  public void testFormatarNome() {
    ProcessadorDeTextos processador = new ProcessadorDeTextos();

    assertEquals("NOME VAZIO", processador.formatarNome(""), "Deve retornar NOME VAZIO para string vazia");
    assertEquals("JOAO", processador.formatarNome("joao"), "Deve retornar nome em maiúsculas e sem espaços");
    assertEquals("MARIA", processador.formatarNome("  maria  "), "Deve remover espaços e converter para maiúsculas");
  }
}