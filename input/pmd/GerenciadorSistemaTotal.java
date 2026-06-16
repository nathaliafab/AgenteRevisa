package com.ficticio.pmd;

import java.util.Date;
import java.util.List;

public class GerenciadorSistemaTotal {

    // ==========================================
    // ATRIBUTOS DE CADASTRO DE USUÁRIOS
    // ==========================================
    private String nomeUsuario;
    private String emailUsuario;
    private String senhaUsuario;
    private String cpfUsuario;
    private String enderecoUsuario;
    private String telefoneUsuario;
    private Date dataNascimentoUsuario;
    private boolean statusUsuarioAtivo;
    private String tokenSessao;
    private Date dataUltimoLogin;

    // ==========================================
    // ATRIBUTOS DE FATURAMENTO / PAGAMENTO
    // ==========================================
    private String numeroCartao;
    private String codigoSeguranca;
    private String validadeCartao;
    private String nomeTitularCartao;
    private double saldoConta;
    private double limiteCredito;
    private String statusPagamentoAtual;
    private Date dataVencimentoFatura;
    private double taxaJurosAtraso;
    private double descontosAcumulados;

    // ==========================================
    // ATRIBUTOS DE CONFIGURAÇÃO DE E-MAIL
    // ==========================================
    private String servidorSmtp;
    private int portaSmtp;
    private String usuarioSmtp;
    private String senhaSmtp;
    private String templateEmailBoasVindas;
    private String templateEmailCobranca;
    private String assinaturaEmailPadrão;
    private String remetenteEmailSistema;
    private String ccoEmailAdmin;
    private List<String> anexosPendentesEmail;

    // ==========================================
    // ATRIBUTOS DE EMISSÃO DE NOTA FISCAL (NFe)
    // ==========================================
    private long numeroNotaFiscalAtual;
    private String serieNotaFiscal;
    private String cnpjEmissorEmpresa;
    private String inscricaoEstadualEmpresa;
    private double valorTotalUltimaNota;
    private double valorImpostosCalculados;
    private String descricaoProdutosServicos;
    private Date dataEmissaoNotaAtual;
    private String chaveAcessoSefaz;
    private String protocoloAutorizacao;
    private String statusSefazAtual;

    // ==========================================
    // MÉTODOS
    // ==========================================

    public void cadastrarNovoUsuario(String nome, String email, String cpf) {
        this.nomeUsuario = nome;
        this.emailUsuario = email;
        this.cpfUsuario = cpf;
        this.statusUsuarioAtivo = true;
    }

    public void processarFaturamentoMensal(double valorFatura) {
        if (this.saldoConta >= valorFatura) {
            this.saldoConta -= valorFatura;
            this.statusPagamentoAtual = "PAGO";
        } else {
            this.statusPagamentoAtual = "INADIMPLENTE";
            this.saldoConta -= (valorFatura + (valorFatura * this.taxaJurosAtraso));
        }
    }

    public void configurarEEnviarEmail(String destinatario, String assunto) {
        String mensagemFormatada = this.templateEmailBoasVindas + "\n" + this.assinaturaEmailPadrão;
    }

    public String emitirNotaFiscalEletronica() {
        this.numeroNotaFiscalAtual++;
        this.dataEmissaoNotaAtual = new Date();
        this.valorImpostosCalculados = this.valorTotalUltimaNota * 0.15;
        this.statusSefazAtual = "AUTORIZADA";
        this.chaveAcessoSefaz = gerarChaveAcessoFicticia();
        
        return "Nota " + this.numeroNotaFiscalAtual + " emitida com sucesso. Chave: " + this.chaveAcessoSefaz;
    }

    private String gerarChaveAcessoFicticia() {
        return "3523" + System.currentTimeMillis() + this.cnpjEmissorEmpresa;
    }
}
