# MC400 Debug Settings

A tiny Android TV shortcut app for the HP MC400 projector. Its three buttons try to open Android TV Developer Options, standard Android Developer Options, or Wireless Debugging. It requests no Android permissions and does not enable debugging itself. Which settings screens open depends on the projector firmware.

The [step-by-step guide and ready-to-install APK](https://steelecodes-site.vercel.app/mc400-debug-settings) are on my site. This repository contains the complete source used for the app. The website APK was signed with a separate key, so a local build will have a different SHA-256 and Android will not install it as an update over the website version. Uninstall the website copy first if you want to try your own build.

## Build your own APK

1. Install Android Studio, or the Android SDK command-line tools, with **Android SDK Platform 35** and **Build Tools 35.0.0**. Have a JDK with `javac` and `keytool` on your PATH.
2. Set `ANDROID_HOME` to your Android SDK directory. On macOS, `~/Library/Android/sdk` is the usual location.
3. Run `./build.sh` from this repository. The APK appears at `build/mc400-debug-settings.apk`.

The build script compiles the single Java activity with `javac` and `d8`, packages it with `aapt2`, aligns it with `zipalign`, and signs it with `apksigner`. Its development signing key is generated in the ignored `build/` directory. Do not publish or reuse that key for a release.

To inspect the source, start with [`MainActivity.java`](src/com/codex/mc400nativesettings/MainActivity.java) and [`AndroidManifest.xml`](AndroidManifest.xml). The manifest includes a TV launcher entry so the app appears on the projector.

## Install and connect

Open the [guide](https://steelecodes-site.vercel.app/mc400-debug-settings) for the projector-side install, wireless pairing, `adb connect`, and optional `adb install` steps. Pairing and connection endpoints are different; copy each port from the corresponding projector screen. Keep your pairing code and local IP private.

## Limitations

This is an unofficial utility, tested on one HP MC400. It does not root the device, change firmware, install a launcher, or grant ADB access by itself. Stop if your projector's settings screens differ from the guide.
