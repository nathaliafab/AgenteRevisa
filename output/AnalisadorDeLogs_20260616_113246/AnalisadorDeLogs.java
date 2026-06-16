package com.ficticio.pmd;

import java.util.ArrayList;
import java.util.List;

/**
 * AnalisadorDeLogs responsavel por processar linhas de log.
 */
public class AnalisadorDeLogs {

    public List<String> analisar(List<String> linhasLog) {
        List<String> linhasComErro = new ArrayList<>();

        if (linhasLog == null) {
            return linhasComErro;
        }

        for (String linha : linhasLog) {
            if (linha != null && linha.contains("[ERROR]")) {
                linhasComErro.add(linha);
            }
        }

        return linhasComErro;
    }
}