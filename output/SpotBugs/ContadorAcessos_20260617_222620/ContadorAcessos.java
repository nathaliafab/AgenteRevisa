package br.com.dataset.spotbugs;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ContadorAcessos gerencia o registro de acessos de forma thread-safe.
 */
public class ContadorAcessos {

  private static final AtomicInteger totalAcessosGlobais = new AtomicInteger(0);
  private final String nomeUsuario;

  public ContadorAcessos(String nomeUsuario) {
    this.nomeUsuario = nomeUsuario;
  }

  public void registrarAcesso() {
    System.out.println("Usuário " + nomeUsuario + " realizou uma ação.");
    totalAcessosGlobais.incrementAndGet();
  }

  public static int getTotalAcessosGlobais() {
    return totalAcessosGlobais.get();
  }
}