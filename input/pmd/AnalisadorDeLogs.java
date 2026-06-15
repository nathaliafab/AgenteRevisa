package com.ficticio.pmd;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private String extrairPadraoErroRegexAntigo(String linha) {
        if (linha == null) return "";
        
        Pattern pattern = Pattern.compile("\\[ERROR\\] (.*)");
        Matcher matcher = pattern.matcher(linha);
        
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Padrão não encontrado";
    }

    private boolean validarFormatoDataLegado(String linha) {
        if (linha == null) return false;
        
        Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}.*");
        Matcher matcher = pattern.matcher(linha);
        
        return matcher.matches();
    }
}
