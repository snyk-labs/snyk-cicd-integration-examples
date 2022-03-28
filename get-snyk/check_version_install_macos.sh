#!/bin/bash

set -eo pipefail

SNYK_LATEST=$(curl -s -L "https://static.snyk.io/cli/latest/version")

SNYK_VERSION=$(snyk --version | cut -d' ' -f1)

if [ "${SNYK_LATEST}" == "${SNYK_VERSION}" ]; then
  echo "Snyk Latest version is: ${SNYK_LATEST}"
  curl -O -s -L "https://static.snyk.io/cli/v${SNYK_LATEST}/snyk-macos"
  curl -O -s -L "https://static.snyk.io/cli/v${SNYK_LATEST}/snyk-macos.sha256"
  if shasum -c snyk-macos.sha256; then
    mv snyk-macos ./snyk
    chmod +x ./snyk
    SNYK_VERSION=$(./snyk --version | cut -d' ' -f1)
    echo "Snyk Version: ${SNYK_LATEST} installed successfully"
  else
    echo "Snyk Binary Download failed, exiting"
    exit 1
  fi
else
  echo "Snyk Local Version: ${SNYK_VERSION}, Snyk Latest Version: ${SNYK_LATEST}"
fi
