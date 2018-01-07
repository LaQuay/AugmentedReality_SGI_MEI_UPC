# AugmentedReality_SGI_MEI_UPC

This project was made in ARToolkit 5.3.2, official release.

Follow this steps: https://www.artoolkit.org/documentation/doku.php?id=4_Android:android_native

### How to compile with Android Studio

- Git clone.
- Inside aRSimpleNativeCars\src\main\jni, rename Android.example.mk to Android.mk
- Change the ARTOOLKIT_DIR in Android.mk of the project. It should point to {ARKToolkit}/android
- Add your own EXPORTS to the {ARKTOOLKIT}/android .sh files (build.sh / build_native_examples.sh)

(Use the same EXPORT key, but use your own value)
```bash
export ANDROID_HOME='/c/.../AppData/Local/Android/Sdk'
export ANDROID_SDK_HOME='/c/.../AppData/Local/Android/Sdk'
export ANDROID_NDK_ROOT=$ANDROID_HOME'/ndk-bundle'
export ANDROID_NDK=$ANDROID_HOME'/ndk-bundle'
export NDK=$ANDROID_HOME'/ndk-bundle'
export HOST_OS='MINGW64_NT-10.0'
```

### Additional information

#### If the build.sh didn't generate the compiled files
Check the ABI in Application.mk of each 'AR...Proj' project.
