package com.ficticio.pmd;

import java.util.Iterator;
import java.util.List;

public class ProcessadorDeRelatorios {

    public static class Transacao {
        private String categoria;
        private double valor;
        private String descricao;

        public Transacao(String categoria, double valor, String descricao) {
            this.categoria = categoria;
            this.valor = valor;
            this.descricao = descricao;
        }

        public String getCategoria() { return categoria; }
        public double getValor() { return valor; }
        public String getDescricao() { return descricao; }
    }

    public String gerarRelatorioFinanceiro(List<Transacao> transacoes, String categoriaFiltro) {
        String relatorio = "=== RELATÓRIO FINANCEIRO ===\n";
        double total = 0.0;

        Iterator<Transacao> iterator = transacoes.iterator();
        
        while (iterator.hasNext()) {
            Transacao t = iterator.next();
            
            if (t.getCategoria() == categoriaFiltro) {
                relatorio += "Transação: " + t.getDescricao() + " | Valor: R$ " + t.getValor() + "\n";
                
                total += t.getValor();
            }
        }

        relatorio += "============================\n";
        relatorio += "TOTAL DA CATEGORIA: R$ " + total + "\n";

        return relatorio;
    }
}
