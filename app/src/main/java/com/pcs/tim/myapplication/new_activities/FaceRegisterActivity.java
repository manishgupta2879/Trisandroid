package com.pcs.tim.myapplication.new_activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.pcs.tim.myapplication.BuildConfig;
import com.pcs.tim.myapplication.DataService;
import com.pcs.tim.myapplication.Face1vManyResultActivity;
import com.pcs.tim.myapplication.R;
import com.pcs.tim.myapplication.Refugee;
import com.pcs.tim.myapplication.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FaceRegisterActivity extends AppCompatActivity {

    Button buttonStart;

    String mImagePath;
    SharedPreferences sharedPreferences;

    boolean permission = false;
    File photoFile;
    Uri photoURI;

    String userChoosenTask;
    boolean loginFlag=false;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    File output = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_register);

        buttonStart =findViewById(R.id.buttonStart);

        Intent intent =getIntent();
        loginFlag=intent.getBooleanExtra("loginFlag",false);

        Log.d("LoginFlagValue__", "onPostExecute: FaceRegisterActivity "+loginFlag);


        PackageManager pm = getPackageManager();
        int hasPerm = pm.checkPermission(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                getPackageName());
        if (hasPerm != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FaceRegisterActivity.this,
                    new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        else
            permission = true;



        sharedPreferences = getSharedPreferences(Utilities.MY_RC_SHARE_PREF, MODE_PRIVATE);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(FaceRegisterActivity.this, CameraCaptureActivity.class);
                intent.putExtra("loginFlag",loginFlag);
                startActivity(intent);
                finish();
               /* if(ContextCompat.checkSelfPermission(FaceRegisterActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(FaceRegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

                    Log.d("cameraCheck__", "onClick: permission granted");
                    selectImage();
                }
                else{

                    Log.d("cameraCheck__", "onClick: not granted");

                    ActivityCompat.requestPermissions(FaceRegisterActivity.this,
                            new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }*/
            }
        });
    }


    public void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(FaceRegisterActivity.this);
        builder.setTitle("Choose Options");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (ContextCompat.checkSelfPermission(FaceRegisterActivity.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (ContextCompat.checkSelfPermission(FaceRegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void galleryIntent() {
        try{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void cameraIntent() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            photoFile = createImageFile();
            photoURI = FileProvider.getUriForFile(FaceRegisterActivity.this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, REQUEST_CAMERA);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = Long.toString(System.currentTimeMillis());


        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "VeriMyRC");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Log.d("imageData__", "onActivityResult: " + data);
                onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    private void onCaptureImageResult(Intent data) {
        try {
            // Bitmap thumbnail = null;


            ContentResolver cr = getContentResolver();
            cr.notifyChange(photoURI, null);
            mImagePath = output.getAbsolutePath();

            Bitmap bitmap = Utilities.getImage(cr,photoURI, mImagePath);


            FaceDetector faceDetector = new
                    FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false)
                    .build();
            if(!faceDetector.isOperational()){
                new android.app.AlertDialog.Builder(getApplicationContext()).setMessage("Could not set up the face detector!").show();
                File destination = new File(getFilesDir().getAbsolutePath(),
                        System.currentTimeMillis() + ".jpg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                FileOutputStream fo;
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                mImagePath = destination.getAbsolutePath();
                Log.d("xxx",mImagePath);
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {

                Paint myRectPaint = new Paint();
                myRectPaint.setStrokeWidth(5);
                myRectPaint.setColor(Color.rgb(50,205,50));
                myRectPaint.setStyle(Paint.Style.STROKE);
                Log.d("xxx","123");
                Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
                Canvas tempCanvas = new Canvas(tempBitmap);
                tempCanvas.drawBitmap(bitmap, 0, 0, null);

                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<Face> faces = faceDetector.detect(frame);



                for (int i = 0; i < faces.size(); i++) {
                    Face thisFace = faces.valueAt(i);
                    float x1 = thisFace.getPosition().x;
                    float y1 = thisFace.getPosition().y;
                    float x2 = x1 + thisFace.getWidth();
                    float y2 = y1 + thisFace.getHeight();
                    if(x1>=0 && y1 >= 0) {
                        tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
                        Bitmap croppedImage = Bitmap.createBitmap(bitmap, (int) x1 + 1, (int) y1 + 1, (int) thisFace.getWidth() + 1, (int) thisFace.getHeight() + 1);

                    }
                }


            }

            new FaceRecognitionVsMany().execute();

        }
        catch (Exception e)
        {
            Log.d("xxxxerror",e.getMessage());
            Toast.makeText(this, "Failed to load image, please try again", Toast.LENGTH_SHORT).show();
        }

    }

    private void onSelectFromGalleryResult(Intent data) {

        try {
            Bitmap bm = null;
            if (data != null) {
                try {

                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    Uri selectedImageURI = data.getData();
                    mImagePath = Utilities.getRealPathFromURI(getApplicationContext(), selectedImageURI);
                    Log.d("imageData__", "onSelectFromGalleryResult: "+mImagePath);
                    ContentResolver cr = getContentResolver();
                    cr.notifyChange(selectedImageURI, null);

                    Bitmap bitmap = Utilities.getImage(cr, selectedImageURI, mImagePath);


                    FaceDetector faceDetector = new
                            FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false)
                            .build();
                    if (!faceDetector.isOperational()) {
                        new android.app.AlertDialog.Builder(getApplicationContext()).setMessage("Could not set up the face detector!").show();
                        File destination = new File(getFilesDir().getAbsolutePath(),
                                System.currentTimeMillis() + ".jpg");
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        FileOutputStream fo;
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        mImagePath = destination.getAbsolutePath();

                        try {
                            destination.createNewFile();
                            fo = new FileOutputStream(destination);
                            fo.write(bytes.toByteArray());
                            fo.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {

                        Paint myRectPaint = new Paint();
                        myRectPaint.setStrokeWidth(5);
                        myRectPaint.setColor(Color.rgb(50, 205, 50));
                        myRectPaint.setStyle(Paint.Style.STROKE);

                        Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
                        Canvas tempCanvas = new Canvas(tempBitmap);
                        tempCanvas.drawBitmap(bitmap, 0, 0, null);

                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray<Face> faces = faceDetector.detect(frame);


                        for (int i = 0; i < faces.size(); i++) {
                            Face thisFace = faces.valueAt(i);
                            float x1 = thisFace.getPosition().x;
                            float y1 = thisFace.getPosition().y;
                            float x2 = x1 + thisFace.getWidth();
                            float y2 = y1 + thisFace.getHeight();
                            if (x1 >= 0 && y1 >= 0) {
                                tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
                                Bitmap croppedImage = Bitmap.createBitmap(bitmap, (int) x1 + 1, (int) y1 + 1, (int) thisFace.getWidth() + 1, (int) thisFace.getHeight() + 1);

                            }
                        }
                        //imageListAdapter.notifyDataSetChanged();

                    }
//
//            thumbnail = Utilities.compressImage(mImagePath);
//            refugeePhoto = thumbnail;

                    /*if (croppedImgList.size() > 0) {

                        for (int i = 0; i < croppedImgList.size(); i++) {
                            FileOutputStream fo;
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            File destination = new File(getFilesDir().getAbsolutePath(),
                                    System.currentTimeMillis() + Integer.toString(i) + ".jpg");

                            mImagePath = destination.getAbsolutePath();

                            try {
                                destination.createNewFile();
                                fo = new FileOutputStream(destination);
                                fo.write(bytes.toByteArray());
                                fo.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }*/
                    new FaceRecognitionVsMany().execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch(Exception ex){
            Toast.makeText(this, "Failed to load image, please try again", Toast.LENGTH_SHORT).show();
        }
        // refugeePhoto = bm;
    }


    private class FaceRecognitionVsMany extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(FaceRegisterActivity.this);

        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setMessage(getString(R.string.loadingtype));
            //show dialog
            asyncDialog.show();
            asyncDialog.setCancelable(false);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            try {

//                String imageUploadPath = System.currentTimeMillis() + ".jpg";
//                FTPClient mFtpClient = Utilities.connnectingwithFTP(Utilities.IP_NoPort, "user", "1234");
//                Utilities.uploadFile(mFtpClient, new File(mImagePath), imageUploadPath);
                if(mImagePath.isEmpty()){
                    return "no mImagePath";
                }

                Log.d("image__!", "doInBackground: "+mImagePath);
                String result = DataService.UploadFileWithBase64String("9",mImagePath);
                Log.d("yyy",result);
                if (result != null && result != "ERROR") {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getString("success").equalsIgnoreCase("true")){
                        String dataImage = jsonObject.getString("data");
                        Log.d("photo__", "doInBackground: "+ sharedPreferences.getString(Utilities.LOGIN_POLICE_PHOTO,""));  ;
                        HashMap<String, String> verifyData = new HashMap<>();
                        verifyData.put("SourceImageURL",dataImage);
//                verifyData.put("Country", country);
//                        if(gender.equals("ANY")){
//                            gender = "";
//                        }
//                        verifyData.put("Gender", gender);
//
//                        verifyData.put("DobFrom",yearOfBirthFrom);
//
//                        verifyData.put("DobTo",yearOfBirthTo);

                       // verifyData.put("TargetImageURL","http://211.24.73.117//Upload/9/20230821141830135.JPG");
                        verifyData.put("TargetImageURL",sharedPreferences.getString(Utilities.LOGIN_POLICE_PHOTO,""));
                        //verifyData.put("YearOfBirth",yearOfBirth);
                        Utilities utilities = new Utilities();

                      String resultVerifyFace1VMany = Utilities.sendPostRequest("http://211.24.73.117/HIKWS.asmx/CompareFace1v1Test", verifyData, null, null);
                      //  String resultVerifyFace1VMany = Utilities.sendPostRequest("http://" + Utilities.IP_NoPort + "/HIKWS.asmx/CompareFaceAllv2", verifyData, null, null);

                        Log.d("photo__", "doInBackground: "+resultVerifyFace1VMany);

                        resultVerifyFace1VMany = resultVerifyFace1VMany.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
                        resultVerifyFace1VMany = resultVerifyFace1VMany.replace("<string xmlns=\"http://tris.my/webservice\">", "");
                        resultVerifyFace1VMany = resultVerifyFace1VMany.replace("</string>", "");
                        resultVerifyFace1VMany = resultVerifyFace1VMany.replace("%", "");


                        return resultVerifyFace1VMany;
                    }else{
                        return "ERROR";
                    }
                }else{
                    return "ERROR";
                }


            } catch (Exception e) {

                return e.getMessage();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            try {

                asyncDialog.dismiss();
                Log.d("xxx___",result);
                if (result != null && result != "ERROR") {
                    float res= Float.parseFloat(result);
                    if(res>=99){





                    Toast.makeText(FaceRegisterActivity.this, "Verified Successfully..", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), PoliceVerifiedActivty.class);
                    intent.putExtra("photoPath", mImagePath);
                    intent.putExtra("policeName",sharedPreferences.getString(Utilities.LOGIN_POLICE_NAME,""));
                    //intent.putExtra("refugee_list", refugeeArrayList);

                    startActivity(intent);
                    finish();}
                    else{
                        Toast.makeText(getApplicationContext(),"Face reorganization failed.", Toast.LENGTH_LONG).show();
                    }
                }


            } catch (Exception ex) {
                asyncDialog.dismiss();
                Toast.makeText(getApplicationContext(),"No result found.", Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
    }
    private class FaceRecognition extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(FaceRegisterActivity.this);

        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setMessage(getString(R.string.loadingtype));
            //show dialog
            asyncDialog.show();
            asyncDialog.setCancelable(false);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            try {


                //Log.i("Image Path", mImagePath);
                String result = DataService.UploadFileWithBase64String("9",mImagePath);
                if (result != null && result != "ERROR") {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getString("success").equalsIgnoreCase("true")){
                        return jsonObject.getString("data");
                    }else{
                        return "ERROR";
                    }
                }else{
                    return "ERROR";
                }

            }catch(Exception e) {
                e.printStackTrace();

                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                // Log.i("Register", result);
                double similarity = 0.0;
                asyncDialog.dismiss();
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

//                JSONArray jsonarray = new JSONArray(result);
//                for(int i=0;i<jsonarray.length();i++){
//                    JSONObject objectResult = jsonarray.getJSONObject(i);
//                    similarity = objectResult.getDouble("similarity");
//                }

                //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //refugeePhoto.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                //byte[] byteArray = stream.toByteArray();

                if (result != null && result != "ERROR") {
                    try {

                        Log.d("cameraCheck__", "onPostExecute: "+result);
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("photoPath", mImagePath);
                        intent.putExtra("result", similarity);
                        /*intent.putExtra("inputType",inputType);
                        intent.putExtra("inputValue", inputValue);*/

                        startActivity(intent);
                        finish();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
            catch(Exception ex) {
                asyncDialog.dismiss();
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
    }

}