package com.example.checkstyle;

public class AvaliadorCondicoes {

    public void avaliar(int parametro1, String parametro2, double parametro3, boolean parametro4, String parametro5) {
        if (parametro1 >= 18) {
            if (parametro2.equals("ativo")) {
                if (parametro3 > 10000) {
                    if (parametro4) {
                        if (parametro5.equals("A") || parametro5.equals("B")) {
                            System.out.println("Condicao satisfeita");
                        }
                    }
                }
            }
        }
    }
}
