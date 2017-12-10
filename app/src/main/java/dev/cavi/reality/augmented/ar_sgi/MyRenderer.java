package dev.cavi.reality.augmented.ar_sgi;

import android.util.Log;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.rendering.ARRenderer;
import org.artoolkit.ar.base.rendering.Cube;

import javax.microedition.khronos.opengles.GL10;

public class MyRenderer extends ARRenderer {
    private String TAG = MyRenderer.class.getSimpleName();
    private int markerHiro = -1;
    private int markerKanji = -1;
    private Cube cube1 = new Cube(50.0f, 0.0f, 0.0f, 20.0f);
    private Cube cube2 = new Cube(60.0f, 0.0f, 0.0f, 10.0f);
    private float angle = 0.0f;
    private boolean spinning = false;

    @Override
    public boolean configureARScene() {
        markerHiro = ARToolKit.getInstance().addMarker("single;Data/patt.hiro;80");
        markerKanji = ARToolKit.getInstance().addMarker("single;Data/patt.kanji;80");

        return markerHiro >= 0 && markerKanji >= 0;
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

        if (ARToolKit.getInstance().queryMarkerVisible(markerHiro)) {
            Log.e(TAG, "MarkerHiro - Drawing");
            gl.glLoadMatrixf(ARToolKit.getInstance().queryMarkerTransformation(markerHiro), 0);

            gl.glPushMatrix();
            gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
            cube1.draw(gl);
            gl.glPopMatrix();

            if (spinning) {
                angle += 5.0f;
            }
        }

        if (ARToolKit.getInstance().queryMarkerVisible(markerKanji)) {
            Log.e(TAG, "markerKanji - Drawing");
            gl.glLoadMatrixf(ARToolKit.getInstance().queryMarkerTransformation(markerKanji), 0);

            gl.glPushMatrix();
            gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
            cube2.draw(gl);
            gl.glPopMatrix();

            if (spinning) {
                angle += 5.0f;
            }
        }
    }
}