package com.ficticio.pmd;

public class ValidadorDeSenha {
    public boolean validarForcaSenha(String senha) {
        
        boolean senhaForte = false;

        if (senha != null) {
            if (senha.length() >= 8) {
                if (senha.matches(".*[A-Z].*")) {
                    if (senha.matches(".*[!@#$%^&*()].*")) {
                        senhaForte = true;
                    }
                }
            }
        }

        return senhaForte;
    }
}
