#!/bin/sh

# A posix install example

set -eo

# this script attempts to replace an existing snyk binary in the path if
# if no path is provided to where snyk should be installed
# ./install_posix.sh -> installs snyk over $(which snyk) or /usr/local/bin/snyk
# ./install_posix.sh ~/bin/snyk -> install snyk at ~/bin/snyk

SNYK_TARGET="${1}"

UNAME=$(uname -s)

if [ "$UNAME" = "Darwin" ]; then
    SNYK_BUILD="macos"
else
    OS_ID=$(awk -F= '$1=="ID" { print $2 ;}' /etc/os-release)
    if [ "$OS_ID" = "alpine" ]; then
        SNYK_BUILD="alpine"
        INSTALL_CURL="apk"
        # if alpine we need libstdc++
        apk add --no-progress --quiet --no-cache libstdc++
    elif [ "$OS_ID" = "debian" ] || [ "$OS_ID" = "ubuntu" ]; then
        SNYK_BUILD="linux"
        INSTALL_CURL="apt"
    else
        SNYK_BUILD="linux"
        INSTALL_CURL="yum"
    fi
fi

if [ -n "${SNYK_TARGET}" ]; then
    SNYK_PATH="${SNYK_TARGET}"
elif SNYK_LOC=$(which snyk); then
    SNYK_PATH="${SNYK_LOC}"
else
    SNYK_PATH="/usr/local/bin/snyk"
fi


if [ "${SNYK_BUILD}" = "macos" ]; then
    SHA_COMMAND="shasum"
else
    SHA_COMMAND="sha256sum"
fi

if ! which curl; then
    if [ "$INSTALL_CURL" = "apt" ]; then
        apt-get update -qq
        apt-get -qq -y install curl > /dev/null
    elif [ "$INSTALL_CURL" = "apk" ]; then
        apk add --no-progress --quiet --no-cache curl
    else
        yum install -q -y curl
    fi
fi

SNYK_LATEST=$(curl -s -L "https://static.snyk.io/cli/latest/version")

curl -O -s -L "https://static.snyk.io/cli/v${SNYK_LATEST}/snyk-${SNYK_BUILD}"
curl -O -s -L "https://static.snyk.io/cli/v${SNYK_LATEST}/snyk-${SNYK_BUILD}.sha256"

if "${SHA_COMMAND}" -c "snyk-${SNYK_BUILD}.sha256"; then
  echo "Snyk binary checksum passed, updating"
  mv "snyk-${SNYK_BUILD}" "${SNYK_PATH}"
  chmod +x "${SNYK_PATH}"
  SNYK_VERSION=$("${SNYK_PATH}" --version | cut -d' ' -f1)
  rm "snyk-${SNYK_BUILD}.sha256"
else
  echo "Snyk Binary Checksum Failed!"
  exit 1
fi


if [ "${SNYK_VERSION}" = "${SNYK_LATEST}" ]; then
    echo "Snyk installed at ${SNYK_PATH} with latest version: ${SNYK_VERSION}"
else
    echo "Snyk didn't install properly"
    exit 1
fi
