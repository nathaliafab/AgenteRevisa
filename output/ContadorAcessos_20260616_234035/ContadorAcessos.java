package br.com.dataset.spotbugs;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Contador de acessos thread-safe utilizando AtomicInteger.
 */
public class ContadorAcessos {

    private static final AtomicInteger TotalAcessosGlobais = new AtomicInteger(0);
    private final String NomeUsuario;

    public ContadorAcessos(String nomeUsuario) {
        this.NomeUsuario = nomeUsuario;
    }

    public void registrarAcesso() {
        System.out.println("Usuário " + NomeUsuario + " realizou uma ação.");
        TotalAcessosGlobais.incrementAndGet();
    }

    public static int getTotalAcessosGlobais() {
        return TotalAcessosGlobais.get();
    }
}