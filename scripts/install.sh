#!/bin/bash

# 설치 디렉토리 (원하는 경로로 바꾸세요. 기본은 ~/.monilab)
INSTALL_DIR="$HOME/.monilab"
JAR_NAME="cli-0.0.1-SNAPSHOT.jar"
TARGET="$INSTALL_DIR/$JAR_NAME"

# 빌드 결과 JAR 경로 (현재 위치 기준, 필요 시 수정)
BUILD_JAR="./cli/build/libs/$JAR_NAME"

# 설치 디렉토리 생성
mkdir -p "$INSTALL_DIR"

# JAR 복사
if [ -f "$BUILD_JAR" ]; then
  cp "$BUILD_JAR" "$TARGET"
  echo "✅ Installed $JAR_NAME to $INSTALL_DIR"
else
  echo "❌ JAR not found at $BUILD_JAR"
  exit 1
fi

# 실행 스크립트 생성
BIN_PATH="$INSTALL_DIR/monilab"
cat <<EOF > "$BIN_PATH"
#!/bin/bash
if [ \$# -eq 0 ]; then
  echo "Usage: monilab <mode>"
  echo "Example: monilab GC"
  exit 1
fi

MODE=\$1
java -jar "$TARGET" "\$MODE"
EOF

chmod +x "$BIN_PATH"

# PATH 안내
echo "⚡ Add the following line to your ~/.zshrc or ~/.bashrc:"
echo "    export PATH=\$HOME/.monilab:\$PATH"
echo
echo "Then restart your shell and run:"
echo "    monilab Summary"
echo "    monilab GC"

export PATH=$HOME/.monilab:$PATH
