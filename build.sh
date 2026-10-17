#!/usr/bin/env bash
set -euo pipefail

root="$(cd "$(dirname "$0")" && pwd)"
sdk="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-${HOME}/Library/Android/sdk}}"
platform="${sdk}/platforms/android-35/android.jar"
tools_dir="${sdk}/build-tools/35.0.0"
out="${root}/build"

if [[ ! -f "$platform" || ! -x "${tools_dir}/aapt2" ]]; then
  echo "Install Android SDK Platform 35 and Build Tools 35.0.0, then set ANDROID_HOME." >&2
  exit 1
fi

mkdir -p "$out/classes" "$out/dex"
"${tools_dir}/aapt2" link -o "$out/unsigned.apk" -I "$platform" --manifest "$root/AndroidManifest.xml"
javac -source 8 -target 8 -cp "$platform" -d "$out/classes" \
  "$root/src/com/codex/mc400nativesettings/MainActivity.java"
"${tools_dir}/d8" --lib "$platform" --output "$out/dex" \
  "$out/classes/com/codex/mc400nativesettings/"*.class
(cd "$out/dex" && "${tools_dir}/aapt" add "$out/unsigned.apk" classes.dex)
"${tools_dir}/zipalign" -f 4 "$out/unsigned.apk" "$out/aligned.apk"

# Local development key only. Never commit it or use it for a production release.
if [[ ! -f "$out/local-debug.jks" ]]; then
  keytool -genkeypair -noprompt -keystore "$out/local-debug.jks" \
    -storepass android -keypass android -alias local-debug \
    -dname 'CN=Local MC400 Debug Build' -keyalg RSA -keysize 2048 -validity 3650
fi
"${tools_dir}/apksigner" sign --ks "$out/local-debug.jks" \
  --ks-pass pass:android --key-pass pass:android --ks-key-alias local-debug \
  --out "$out/mc400-debug-settings.apk" "$out/aligned.apk"
"${tools_dir}/apksigner" verify --verbose "$out/mc400-debug-settings.apk"
echo "Built $out/mc400-debug-settings.apk"
