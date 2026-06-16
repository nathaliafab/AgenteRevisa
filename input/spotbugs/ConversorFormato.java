package br.com.dataset.spotbugs;

import java.util.Collection;
import java.util.ArrayList;

public class ConversorFormato {

    public ArrayList<Object> transformarParaLista(Collection<Object> dados) {
        
        ArrayList<Object> listaGarantida = (ArrayList<Object>) dados; 
        
        return listaGarantida;
    }
}