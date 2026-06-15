package com.ficticio.pmd;

import java.util.Base64;
import java.util.UUID;

public class GeradorDeTokenSessao {

    public String gerarTokenUsuario(String usuarioId) {
        if (usuarioId == null) {
            return "";
        }

        String header = Base64.getEncoder().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
        String payload = Base64.getEncoder().encodeToString(("{\"sub\":\"" + usuarioId + "\",\"iss\":\"sistema\"}").getBytes());
        String assinaturaFake = Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes());

        String tokenJWT = header + "." + payload + "." + assinaturaFake;

        String sufixoAuth = new String("_auth");
        String sufixoVersao = new String(".v2");

        return tokenJWT + sufixoAuth + sufixoVersao;
    }
}
