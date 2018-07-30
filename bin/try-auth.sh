#!/usr/bin/env bash
cd "$(dirname "$0")/.." || exit
git submodule update --init --checkout || exit
export PATH=$PWD/bin:$PATH
bash
