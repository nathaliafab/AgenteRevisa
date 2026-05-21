# AgenteRevisa

O **AgenteRevisa** é um sistema automatizado de revisão e refatoração de código Java, movido a Inteligência Artificial. Ele utiliza a arquitetura de grafos do **LangGraph** em conjunto com modelos LLM (como **Google Gemini**) para rodar ferramentas clássicas de análise estática de forma iterativa: ele encontra vulnerabilidades, corrige o código através da IA e refaz os testes em loop até que o código esteja perfeito.

## Ferramentas Suportadas

O sistema orquestra os seguintes analisadores (sub-agentes):
- **PMD**: Focado em detectar más práticas, complexidade ciclomática elevada, código morto e loops ineficientes.
- **CheckStyle**: Focado em formatação de código, indentação e adesão a regras de estilo (como o *Google Java Style*).
- **SpotBugs**: Focado em segurança, vulnerabilidades de concorrência (*Thread-Safety*) e problemas lógicos a nível de compilação (ex: `NullPointerException`). *(Faz a auto-instalação da biblioteca no primeiro uso).*

## Pré-requisitos

- **Python 3.10+**
- **Java JDK** instalado no sistema (necessário para que o Checkstyle rode e para o agente do SpotBugs invocar o comando `javac`).
- Uma chave de API válida do Google Gemini (ou de outra IA suportada pelo *LangChain*).

## Instalação e Configuração

1. **Clone o repositório** e ative um ambiente virtual (recomendado):
   ```bash
   python -m venv venv
   source venv/bin/activate
   ```

2. **Instale as dependências Python** do projeto de análise:
   ```bash
   pip install -r requirements.txt
   ```

3. **Configure as credenciais (Variáveis de Ambiente)**:
   Crie um arquivo `.env` na raiz do projeto copiando o modelo já existente:
   ```bash
   cp .env.example .env
   ```
   
   Após a cópia, abra o arquivo `.env` e preencha com a sua `GOOGLE_API_KEY`. As demais chaves dos caminhos para execução já estarão configuradas nos valores padrão de sistema.

## 💻 Como Executar

O orquestrador principal é o script `src/main.py`. Ao ser executado, o agente fará uma análise profunda no código recebido (atualmente, um código de avaliação predefinido) e vai rodar as iterações da IA sobre ele até esgotar o limite configurado (padrão de 3 tentativas).

Para testar sub-ferramentas, utilize a tag `--tool`:

```bash
# Executar a pipeline de revisão baseada na ferramenta PMD
python src/main.py --tool pmd

# Executar a pipeline observando estritamente regras de sintaxe CheckStyle
python src/main.py --tool checkstyle

# Executar a pipeline com validação SpotBugs de Memory e Bytecode (Baixa CLI na hora) (Padrão)
python src/main.py --tool spotbugs
```