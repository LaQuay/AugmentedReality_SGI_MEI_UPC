package dev.cavi.reality.augmented.ar_sgi;

import android.util.Log;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.rendering.ARRenderer;
import org.artoolkit.ar.base.rendering.Cube;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

public class MyRenderer extends ARRenderer {
    private String TAG = MyRenderer.class.getSimpleName();
    private int markerHiro = -1;
    private int markerKanji = -1;
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
    private Cube cube7 = new Cube(70.0f, 0.0f, 0.0f, 20.0f);
    private Cube cube8 = new Cube(80.0f, 0.0f, 0.0f, 20.0f);
    private float angle = 0.0f;
    private boolean spinning = false;

    @Override
    public boolean configureARScene() {
        markerHiro = ARToolKit.getInstance().addMarker("single;Data/patt.hiro;80");
        if (markerHiro < 0) {
            Log.e(TAG, "Unable to load markerHiro");
            return false;
        }

        markerKanji = ARToolKit.getInstance().addMarker("single;Data/patt.kanji;80");
        if (markerKanji < 0) {
            Log.e(TAG, "Unable to load markerKanji");
            return false;
        }

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

        markerArray.add(markerHiro);
        markerArray.add(markerKanji);
        markerArray.add(markerA);
        markerArray.add(markerB);
        markerArray.add(markerC);
        markerArray.add(markerD);
        markerArray.add(markerF);
        markerArray.add(markerG);

        return true;
    }

    public void click() {
        spinning = !spinning;
    }

    public void draw(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadMatrixf(ARToolKit.getInstance().getProjectionMatrix(), 0);

        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glFrontFace(GL10.GL_CW);

        gl.glMatrixMode(GL10.GL_MODELVIEW);

        Map<Integer, float[]> transformationMatrixPerVisibleMarker = storeTransformationMatrixPerVisibleMarker();

        workWithVisibleMarkers(gl, transformationMatrixPerVisibleMarker);
    }

    private void workWithVisibleMarkers(GL10 gl, Map<Integer, float[]> transformationArray) {
        for (Map.Entry<Integer, float[]> entry : transformationArray.entrySet()) {
            if (entry.getKey() == markerHiro) {
                Log.e(TAG, "MarkerHiro - Drawing");
                gl.glLoadMatrixf(entry.getValue(), 0);

                gl.glPushMatrix();
                gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                cube1.draw(gl);
                gl.glPopMatrix();
            }

            if (entry.getKey() == markerKanji) {
                Log.e(TAG, "markerKanji - Drawing");
                gl.glLoadMatrixf(entry.getValue(), 0);

                gl.glPushMatrix();
                gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                cube2.draw(gl);
                gl.glPopMatrix();
            }

            if (entry.getKey() == markerA) {
                Log.e(TAG, "markerA - Drawing");
                gl.glLoadMatrixf(entry.getValue(), 0);

                gl.glPushMatrix();
                gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                cube3.draw(gl);
                gl.glPopMatrix();
            }

            if (entry.getKey() == markerB) {
                Log.e(TAG, "markerB - Drawing");
                gl.glLoadMatrixf(entry.getValue(), 0);

                gl.glPushMatrix();
                gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                cube4.draw(gl);
                gl.glPopMatrix();
            }

            if (entry.getKey() == markerC) {
                Log.e(TAG, "markerC - Drawing");
                gl.glLoadMatrixf(entry.getValue(), 0);

                gl.glPushMatrix();
                gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                cube5.draw(gl);
                gl.glPopMatrix();
            }

            if (entry.getKey() == markerD) {
                Log.e(TAG, "markerD - Drawing");
                gl.glLoadMatrixf(entry.getValue(), 0);

                gl.glPushMatrix();
                gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                cube6.draw(gl);
                gl.glPopMatrix();
            }

            if (entry.getKey() == markerF) {
                Log.e(TAG, "markerF - Drawing");
                gl.glLoadMatrixf(entry.getValue(), 0);

                gl.glPushMatrix();
                gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                cube7.draw(gl);
                gl.glPopMatrix();
            }

            if (entry.getKey() == markerG) {
                Log.e(TAG, "markerG - Drawing");
                gl.glLoadMatrixf(entry.getValue(), 0);

                gl.glPushMatrix();
                gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                cube8.draw(gl);
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