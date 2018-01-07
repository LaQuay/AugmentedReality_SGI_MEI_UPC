package org.artoolkit.ar.samples.ARSimpleNativeCars;

import android.Manifest;
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

public class ARSimpleNativeCarsActivity extends ARActivity {
    private SimpleNativeRenderer simpleNativeRenderer = new SimpleNativeRenderer();
    private FrameLayout controlLayout;
    private TextView plusScale;
    private TextView minusScale;
    private TextView rightRotation;
    private TextView leftRotation;
    private TextView upRotation;
    private TextView downRotation;
    private TextView nextElement;
    private TextView previousElement;

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
        final Runnable r = new Runnable() {
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

        handler.postDelayed(r, 500);
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

        plusScale = (TextView) controlLayout.findViewById(R.id.control_plus_scale_textbutton);
        minusScale = (TextView) controlLayout.findViewById(R.id.control_minus_scale_textbutton);
        rightRotation = (TextView) controlLayout.findViewById(R.id.control_right_rotation_textbutton);
        leftRotation = (TextView) controlLayout.findViewById(R.id.control_left_rotation_textbutton);
        upRotation = (TextView) controlLayout.findViewById(R.id.control_up_rotation_textbutton);
        downRotation = (TextView) controlLayout.findViewById(R.id.control_down_rotation_textbutton);

        nextElement = (TextView) controlLayout.findViewById(R.id.control_next_element_textbutton);
        previousElement = (TextView) controlLayout.findViewById(R.id.control_previous_element_textbutton);
    }

    private void setUpListeners() {
        plusScale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        minusScale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        rightRotation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        leftRotation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        upRotation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        downRotation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

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