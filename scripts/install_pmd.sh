#!/bin/bash
set -e

PMD_VERSION="7.24.0"
BIN_DIR="bin"
PMD_DIR="$BIN_DIR/pmd-bin-$PMD_VERSION"

# Verifica se a pasta já existe
if [ -d "$PMD_DIR" ]; then
    echo "PMD já está instalado em $PMD_DIR"
    exit 0
fi

echo "PMD não encontrado. Baixando e instalando a versão $PMD_VERSION..."

# Cria o diretório de destino se não existir
mkdir -p "$BIN_DIR"
cd "$BIN_DIR"

# Baixa a versão .zip do PMD
wget -q --show-progress "https://github.com/pmd/pmd/releases/download/pmd_releases%2F$PMD_VERSION/pmd-dist-$PMD_VERSION-bin.zip"

# Descompacta
unzip -q "pmd-dist-$PMD_VERSION-bin.zip"

# O unzip extrai como 'pmd-bin-7.24.0' automaticamente
# Remove o arquivo compactado
rm "pmd-dist-$PMD_VERSION-bin.zip"

# Dá permissão de execução ao binário
chmod +x "pmd-bin-$PMD_VERSION/bin/pmd"

echo "PMD instalado com sucesso na pasta $PMD_DIR!"
