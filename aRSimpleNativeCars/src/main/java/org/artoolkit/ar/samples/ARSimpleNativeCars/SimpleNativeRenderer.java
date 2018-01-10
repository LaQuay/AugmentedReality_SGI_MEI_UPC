package org.artoolkit.ar.samples.ARSimpleNativeCars;

import android.util.Log;

import org.artoolkit.ar.base.FPSCounter;
import org.artoolkit.ar.base.rendering.ARRenderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SimpleNativeRenderer extends ARRenderer {
    private static boolean initialized = false;

    // Load the native libraries.
    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("ARWrapper");
        System.loadLibrary("ARWrapperNativeCarsExample");
    }

    private String TAG = SimpleNativeRenderer.class.getSimpleName();
    private FPSCounter counter = new FPSCounter();

    public static native void demoInitialise();

    public static native void demoShutdown();

    public static native void demoSurfaceCreated();

    public static native void demoSurfaceChanged(int w, int h);

    public static native void demoDrawFrame();

    public static native int[] getArrayMarkersID();

    public static native void selectMarkerByID(int id);

    public static native void changeLightByID(int id);

    public static native boolean isMarkerVisibleByID(int id);

    public static native void changeOffsetTranslation(int id, float transX, float transY, float transZ);
    public static native void changeOffsetRotationX(int id, float angle);
    public static native void changeOffsetRotationY(int id, float angle);
    public static native void changeOffsetRotationZ(int id, float angle);

    public static native void changeOffsetScale(int id, float scale);

    static boolean isInitialized() {
        return initialized;
    }

    /**
     * By overriding {@link #configureARScene}, the markers and other settings can be configured
     * after the native library is initialised, but prior to the rendering actually starting.
     * Note that this does not run on the OpenGL thread. Use onSurfaceCreated/demoSurfaceCreated
     * to do OpenGL initialisation.
     */
    @Override
    public boolean configureARScene() {
        SimpleNativeRenderer.demoInitialise();

        return true;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        super.onSurfaceChanged(gl, w, h);
        SimpleNativeRenderer.demoSurfaceChanged(w, h);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        SimpleNativeRenderer.demoSurfaceCreated();
    }

    @Override
    public void draw(GL10 gl) {
        if (!initialized) {
            initialized = true;
        }

        SimpleNativeRenderer.demoDrawFrame();

        if (counter.frame())
            Log.i("demo", counter.toString());
    }
}

