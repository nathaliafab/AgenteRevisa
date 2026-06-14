# AgenteRevisa

O **AgenteRevisa** é um sistema automatizado de revisão e refatoração de código Java, movido a Inteligência Artificial. Ele utiliza a arquitetura de grafos do **LangGraph** em conjunto com modelos LLM (como **Google Gemini**) para rodar ferramentas clássicas de análise estática de forma iterativa: ele encontra vulnerabilidades, corrige o código através da IA e refaz os testes em loop até que o código esteja perfeito.

## Ferramentas Suportadas

O sistema orquestra os seguintes analisadores (sub-agentes):
- **Test Generation**: Agente proativo executado no início do fluxo que cria testes baseados em regras de negócio com o framework JUnit 5 e garante que as lógicas de negócio não quebrem ao longo da execução dos agentes de review.
- **PMD**: Focado em detectar más práticas, complexidade ciclomática elevada, código morto e loops ineficientes. *(Faz a auto-instalação da biblioteca no primeiro uso).*
- **CheckStyle**: Focado em formatação de código, indentação e adesão a regras de estilo (como o *Google Java Style*).
- **SpotBugs**: Focado em segurança, vulnerabilidades de concorrência (*Thread-Safety*) e problemas lógicos a nível de compilação (ex: `NullPointerException`). *(Faz a auto-instalação da biblioteca no primeiro uso).*

## Pré-requisitos

- **Docker**
- **Docker Compose**
- Uma chave de API válida do Google Gemini (ou de outra IA suportada pelo *LangChain*).

### Chave de API do Google Gemini

Para conseguir uma chave válida da API do Google Gemini, basta acessar o link a abaixo, fazer o login na sua conta Google e clicar em `Criar chave de API`

- [Google Gemini](https://aistudio.google.com/app/api-keys)

## Configuração

1. **Configure as credenciais (Variáveis de Ambiente)**:
   Crie um arquivo `.env` na raiz do projeto copiando o modelo já existente:
   ```bash
   cp .env.example .env
   ```
   
   Após a cópia, abra o arquivo `.env` e preencha com a sua `GOOGLE_API_KEY`.
   Se quiser, ajuste `REVIEW_MODEL` para o modelo desejado (ex: `gemini-3.1-flash-lite`).

2. **Opcional (evitar permissões no volume)**:
   Defina `LOCAL_UID` e `LOCAL_GID` no `.env` para usar o seu usuário dentro do container.
   Em Linux/macOS, você pode descobrir com:
   ```bash
   id -u
   id -g
   ```

## 🐋 Como Executar com Docker

O orquestrador principal é o script `src/main.py`. Ao subir o container, o comando de help roda automaticamente e em seguida um terminal interativo é aberto.

Para iniciar:
```bash
docker compose build
docker compose run --rm agente-revisa
```

Para rodar as análises, é **obrigatório** informar quais arquivos devem ser processados utilizando a flag `--all` (para todos os arquivos dentro de `input/`) ou `--file` (para arquivos específicos).

Exemplos de uso:

```bash
# Executar pipeline de revisão completo com todos os agentes em TODOS os arquivos
python src/main.py --all

# Executar a pipeline completa em um arquivo específico
python src/main.py --file userManager.java

# Executar a pipeline de revisão baseada na ferramenta PMD em todos os arquivos
python src/main.py --tool pmd --all

# Executar a pipeline observando estritamente regras de sintaxe CheckStyle
python src/main.py --tool checkstyle --all

# Executar a pipeline com validação SpotBugs de Memory e Bytecode apenas em arquivos específicos
python src/main.py --tool spotbugs --file arquivo1.java,arquivo2.java

```

## 🔄 Como Funciona

O **Orchestrator** atua como o cérebro da operação. Ele recebe seus arquivos Java e coordena a execução de múltiplos agentes de análise em um ciclo contínuo, garantindo que o código final atenda aos padrões de todas as ferramentas sem perder a lógica de negócio original.

### Visão Macro: O Fluxo de Execução

O processo começa gerando uma suíte de testes de base (baseline). Em seguida, o orquestrador inicia um grande ciclo externo (limitado a 5 repetições) onde o código modificado passa sequencialmente pelos três agentes especializados: **SpotBugs**, **PMD** e **CheckStyle**. A execução finaliza quando uma rodada completa não resulta em nenhuma alteração ou quando o limite de ciclos é atingido, gerando um relatório final.

![Visão Macro do Orquestrador](/img/DG2.png)

### Visão Micro: O Comportamento de Cada Agente

Ao chegar em uma ferramenta específica, o agente inicia seu próprio loop interno de correções (limitado pelo parâmetro `--max-iter`). Ele analisa o código e, se encontrar problemas, julga via IA se são solucionáveis. Caso positivo, ele altera o código e roda os testes de regressão. Se os testes passarem, ele reanalisa o código, repetindo esse micro-ciclo até zerar os erros daquela ferramenta.

![Visão Detalhada das Decisões](/img/DG1.png)

### Características Principais

1. **Testes como Base (Baseline):** O agente de testes cria a fundação no início. Qualquer alteração feita pelas ferramentas de análise só é aceita se os testes continuarem passando, impedindo que "correções" quebrem a lógica do sistema.

2. **Correção em Camadas:** A validação ocorre em estágios lógicos: primeiro o **SpotBugs** limpa bugs severos e vulnerabilidades; depois o **PMD** remove más práticas e simplifica o código; por fim, o **CheckStyle** padroniza a formatação visual.

3. **Loops Independentes (Convergência):**
   * *Loop Interno (Agente):* Focado em corrigir múltiplos erros de uma mesma ferramenta de uma vez.
   * *Loop Externo (Orquestrador):* Garante que uma formatação do CheckStyle não reintroduza um aviso no PMD, rodando tudo de novo até o código estabilizar completamente.

4. **LLM "Pé no Chão":** A IA não tenta consertar o impossível. Se um alerta não puder ser resolvido alterando o código (ex: falsos positivos ou falta de configuração externa), o agente ignora o aviso para evitar loops infinitos de tentativa e erro.

5. **Relatório Unificado:** Ao rodar o orquestrador, os *logs* individuais são ocultados para não poluir o terminal. No final, um relatório consolidado é gerado em `output/{nome_do_arquivo}_orchestrator_output_{timestamp}.md`, contendo todo o histórico de decisões e o código refatorado.
