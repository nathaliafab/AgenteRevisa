package com.ficticio.pmd;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Cliente para integração com API externa.
 */
public class ClienteApiExterno {
  private static final String SERVIDOR_PARCEIRO_HOST = System.getProperty("api.partner.host", "localhost");
  private static final int PORTA_PADRAO = 8080;

  public String buscarDadosParceiro(String endpoint) {
    String urlCompleta = "http://" + SERVIDOR_PARCEIRO_HOST + ":" + PORTA_PADRAO + "/api/" + endpoint;

    try {
      URL url = new URL(urlCompleta);
      HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
      conexao.setRequestMethod("GET");
      conexao.setConnectTimeout(5000);

      int codigoResposta = conexao.getResponseCode();

      if (codigoResposta != HttpURLConnection.HTTP_OK) {
        throw new RuntimeException("Falha na API. O servidor retornou código: " + codigoResposta);
      }

      return "{ \"status\": \"sucesso\", \"dados\": \"Informações do parceiro\" }";

    } catch (IOException e) {
      throw new RuntimeException("Falha na API devido a erro de conexão ou rede", e);
    }
  }
}