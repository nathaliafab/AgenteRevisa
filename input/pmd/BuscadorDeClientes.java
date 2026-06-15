package com.ficticio.pmd;

import java.util.ArrayList;
import java.util.List;

public class BuscadorDeClientes {

    public static class Cliente {
        private String nome;
        private String email;

        public Cliente(String nome, String email) {
            this.nome = nome;
            this.email = email;
        }

        public String getNome() { return nome; }
        public String getEmail() { return email; }
    }

    public List<Cliente> buscarClientesPorNome(String nomeBusca) {
        if (nomeBusca == null || nomeBusca.trim().isEmpty()) {
            return null;
        }

        List<Cliente> resultados = new ArrayList<>();

        if (nomeBusca.equalsIgnoreCase("Maria")) {
            resultados.add(new Cliente("Maria Silva", "maria@email.com"));
            return resultados;
        }

        return null;
    }

    public Cliente[] buscarArrayDeClientes(String nomeBusca) {
        if (nomeBusca == null || nomeBusca.trim().isEmpty()) {
            return null;
        }

        if (nomeBusca.equalsIgnoreCase("Jose")) {
            return new Cliente[] { new Cliente("Jose Santos", "jose@email.com") };
        }

        return null;
    }
}
