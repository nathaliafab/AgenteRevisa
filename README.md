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
# Executar a pipeline de revisão baseada na ferramenta PMD
python src/main.py --tool pmd

# Executar a pipeline observando estritamente regras de sintaxe CheckStyle
python src/main.py --tool checkstyle

# Executar a pipeline com validação SpotBugs de Memory e Bytecode (Padrão)
python src/main.py --tool spotbugs
```