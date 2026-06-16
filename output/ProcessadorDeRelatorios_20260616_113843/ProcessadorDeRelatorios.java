package com.ficticio.pmd;

import java.util.List;

public class ProcessadorDeRelatorios {

    public static class Transacao {
        private final String categoria;
        private final double valor;
        private final String descricao;

        public Transacao(String categoria, double valor, String descricao) {
            this.categoria = categoria;
            this.valor = valor;
            this.descricao = descricao;
        }

        public String getCategoria() {
            return categoria;
        }

        public double getValor() {
            return valor;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public String gerarRelatorioFinanceiro(List<Transacao> transacoes, String categoriaFiltro) {
        StringBuilder relatorio = new StringBuilder("=== RELATÓRIO FINANCEIRO ===\n");
        double total = 0.0;

        for (Transacao transacao : transacoes) {
            if (transacao.getCategoria().equals(categoriaFiltro)) {
                relatorio.append("Transação: ")
                        .append(transacao.getDescricao())
                        .append(" | Valor: R$ ")
                        .append(transacao.getValor())
                        .append("\n");
                
                total += transacao.getValor();
            }
        }

        relatorio.append("============================\n")
                .append("TOTAL DA CATEGORIA: R$ ")
                .append(total)
                .append("\n");

        return relatorio.toString();
    }
}