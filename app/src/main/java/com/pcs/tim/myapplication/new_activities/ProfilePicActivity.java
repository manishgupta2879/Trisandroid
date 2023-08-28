package com.pcs.tim.myapplication.new_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.face.FaceDetector;
import com.google.gson.Gson;
import com.pcs.tim.myapplication.BuildConfig;
import com.pcs.tim.myapplication.DataService;
import com.pcs.tim.myapplication.EditProfileActivity;
import com.pcs.tim.myapplication.NewRegisterResponse;
import com.pcs.tim.myapplication.R;
import com.pcs.tim.myapplication.Utilities;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

public class ProfilePicActivity extends AppCompatActivity {

    ImageView profileImg;

    SharedPreferences sharedPreferences;
    String photoPath = "";
    String mImagePath;
    String changedPhoto = "";
    boolean loginFlag=false;
    String imagePath,newImagePath;
    boolean cameraPermission = false;
    boolean isPhotoChanged = false;
    String userChoosenTask;
    Bitmap policePhoto;
    File output = null;


    Uri photoURI;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;


    String department,fullName,rank,hpNo,policeId,id,station,agency,state,icNo,password,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pic);

        profileImg =findViewById(R.id.profileImg);

        Intent intent=getIntent();

        loginFlag=intent.getBooleanExtra("loginFlag",false);


        sharedPreferences = getSharedPreferences(Utilities.MY_RC_SHARE_PREF, MODE_PRIVATE);

        department = sharedPreferences.getString(Utilities.LOGIN_POLICE_DEPT, "");
        fullName = sharedPreferences.getString(Utilities.LOGIN_POLICE_NAME, "");
        rank = sharedPreferences.getString(Utilities.LOGIN_POLICE_RANK, "");
        hpNo = sharedPreferences.getString(Utilities.LOGIN_POLICE_MOBILE, "");
        policeId = sharedPreferences.getString(Utilities.LOGIN_POLICE_ID, "");
        id = sharedPreferences.getString(Utilities.LOGIN_ID, "");
        station = sharedPreferences.getString(Utilities.LOGIN_POLICE_STATION, "");
        agency = sharedPreferences.getString(Utilities.LOGIN_POLICE_AGENCY, "");
        state = sharedPreferences.getString(Utilities.LOGIN_POLICE_STATE, "");
        icNo = sharedPreferences.getString(Utilities.LOGIN_POLICE_IC, "");
        password = sharedPreferences.getString(Utilities.LOGIN_POLICE_PWD, "");
        status = sharedPreferences.getString(Utilities.LOGIN_POLICE_STATUS, "");

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(ProfilePicActivity.this,
                        new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                selectImage();
            }
        });
    }
    public void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilePicActivity.this);
        builder.setTitle("Choose Options");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (cameraPermission)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (cameraPermission)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // File dir =
            //    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

            //output = new File(dir, System.currentTimeMillis() + ".jpg");
            photoURI = FileProvider.getUriForFile(ProfilePicActivity.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile());
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void galleryIntent() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = Long.toString(System.currentTimeMillis());
//        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DCIM), "Camera");
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//        output = image.getAbsoluteFile();
//        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "VeriMyRC");
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "VeriMyRC");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("VeriMyRC", "failed to create directory");
        }

        //Return the file target for the photo based on filename
//        File file = new File(mediaStorageDir.getPath() + File.separator + imageFileName);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                mediaStorageDir /* directory */
        );
        output = image.getAbsoluteFile();
        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraPermission = true;
                } else {
                    cameraPermission = false;
                    Toast.makeText(ProfilePicActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
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
        //Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        try {


            ContentResolver cr = getContentResolver();
            cr.notifyChange(photoURI, null);
            mImagePath = output.getAbsolutePath();

            Bitmap bitmap = Utilities.getImage(cr,photoURI, mImagePath);
            profileImg.setImageBitmap(bitmap);

            FaceDetector faceDetector = new
                    FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false)
                    .build();
            if(faceDetector.isOperational()){
           //     new android.app.AlertDialog.Builder(getApplicationContext()).setMessage("Could not set up the face detector!").show();
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
            /*Bitmap thumbnail;
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            File destination = new File(getFilesDir().getAbsolutePath(),
                    System.currentTimeMillis() + ".jpg");
//            File destination = new File(Environment.getExternalStorageDirectory(),
//                    System.currentTimeMillis() + ".jpg");

            ContentResolver cr = getContentResolver();
            cr.notifyChange(photoURI, null);
            mImagePath = output.getAbsolutePath();

            Bitmap bitmap = Utilities.getImage(cr, photoURI, mImagePath);
            profileImg.setImageBitmap(bitmap);

            //imagePath = destination.getAbsolutePath();
            FileOutputStream fo;
            thumbnail = Utilities.compressImage(mImagePath);
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            imagePath = destination.getAbsolutePath();
            Log.i("imagePath on device", mImagePath);
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
                isPhotoChanged = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
*/
            // imageFileLength = destination.length() / 1024;
            policePhoto = bitmap;
            new UploadPhoto().execute();
            //imageViewPhoto.setImageBitmap(thumbnail);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("errorLoadImage", "onCaptureImageResult: "+e.getMessage());
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }


    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {



                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                Uri selectedImageURI = data.getData();
                mImagePath = Utilities.getRealPathFromURI(getApplicationContext(), selectedImageURI);

                ContentResolver cr = getContentResolver();
                cr.notifyChange(selectedImageURI, null);

                Bitmap bitmap = Utilities.getImage(cr, selectedImageURI, mImagePath);
             //   imageViewPhoto.setImageBitmap(bitmap);

                FaceDetector faceDetector = new
                        FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false)
                        .build();
                if (faceDetector.isOperational()) {
                    //new android.app.AlertDialog.Builder(getApplicationContext()).setMessage("Could not set up the face detector!").show();
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
                }


              /*  bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                Uri selectedImageURI = data.getData();
                String mImagePath = Utilities.getRealPathFromURI(getApplicationContext(), selectedImageURI);
                ContentResolver cr = getContentResolver();
                cr.notifyChange(selectedImageURI, null);
                //imagePath = output.getAbsolutePath();

                Bitmap bitmap = Utilities.getImage(cr, selectedImageURI, mImagePath);
                imageViewPhoto.setImageBitmap(bitmap);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm = Utilities.compressImage(mImagePath);

                bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                imagePath = destination.getAbsolutePath();

                newImagePath = Utilities.getRealPathFromURI(getApplicationContext(), selectedImageURI);

                Log.d("NewImagePath__", "onSelectFromGalleryResult: "+mImagePath);
                Log.i("imagePath on device", imagePath);
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    isPhotoChanged = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                //Uri selectedImageUri = data.getData();
                //imagePath = Utilities.getPath(getApplicationContext(), selectedImageUri);
                //Toast.makeText(getBaseContext(),imagePath,Toast.LENGTH_LONG).show();
                new UploadPhoto().execute();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("errorLoadImage", "onCaptureImageResult: "+e.getMessage());
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }

        //imageFileLength = new File(imagePath).length()/1024;
        policePhoto = bm;
    }
   /* private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                Uri selectedImageURI = data.getData();
                String mImagePath = Utilities.getRealPathFromURI(getApplicationContext(), selectedImageURI);
                ContentResolver cr = getContentResolver();
                cr.notifyChange(selectedImageURI, null);
                //imagePath = output.getAbsolutePath();

                Bitmap bitmap = Utilities.getImage(cr, selectedImageURI, mImagePath);
                profileImg.setImageBitmap(bitmap);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm = Utilities.compressImage(mImagePath);

                bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                imagePath = destination.getAbsolutePath();

                newImagePath = Utilities.getRealPathFromURI(getApplicationContext(), selectedImageURI);

                Log.d("NewImagePath__", "onSelectFromGalleryResult: "+mImagePath);
                Log.i("imagePath on device", imagePath);
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    isPhotoChanged = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Uri selectedImageUri = data.getData();
                //imagePath = Utilities.getPath(getApplicationContext(), selectedImageUri);
                //Toast.makeText(getBaseContext(),imagePath,Toast.LENGTH_LONG).show();
                new UploadPhoto().execute();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }

        //imageFileLength = new File(imagePath).length()/1024;
        policePhoto = bm;
    }*/

    private class UploadPhoto extends AsyncTask<String, Void, String> {

        ProgressDialog asyncDialog = new ProgressDialog(ProfilePicActivity.this);

        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setMessage(getString(R.string.loadingtype));
            //show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Log.d("imagePathEdit__", mImagePath);
                String result = DataService.UploadFileWithBase64String("9", mImagePath);
                if (result != null && result != "ERROR") {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        return jsonObject.getString("data").toString();
                    } else {
                        return "ERROR";
                    }
                } else {
                    return "ERROR";
                }
            } catch (Exception ex) {
                return "ERROR";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                asyncDialog.dismiss();
                Log.d("uploadedPhoto", result);
                if (result != null && result != "ERROR") {
                    URI uri = new URI(result);
                    photoPath = uri.getPath();
                    Log.d("urlPath", photoPath);
                    changedPhoto = result;
                    isPhotoChanged = true;

                    new UpdateAccount().execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred while uploading your image.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class UpdateAccount extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(ProfilePicActivity.this);
        String imageUploadPath;

        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setMessage(getString(R.string.loadingtype));
            //show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            try {
//                String tokenResponse = Utilities.SendTokenPostRequest();
//                JSONObject jsonObject = new JSONObject(tokenResponse);
//                String token = jsonObject.getString("access_token");
//                String tokenType = jsonObject.getString("token_type");
                imageUploadPath = System.currentTimeMillis() + "-" + policeId + ".jpg";

//                HashMap<String,String> accountData = new HashMap<>();
                if (isPhotoChanged) {
//                    FTPClient mFtpClient = Utilities.connnectingwithFTP(Utilities.IP_NoPort, "enuser", "1234");
//                    Utilities.uploadFile(mFtpClient, new File(imagePath), imageUploadPath);
//                    accountData.put(Utilities.PHOTO, "Upload/"+imageUploadPath);


                } else {
//                    accountData.put(Utilities.PHOTO, sharedPreferences.getString(Utilities.LOGIN_POLICE_PHOTO,""));
                    changedPhoto = sharedPreferences.getString(Utilities.LOGIN_POLICE_PHOTO, "");
                }
//                accountData.put(Utilities.POLICE_ID, policeId);
//                accountData.put(Utilities.POLICE_NAME, fullName);
//                accountData.put(Utilities.IC_NO, icNo);
//                accountData.put(Utilities.PASSWORD, password);
//                accountData.put(Utilities.POLICE_DEPARTMENT, department);
//                accountData.put(Utilities.POLICE_STATION, station);
//                accountData.put(Utilities.POLICE_MOBILE, hpNo);
//                accountData.put(Utilities.POLICE_RANK, rank);
//                accountData.put(Utilities.POLICE_AGENCY, agency);
//                accountData.put(Utilities.POLICE_STATE, state);
//                accountData.put(Utilities.STATUS, status);
//
//                return Utilities.sendPostRequest(Utilities.ENFORCEMENT,accountData, token, tokenType);
                String result = DataService.EnforcementUpdate(Long.parseLong(id), policeId, icNo, fullName, hpNo, rank, agency, state, station, department, Utilities.getMD5Hash(password), photoPath);
                if (result != null && result != "ERROR") {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        return jsonObject.getString("data");
                    } else {
                        return "ERROR";
                    }
                } else {
                    return "ERROR";
                }
            } catch (Exception e) {

                return null;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {

                asyncDialog.dismiss();
                if (result != null && result != "ERROR") {
                    NewRegisterResponse respEditProfile = new Gson().fromJson(result, NewRegisterResponse.class);
                    if (respEditProfile.getId() > 0) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String name = respEditProfile.getFullname();

                        editor.putString(Utilities.LOGIN_POLICE_PHOTO, "http://" + DataService.instance().fetchValueString(DataService.VERIMYRC_URL) + photoPath);
                        editor.putString(Utilities.LOGIN_POLICE_AGENCY, agency);
                        editor.putString(Utilities.LOGIN_POLICE_DEPT, department);
                        editor.putString(Utilities.LOGIN_POLICE_RANK, rank);
                        editor.putString(Utilities.LOGIN_POLICE_STATE, state);
                        editor.putString(Utilities.LOGIN_POLICE_IC, icNo);
                        editor.putString(Utilities.LOGIN_POLICE_NAME, fullName);
                        editor.putString(Utilities.LOGIN_POLICE_STATION, station);
                        editor.putString(Utilities.LOGIN_POLICE_MOBILE, hpNo);
                        editor.putBoolean(Utilities.LOGGED_IN, true);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "Thank you, " + name + ". Profile is updated.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ProfilePicActivity.this, FaceRegisterActivity.class);
                        intent.putExtra("loginFlag",loginFlag);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred while updating your profile.", Toast.LENGTH_LONG).show();
                }
//                if (result!=null) {
//                    JSONObject jsonObject = new JSONObject(result);
//                    String name = jsonObject.getString(Utilities.POLICE_NAME);
//                    if(name!=null) {
//                        asyncDialog.dismiss();
//                        Log.i("Register", result);
//
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                        if(isPhotoChanged)
//                            editor.putString(Utilities.LOGIN_POLICE_PHOTO, "Upload/"+imageUploadPath);
//                        editor.putString(Utilities.LOGIN_POLICE_AGENCY, agency);
//                        editor.putString(Utilities.LOGIN_POLICE_DEPT, department);
//                        editor.putString(Utilities.LOGIN_POLICE_RANK, rank);
//                        editor.putString(Utilities.LOGIN_POLICE_STATE, state);
//                        editor.putString(Utilities.LOGIN_POLICE_IC, icNo);
//                        editor.putString(Utilities.LOGIN_POLICE_NAME, fullName);
//                        editor.putString(Utilities.LOGIN_POLICE_STATION, station);
//                        editor.putString(Utilities.LOGIN_POLICE_MOBILE, hpNo);
//                        editor.putString(Utilities.LOGIN_POLICE_PWD, password);
//                        editor.putBoolean(Utilities.LOGGED_IN, true);
//                        editor.apply();
//                        Toast.makeText(getApplicationContext(), "Thank you, "+ name +". Profile is updated.", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent();
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    }
//                }
//                else {
//                    asyncDialog.dismiss();
//                    Toast.makeText(getApplicationContext(), "Error occurred while updating your profile.", Toast.LENGTH_LONG).show();
//                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error occurred while updating your profile.", Toast.LENGTH_LONG).show();
            }
        }
    }
}