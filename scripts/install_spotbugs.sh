#!/bin/bash
set -e

SPOTBUGS_VERSION="4.8.6"
BIN_DIR="bin"
SPOTBUGS_DIR="$BIN_DIR/spotbugs-$SPOTBUGS_VERSION"

# Verifica se a pasta já existe
if [ -d "$SPOTBUGS_DIR" ]; then
    echo "SpotBugs já está instalado em $SPOTBUGS_DIR"
    exit 0
fi

echo "SpotBugs não encontrado. Baixando e instalando a versão $SPOTBUGS_VERSION..."

# Cria o diretório de destino se não existir
mkdir -p "$BIN_DIR"

# Baixa a versão otimizada (.tgz)
ARCHIVE_PATH="$BIN_DIR/spotbugs-$SPOTBUGS_VERSION.tgz"
wget -q --show-progress -O "$ARCHIVE_PATH" \
    "https://github.com/spotbugs/spotbugs/releases/download/$SPOTBUGS_VERSION/spotbugs-$SPOTBUGS_VERSION.tgz"

# Descompacta
tar -xzf "$ARCHIVE_PATH" -C "$BIN_DIR"

# Remove o arquivo compactado
rm "$ARCHIVE_PATH"

# Dá permissão de execução ao binário
chmod +x "$SPOTBUGS_DIR/bin/spotbugs"

echo "SpotBugs instalado com sucesso na pasta $SPOTBUGS_DIR!"
