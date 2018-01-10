package org.artoolkit.ar.samples.ARSimpleNativeCars.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomImageButton extends android.support.v7.widget.AppCompatImageButton {
    private boolean isPressed;

    public CustomImageButton(Context context) {
        super(context);

        init();
    }

    public CustomImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CustomImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        isPressed = false;
    }

    @Override
    public boolean isPressed() {
        return isPressed;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                return true;
            case MotionEvent.ACTION_UP:
                isPressed = false;
                return true;
        }
        return false;
    }
}
