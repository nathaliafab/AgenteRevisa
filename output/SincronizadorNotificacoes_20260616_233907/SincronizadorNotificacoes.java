package br.com.dataset.spotbugs;

/**
 * Sincronizador responsavel por gerenciar o envio de notificacoes.
 */
public class SincronizadorNotificacoes {

  private final Object lockInterno = new Object();

  public void enviarNotificacao(String mensagem) {
    synchronized (lockInterno) {
      System.out.println("Enviando de forma segura: " + mensagem);
    }
  }
}