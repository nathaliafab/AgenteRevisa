package com.ficticio.pmd;

public class FormatadorDeMensagens {

    public String formatarErroBanco() {
        return "Falha interna do servidor: Erro de conexão com o banco de dados.";
    }

    public String formatarErroRede() {
        return "Falha interna do servidor: Tempo limite de requisição esgotado.";
    }

    public String formatarErroAutenticacao() {
        return "Falha interna do servidor: Serviço de tokens indisponível.";
    }

    public String formatarErroAutorizacao() {
        return "Falha interna do servidor: Falha ao validar permissões do usuário.";
    }

    public String formatarErroArquivo() {
        return "Falha interna do servidor: Espaço em disco insuficiente para salvar log.";
    }

    public String formatarErroValidacao() {
        return "Falha interna do servidor: Estrutura do payload JSON corrompida.";
    }

    public String formatarErroIntegracao() {
        return "Falha interna do servidor: API externa do parceiro não respondeu.";
    }

    public String formatarErroFaturamento() {
        return "Falha interna do servidor: Gateway de pagamento retornou erro 500.";
    }

    public String formatarErroMemoria() {
        return "Falha interna do servidor: Estouro de memória heap detectado.";
    }

    public String formatarErroHardware() {
        return "Falha interna do servidor: Superaquecimento ou falha física de nó.";
    }

    public String formatarErroDesconhecido() {
        return "Falha interna do servidor: Erro inesperado no thread principal.";
    }

    public String formatarErroCritico() {
        return "Falha interna do servidor: Falha geral do sistema, reiniciando...";
    }
}
