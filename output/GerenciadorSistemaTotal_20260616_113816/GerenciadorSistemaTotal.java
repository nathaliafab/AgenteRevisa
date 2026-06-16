package com.ficticio.pmd;

public class Usuario {
    private String nome;
    private String email;
    private String cpf;
    private boolean statusAtivo;

    public void cadastrar(String nome, String email, String cpf) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.statusAtivo = true;
    }

    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getCpf() { return cpf; }
    public boolean isStatusAtivo() { return statusAtivo; }
}

class Faturamento {
    private double saldoConta;
    private double taxaJurosAtraso;

    public void processarFaturamento(double valorFatura) {
        if (this.saldoConta >= valorFatura) {
            this.saldoConta -= valorFatura;
        } else {
            this.saldoConta -= (valorFatura + (valorFatura * this.taxaJurosAtraso));
        }
    }
}

class NotaFiscalService {
    private long numeroNotaFiscalAtual;
    private String cnpjEmissor;

    public String emitirNotaFiscal() {
        this.numeroNotaFiscalAtual++;
        String chaveAcesso = "3523" + System.currentTimeMillis() + this.cnpjEmissor;
        return "Nota " + this.numeroNotaFiscalAtual + " emitida com sucesso. Chave: " + chaveAcesso;
    }
}