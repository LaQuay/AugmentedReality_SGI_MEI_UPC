# AugmentedReality_SGI_MEI_UPC

This project was made in ARToolkit 5.3.2, official release, but with a tweaked version of the Android-ARWrapper.mk, included in the extras folder.

### How to compile ARToolkit

Follow this steps: https://www.artoolkit.org/documentation/doku.php?id=4_Android:android_native

And when asked, add this in both .sh (build.sh / build_native_examples.sh):

```bash
export ANDROID_HOME='/c/Users/LaQuay/AppData/Local/Android/Sdk'
export ANDROID_NDK_ROOT=$ANDROID_HOME'/ndk-bundle/'
export NDK=$ANDROID_NDK_ROOT
export PATH=$PATH:$NDK
export HOST_OS='MINGW64_NT-10.0'
```
