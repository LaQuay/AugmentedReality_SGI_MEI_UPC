# AugmentedReality_SGI_MEI_UPC

This project was made in ARToolkit 5.3.2, official release.

### How to compile ARToolkit directly

Follow this steps: https://www.artoolkit.org/documentation/doku.php?id=4_Android:android_native

And when asked, add this in both .sh (build.sh / build_native_examples.sh):

```bash
export ANDROID_HOME='/c/.../AppData/Local/Android/Sdk'
export ANDROID_SDK_HOME='/c/.../AppData/Local/Android/Sdk'
export ANDROID_NDK_ROOT=$ANDROID_HOME'/ndk-bundle'
export ANDROID_NDK=$ANDROID_HOME'/ndk-bundle'
export NDK=$ANDROID_HOME'/ndk-bundle'
export HOST_OS='MINGW64_NT-10.0'
```

Some tweaked files:

- Android-ARWrapper.mk, included in the extras folder.

### How to compile with Android Studio

- Git clone.
- Change the ARTOOLKIT_DIR in Android.mk of the project. It should point to {ARKToolkit}/android

#### If deprecation warnings while compiling
It's ok, no problem, future change.

### Additional information

#### If the build.sh didn't generate the compiled files
Check the ABI in Application.mk of each 'AR...Proj' project.
