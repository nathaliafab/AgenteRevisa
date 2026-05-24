#!/usr/bin/env bash
set -euo pipefail

cd /workspace

if [ -f scripts/install_pmd.sh ]; then
  chmod +x scripts/install_pmd.sh
  ./scripts/install_pmd.sh
fi

if [ -f scripts/install_spotbugs.sh ]; then
  chmod +x scripts/install_spotbugs.sh
  ./scripts/install_spotbugs.sh
fi

python src/main.py --help

exec bash
