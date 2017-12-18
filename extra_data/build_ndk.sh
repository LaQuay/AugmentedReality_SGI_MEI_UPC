#! /bin/bash

export ANDROID_HOME='/c/Users/Marc/AppData/Local/Android/sdk'
export ANDROID_SDK_HOME='/c/Users/Marc/AppData/Local/Android/sdk'
export ANDROID_NDK_ROOT=$ANDROID_HOME'/ndk-bundle'
export ANDROID_NDK=$ANDROID_HOME'/ndk-bundle'
export NDK=$ANDROID_HOME'/ndk-bundle'
export HOST_OS='MINGW64_NT-10.0'

#
# Find out where we are and change to ARToolKit5-Android root.
#
OURDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "${OURDIR}/../"
echo "Working from directory \"$PWD\"."

# Set OS-dependent variables.
OS=`uname -s`
ARCH=`uname -m`
CPUS=
NDK_BUILD_SCRIPT_FILE_EXT=
if [[ "$OS" = "Linux" ]]; then
    echo Building on Linux \(${ARCH}\)
    CPUS=`/usr/bin/nproc`
elif [[ "$OS" = "Darwin" ]]; then
    echo Building on Apple Mac OS X \(${ARCH}\)
    CPUS=`/usr/sbin/sysctl -n hw.ncpu`
else #Checking for Windows in a non-cygwin dependent way.
    WinsOS=
	if [[ $OS ]]; then
        WinsVerNum=${OS##*-}
		if [[ $WinsVerNum = "10.0" || $WinsVerNum = "6.3" ]]; then
			if [[ $WinsVerNum = "10.0" ]]; then
			    WinsOS="Wins10"
			else
			    WinsOS="Wins8.1"
			fi
			echo Building on Microsoft ${WinsOS} Desktop \(${ARCH}\)
			export HOST_OS="windows"
			NDK_BUILD_SCRIPT_FILE_EXT=".cmd"
			CPUS=`/usr/bin/nproc`
		fi
    fi
fi

if [[ ! $CPUS ]]; then
	echo **Development platform not supported, exiting script**
    exit 1
fi

ARTK_LibsDir=libs

#
# Build native targets
#
NATIVE_PROJS=" \
    app \
"
for i in $NATIVE_PROJS
do
    echo from `pwd`: going to $i
    cd $i
    $NDK/ndk-build$NDK_BUILD_SCRIPT_FILE_EXT -j $CPUS $1
done
cd ..
