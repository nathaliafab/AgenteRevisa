package com.ficticio.pmd;

import java.io.IOException;

/**
 * Comunicador responsavel pelo envio de mensagens ao RabbitMQ.
 */
public class ComunicadorRabbitMq {

    public static class MensagemNaoEnviadaException extends Exception {
        public MensagemNaoEnviadaException(String mensagem, Throwable causa) {
            super(mensagem, causa);
        }
    }

    public void publicarMensagem(String nomeFila, String conteudo) throws MensagemNaoEnviadaException {
        try {
            abrirConexaoEEnviar(nomeFila, conteudo);
            System.out.println("Mensagem enviada com sucesso para " + nomeFila);
        } catch (IOException e) {
            throw new MensagemNaoEnviadaException("Falha ao publicar mensagem na fila: " + nomeFila, e);
        }
    }

    private void abrirConexaoEEnviar(String fila, String mensagem) throws IOException {
        // Uso dos parâmetros para satisfazer o PMD e manter o contrato de erro
        throw new IOException("Connection refused to queue " + fila + " with content: " + mensagem);
    }
}