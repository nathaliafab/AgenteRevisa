### PMD Evaluation Report

Status: Parcial. Atingiu max iterações (3).
Últimos achados observados:
```text
[WARN] Progressbar rendering conflicts with reporting to STDOUT. No progressbar will be shown. Try running with argument -r <file> to output the report to a file instead.
[WARN] This analysis could be faster, please consider using Incremental Analysis: https://docs.pmd-code.org/pmd-doc-7.24.0/pmd_userdocs_incremental_analysis.html
```

Foram feitas 2 tentativas de correção no código.

### Passos Intermediarios

#### Iteracao 1
Achados:
```text
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:11:	UnusedPrivateField:	Avoid unused private fields such as 'nomeUsuario'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:12:	UnusedPrivateField:	Avoid unused private fields such as 'emailUsuario'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:13:	UnusedPrivateField:	Avoid unused private fields such as 'senhaUsuario'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:14:	UnusedPrivateField:	Avoid unused private fields such as 'cpfUsuario'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:15:	UnusedPrivateField:	Avoid unused private fields such as 'enderecoUsuario'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:16:	UnusedPrivateField:	Avoid unused private fields such as 'telefoneUsuario'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:17:	UnusedPrivateField:	Avoid unused private fields such as 'dataNascimentoUsuario'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:18:	UnusedPrivateField:	Avoid unused private fields such as 'statusUsuarioAtivo'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:19:	UnusedPrivateField:	Avoid unused private fields such as 'tokenSessao'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:20:	UnusedPrivateField:	Avoid unused private fields such as 'dataUltimoLogin'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:25:	UnusedPrivateField:	Avoid unused private fields such as 'numeroCartao'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:26:	UnusedPrivateField:	Avoid unused private fields such as 'codigoSeguranca'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:27:	UnusedPrivateField:	Avoid unused private fields such as 'validadeCartao'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:28:	UnusedPrivateField:	Avoid unused private fields such as 'nomeTitularCartao'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:30:	UnusedPrivateField:	Avoid unused private fields such as 'limiteCredito'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:31:	UnusedPrivateField:	Avoid unused private fields such as 'statusPagamentoAtual'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:32:	UnusedPrivateField:	Avoid unused private fields such as 'dataVencimentoFatura'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:34:	UnusedPrivateField:	Avoid unused private fields such as 'descontosAcumulados'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:39:	UnusedPrivateField:	Avoid unused private fields such as 'servidorSmtp'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:40:	UnusedPrivateField:	Avoid unused private fields such as 'portaSmtp'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:41:	UnusedPrivateField:	Avoid unused private fields such as 'usuarioSmtp'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:42:	UnusedPrivateField:	Avoid unused private fields such as 'senhaSmtp'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:44:	UnusedPrivateField:	Avoid unused private fields such as 'templateEmailCobranca'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:46:	UnusedPrivateField:	Avoid unused private fields such as 'remetenteEmailSistema'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:47:	UnusedPrivateField:	Avoid unused private fields such as 'ccoEmailAdmin'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:48:	UnusedPrivateField:	Avoid unused private fields such as 'anexosPendentesEmail'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:54:	UnusedPrivateField:	Avoid unused private fields such as 'serieNotaFiscal'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:56:	UnusedPrivateField:	Avoid unused private fields such as 'inscricaoEstadualEmpresa'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:58:	UnusedPrivateField:	Avoid unused private fields such as 'valorImpostosCalculados'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:59:	UnusedPrivateField:	Avoid unused private fields such as 'descricaoProdutosServicos'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:60:	UnusedPrivateField:	Avoid unused private fields such as 'dataEmissaoNotaAtual'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:61:	SingularField:	Perhaps 'chaveAcessoSefaz' could be replaced by a local variable.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:62:	UnusedPrivateField:	Avoid unused private fields such as 'protocoloAutorizacao'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:63:	UnusedPrivateField:	Avoid unused private fields such as 'statusSefazAtual'.
/tmp/tmp_0os7wrf/GerenciadorSistemaTotal.java:87:	UnusedLocalVariable:	Avoid unused local variables such as 'mensagemFormatada'.
[WARN] Progressbar rendering conflicts with reporting to STDOUT. No progressbar will be shown. Try running with argument -r <file> to output the report to a file instead.
[WARN] This analysis could be faster, please consider using Incremental Analysis: https://docs.pmd-code.org/pmd-doc-7.24.0/pmd_userdocs_incremental_analysis.html
```
Codigo Gerado:
```java
package com.ficticio.pmd;

import java.util.Date;

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
}

class Faturamento {
    private double saldoConta;
    private double taxaJurosAtraso;
    private String statusPagamento;

    public void processarFaturamento(double valorFatura) {
        if (this.saldoConta >= valorFatura) {
            this.saldoConta -= valorFatura;
            this.statusPagamento = "PAGO";
        } else {
            this.statusPagamento = "INADIMPLENTE";
            this.saldoConta -= (valorFatura + (valorFatura * this.taxaJurosAtraso));
        }
    }
}

class NotaFiscalService {
    private long numeroNotaFiscalAtual;
    private String cnpjEmissor;
    private double valorTotalUltimaNota;

    public String emitirNotaFiscal() {
        this.numeroNotaFiscalAtual++;
        String chaveAcesso = "3523" + System.currentTimeMillis() + this.cnpjEmissor;
        return "Nota " + this.numeroNotaFiscalAtual + " emitida com sucesso. Chave: " + chaveAcesso;
    }
}
```

#### Iteracao 2
Achados:
```text
/tmp/tmpcwrqdpmu/Usuario.java:3:	UnnecessaryImport:	Unused import 'java.util.Date'
/tmp/tmpcwrqdpmu/Usuario.java:6:	UnusedPrivateField:	Avoid unused private fields such as 'nome'.
/tmp/tmpcwrqdpmu/Usuario.java:7:	UnusedPrivateField:	Avoid unused private fields such as 'email'.
/tmp/tmpcwrqdpmu/Usuario.java:8:	UnusedPrivateField:	Avoid unused private fields such as 'cpf'.
/tmp/tmpcwrqdpmu/Usuario.java:9:	UnusedPrivateField:	Avoid unused private fields such as 'statusAtivo'.
/tmp/tmpcwrqdpmu/Usuario.java:22:	UnusedPrivateField:	Avoid unused private fields such as 'statusPagamento'.
/tmp/tmpcwrqdpmu/Usuario.java:38:	UnusedPrivateField:	Avoid unused private fields such as 'valorTotalUltimaNota'.
[WARN] Progressbar rendering conflicts with reporting to STDOUT. No progressbar will be shown. Try running with argument -r <file> to output the report to a file instead.
[WARN] This analysis could be faster, please consider using Incremental Analysis: https://docs.pmd-code.org/pmd-doc-7.24.0/pmd_userdocs_incremental_analysis.html
```
Codigo Gerado:
```java
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
```


### Final Code

```java
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
```
