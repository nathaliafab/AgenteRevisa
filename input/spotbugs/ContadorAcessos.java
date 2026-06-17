package br.com.dataset.spotbugs;

public class ContadorAcessos {

    private static int totalAcessosGlobais = 0;
    private String nomeUsuario;

    public ContadorAcessos(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public void registrarAcesso() {
        System.out.println("Usuário " + nomeUsuario + " realizou uma ação.");

        totalAcessosGlobais++; 
    }

    public static int getTotalAcessosGlobais() {
        return totalAcessosGlobais;
    }
}