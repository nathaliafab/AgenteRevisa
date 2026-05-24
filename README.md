# AgenteRevisa

O **AgenteRevisa** é um sistema automatizado de revisão e refatoração de código Java, movido a Inteligência Artificial. Ele utiliza a arquitetura de grafos do **LangGraph** em conjunto com modelos LLM (como **Google Gemini**) para rodar ferramentas clássicas de análise estática de forma iterativa: ele encontra vulnerabilidades, corrige o código através da IA e refaz os testes em loop até que o código esteja perfeito.

## Ferramentas Suportadas

O sistema orquestra os seguintes analisadores (sub-agentes):
- **PMD**: Focado em detectar más práticas, complexidade ciclomática elevada, código morto e loops ineficientes.
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

Depois do help aparecer, execute as ferramentas com a tag `--tool`:

```bash

# Executar pipeline de revisão completo com todos os agentes
python src/main.py

# Executar a pipeline de revisão baseada na ferramenta PMD
python src/main.py --tool pmd

# Executar a pipeline observando estritamente regras de sintaxe CheckStyle
python src/main.py --tool checkstyle

# Executar a pipeline com validação SpotBugs de Memory e Bytecode (Padrão)
python src/main.py --tool spotbugs
```

## 🔄 Como Funciona o Orquestrador

O **OrchestratorAgent** coordena a execução sequencial dos três agentes em múltiplos ciclos até que o código esteja completamente limpo conforme as regras de todas as ferramentas.

### Fluxo de Execução

```
┌─────────────────────────────────────────────────────────────┐
│         Orquestrador inicia (máx 5 ciclos)                  │
└──────────────────────┬──────────────────────────────────────┘
                       │
        ┌──────────────┴──────────────┐
        │      Código Inicial         │
        └──────────────┬──────────────┘
                       │
         ┌─────────────▼──────────────┐
         │   Ciclo 1, 2, 3...        │
         └─────────────┬──────────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ▼              ▼              ▼
    ┌────────┐    ┌────────┐    ┌──────────┐
    │SpotBugs│───▶│  PMD   │───▶│CheckStyle│
    └────────┘    └────────┘    └──────────┘
        │              │              │
        └──────────────┼──────────────┘
                       │
            ┌──────────▼──────────┐
            │   Código Corrigido  │
            └──────────┬──────────┘
                       │
          Nenhuma mudança no ciclo?
          SIM ─────────────────────▶ FIM
          NÃO │
              ▼
          Próximo ciclo
```

### Características Principais

1. **Execução Sequencial**: Cada agente recebe o código corrigido pelo agente anterior no mesmo ciclo
   - **SpotBugs** analisa segurança, concorrência e bugs lógicos
   - **PMD** detecta más práticas, complexidade e código morto
   - **CheckStyle** verifica formatação e estilo de código

2. **Múltiplos Ciclos**: O orquestrador repete até 5 ciclos
   - Garante que correções de um agente não quebrem as regras de outro
   - Continua até convergência: quando nenhum agente faz mudanças no código

3. **LLM Inteligente**: Antes de corrigir, cada agente pergunta ao LLM:
   > "Esses erros podem ser corrigidos apenas mudando o código Java?"
   - Se não, o agente para (marca como "não corrigível via código")
   - Evita loops infinitos em warnings que não podem ser resolvidos

4. **Relatório Unificado**: Quando executado em modo orquestrador (`python src/main.py`):
   - Suprime outputs individuais dos agentes
   - Salva um único relatório consolidado em `output/orchestrator_output_{timestamp}.md`
   - Contém análise de todos os 3 agentes para cada ciclo