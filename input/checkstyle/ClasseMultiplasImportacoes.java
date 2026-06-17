package com.example.checkstyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class GerenciadorRecursos {

    private ArrayList<String> lista1 = new ArrayList<>();
    private HashMap<String, String> mapa1 = new HashMap<>();
    private HashSet<Integer> conjunto1 = new HashSet<>();
    private LinkedList<Object> lista2 = new LinkedList<>();

    public void executar() {
        File arquivo = new File("dados.txt");
        System.out.println("Processando");
    }
}
