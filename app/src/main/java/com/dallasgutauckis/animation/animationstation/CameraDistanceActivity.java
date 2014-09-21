package com.dallasgutauckis.animation.animationstation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Sample showing the effect of camera distance on rotationX/rotationY
 */
public class CameraDistanceActivity extends Activity {
    private View     card;
    private TextView cameraDistanceText;
    private TextView cameraDistanceMultiplierText;
    private SeekBar  seekBar;

    private float defaultCameraDistance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_distance);

        cameraDistanceText = (TextView) findViewById(R.id.camera_distance_text);
        cameraDistanceMultiplierText = (TextView) findViewById(R.id.camera_distance_multiplier_text);

        card = findViewById(R.id.card);
        card.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                final float cameraDistance = CameraDistance.get(card);

                if (defaultCameraDistance == 0) {
                    defaultCameraDistance = cameraDistance;
                }

                cameraDistanceText.setText("" + cameraDistance);
                seekBar.setProgress((int) (cameraDistance / (defaultCameraDistance / 2)));
            }
        });

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cameraDistanceMultiplierText.setText("" + progress);

                if (fromUser) {
                    card.setCameraDistance(progress * defaultCameraDistance / 2);
                    cameraDistanceText.setText("" + progress * defaultCameraDistance / 2);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        findViewById(R.id.btn_animate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card.clearAnimation();
                card.setRotationY(0);

                AnimatorSet set = new AnimatorSet();
                final ObjectAnimator secondAnimator = ObjectAnimator.ofFloat(card, View.ROTATION_Y, 0);
                secondAnimator.setStartDelay(300);

                set.playSequentially(
                                            ObjectAnimator.ofFloat(card, View.ROTATION_Y, -180),
                                            secondAnimator
                                    );

                set.start();
            }
        });
    }

    private static class CameraDistance {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static float get(View view) {
            return view.getCameraDistance();
        }
    }
}
