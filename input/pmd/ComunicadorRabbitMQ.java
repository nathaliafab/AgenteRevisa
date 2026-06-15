package com.ficticio.pmd;

import java.io.IOException;

public class ComunicadorRabbitMQ {

    public static class MensagemNaoEnviadaException extends Exception {
        public MensagemNaoEnviadaException(String mensagem) {
            super(mensagem);
        }
    }

    public void publicarMensagem(String nomeFila, String conteudo) throws MensagemNaoEnviadaException {
        try {
            abrirConexaoEEnviar(nomeFila, conteudo);
            System.out.println("Mensagem enviada com sucesso para " + nomeFila);

        } catch (IOException e) {
            throw new MensagemNaoEnviadaException("Falha ao publicar mensagem na fila: " + nomeFila);
        }
    }

    private void abrirConexaoEEnviar(String fila, String mensagem) throws IOException {
        throw new IOException("Connection refused: connect to RabbitMQ broker no IP 10.0.0.100");
    }
}
