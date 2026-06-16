package com.ficticio.pmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuscadorDeClientes {

    public static class Cliente {
        private final String nome;
        private final String email;

        public Cliente(String nome, String email) {
            this.nome = nome;
            this.email = email;
        }

        public String getNome() { return nome; }
        public String getEmail() { return email; }
    }

    public List<Cliente> buscarClientesPorNome(String nomeBusca) {
        if (nomeBusca == null || nomeBusca.trim().isEmpty()) {
            return Collections.emptyList();
        }

        if ("Maria".equalsIgnoreCase(nomeBusca)) {
            List<Cliente> resultados = new ArrayList<>();
            resultados.add(new Cliente("Maria Silva", "maria@email.com"));
            return resultados;
        }

        return Collections.emptyList();
    }

    public Cliente[] buscarArrayDeClientes(String nomeBusca) {
        if (nomeBusca == null || nomeBusca.trim().isEmpty()) {
            return new Cliente[0];
        }

        if ("Jose".equalsIgnoreCase(nomeBusca)) {
            return new Cliente[] { new Cliente("Jose Santos", "jose@email.com") };
        }

        return new Cliente[0];
    }
}