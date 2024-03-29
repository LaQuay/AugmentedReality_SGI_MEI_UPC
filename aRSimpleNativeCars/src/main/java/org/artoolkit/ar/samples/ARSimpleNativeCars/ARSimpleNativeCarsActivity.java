package org.artoolkit.ar.samples.ARSimpleNativeCars;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.artoolkit.ar.base.ARActivity;
import org.artoolkit.ar.base.rendering.ARRenderer;
import org.artoolkit.ar.samples.ARSimpleNativeCars.components.CustomImageButton;

public class ARSimpleNativeCarsActivity extends ARActivity {
    private SimpleNativeRenderer simpleNativeRenderer = new SimpleNativeRenderer();
    private FrameLayout controlLayout;
    private CustomImageButton plusScale;
    private CustomImageButton minusScale;
    private CustomImageButton rightRotation;
    private CustomImageButton leftRotation;
    private CustomImageButton upRotation;
    private CustomImageButton downRotation;
    private CustomImageButton rightTranslation;
    private CustomImageButton leftTranslation;
    private CustomImageButton upTranslation;
    private CustomImageButton downTranslation;
    private TextView nextElement;
    private TextView previousElement;
    private TextView patternInfo;

    private TextView controlRotateText;

    private int[] idsArray;
    private Handler handler = new Handler();
    private int posMarkerSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    }
                }).check();

        setElements();
        setUpListeners();

        // No es sincrono, así que trabajar a partir de aquí
        final Runnable runnableInit = new Runnable() {
            public void run() {
                if (SimpleNativeRenderer.isInitialized()) {
                    Log.e(TAG, "Initialized");
                    startCode();
                } else {
                    Log.e(TAG, "NOT Initialized");
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(runnableInit, 500);

        // Mostrar mensaje si el Marker seleccionado está visible
        final Runnable runnableMarkerVisible = new Runnable() {
            public void run() {
                if (SimpleNativeRenderer.isMarkerVisibleByID(posMarkerSelected)) {
                    String title = "";

                    switch (posMarkerSelected) {
                        case 0:
                            title = "Porsche 911 GT3";
                            break;
                        case 1:
                            title = "Ferrari Modena Spider";
                            break;
                        case 2:
                            title = "Marco Polo Ideale";
                            break;
                        case 3:
                            title = "Chevrolet Camaro Patrol";
                            break;
                    }
                    patternInfo.setText(title);
                    patternInfo.setVisibility(View.VISIBLE);
                } else {
                    patternInfo.setVisibility(View.INVISIBLE);
                }
                handler.postDelayed(this, 250);
            }
        };
        handler.postDelayed(runnableMarkerVisible, 1000);
    }

    private void startCode() {
        idsArray = SimpleNativeRenderer.getArrayMarkersID();

        if (idsArray.length > 0) {
            posMarkerSelected = idsArray[0];
            SimpleNativeRenderer.selectMarkerByID(posMarkerSelected);
        } else {
            Toast.makeText(this, "Failed to initialize MarkersArray", Toast.LENGTH_SHORT).show();
        }
    }

    private void setElements() {
        controlLayout = (FrameLayout) findViewById(R.id.controlLayout);
        controlLayout.addView(getLayoutInflater().inflate(R.layout.control, null));

        plusScale = (CustomImageButton) controlLayout.findViewById(R.id.iv_control_plus_scale);
        minusScale = (CustomImageButton) controlLayout.findViewById(R.id.iv_control_minus_scale);
        rightRotation = (CustomImageButton) controlLayout.findViewById(R.id.iv_control_right_rotation);
        leftRotation = (CustomImageButton) controlLayout.findViewById(R.id.iv_control_left_rotation);
        upRotation = (CustomImageButton) controlLayout.findViewById(R.id.iv_control_up_rotation);
        downRotation = (CustomImageButton) controlLayout.findViewById(R.id.iv_control_down_rotation);

        rightTranslation = (CustomImageButton) controlLayout.findViewById(R.id.iv_control_right_translation);
        leftTranslation = (CustomImageButton) controlLayout.findViewById(R.id.iv_control_left_translation);
        upTranslation = (CustomImageButton) controlLayout.findViewById(R.id.iv_control_up_translation);
        downTranslation = (CustomImageButton) controlLayout.findViewById(R.id.iv_control_down_translation);

        nextElement = (TextView) controlLayout.findViewById(R.id.control_next_element_textbutton);
        previousElement = (TextView) controlLayout.findViewById(R.id.control_previous_element_textbutton);

        controlRotateText = (TextView) controlLayout.findViewById(R.id.control_rotate_text);

        patternInfo = (TextView) controlLayout.findViewById(R.id.tv_control_pattern_info);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpListeners() {
        final Runnable runnableInit2 = new Runnable() {
            public void run() {
                if (plusScale.isPressed()) {
                    SimpleNativeRenderer.changeOffsetScale(posMarkerSelected, 0.02f);
                } else if (minusScale.isPressed()) {
                    SimpleNativeRenderer.changeOffsetScale(posMarkerSelected, -0.02f);
                } else if (rightRotation.isPressed()) {
                    SimpleNativeRenderer.changeOffsetRotationY(posMarkerSelected, -5.0f);
                } else if (leftRotation.isPressed()) {
                    SimpleNativeRenderer.changeOffsetRotationY(posMarkerSelected, 5.0f);
                } else if (upRotation.isPressed()) {
                    SimpleNativeRenderer.changeOffsetRotationY(posMarkerSelected, 5.0f);
                } else if (downRotation.isPressed()) {
                    SimpleNativeRenderer.changeOffsetRotationX(posMarkerSelected, 5.0f);
                } else if (rightTranslation.isPressed()) {
                    SimpleNativeRenderer.changeOffsetTranslation(posMarkerSelected, 10.0f, 0.0f, 0.0f);
                } else if (leftTranslation.isPressed()) {
                    SimpleNativeRenderer.changeOffsetTranslation(posMarkerSelected, -10.0f, 0.0f, 0.0f);
                } else if (upTranslation.isPressed()) {
                    SimpleNativeRenderer.changeOffsetTranslation(posMarkerSelected, 0.0f, 8.0f, 0.0f);
                } else if (downTranslation.isPressed()) {
                    SimpleNativeRenderer.changeOffsetTranslation(posMarkerSelected, 0.0f, -8.0f, 0.0f);
                }
                handler.postDelayed(this, 100);
            }
        };
        handler.postDelayed(runnableInit2, 500);

        nextElement.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectNextModel();
            }
        });

        previousElement.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectPreviousModel();
            }
        });

        controlRotateText.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        SimpleNativeRenderer.changeLightByID(posMarkerSelected);
                        return false;
                    }
                }
        );
    }

    private void selectNextModel() {
        if (posMarkerSelected == idsArray.length - 1) {
            posMarkerSelected = idsArray[0];
        } else {
            posMarkerSelected = idsArray[posMarkerSelected + 1];
        }
        SimpleNativeRenderer.selectMarkerByID(posMarkerSelected);
        Log.e(TAG, "Selecting: " + posMarkerSelected);
    }

    private void selectPreviousModel() {
        if (posMarkerSelected == 0) {
            posMarkerSelected = idsArray[idsArray.length - 1];
        } else {
            posMarkerSelected = idsArray[posMarkerSelected - 1];
        }
        SimpleNativeRenderer.selectMarkerByID(posMarkerSelected);
        Log.e(TAG, "Selecting: " + posMarkerSelected);
    }

    public void onStop() {
        SimpleNativeRenderer.demoShutdown();

        super.onStop();
    }

    @Override
    protected ARRenderer supplyRenderer() {
        return simpleNativeRenderer;
    }

    @Override
    protected FrameLayout supplyFrameLayout() {
        return (FrameLayout) this.findViewById(R.id.rendererLayout);
    }
}