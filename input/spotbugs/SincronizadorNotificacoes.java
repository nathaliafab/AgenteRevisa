package br.com.dataset.spotbugs;

public class SincronizadorNotificacoes {

    private final String LOCK_INTERNO = "LOCK_DA_FILA";

    public void enviarNotificacao(String mensagem) {
        synchronized (LOCK_INTERNO) {
            System.out.println("Enviando de forma segura: " + mensagem);
        }
    }
}