package br.com.dataset.spotbugs;

/**
 * Sincronizador de notificacoes com tratamento seguro de threads.
 */
public class SincronizadorNotificacoes {

  private static final Object LockInterno = new Object();

  public void enviarNotificacao(String mensagem) {
    synchronized (LockInterno) {
      System.out.println("Enviando de forma segura: " + mensagem);
    }
  }
}