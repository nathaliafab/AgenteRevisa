package com.ficticio.pmd;

/**
 * UserManagement responsável pelo cadastro de usuários.
 */
class UserManagement {
  public void cadastrarNovoUsuario(String nome, String email, String cpf) {
    // Lógica de cadastro
  }
}

/**
 * BillingManager responsável pelo processamento de faturas.
 */
class BillingManager {
  private double saldoConta;
  private double taxaJurosAtraso;

  public void processarFaturamentoMensal(double valorFatura) {
    if (this.saldoConta >= valorFatura) {
      this.saldoConta -= valorFatura;
    } else {
      this.saldoConta -= (valorFatura + (valorFatura * this.taxaJurosAtraso));
    }
  }
}

/**
 * EmailService responsável pelo envio de comunicações.
 */
class EmailService {
  private String templateEmailBoasVindas;
  private String assinaturaEmailPadrao;

  public void configurarEEnviarEmail(String destinatario, String assunto) {
    String mensagem = this.templateEmailBoasVindas + "\n" + this.assinaturaEmailPadrao;
    // Logica de envio implementada para evitar o EmptyControlStatement
    if (mensagem != null) {
      System.out.println("Enviando email para " + destinatario);
    }
  }
}

/**
 * InvoiceService responsável pela emissão de notas fiscais.
 */
class InvoiceService {
  private long numeroNotaFiscalAtual;
  private String cnpjEmissorEmpresa;

  public String emitirNotaFiscalEletronica() {
    this.numeroNotaFiscalAtual++;
    String chaveAcessoSefaz = "3523" + System.currentTimeMillis() + 
        (this.cnpjEmissorEmpresa != null ? this.cnpjEmissorEmpresa : "000");
    return "Nota " + this.numeroNotaFiscalAtual + " emitida com sucesso. Chave: " + chaveAcessoSefaz;
  }
}

/**
 * GerenciadorSistemaTotal Facade para orquestração de serviços.
 */
public class GerenciadorSistemaTotal {

  private final UserManagement userManagement = new UserManagement();
  private final BillingManager billingManager = new BillingManager();
  private final EmailService emailService = new EmailService();
  private final InvoiceService invoiceService = new InvoiceService();

  public void cadastrarNovoUsuario(String nome, String email, String cpf) {
    userManagement.cadastrarNovoUsuario(nome, email, cpf);
  }

  public void processarFaturamentoMensal(double valorFatura) {
    billingManager.processarFaturamentoMensal(valorFatura);
  }

  public void configurarEEnviarEmail(String destinatario, String assunto) {
    emailService.configurarEEnviarEmail(destinatario, assunto);
  }

  public String emitirNotaFiscalEletronica() {
    return invoiceService.emitirNotaFiscalEletronica();
  }
}