Primeiro, selecionei **20 problemas diferentes** (regras do PMD) que são muito comuns e abrangem categorias como Boas Práticas, Design, Performance e Tratamento de Erros:

1. **EmptyCatchBlock**: Bloco `catch` vazio (engole a exceção).
2. **SystemPrintln**: Uso de `System.out.println` no lugar de um framework de log profissional.
3. **GodClass**: Classe "Deus" que faz coisas demais e conhece muito sobre o sistema.
4. **TooManyFields**: Classe com excesso de atributos.
5. **CompareObjectsWithEquals**: Comparar objetos (como Strings) usando `==` em vez de `.equals()`.
6. **UseStringBufferForStringAppends**: Concatenar strings usando `+` dentro de um loop, o que degrada a performance.
7. **CloseResource**: Esquecer de fechar um recurso, como uma conexão de banco ou arquivo (`Connection`, `InputStream`).
8. **AvoidCatchingGenericException**: Capturar `Exception` ou `Throwable` de forma genérica em vez de exceções específicas.
9. **HardCodedIP**: Deixar endereços IP fixos (chumbados) diretamente no código fonte.
10. **AvoidThrowingRawExceptionTypes**: Lançar exceções genéricas como `new Exception()` ou `new RuntimeException()`.
11. **UnusedLocalVariable**: Declarar uma variável local e nunca utilizá-la.
12. **AvoidDuplicateLiterals**: Repetir a mesma string literal (ex: `"Erro de conexao"`) em várias partes do código.
13. **CollapsibleIfStatements**: Blocos `if` aninhados que poderiam ser combinados facilmente com um `&&`.
14. **CyclomaticComplexity**: Método muito complexo, cheio de `ifs`, `elses`, `fors` e `switches`.
15. **ReturnEmptyArrayRatherThanNull**: Retornar `null` em métodos que deveriam retornar arrays ou coleções, forçando quem chama a fazer verificação de nulo.
16. **MethodReturnsInternalArray**: Retornar a referência direta de um array interno, quebrando o encapsulamento.
17. **ConstantsInInterface**: Usar uma `interface` apenas para declarar constantes, em vez de definir comportamentos.
18. **UnusedPrivateMethod**: Método privado implementado que nunca é chamado em lugar nenhum da classe.
19. **StringInstantiation**: Instanciar uma string usando o construtor `new String("texto")` ao invés de usar o literal diretamente.
20. **PreserveStackTrace**: Lançar uma nova exceção dentro do `catch` esquecendo de repassar a exceção original, perdendo o rastro (stack trace) do erro.

Aqui está a sua lista de **15 classes fictícias**, distribuindo os 20 problemas sem nenhuma repetição:

**1- problema EmptyCatchBlock e SystemPrintln:** `SincronizadorDeArquivos` lê arquivos de um diretório e os envia para a nuvem. Ela engole erros de leitura (catch vazio) para não travar o processo e imprime o progresso no console usando `System.out.println` em vez de um Logger.

**2- problema GodClass e TooManyFields:** `GerenciadorSistemaTotal` concentra regras de negócio de faturamento, cadastro de usuários, envio de e-mails e emissão de notas fiscais, contendo mais de 40 atributos de estado diferentes.

**3- problema CompareObjectsWithEquals e UseStringBufferForStringAppends:** `ProcessadorDeRelatorios` gera um relatório financeiro gigante concatenando texto com operador `+` dentro de um loop `while` e verifica se os nomes das categorias batem usando `==`.

**4- problema CloseResource e AvoidCatchingGenericException:** `ExportadorBancoDeDados` abre uma conexão direta com o banco MySQL para gerar um CSV de backup, mas nunca chama o método `.close()`. Em caso de falha de conexão, ele captura um `Exception` genérico para abortar a operação.

**5- problema HardCodedIP e AvoidThrowingRawExceptionTypes:** `ClienteAPIExterno` faz integrações HTTP com um serviço parceiro utilizando um endereço IP (ex: "192.168.1.55") cravado na classe e simplesmente lança um `new RuntimeException("Falha na API")` se a requisição falhar.

**6- problema UnusedLocalVariable:** `CalculadoraDeImpostos` realiza a apuração de tributos sobre produtos, mas possui três variáveis locais antigas de alíquotas declaradas no meio do cálculo que nunca chegam a ser usadas no resultado final.

**7- problema AvoidDuplicateLiterals:** `FormatadorDeMensagens` é um utilitário que monta avisos de sistema e repete a string `"Falha interna do servidor: "` espalhada hardcoded em mais de 12 métodos distintos.

**8- problema CollapsibleIfStatements:** `ValidadorDeSenha` verifica a força da senha do usuário em etapas, possuindo `if`s um dentro do outro checando tamanho, letras maiúsculas e caracteres especiais que poderiam estar todos em uma única expressão condicional.

**9- problema CyclomaticComplexity:** `AvaliadorDeCredito` é um motor de regras que decide se um limite de cartão deve ser aprovado através de um único método gigantesco contendo incontáveis `if-elses` e `switches` baseados no score do cliente.

**10- problema ReturnEmptyArrayRatherThanNull:** `BuscadorDeClientes` faz buscas no banco de dados por nome, mas quando não encontra nenhum registro correspondente, ele retorna `null` para quem chamou, em vez de retornar uma lista/array vazio de clientes.

**11- problema MethodReturnsInternalArray:** `ConfiguracoesGlobais` carrega chaves sensíveis na memória da aplicação e fornece um getter que devolve diretamente a referência do seu array privado de bytes de segurança, permitindo modificação indevida de fora da classe.

**12- problema ConstantsInInterface:** `StatusDoPedidoConstantes` foi criada como uma `interface` apenas para agrupar as strings "PENDENTE", "PAGO" e "CANCELADO", sem definir nenhum contrato de método a ser implementado.

**13- problema UnusedPrivateMethod:** `AnalisadorDeLogs` varre os logs em busca de padrões de erro, mas possui dois métodos privados antigos de expressões regulares que foram refatorados há meses e esquecidos lá sem ninguém chamar.

**14- problema StringInstantiation:** `GeradorDeTokenSessao` é responsável por criar o token JWT do usuário recém-logado e concatena sufixos instanciando novos objetos com `new String("_auth")` de forma totalmente redundante.

**15- problema PreserveStackTrace:** `ComunicadorRabbitMQ` tenta publicar mensagens em uma fila. Ao dar erro de rede (`IOException`), ele lança uma `MensagemNaoEnviadaException` customizada no catch, mas omite a exceção original, apagando o rastro de onde o erro de IO realmente aconteceu.


-------

## Ajustes de acordo com real outputs:
(Algumas classes foram removidas por conterem problemas que são de outras rulesets que nós não usamos, portanto, o PMD não reportaria esses problemas)

1. AnalisadorDeLogs.java:
    - UnusedPrivateMethod
    - ControlStatementBraces

2. AvaliadorDeCredito.java (removido):
    - UseLocaleWithCaseConversions

3. BuscadorDeClientes.java:
    - ReturnEmptyCollectionRatherThanNull
    - LiteralsFirstInComparisons

4. CalculadoraDeImpostos.java:
    - UnusedLocalVariable

5. ClienteAPIExterno.java:
    - AvoidUsingHardCodedIP

6. ComunicadorRabbitMQ.java:
    - PreserveStackTrace
    - UnusedFormalParameter

7. ConfiguracoesGlobais.java (removido):
    - Nada

8. ExportadorBancoDeDados.java:
    - CloseResource

9. FormatadorDeMensagens.java (removido):
    - Nada

10. GeradorDeTokenSessao.java (removido):
Nada

11. GerenciadorSistemaTotal.java:
    - UnusedPrivateField
    - SingularField
    - UnusedLocalVariable

12. ProcessadorDeRelatorios.java:
    - CompareObjectsWithEquals
    - UseEqualsToCompareStrings

13. ProcessadorDeTextos (adicionado):
    - NoPackage
    - UselessPureMethodCall
    - UseLocaleWithCaseConversions
    - CompareObjectsWithEquals
    - UseEqualsToCompareStrings

14. SincronizadorDeArquivos.java:
    - UnusedPrivateField
    - EmptyCatchBlock
    - UnusedLocalVariable

15. StatusDoPedidoConstantes.java:
    - ConstantsInInterface

16. ValidadorDeSenha.java (removido):
    - Nada