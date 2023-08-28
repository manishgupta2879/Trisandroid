package com.pcs.tim.myapplication.new_added_classes;

import android.util.Log;

import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.pcs.tim.myapplication.new_activities.CameraCaptureActivity;


public class GraphicFaceTracker extends Tracker<Face> {

    private static final float OPEN_THRESHOLD = 0.85f;
    private static final float CLOSE_THRESHOLD = 0.4f;
    private final CameraCaptureActivity cameraCaptureActivity;
    private int state = 0;

    public GraphicFaceTracker(CameraCaptureActivity cameraCaptureActivity) {
        this.cameraCaptureActivity = cameraCaptureActivity;
    }

    private void blink(float value) {
        switch (state) {
            case 0:
                if (value > OPEN_THRESHOLD) {
                    // Both eyes are initially open
                    state = 1;
                }
                break;
            case 1:
                if (value < CLOSE_THRESHOLD) {
                    // Both eyes become closed
                    state = 2;
                }
                break;
            case 2:
                if (value > OPEN_THRESHOLD) {
                    // Both eyes are open again
                    Log.i("Camera Demo", "blink has occurred!");
                    state = 0;
                    cameraCaptureActivity.captureImage();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Update the position/characteristics of the face within the overlay.
     */
    @Override
    public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
        float left = face.getIsLeftEyeOpenProbability();
        float right = face.getIsRightEyeOpenProbability();
        if ((left == Face.UNCOMPUTED_PROBABILITY) ||
                (right == Face.UNCOMPUTED_PROBABILITY)) {
            // One of the eyes was not detected.
            return;
        }

        float value = Math.min(left, right);
        blink(value);
    }

}
