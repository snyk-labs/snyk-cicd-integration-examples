# Install Snyk

This is a repo meant to collect examples of quick ways to deploy / install snyk.

More complex examples will be added, but the basic one, for a linux host is included and is the following:

```bash
#!/bin/bash

set -eo pipefail

SNYK_LATEST=$(curl -s -L "https://static.snyk.io/cli/latest/version")

echo "Snyk Latest: ${SNYK_LATEST}"

curl -O -s -L "https://static.snyk.io/cli/v${SNYK_LATEST}/snyk-linux"
curl -O -s -L "https://static.snyk.io/cli/v${SNYK_LATEST}/snyk-linux.sha256"

# or you can do
# curl -O -s -L "https://static.snyk.io/cli/latest/snyk-linux"
# curl -O -s -L "https://static.snyk.io/cli/latest/snyk-linux.sha256"


if sha256sum -c snyk-linux.sha256; then
  mv snyk-linux ./snyk
  chmod +x ./snyk
  echo "Snyk Version: ${SNYK_LATEST} installed successfully"
else
  echo "Snyk Binary Download failed, exiting"
  exit 1
fi
```
