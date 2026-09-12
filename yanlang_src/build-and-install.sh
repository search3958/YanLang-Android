#!/bin/bash
# Rebuild and install YanLang app on Sony device

export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"

cd /Users/cheontaerang/AndroidStudioProjects/YanLang

echo "=== Building YanLang app ==="
./gradlew installDebug 2>&1 | tail -20

if [ $? -eq 0 ]; then
    echo "=== Build successful. Verifying installation ==="
    adb -s HQ625J00B0 shell pm list packages | grep com.sentaro.yanlang
    echo "=== Done ==="
else
    echo "=== Build failed ==="
    exit 1
fi
