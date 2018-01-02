/*
 *  ARToolKit.java
 *  ARToolKit5
 *
 *  This file is part of ARToolKit.
 *
 *  ARToolKit is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ARToolKit is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with ARToolKit.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  As a special exception, the copyright holders of this library give you
 *  permission to link this library with independent modules to produce an
 *  executable, regardless of the license terms of these independent modules, and to
 *  copy and distribute the resulting executable under terms of your choice,
 *  provided that you also meet, for each linked independent module, the terms and
 *  conditions of the license of that module. An independent module is a module
 *  which is neither derived from nor based on this library. If you modify this
 *  library, you may extend this exception to your version of the library, but you
 *  are not obligated to do so. If you do not wish to do so, delete this exception
 *  statement from your version.
 *
 *  Copyright 2015 Daqri, LLC.
 *  Copyright 2011-2015 ARToolworks, Inc.
 *
 *  Author(s): Julian Looser, Philip Lamb
 *
 */

package org.artoolkit.ar.base;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.Matrix;
import android.util.Log;

/**
 * The ARToolKit class is a singleton which manages access to the underlying
 * native functions defined in the {@link NativeInterface} class. Native calls
 * should be made only from within this class so that adequate error checking
 * and correct type conversion can take place.
 */
public class ARToolKit {
    /**
     * Android logging tag for this class.
     */
    private static final String TAG = "ARToolKit";
    /**
     * Set to true only once the native library has been loaded.
     */
    private static boolean loadedNative = false;
    private static boolean initedNative = false;
    /**
     * Single instance of the ARToolKit class.
     */
    private static ARToolKit instance = null;

    static {
        loadedNative = NativeInterface.loadNativeLibrary();
        if (!loadedNative) Log.e(TAG, "Loading native library failed!");
        else Log.i(TAG, "Loaded native library.");
    }

    private int frameWidth;
    private int frameHeight;
    private int cameraIndex;
    private boolean cameraIsFrontFacing;
    /**
     * Array of RGB color values containing the debug video image data.
     */
    private byte[] debugImageData;
    /**
     * Array of int color values containing the debug video image data.
     */
    private int[] debugImageColors;
    private Bitmap debugBitmap = null;

    /**
     * Private constructor as required by the singleton pattern.
     */
    private ARToolKit() {
        Log.i(TAG, "ARToolKit(): ARToolKit constructor");
    }

    /**
     * Implementation of the singleton pattern to provide a sole instance of the ARToolKit class.
     *
     * @return The single instance of ARToolKit.
     */
    public static ARToolKit getInstance() {
        if (instance == null) instance = new ARToolKit();
        return instance;
    }

    /**
     * Initialises the native code library if it is available.
     *
     * @param resourcesDirectoryPath The full path (in the filesystem) to the directory to be used by the
     *                               native routines as the base for relative references.
     *                               e.g. Activity.getContext().getCacheDir().getAbsolutePath()
     *                               or Activity.getContext().getFilesDir().getAbsolutePath()
     * @return true if the library was found and successfully initialised.
     */
    public boolean initialiseNative(String resourcesDirectoryPath) {
        if (!loadedNative) return false;
        if (!NativeInterface.arwInitialiseAR()) {
            Log.e(TAG, "initialiseNative(): Error initialising native library!");
            return false;
        }
        Log.i(TAG, "initialiseNative(): ARToolKit version: " + NativeInterface.arwGetARToolKitVersion());
        if (!NativeInterface.arwChangeToResourcesDir(resourcesDirectoryPath)) {
            Log.i(TAG, "initialiseNative(): Error while attempting to change working directory to resources directory.");
        }
        initedNative = true;
        return true;
    }

    /**
     * Returns whether the native library was found and successfully initialised.
     * Native functions will not be called unless this is true.
     *
     * @return true if native functions are available.
     */
    public boolean nativeInitialised() {
        return initedNative;
    }

    /**
     * Initialises the ARToolKit using the specified video size.
     *
     * @param videoWidth     The width of the video image in pixels.
     * @param videoHeight    The height of the video image in pixels.
     * @param cameraParaPath The full path (in the filesystem) to a camera parameter file,
     *                       or the path expressed relative to the resourcesDirectoryPath set in initialiseNative().
     * @return true if initialisation was successful.
     */
    public boolean initialiseAR(int videoWidth, int videoHeight, String cameraParaPath, int cameraIndex, boolean cameraIsFrontFacing) {

        if (!initedNative) {
            Log.e(TAG, "initialiseAR(): Cannot initialise camera because native interface not inited.");
            return false;
        }

        this.frameWidth = videoWidth;
        this.frameHeight = videoHeight;
        this.cameraIndex = cameraIndex;
        this.cameraIsFrontFacing = cameraIsFrontFacing;

        if (!NativeInterface.arwStartRunning("-format=NV21", cameraParaPath, 10.0f, 10000.0f)) {
            Log.e(TAG, "initialiseAR(): Error starting video");
            return false;
        }

        debugImageData = new byte[frameWidth * frameHeight * 4];
        debugImageColors = new int[frameWidth * frameHeight];
        debugBitmap = Bitmap.createBitmap(frameWidth, frameHeight, Bitmap.Config.ARGB_8888);

        return true;
    }

    /**
     * Gets an updated debug image buffer from the native library and uses it to
     * update the local color array. This is then applied to the debug Bitmap, which
     * can be displayed in the user interface.
     *
     * @return The debug image Bitmap.
     */
    public Bitmap updateDebugBitmap() {
        if (!initedNative) return null;

        if (!NativeInterface.arwUpdateDebugTexture(debugImageData, false)) {
            return null;
        }

        int w = debugBitmap.getWidth();
        int h = debugBitmap.getHeight();

        int idx1, idx2;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                idx1 = (y * w + x) * 4;
                idx2 = (y * w + x);
                debugImageColors[idx2] = Color.argb(255 /*debugImageData[idx1+3] */, debugImageData[idx1], debugImageData[idx1 + 1], debugImageData[idx1 + 2]);
                //debugImageColors[idx2] = Color.argb(255, 255, 0, 0);
            }
        }

        debugBitmap.setPixels(debugImageColors, 0, w, 0, 0, w, h);

        return debugBitmap;
    }

    /**
     * Returns the Bitmap containing the ARToolKit debug video image.
     *
     * @return The debug video Bitmap.
     */
    public Bitmap getDebugBitmap() {
        return debugBitmap;
    }

    /**
     * Returns whether the ARToolKit debug video image is enabled.
     *
     * @return Whether the ARToolKit debug video image is enabled.
     */
    public boolean getDebugMode() {
        if (!initedNative) return false;
        return NativeInterface.arwGetVideoDebugMode();
    }

    /**
     * Enables or disables the debug video image in ARToolKit.
     *
     * @param debug Whether or not to enable the debug video image.
     */
    public void setDebugMode(boolean debug) {
        if (!initedNative) return;
        NativeInterface.arwSetVideoDebugMode(debug);
    }

    /**
     * Returns the threshold used to binarize the video image for marker
     * detection.
     *
     * @return The current threshold value in the range 0 to 255, or -1 if the
     * threshold could not be retrieved.
     */
    public int getThreshold() {
        if (!initedNative) return -1;
        return NativeInterface.arwGetVideoThreshold();
    }

    /**
     * Sets the threshold used to binarize the video image for marker detection.
     *
     * @param threshold The new threshold value in the range 0 to 255.
     */
    public void setThreshold(int threshold) {
        if (!initedNative) return;
        NativeInterface.arwSetVideoThreshold(threshold);
    }

    /**
     * Returns the projection matrix calculated from camera parameters.
     *
     * @return Projection matrix as an array of floats in OpenGL style.
     */
    public float[] getProjectionMatrix() {
        if (!initedNative) return null;
        return NativeInterface.arwGetProjectionMatrix();
    }

    /**
     * Adds a new single marker to the set of currently active markers.
     *
     * @param cfg The config string for the used marker. Possible configurations look like this:
     *            `single;data/hiro.patt;80`
     *            `single_buffer;80;buffer=234 221 237...`
     *            `single_barcode;0;80`
     *            `multi;data/multi/marker.dat`
     *            `nft;data/nft/pinball`
     *            (see the Wrapper project for more details: lib/SRC/ARWrapper/ARMarker.cpp/ARMarker::newWithConfig)
     * @return The unique identifier (UID) of the new marker, or -1 on error.
     */
    public int addMarker(String cfg) {
        if (!initedNative) return -1;
        return NativeInterface.arwAddMarker(cfg);
    }

    /**
     * Returns whether the marker with the specified ID is currently visible.
     *
     * @param markerUID The unique identifier (UID) of the marker to query.
     * @return true if the marker is visible and tracked in the current video frame.
     */
    public boolean queryMarkerVisible(int markerUID) {
        if (!initedNative) return false;
        return NativeInterface.arwQueryMarkerVisibility(markerUID);
    }

    /**
     * Returns the transformation matrix for the specifed marker.
     *
     * @param markerUID The unique identifier (UID) of the marker to query.
     * @return Transformation matrix as an array of floats in OpenGL style.
     */
    public float[] queryMarkerTransformation(int markerUID) {
        if (!initedNative) return null;
        return NativeInterface.arwQueryMarkerTransformation(markerUID);
    }

    /**
     * Returns true when video and marker detection are running.
     *
     * @return true when video and marker detection are running, otherwise false
     */
    public boolean isRunning() {
        if (!initedNative) return false;
        return NativeInterface.arwIsRunning();
    }

    /**
     * Takes an incoming frame from the Android camera and passes it to native
     * code for conversion and marker detection.
     *
     * @param frame New video frame to process.
     * @return true if successful, otherwise false.
     */
    public boolean convertAndDetect(byte[] frame) {

        if (!initedNative) return false;
        if (frame == null) return false;
        if (!NativeInterface.arwAcceptVideoImage(frame, frameWidth, frameHeight, cameraIndex, cameraIsFrontFacing))
            return false;
        if (!NativeInterface.arwCapture()) return false;
        return NativeInterface.arwUpdateAR();
    }

    /**
     * Instructs the native code to free resources.
     */
    public void cleanup() {

        if (!initedNative) return;

        NativeInterface.arwStopRunning();
        NativeInterface.arwShutdownAR();

        debugBitmap.recycle();
        debugBitmap = null;

        initedNative = false;
    }

    public float getBorderSize() {
        return NativeInterface.arwGetBorderSize();
    }

    public void setBorderSize(float size) {
        NativeInterface.arwSetBorderSize(size);
    }

    /**
     * Calculates the reference matrix for the given markers. First marker is the base.
     *
     * @param idMarkerBase Reference base
     * @param idMarker2    Marker that will be depending on that base
     * @return Matrix that contains the transformation from @idMarkerBase to @idMarker2
     */
    public float[] calculateReferenceMatrix(int idMarkerBase, int idMarker2) {
        float[] referenceMarkerTranslationMatrix = this.queryMarkerTransformation(idMarkerBase);
        float[] secondMarkerTranslationMatrix = this.queryMarkerTransformation(idMarker2);

        if (referenceMarkerTranslationMatrix != null && secondMarkerTranslationMatrix != null) {
            float[] invertedMatrixOfReferenceMarker = new float[16];

            Matrix.invertM(invertedMatrixOfReferenceMarker, 0, referenceMarkerTranslationMatrix, 0);

            float[] transformationFromMarker1ToMarker2 = new float[16];
            Matrix.multiplyMM(transformationFromMarker1ToMarker2, 0, invertedMatrixOfReferenceMarker, 0, secondMarkerTranslationMatrix, 0);

            return transformationFromMarker1ToMarker2;
        } else {
            //It seems like ARToolkit might be faster with updating then the Android part. Because of that
            //it can happen that, even though one ensured in there Android-App that both markers are visible,
            //ARToolkit might not return a transformation matrix for both markers. In that case this RuntimeException is thrown.
            Log.e(TAG, "calculateReferenceMatrix(): Currently there are no two markers visible at the same time");
            return null;
        }
    }

    /**
     * Calculated the distance between two markers
     *
     * @param referenceMarker Reference base. Marker from which the distance is calculated
     * @param markerId2       Marker to which the distance is calculated
     * @return distance
     */
    public float distance(int referenceMarker, int markerId2) {

        float[] referenceMatrix = calculateReferenceMatrix(referenceMarker, markerId2);

        if (referenceMatrix != null) {
            float distanceX = referenceMatrix[12];
            float distanceY = referenceMatrix[13];
            float distanceZ = referenceMatrix[14];

            Log.d(TAG, "distance(): Marker distance: x: " + distanceX + " y: " + distanceY + " z: " + distanceZ);
            float length = Matrix.length(distanceX, distanceY, distanceZ);
            Log.d(TAG, "distance(): Absolute distance: " + length);

            return length;
        }
        return 0;
    }

    /**
     * Calculates the position depending on the referenceMarker
     *
     * @param referenceMarkerId           Reference marker id
     * @param markerIdToGetThePositionFor Id of the marker for which the position is calculated
     * @return Position vector with length 4 x,y,z,1
     */
    public float[] retrievePosition(int referenceMarkerId, int markerIdToGetThePositionFor) {
        float[] initialVector = {1f, 1f, 1f, 1f};
        float[] positionVector = new float[4];

        float[] transformationMatrix = calculateReferenceMatrix(referenceMarkerId, markerIdToGetThePositionFor);
        if (transformationMatrix != null) {
            Matrix.multiplyMV(positionVector, 0, transformationMatrix, 0, initialVector, 0);
            return positionVector;
        }
        return null;
    }
}
