package com.ficticio.pmd;

import java.io.IOException;

/**
 * Comunicador responsavel pela integracao com RabbitMQ.
 */
public class ComunicadorRabbitMQ {

    public static class MensagemNaoEnviadaException extends Exception {
        public MensagemNaoEnviadaException(String mensagem, Throwable causa) {
            super(mensagem, causa);
        }
    }

    public void publicarMensagem(String nomeFila, String conteudo) 
            throws MensagemNaoEnviadaException {
        try {
            abrirConexaoEEnviar();
            System.out.println("Mensagem enviada com sucesso para " + nomeFila);
        } catch (IOException e) {
            throw new MensagemNaoEnviadaException(
                "Falha ao publicar mensagem na fila: " + nomeFila, e);
        }
    }

    private void abrirConexaoEEnviar() throws IOException {
        throw new IOException("Connection refused: connect to RabbitMQ broker no IP 10.0.0.100");
    }
}