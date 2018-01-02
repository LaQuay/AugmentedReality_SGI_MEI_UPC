package org.artoolkit.ar.samples.ARSimpleNativeCars;

import android.util.Log;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.FPSCounter;
import org.artoolkit.ar.base.rendering.ARRenderer;
import org.artoolkit.ar.base.rendering.Cube;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SimpleNativeRenderer extends ARRenderer {
    // Load the native libraries.
    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("ARWrapper");
        System.loadLibrary("ARWrapperNativeCarsExample");
    }

    private String TAG = SimpleNativeRenderer.class.getSimpleName();
    private FPSCounter counter = new FPSCounter();
    private int markerA = -1;
    private int markerB = -1;
    private int markerC = -1;
    private int markerD = -1;
    private int markerG = -1;
    private int markerF = -1;
    private List<Integer> markerArray = new ArrayList<>();
    private Cube cube1 = new Cube(10.0f, 0.0f, 0.0f, 20.0f);
    private Cube cube2 = new Cube(20.0f, 0.0f, 0.0f, 20.0f);
    private Cube cube3 = new Cube(30.0f, 0.0f, 0.0f, 20.0f);
    private Cube cube4 = new Cube(40.0f, 0.0f, 0.0f, 20.0f);
    private Cube cube5 = new Cube(50.0f, 0.0f, 0.0f, 20.0f);
    private Cube cube6 = new Cube(60.0f, 0.0f, 0.0f, 20.0f);
    private float angle = 0.0f;

    public static native void demoInitialise();

    public static native void demoShutdown();

    public static native void demoSurfaceCreated();

    public static native void demoSurfaceChanged(int w, int h);

    public static native void demoDrawFrame();

    /**
     * By overriding {@link #configureARScene}, the markers and other settings can be configured
     * after the native library is initialised, but prior to the rendering actually starting.
     * Note that this does not run on the OpenGL thread. Use onSurfaceCreated/demoSurfaceCreated
     * to do OpenGL initialisation.
     */
    @Override
    public boolean configureARScene() {
        SimpleNativeRenderer.demoInitialise();

        markerA = ARToolKit.getInstance().addMarker("single;Data/multi/patt.a;80");
        if (markerA < 0) {
            Log.e(TAG, "Unable to load markerA");
            return false;
        }

        markerB = ARToolKit.getInstance().addMarker("single;Data/multi/patt.b;80");
        if (markerB < 0) {
            Log.e(TAG, "Unable to load markerB");
            return false;
        }

        markerC = ARToolKit.getInstance().addMarker("single;Data/multi/patt.c;80");
        if (markerC < 0) {
            Log.e(TAG, "Unable to load markerC");
            return false;
        }

        markerD = ARToolKit.getInstance().addMarker("single;Data/multi/patt.d;80");
        if (markerD < 0) {
            Log.e(TAG, "Unable to load markerD");
            return false;
        }

        markerF = ARToolKit.getInstance().addMarker("single;Data/multi/patt.f;80");
        if (markerF < 0) {
            Log.e(TAG, "Unable to load markerF");
            return false;
        }

        markerG = ARToolKit.getInstance().addMarker("single;Data/multi/patt.g;80");
        if (markerG < 0) {
            Log.e(TAG, "Unable to load markerG");
            return false;
        }

        markerArray.add(markerA);
        markerArray.add(markerB);
        markerArray.add(markerC);
        markerArray.add(markerD);
        markerArray.add(markerF);
        markerArray.add(markerG);

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
        SimpleNativeRenderer.demoDrawFrame();

        if (counter.frame()) Log.i("demo", counter.toString());

        Map<Integer, float[]> transformationMatrixPerVisibleMarker = storeTransformationMatrixPerVisibleMarker();

        workWithVisibleMarkers(gl, transformationMatrixPerVisibleMarker);
    }

    private void workWithVisibleMarkers(GL10 gl, Map<Integer, float[]> transformationArray) {
        for (Map.Entry<Integer, float[]> entry : transformationArray.entrySet()) {
            if (entry.getKey() == markerA) {
                gl.glLoadMatrixf(entry.getValue(), 0);

                gl.glPushMatrix();
                gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                cube1.draw(gl);
                gl.glPopMatrix();
            }

            if (entry.getKey() == markerB) {
                gl.glLoadMatrixf(entry.getValue(), 0);

                gl.glPushMatrix();
                gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                cube2.draw(gl);
                gl.glPopMatrix();
            }

            if (entry.getKey() == markerC) {
                gl.glLoadMatrixf(entry.getValue(), 0);

                gl.glPushMatrix();
                gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                cube3.draw(gl);
                gl.glPopMatrix();
            }

            if (entry.getKey() == markerD) {
                gl.glLoadMatrixf(entry.getValue(), 0);

                gl.glPushMatrix();
                gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                cube4.draw(gl);
                gl.glPopMatrix();
            }

            if (entry.getKey() == markerF) {
                gl.glLoadMatrixf(entry.getValue(), 0);

                gl.glPushMatrix();
                gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                cube5.draw(gl);
                gl.glPopMatrix();
            }

            if (entry.getKey() == markerG) {
                gl.glLoadMatrixf(entry.getValue(), 0);

                gl.glPushMatrix();
                gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                cube6.draw(gl);
                gl.glPopMatrix();
            }
        }
    }

    private Map<Integer, float[]> storeTransformationMatrixPerVisibleMarker() {
        Map<Integer, float[]> transformationArray = new HashMap<>();

        for (int markerId : markerArray) {
            if (ARToolKit.getInstance().queryMarkerVisible(markerId)) {
                float[] transformation = ARToolKit.getInstance().queryMarkerTransformation(markerId);

                if (transformation != null) {
                    Log.e(TAG, "Found Marker " + markerId + " with transformation " + Arrays.toString(transformation));
                    transformationArray.put(markerId, transformation);
                }
            } else {
                transformationArray.remove(markerId);
            }
        }
        return transformationArray;
    }
}

