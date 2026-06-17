# Checkstyle Problems Documentation

Este documento descreve os problemas detectados pelo Checkstyle (configuração `google_checks.xml`) nos arquivos de entrada, com suas respectivas descrições e soluções esperadas.

---

## 1. CodigoLinhaLonga.java

### Problemas Detectados:

#### LineLength
- **Descrição:** Múltiplas linhas ultrapassam o limite de 100 caracteres:
  - Linha da assinatura do método `processarEntrada` (118 caracteres).
  - Linha de concatenação interna do método `processarEntrada` (148 caracteres).
  - Linha da assinatura do método `construirSaida` (114 caracteres).
  - Linha de retorno do método `construirSaida` (191 caracteres).
- **Solução Esperada:** Quebrar as linhas longas em múltiplas linhas respeitando o limite de 100 caracteres, usando quebra de parâmetros e de expressões de concatenação.
- **Solução:** Quebrou as linhas corretamente. EXCELENTE.

#### MissingJavadocType
- **Descrição:** A classe não possui comentário Javadoc.
- **Solução Esperada:** Adicionar um comentário Javadoc à declaração da classe descrevendo seu propósito.
- **Solução:** Adicionou javadocs. EXCELENTE.

#### Indentation
- **Descrição:** Os membros da classe estão com nível de indentação incorreto (4 espaços, esperado 2 conforme Google Style).
- **Solução Esperada:** Reindenter o corpo da classe usando 2 espaços por nível de indentação, conforme o padrão Google Java Style.
- **Solução:** Ajustou a identação corretamente. EXCELENTE.

_obs: Geral: EXCELENTE._

---

## 2. ClassePublicaSemDocumentacao.java

### Problemas Detectados:

#### MissingJavadocType
- **Descrição:** A classe não possui comentário Javadoc. O Checkstyle (google_checks.xml) exige documentação no nível da classe pública.
- **Solução Esperada:** Adicionar um comentário Javadoc à declaração da classe descrevendo seu propósito.
- **Solução:** Adicionou javadoc na classe e nos métodos. EXCELENTE.

#### Indentation
- **Descrição:** Os membros da classe estão com nível de indentação incorreto (4 espaços, esperado 2 conforme Google Style).
- **Solução Esperada:** Reindenter o corpo da classe usando 2 espaços por nível de indentação, conforme o padrão Google Java Style.
- **Solução:** Ajustou a identação corretamente. EXCELENTE.

_obs: Geral: EXCELENTE._
---

## 3. MetodoPublicoSemDocumentacao.java

### Problemas Detectados:

#### MissingJavadocType
- **Descrição:** A classe não possui comentário Javadoc.
- **Solução Esperada:** Adicionar um comentário Javadoc à declaração da classe.
- **Solução:** Adicionou javadoc na classe e métodos. EXCELENTE.

#### MissingJavadocMethod
- **Descrição:** O método público `processar()` não possui comentário Javadoc.
- **Solução Esperada:** Adicionar um comentário Javadoc ao método descrevendo seu propósito.
- **Solução:** Adicionou javadoc no método. EXCELENTE.

#### Indentation
- **Descrição:** O corpo do método está com indentação de 4 espaços; o Google Style exige 2 espaços.
- **Solução Esperada:** Reindenter usando 2 espaços por nível.
- **Solução:** Ajustou identação. EXCELENTE.

_obs: O agente renomeou variáveis com nome pouco compreensível sem ser solicitado. Geral: EXCELENTE._

---

## 4. IndentacaoTab.java

### Problemas Detectados:

#### FileTabCharacter
- **Descrição:** Todas as linhas indentadas do arquivo utilizam caracteres de tabulação (`\t`) em vez de espaços. O Google Style proíbe o uso de tabs.
- **Solução Esperada:** Substituir todos os caracteres de tab por espaços (2 espaços por nível de indentação conforme Google Style).
- **Solução:** Substituiu as identações por espaços. EXCELENTE.

#### MissingJavadocType e MissingJavadocMethod
- **Descrição:** Tanto a classe quanto o método público `executar()` estão sem comentário Javadoc.
- **Solução Esperada:** Adicionar comentários Javadoc à classe e ao método.
- **Solução:** Adicionou os javadocs. EXCELENTE.

#### Indentation
- **Descrição:** Como a indentação é feita com tabs (interpretados como 8 espaços), todos os níveis ficam incorretos segundo o padrão de 2 espaços.
- **Solução Esperada:** Corrigido automaticamente ao substituir tabs por espaços.
- **Solução:** Corrigiu a identação. EXCELENTE.

_obs: Geral: EXCELENTE._

---

## 5. AssinaturaMetodoLonga.java

### Problemas Detectados:

#### LineLength
- **Descrição:** As assinaturas dos métodos ultrapassam 100 caracteres:
  - `registrar(...)` com 9 parâmetros: 173 caracteres.
  - `calcular(...)` com 7 parâmetros: 166 caracteres.
- **Solução Esperada:** Refatorar os métodos para reduzir o tamanho das assinaturas, usando classes de transferência de dados (DTOs) ou objetos de configuração para agrupar parâmetros relacionados.
- **Solução:** Quebrou as linhas, mas removeu parâmetros não usados sem ser solicitado. RAZOAVEL.

#### MissingJavadocType
- **Descrição:** A classe não possui comentário Javadoc.
- **Solução Esperada:** Adicionar comentário Javadoc à classe.
- **Solução:** Adicionou javadoc para a classe e para todos os métodos. EXCELENTE.

_obs: o agente fez alterações que não foram solicitadas, como remover parâmetros não utilizados e renomear parâmetros com nomes sem significado. Geral: BOM._

---

## 6. NomeVariavelIncorreto.java

### Problemas Detectados:

#### LocalVariableName
- **Descrição:** Variáveis locais não obedecem ao padrão `^[a-z]([a-z0-9][a-zA-Z0-9]*)?$` exigido pelo Google Style:
  - `Valor` (deve iniciar com minúscula: `valor`)
  - `Indice` (deve iniciar com minúscula: `indice`)
  - `Total_Apurado` (contém underscore e maiúscula: deve ser `totalApurado`)
  - `Codigo` (deve iniciar com minúscula: `codigo`)
  - `Contagem` (deve iniciar com minúscula: `contagem`)
  - `Esta_Ativo` (contém underscore e maiúscula: deve ser `estaAtivo`)
- **Solução Esperada:** Renomear todas as variáveis locais para camelCase com inicial minúscula, sem underscores.
- **Solução:** Renomeou corretamente. EXCELENTE.

#### MissingJavadocType e MissingJavadocMethod
- **Descrição:** Classe e métodos públicos sem comentário Javadoc.
- **Solução Esperada:** Adicionar comentários Javadoc à classe e a cada método.
- **Solução:** Adicionou javadocs. EXCELENTE.

_obs: Geral: EXCELENTE._

---

## 7. IndentacaoAtributo.java

### Problemas Detectados:

#### MissingJavadocType
- **Descrição:** A classe não possui comentário Javadoc.
- **Solução Esperada:** Adicionar comentário Javadoc à classe.
- **Solução:** Adicionou javadoc. EXCELENTE.

#### Indentation
- **Descrição:** Todos os membros da classe (campos estáticos e método) estão com indentação de 4 espaços; o Google Style exige 2.
- **Solução Esperada:** Reindenter usando 2 espaços por nível.
- **Solução:**  Ajustou identação. EXCELENTE.

> **Nota:** A regra `ConstantName` do `google_checks.xml` não disparou avisos para as constantes com nomes incorretos. Os problemas reportados pelo Checkstyle para este arquivo são `MissingJavadocType` e `Indentation`.

_obs: Mesmo sem o output do checkstyle, o agente ajustou o nome das contantes apenas pelo contexto. Geral: EXCELENTE._

---

## 8. MetodoLinhaLonga.java

### Problemas Detectados:

#### LineLength
- **Descrição:** A assinatura do método `avaliar(...)` com 5 parâmetros resulta em uma linha de 112 caracteres, acima do limite de 100.
- **Solução Esperada:** Quebrar a assinatura do método em múltiplas linhas.
- **Solução:** Renomeou os parâmetros para nomes relevantes (mas sem base nenhuma), e acabou resolvendo o tamanho da linha. RUIM.

#### MissingJavadocType e MissingJavadocMethod
- **Descrição:** Classe e método público sem comentário Javadoc.
- **Solução Esperada:** Adicionar comentários Javadoc à classe e ao método.
- **Solução:** Adicionou javadoc, mas baseado nas mudanças dos nomes. RUIM.

#### Indentation
- **Descrição:** Todos os níveis de indentação (método e `if`s aninhados) estão com 4 espaços; o padrão Google exige 2.
- **Solução Esperada:** Reindenter usando 2 espaços por nível.
- **Solução:** Ajustou identação. EXCELENTE.

> **Nota:** O checkstyle não reportou problemas relacionados ao aninhamento dos if's.

_obs: O agente renomeou parâmetros e criou os javadocs baseado numa lógica inventada. Além disso, alterou o aninhamento das condições com o uso de cláusulas de return mesmo sem um report do checkstyle. Geral: RAZOAVEL (tendendo para RUIM)._

---

## 9. ClasseMultiplasImportacoes.java

### Problemas Detectados:

#### MissingJavadocType
- **Descrição:** A classe não possui comentário Javadoc.
- **Solução Esperada:** Adicionar comentário Javadoc à classe.
- **Solução:** Adicionou javadoc. EXCELENTE.

#### Indentation
- **Descrição:** Campos e método da classe com indentação de 4 espaços; o padrão Google exige 2.
- **Solução Esperada:** Reindenter usando 2 espaços por nível.
- **Solução:** Ajustou identação. EXCELENTE. 

> **Nota:** O checkstyle não reportou problemas com as importações.

_obs: O agente removeu importações não utilizadas mesmo sem um report do checkstyle. Geral: EXCELENTE._

---

## 10. EspacamentoOperadores.java

### Problemas Detectados:

#### WhitespaceAround
- **Descrição:** Operadores sem espaços ao redor em todo o método `calcular()`:
  - Atribuições: `a=10`, `b=20`, `soma=a+b`, `produto=a*b`, `diferenca=a-b`
  - Operadores aritméticos sem espaço: `a+b`, `a*b`, `a-b`
  - Comparações: `a>5`, `b<30`, `a==10`, `i<10`
  - Atribuição composta: `soma+=i`
- **Solução Esperada:** Adicionar um espaço antes e depois de cada operador (`=`, `+`, `-`, `*`, `>`, `<`, `==`, `+=`).
- **Solução:** Adicionou espaços corretamente. EXCELENTE.

#### MissingJavadocType e MissingJavadocMethod
- **Descrição:** Classe e método público sem comentário Javadoc.
- **Solução Esperada:** Adicionar comentários Javadoc à classe e ao método.
- **Solução:** Adicionou javadoc. EXCELENTE.

_obs: Geral: EXCELENTE._

---

## Resumo dos Problemas

| # | Arquivo | Regra Principal Detectada | Regra Secundária |
|---|---------|--------------------------|-----------------|
| 1 | CodigoLinhaLonga.java | `LineLength` | `MissingJavadocType` |
| 2 | ClassePublicaSemDocumentacao.java | `MissingJavadocType` | `Indentation` |
| 3 | MetodoPublicoSemDocumentacao.java | `MissingJavadocMethod` | `Indentation` |
| 4 | IndentacaoTab.java | `FileTabCharacter` | `Indentation` |
| 5 | AssinaturaMetodoLonga.java | `LineLength` | `MissingJavadocType` |
| 6 | NomeVariavelIncorreto.java | `LocalVariableName` | `MissingJavadocMethod` |
| 7 | IndentacaoAtributo.java | `MissingJavadocType` | `Indentation` |
| 8 | MetodoLinhaLonga.java | `LineLength` + `MissingJavadocMethod` | `Indentation` |
| 9 | ClasseMultiplasImportacoes.java | `MissingJavadocType` | `Indentation` |
| 10 | EspacamentoOperadores.java | `WhitespaceAround` | `MissingJavadocType` |

---

## Notas Importantes

- A configuração usada é `google_checks.xml`, embutida no JAR do Checkstyle (`checkstyle-10.12.7-all.jar`)
- Regras como `ParameterNumber`, `NestedIfDepth` e `ClassFanOutComplexity` **não fazem parte** do `google_checks.xml` e portanto não são detectadas
- A regra `Indentation` do Google Style exige **2 espaços** por nível (não 4), e dispara em praticamente todos os arquivos formatados com 4 espaços
- Nenhum desses problemas impede que o código compile ou funcione corretamente em runtime
