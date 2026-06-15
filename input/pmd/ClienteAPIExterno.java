package com.ficticio.pmd;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClienteAPIExterno {
    private static final String IP_SERVIDOR_PARCEIRO = "192.168.1.55";

    public String buscarDadosParceiro(String endpoint) {
        String urlCompleta = "http://" + IP_SERVIDOR_PARCEIRO + ":8080/api/" + endpoint;

        try {
            URL url = new URL(urlCompleta);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setConnectTimeout(5000);

            int codigoResposta = conexao.getResponseCode();

            if (codigoResposta != 200) {
                throw new RuntimeException("Falha na API. O servidor retornou código: " + codigoResposta);
            }

            return "{ \"status\": \"sucesso\", \"dados\": \"Informações do parceiro\" }";

        } catch (IOException e) {
            throw new RuntimeException("Falha na API devido a erro de conexão ou rede", e);
        }
    }
}
