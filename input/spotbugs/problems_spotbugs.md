## Criação do dataset

O dataset foi criado usando LLMs, abaixo segue detalhes de como foi esse processo

### Parte 1

Primeiro, foram elencados **15 problemas diferentes** (regras do SpotBugs) que são muito comuns e abrangem categorias como Correção (Bad Practice/Correctness), Performance, Concorrência e Código Malicioso:

1. **NP_ALWAYS_NULL**: Desreferenciamento de ponteiro nulo garantido (gera um NullPointerException em tempo de execução).
2. **IL_INFINITE_LOOP**: Laço de repetição evidente cuja condição de parada nunca é atingida (loop infinito).
3. **EI_EXPOSE_REP**: Método tipo getter que retorna a referência direta de um objeto mutável interno, expondo a representação da classe.
4. **EI_EXPOSE_REP2**: Construtor ou setter que aceita e armazena diretamente a referência de um objeto mutável externo, permitindo modificação indevida.
5. **SE_BAD_FIELD**: Atributo de instância que não é serializável declarado dentro de uma classe que implementa `Serializable`.
6. **RV_RETURN_VALUE_IGNORED**: Ignorar intencionalmente ou por erro o valor de retorno de métodos imutáveis ou de checagem essencial (como `String.trim()` ou `File.delete()`).
7. **ES_COMPARING_STRINGS_WITH_EQ**: Comparar referências de objetos String utilizando o operador `==` em vez do método `.equals()`.
8. **DM_DEFAULT_ENCODING**: Instanciar leitores/escritores de arquivos confiando no charset padrão do Sistema Operacional, gerando inconsistências entre servidores.
9. **ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD**: Atualizar um campo estático (`static`) por meio de um método de instância comum, gerando falhas graves de concorrência.
10. **GC_UNCHECKED_TYPE_IN_GENERIC_CALL**: Passar um objeto de tipo totalmente incompatível em chamadas de coleções genéricas (ex: buscar uma chave String em um `Map<Long, Object>`).
11. **DMI_VACUOUS_COLLECTION_CALL**: Realizar uma chamada redundante ou inútil sobre uma coleção que sempre retornará um resultado óbvio (ex: verificar se uma lista contém ela mesma).
12. **ODR_OPEN_DATABASE_RESOURCE**: Deixar de fechar adequadamente recursos de banco de dados (`Connection`, `Statement`), causando vazamento de conexões.
13. **FE_FLOATING_POINT_EQUALITY**: Comparar números de ponto flutuante (`float` ou `double`) diretamente com o operador `==`, ignorando problemas de precisão binária.
14. **DM_NEXTINT_VIA_NEXTDOUBLE**: Gerar números inteiros aleatórios multiplicando o retorno de `Random.nextDouble()`, uma abordagem ineficiente e imprecisa.
15. **DL_SYNCHRONIZATION_ON_SHARED_CONSTANT**: Realizar controle de concorrência (`synchronized`) travando em literais de String ou constantes que são compartilhadas globalmente na JVM.
16. **BC_IMPOSSIBLE_CAST**: Realizar um cast forçado em tempo de compilação entre tipos de dados que são comprovadamente incompatíveis em tempo de execução.

### Parte 2

Em seguida, foram criadas usando LLMs **10 classes fictícias**, distribuindo os 16 problemas sem nenhuma repetição:

**1- problema NP_ALWAYS_NULL e IL_INFINITE_LOOP:** `ProcessadorRelatorio` tenta gerar um relatório financeiro usando um laço `while` cuja condição de parada nunca muda. Para piorar, dentro do bloco ela tenta acessar métodos de uma String inicializada explicitamente como `null`.

**2- problema EI_EXPOSE_REP e EI_EXPOSE_REP2:** `PerfilUsuario` gerencia o cadastro e modificação de usuários do sistema. Ela quebra o encapsulamento ao retornar a referência direta de seu atributo `Date` interno no getter e ao salvar diretamente a instância recebida de fora no construtor.

**3- problema SE_BAD_FIELD e RV_RETURN_VALUE_IGNORED:** `ExportadorDados` implementa a interface `Serializable` para trafegar dados, mas possui um atributo `InputStream` que não é serializável. Além disso, ela limpa textos chamando `String.trim()` e apaga arquivos com `File.delete()` ignorando por completo os retornos dessas chamadas.

**4- problema ES_COMPARING_STRINGS_WITH_EQ e DM_DEFAULT_ENCODING:** `AutenticadorSimples` faz a validação de tokens de segurança do sistema utilizando o operador `==` para comparar as Strings das credenciais. Ela também lê arquivos de propriedades do servidor usando `FileReader` comum, ficando refém do encoding padrão da máquina.

**5- problema ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD:** `ContadorAcessos` registra as requisições que chegam na aplicação e atualiza um contador global acumulador (`static int`) de dentro de um método de instância comum e totalmente livre de sincronização multi-thread.

**6- problema GC_UNCHECKED_TYPE_IN_GENERIC_CALL e DMI_VACUOUS_COLLECTION_CALL:** `GerenciadorEstoque` manipula um mapa estruturado com chaves numéricas (`Long`), mas tenta ler os produtos passando um identificador em formato de `String`. Na mesma rotina, ela faz uma verificação inútil chamando `estoque.containsAll(estoque)`.

**7- problema ODR_OPEN_DATABASE_RESOURCE:** `HistoricoAcessosDAO` abre conexões brutas via driver JDBC para persistir logs de auditoria no banco de dados, contudo realiza a operação sem blocos try-with-resources ou tratamento em `finally`, esquecendo as conexões abertas em caso de falha.

**8- problema FE_FLOATING_POINT_EQUALITY e DM_NEXTINT_VIA_NEXTDOUBLE:** `CalculadoraEstatistica` computa médias e margens de erro de cálculos matemáticos. Ela faz asserções de igualdade entre valores de ponto flutuante usando `==` e tenta sortear identificadores de lotes multiplicando o retorno de `random.nextDouble()`.

**9- problema DL_SYNCHRONIZATION_ON_SHARED_CONSTANT:** `SincronizadorNotificacoes` gerencia o envio de mensagens em lote e tenta garantir o isolamento da operação utilizando um bloco `synchronized` que trava o fluxo em cima de uma String literal fixa.

**10- problema BC_IMPOSSIBLE_CAST:** `ConversorFormato` recebe coleções genéricas de dados da aplicação e, sem realizar nenhuma checagem prévia com `instanceof`, tenta forçar um cast direto para a classe concreta `ArrayList`, gerando quebras inevitáveis.

### Distribuição dos problemas nas classes

(Todas as 10 classes foram mantidas pois simulam o escopo padrão de detecção e as regras de bytecode analisadas nativamente pelo SpotBugs)

1. AutenticadorSimples.java:

    * ES_COMPARING_STRINGS_WITH_EQ
    * DM_DEFAULT_ENCODING

2. CalculadoraEstatistica.java:

    * FE_FLOATING_POINT_EQUALITY
    * DM_NEXTINT_VIA_NEXTDOUBLE

3. ContadorAcessos.java:

    * ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD

4. ConversorFormato.java:

    * BC_IMPOSSIBLE_CAST

5. ExportadorDados.java:

    * SE_BAD_FIELD
    * RV_RETURN_VALUE_IGNORED

6. GerenciadorEstoque.java:

    * GC_UNCHECKED_TYPE_IN_GENERIC_CALL
    * DMI_VACUOUS_COLLECTION_CALL

7. HistoricoAcessosDAO.java:

    * ODR_OPEN_DATABASE_RESOURCE

8. PerfilUsuario.java:

    * EI_EXPOSE_REP
    * EI_EXPOSE_REP2

9. ProcessadorRelatorio.java:

    * NP_ALWAYS_NULL
    * IL_INFINITE_LOOP

10. SincronizadorNotificacoes.java:

    * DL_SYNCHRONIZATION_ON_SHARED_CONSTANT

## Avaliação das Correções do Agente (SpotBugs)

`OBS: Os outputs completos do agente para cada classe estão na pasta outputs`

O agente se saiu bem em corrigir os erros do Spotbugs mas modificou o sentido do código em algumas ocasiões, adicionando novas funcionalidades, o que pode consistir em um overengineering. Algumas dessas alteraçãoes podem ser benignas, mas a intenção do agente era manter o código mais próximos do original o possível. Além disso ele causou alguns problemas de formatação desnecessários, como desrespeitar casings. 

Em relação a criação de testes, eles são satisfatórios como testes iniciais, mas não garantem 100% de coverage por exemplo, isso poderia ser mudado tornando o agente de testes mais complexo, talvez rodando uma ferramenta de coverage.

*   **AutenticadorSimples:** Nenhum problema

*   **CalculadoraEstatistica:**: Adicionou do nada uma trava com `if (valorMaximo <= 0) throw new IllegalArgumentException(...)`. Quebrou a retrocompatibilidade. Antes se alguém mandasse 0 ele só devolveria 0. Agora lança exceção em runtime e derruba a aplicação.

*   **ContadorAcessos:**: Nenhum problema

*   **ConversorFormato:**: Nenhum problema

*   **ExportadorDados (ExportadorDeDados):**: Nenhum problema

*   **GerenciadorEstoque:**: Nenhum problema

*   **HistoricoAcessosDAO:**:  Tirou os parâmetros fixos de usuário e forçou o código a variáveis de ambiente. Isso é mais profissional para um código em produção, mas quebra retrocompatibilidade.

*   **PerfilUsuario:**: Nenhum problema

*   **ProcessadorRelatorio (ProcessadorDeRelatorios):**: Nenhum problema

*   **SincronizadorNotificacoes:**: Nenhum problema

