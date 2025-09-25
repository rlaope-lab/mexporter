#!/bin/bash

JAR_PATH="/Users/heemangkim/Desktop/user/khope/monilab-exporter-ex/cli/build/libs/cli-0.0.1-SNAPSHOT.jar" # khope이 쓸거임

if [ $# -eq 0 ]; then
  echo "Usage: monilab <mode>"
  echo "Example: monilab GC"
  exit 1
fi

MODE=$1

java -jar "$JAR_PATH" "$MODE"
