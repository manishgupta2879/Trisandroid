package com.pcs.tim.myapplication.new_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.pcs.tim.myapplication.DataService;
import com.pcs.tim.myapplication.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;

import android.os.Bundle;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;


import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;
import com.pcs.tim.myapplication.Utilities;
import com.pcs.tim.myapplication.new_added_classes.GraphicFaceTracker;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.Manifest.permission.CAMERA;

import static com.pcs.tim.myapplication.MainActivity.MY_RC_RMB_ME;

import org.json.JSONObject;


public class CameraCaptureActivity extends AppCompatActivity implements SurfaceHolder.Callback, CameraSource.PictureCallback {

    String mImagePath;
    ImageView faceLine;
    File output = null;
    AnimatorSet animatorSet;
    float startY;
    float targetYUp;
    float targetYDown;
    RelativeLayout photoLay, capturePhotoLay;
    TextView tvText;
    ImageView faceImage;
    SharedPreferences sharedPreferences;
    boolean captureFlag = true;
    boolean loginFlag = false;


    public static final int CAMERA_REQUEST = 101;
    public static Bitmap bitmap;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private String[] neededPermissions = new String[]{CAMERA};
    private FaceDetector detector;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_capture);

        sharedPreferences = getSharedPreferences(Utilities.MY_RC_SHARE_PREF, MODE_PRIVATE);

        tvText = findViewById(R.id.tv_capture);
        surfaceView = findViewById(R.id.surfaceView);
        photoLay = findViewById(R.id.photoLay);
        capturePhotoLay = findViewById(R.id.capture_photo_lay);
        faceLine = findViewById(R.id.face_line);
        faceImage = findViewById(R.id.faceImage);


        Intent intent = getIntent();

        loginFlag = intent.getBooleanExtra("loginFlag", false);

        Log.d("LoginFlagValue__", "onPostExecute: CameraCaptureActivity " + loginFlag);




        startY = 0.0f;//faceLine.getY(); // Original Y position
        targetYUp = startY - 1100;  // Move up by 1100 pixels
        targetYDown = startY + 1100; // Move down by 1100 pixels


        ObjectAnimator moveUpAnimator = ObjectAnimator.ofFloat(faceLine, "translationY", targetYUp);
        moveUpAnimator.setDuration(1000); // Animation duration in milliseconds
        ObjectAnimator moveDownAnimator = ObjectAnimator.ofFloat(faceLine, "translationY", targetYDown);
        moveDownAnimator.setDuration(1000); // Animation duration in milliseconds

        animatorSet = new AnimatorSet();
        animatorSet.playSequentially(moveUpAnimator, moveDownAnimator);
        animatorSet.setInterpolator(new LinearInterpolator());
        moveUpAnimator.setRepeatMode(ValueAnimator.REVERSE);
        moveUpAnimator.setRepeatCount(ValueAnimator.INFINITE);
        moveDownAnimator.setRepeatMode(ValueAnimator.REVERSE);
        moveDownAnimator.setRepeatCount(ValueAnimator.INFINITE);



        detector = new FaceDetector.Builder(this)
                .setProminentFaceOnly(true) // optimize for single, relatively large face
                .setTrackingEnabled(true) // enable face tracking
                .setClassificationType(/* eyes open and smile */ FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE) // for one face this is OK
                .build();

        if (!detector.isOperational()) {
            Log.w("MainActivity", "Detector Dependencies are not yet available");
        } else {
            Log.w("MainActivity", "Detector Dependencies are available");
            if (surfaceView != null) {
                boolean result = checkPermission();
                if (result) {
                    setViewVisibility(R.id.tv_capture);
                    setViewVisibility(R.id.surfaceView);
                    setupSurfaceHolder();
                }
            }
        }
    }

    private boolean checkPermission() {
        ArrayList<String> permissionsNotGranted = new ArrayList<>();
        for (String permission : neededPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNotGranted.add(permission);
            }
        }
        if (!permissionsNotGranted.isEmpty()) {
            boolean shouldShowAlert = false;
            for (String permission : permissionsNotGranted) {
                shouldShowAlert = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
            }
            if (shouldShowAlert) {
                showPermissionAlert(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]));
            } else {
                requestPermissions(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]));
            }
            return false;
        }
        return true;
    }

    private void showPermissionAlert(final String[] permissions) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle(R.string.permission_required);
        alertBuilder.setMessage(R.string.permission_message);
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(permissions);
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(CameraCaptureActivity.this, permissions, CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(CameraCaptureActivity.this, R.string.permission_warning, Toast.LENGTH_LONG).show();
                    //setViewVisibility(R.id.showPermissionMsg);
                    checkPermission();
                    return;
                }
            }
            setViewVisibility(R.id.tv_capture);
            setViewVisibility(R.id.surfaceView);
            setupSurfaceHolder();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setViewVisibility(int id) {
        View view = findViewById(id);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void setupSurfaceHolder() {
        cameraSource = new CameraSource.Builder(this, detector)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(2.0f)
                .setAutoFocusEnabled(true)
                .build();

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    public void captureImage() {
        // We add a delay of 200ms so that image captured is stable.
        if (captureFlag) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            clickImage();
                            captureFlag = false;
                        }
                    });
                }
            }, 200);
        }
    }

    private void clickImage() {
        if (cameraSource != null) {
            cameraSource.takePicture(null, this);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startCamera();
    }

    private void startCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            cameraSource.start(surfaceHolder);
            detector.setProcessor(new LargestFaceFocusingProcessor(detector,
                    new GraphicFaceTracker(this)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        cameraSource.stop();
    }

    @Override
    public void onPictureTaken(byte[] bytes) {
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


        Log.d("imageCapture__", "onPictureTaken: ");


        try {
            File imageFile = createImageFile();

            // Save the bitmap to the imageFile
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            mImagePath = imageFile.getAbsolutePath();
            Log.d("imageCapture__", "onPictureTaken: " + mImagePath);

            try {

                // Check the image's Exif orientation information
                ExifInterface exifInterface = new ExifInterface(new ByteArrayInputStream(bytes));

                String orientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);

                bitmap = rotateBitmap(bitmap, Integer.parseInt(orientation));
                surfaceView.setVisibility(View.GONE);

                faceImage.setVisibility(View.VISIBLE);
                faceImage.setImageBitmap(bitmap);

               /* ExifInterface exif = new ExifInterface(mImagePath);
                    //int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_ROTATE_270);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(270);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
*/

                imageFile = createImageFile();

                // Save the bitmap to the imageFile
                outputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();

                mImagePath = imageFile.getAbsolutePath();

                // Now you have a rotatedBitmap with the correct orientation.
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Call your function to upload the image

            //  if (captureFlag) {
            new FaceRecognitionVsMany().execute();
            //    captureFlag = false;
            //  }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("imageCapture__", "uploadImage: error" + e.getMessage());
        }


    }

    private Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return rotatedBitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = Long.toString(System.currentTimeMillis());


        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "VeriMyRC");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("VeriMyRC", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                mediaStorageDir /* directory */
        );
        output = image.getAbsoluteFile();


        return image;
    }


    private class FaceRecognitionVsMany extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(CameraCaptureActivity.this);

        @Override
        protected void onPreExecute() {
            //set message of the dialog
            //  asyncDialog.setMessage(getString(R.string.verifying));
            //show dialog
            //  asyncDialog.show();
            //  asyncDialog.setCancelable(false);
            photoLay.setVisibility(View.GONE);
            capturePhotoLay.setVisibility(View.VISIBLE);

          /*  AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.move_up_down);
            animatorSet.setTarget(faceLine);
            animatorSet.start();*/

            animatorSet.start();


            tvText.setText("Verifying...");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            try {


                if (mImagePath.isEmpty()) {
                    return "no mImagePath";
                }


                //  if (threadFlag) {
                String result = DataService.UploadFileWithBase64String("9", mImagePath);
                Log.d("yyy___1", result);
                if (result != null && result != "ERROR") {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        String dataImage = jsonObject.getString("data");

                        dataImage = dataImage.replace("http://192.168.0.200/", "http://211.24.73.117/");
                        Log.d("photo__1", "doInBackground: " + sharedPreferences.getString(Utilities.LOGIN_POLICE_PHOTO, ""));
                        Log.d("photo__2", "doInBackground: " + dataImage);

                        HashMap<String, String> verifyData = new HashMap<>();
                        verifyData.put("SourceImageURL", dataImage);


                        verifyData.put("TargetImageURL", sharedPreferences.getString(Utilities.LOGIN_POLICE_PHOTO, ""));

                        //verifyData.put("YearOfBirth",yearOfBirth);


                        String resultVerifyFace1VMany = Utilities.sendPostRequest("http://211.24.73.117/HIKWS.asmx/CompareFace1v1Test", verifyData, null, null);
                        //  String resultVerifyFace1VMany = Utilities.sendPostRequest("http://" + Utilities.IP_NoPort + "/HIKWS.asmx/CompareFaceAllv2", verifyData, null, null);

                        Log.d("photo__", "doInBackground: 1" + resultVerifyFace1VMany);

                        resultVerifyFace1VMany = resultVerifyFace1VMany.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
                        resultVerifyFace1VMany = resultVerifyFace1VMany.replace("<string xmlns=\"http://tris.my/webservice\">", "");
                        resultVerifyFace1VMany = resultVerifyFace1VMany.replace("</string>", "");
                        resultVerifyFace1VMany = resultVerifyFace1VMany.replace("%", "");
                        Log.d("photo__", "doInBackground: 2" + resultVerifyFace1VMany);
                        //    threadFlag=false;

                        return resultVerifyFace1VMany;
                    } else {
                        return "ERROR";
                    }
                } else {
                    return "ERROR";
                }

                //      }


            } catch (Exception e) {
                captureFlag = true;
                return e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                photoLay.setVisibility(View.VISIBLE);
                capturePhotoLay.setVisibility(View.GONE);
                faceImage.setVisibility(View.GONE);
                animatorSet.cancel();
                surfaceView.setVisibility(View.VISIBLE);
                faceLine.setY(startY);

                tvText.setText("Blink your eyes to capture Photo");
                asyncDialog.dismiss();
                Log.d("xxx___dd", result);
                if (result != null && result != "ERROR") {
                    float res = Float.parseFloat(result);
                    if (res >= 90) {


                        Toast.makeText(CameraCaptureActivity.this, "Verified Successfully..", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putBoolean(MY_RC_RMB_ME, loginFlag);

                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), PoliceVerifiedActivty.class);
                        intent.putExtra("photoPath", mImagePath);
                        intent.putExtra("policeName", sharedPreferences.getString(Utilities.LOGIN_POLICE_NAME, ""));
                        //intent.putExtra("refugee_list", refugeeArrayList);

                        startActivity(intent);
                        finish();
                    } else {
                        captureFlag = true;
                        Toast.makeText(getApplicationContext(), "Face recognition failed.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    photoLay.setVisibility(View.VISIBLE);
                    capturePhotoLay.setVisibility(View.GONE);
                    faceImage.setVisibility(View.GONE);
                    surfaceView.setVisibility(View.VISIBLE);
                    animatorSet.cancel();
                    tvText.setText("Blink your eyes to capture Photo");
                    captureFlag = true;
                    Toast.makeText(getApplicationContext(), "Please try again.", Toast.LENGTH_LONG).show();

                }


            } catch (Exception ex) {
                // asyncDialog.dismiss();
                animatorSet.cancel();
                photoLay.setVisibility(View.VISIBLE);
                capturePhotoLay.setVisibility(View.GONE);
                faceImage.setVisibility(View.GONE);
                surfaceView.setVisibility(View.VISIBLE);

                tvText.setText("Blink your eyes to capture Photo");
                Toast.makeText(getApplicationContext(), "No result found.", Toast.LENGTH_LONG).show();
                //threadFlag =true;
                captureFlag = true;
                ex.printStackTrace();
            }
        }
    }

}