#!/bin/bash
set -e

SPOTBUGS_VERSION="4.8.6"
BIN_DIR="bin"
SPOTBUGS_DIR="$BIN_DIR/spotbugs"

# Verifica se a pasta já existe
if [ -d "$SPOTBUGS_DIR" ]; then
    echo "SpotBugs já está instalado em $SPOTBUGS_DIR"
    exit 0
fi

echo "SpotBugs não encontrado. Baixando e instalando a versão $SPOTBUGS_VERSION..."

# Cria o diretório de destino se não existir
mkdir -p "$BIN_DIR"
cd "$BIN_DIR"

# Baixa a versão otimizada (.tgz)
wget -q --show-progress "https://github.com/spotbugs/spotbugs/releases/download/$SPOTBUGS_VERSION/spotbugs-$SPOTBUGS_VERSION.tgz"

# Descompacta e renomeia
tar -xzf "spotbugs-$SPOTBUGS_VERSION.tgz"
mv "spotbugs-$SPOTBUGS_VERSION" "spotbugs"

# Remove o arquivo compactado
rm "spotbugs-$SPOTBUGS_VERSION.tgz"

# Dá permissão de execução ao binário
chmod +x "spotbugs/bin/spotbugs"

echo "SpotBugs instalado com sucesso na pasta $SPOTBUGS_DIR!"
