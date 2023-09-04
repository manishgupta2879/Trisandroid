package com.pcs.tim.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.vision.face.FaceDetector;
import com.google.gson.Gson;
import com.pcs.tim.myapplication.new_activities.ProfilePicActivity;
import com.pcs.tim.myapplication.new_added_classes.NotificationUtils;

import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class AccountRegistrationActivity extends AppCompatActivity {

    EditText editTextPoliceID;
    EditText editTextName;
    EditText editTextIc;
    EditText editTextRank;
    EditText editTextHp;
    Spinner editTextAgency;
    Spinner editTextState;
    Spinner editTextStation;
    Button buttonSave;
//    EditText editTextDepartment;
    Spinner editTextDepartment;
    EditText editTextPassword;
    EditText editTextConfirmPwd;
    Button buttonSubmit;
    ImageView imageViewPhoto;

    File output = null;
    String policeId;
    String icNo;
    String fullName;
    String rank;
    String hpNo;
    String agency;
    String state;
    String department;
    String station;
    String password;
    String confirmPassword;

    String dbResult;
    String imagePath;
    String uploadedPhoto;
    String photoPath;
    String userChoosenTask;
    boolean cameraPermission = false;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    static final char space = '-';
    Bitmap policePhoto;
    Uri photoURI;
    long imageFileLength = 0;
    ArrayList<String> arrayListAgency,arrayListState,arrayListStation,arrayListDepartment;
    ArrayAdapter<String> spinnerArrayAdapterStation,spinnerArrayAdapterDepartment;
    String selected_agency="",selected_state="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_profile_layout);

        editTextConfirmPwd = (EditText) findViewById(R.id.input_confirm_password);

        editTextHp = (EditText) findViewById(R.id.input_hp);
        editTextIc = (EditText) findViewById(R.id.input_ic);
        editTextName = (EditText) findViewById(R.id.input_name);
        editTextPassword = (EditText) findViewById(R.id.input_password);
        editTextPoliceID = (EditText) findViewById(R.id.input_id);
        editTextRank = (EditText) findViewById(R.id.input_rank);
        editTextAgency = (Spinner) findViewById(R.id.input_agency);
        editTextState = (Spinner) findViewById(R.id.input_state);
        buttonSave=findViewById(R.id.buttonSave);
//        editTextDepartment = (EditText) findViewById(R.id.input_department);
        editTextDepartment = (Spinner) findViewById(R.id.input_department);
        editTextStation = (Spinner) findViewById(R.id.input_station);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        buttonSubmit.setVisibility(View.VISIBLE);
        imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);

        try{
            callAgency();

//            if(!selected_agency.isEmpty()){
//                Toast.makeText(getApplicationContext(), editTextAgency.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
//                selected_agency = editTextAgency.getSelectedItem().toString();
//
//            }

        }
        catch(Exception e)
        {

        }

        buttonSave.setVisibility(View.GONE);
        editTextAgency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
//                int index = arg0.getSelectedItemPosition();
//                Toast.makeText(getBaseContext(),
//                        "You have selected item : " + arrayListAgency.get(index),
//                        Toast.LENGTH_SHORT).show();
                if(editTextState != null && editTextState.getSelectedItem()!=null){
                    int index = arg0.getSelectedItemPosition();
                    selected_agency = arrayListAgency.get(index);
                    callStation();
                }
                callDepartment();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        editTextState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
//
                if(editTextAgency != null && editTextAgency.getSelectedItem()!=null){
                    int index = arg0.getSelectedItemPosition();
                    selected_state =arrayListState.get(index);
//                    Toast.makeText(getBaseContext(),
//                        "You have selected item : " + selected_state,
//                        Toast.LENGTH_SHORT).show();
                    callStation();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        editTextIc.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                // Insert char where needed.
                if (s.length() == 7) {

                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 2) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                } else if (s.length() == 10) {

                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 2) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }

            }
        });
        imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(AccountRegistrationActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                selectImage();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitApplication();
            }
        });
    }

    public void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AccountRegistrationActivity.this);
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

    private void galleryIntent() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void cameraIntent() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
           // File dir =
                //    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

           //output = new File(dir, System.currentTimeMillis() + ".jpg");
            photoURI = FileProvider.getUriForFile(AccountRegistrationActivity.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile());
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
//        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DCIM), "Camera");
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//        output = image.getAbsoluteFile();

        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "VeriMyRC");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        //Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        try {


            ContentResolver cr = getContentResolver();
            cr.notifyChange(photoURI, null);
            imagePath = output.getAbsolutePath();

            Bitmap bitmap = Utilities.getImage(cr,photoURI, imagePath);
            imageViewPhoto.setImageBitmap(bitmap);

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
                imagePath = destination.getAbsolutePath();
                Log.d("xxx",imagePath);

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
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");

            ContentResolver cr = getContentResolver();
            cr.notifyChange(photoURI, null);
            imagePath = output.getAbsolutePath();

            Bitmap bitmap = Utilities.getImage(cr,photoURI, imagePath);
            imageViewPhoto.setImageBitmap(bitmap);

            //imagePath = destination.getAbsolutePath();
            FileOutputStream fo;
            thumbnail = Utilities.compressImage(imagePath);
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            imagePath = destination.getAbsolutePath();

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
*/
            // imageFileLength = destination.length() / 1024;
            policePhoto = bitmap;
            new UploadPhoto().execute();
            //imageViewPhoto.setImageBitmap(thumbnail);
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }


    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {



                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                Uri selectedImageURI = data.getData();
                imagePath = Utilities.getRealPathFromURI(getApplicationContext(), selectedImageURI);

                ContentResolver cr = getContentResolver();
                cr.notifyChange(selectedImageURI, null);

                Bitmap bitmap = Utilities.getImage(cr, selectedImageURI, imagePath);
                   imageViewPhoto.setImageBitmap(bitmap);

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
                    imagePath = destination.getAbsolutePath();

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
                Log.d("imagePath on device1", "onCaptureImageResult: "+e.getMessage());
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }

        //imageFileLength = new File(imagePath).length()/1024;
        policePhoto = bm;
    }
    @SuppressWarnings("deprecation")
/*    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                Uri selectedImageURI = data.getData();
                String mImagePath = Utilities.getRealPathFromURI(getApplicationContext(), selectedImageURI);
                ContentResolver cr = getContentResolver();
                cr.notifyChange(selectedImageURI, null);
                //imagePath = output.getAbsolutePath();

                Bitmap bitmap = Utilities.getImage(cr,selectedImageURI, mImagePath);
                imageViewPhoto.setImageBitmap(bitmap);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                //orientation
//                try {
//                    int rotate = 0;
//                    try {
//                        ExifInterface exif = new ExifInterface(mImagePath);
//                        int orientation = exif.getAttributeInt(
//                                ExifInterface.TAG_ORIENTATION,
//                                ExifInterface.ORIENTATION_NORMAL);
//
//                        switch (orientation) {
//                            case ExifInterface.ORIENTATION_ROTATE_270:
//                                rotate = 270;
//                                break;
//                            case ExifInterface.ORIENTATION_ROTATE_180:
//                                rotate = 180;
//                                break;
//                            case ExifInterface.ORIENTATION_ROTATE_90:
//                                rotate = 90;
//                                break;
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    Matrix matrix = new Matrix();
//                    matrix.postRotate(rotate);
//                    bm = Bitmap.createBitmap(bm , 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);  }
//                catch (Exception e) {}
                //end of orientation
                bm = Utilities.compressImage(mImagePath);

                bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                imagePath = destination.getAbsolutePath();

                FileOutputStream fo;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraPermission = true;
                } else {
                    cameraPermission = false;
                    Toast.makeText(AccountRegistrationActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void callAgency(){
        new GetAgency().execute();
    }

    private void callState(){
        new GetState().execute();
    }

    private  void callStation(){
        new GetStation().execute();
    }

    private  void callDepartment(){
        new GetDepartment().execute();
    }

    private void submitApplication() {
        boolean cancel = false;
        View focusView = null;

        if (policePhoto != null) {
            try{
                policeId = editTextPoliceID.getText().toString();
                icNo = editTextIc.getText().toString();
                fullName = editTextName.getText().toString();
                rank = editTextRank.getText().toString();
                hpNo = editTextHp.getText().toString();
                agency = editTextAgency.getSelectedItem().toString();
//                department = editTextDepartment.getText().toString();
                department = editTextDepartment.getSelectedItem().toString();
                state = editTextState.getSelectedItem().toString();
                station = editTextStation.getSelectedItem().toString();
                password = editTextPassword.getText().toString();
                confirmPassword = editTextConfirmPwd.getText().toString();

                editTextConfirmPwd.setError(null);
                editTextHp.setError(null);
                editTextIc.setError(null);
                editTextName.setError(null);
                editTextPassword.setError(null);
                editTextPoliceID.setError(null);
                editTextRank.setError(null);
//                editTextDepartment.setError(null);

                if (policeId.trim().isEmpty()) {
                    focusView = editTextPoliceID;
                    cancel = true;
                    editTextPoliceID.setError(getString(R.string.err_field_required));
                }
                if (icNo.trim().isEmpty()) {
                    focusView = editTextIc;
                    cancel = true;
                    editTextIc.setError(getString(R.string.err_field_required));
                }
                if (!isNumber(icNo)) {
                    focusView = editTextIc;
                    cancel = true;
                    editTextIc.setError(getString(R.string.err_invalid_ic));
                }
                if (fullName.trim().isEmpty()) {
                    focusView = editTextName;
                    cancel = true;
                    editTextName.setError(getString(R.string.err_field_required));
                }
                if (rank.trim().isEmpty()) {
                    focusView = editTextRank;
                    cancel = true;
                    editTextRank.setError(getString(R.string.err_field_required));
                }
                if (hpNo.trim().isEmpty()) {
                    focusView = editTextHp;
                    cancel = true;
                    editTextHp.setError(getString(R.string.err_field_required));
                }
                if (agency.equals("Please Choose Agency")) {
                    focusView = editTextAgency;
                    cancel = true;
                    Toast.makeText(this,"Please Choose Agency",Toast.LENGTH_LONG).show();
                }
                if (state.equals("Please Choose State")) {
                    focusView = editTextState;
                    cancel = true;
                    Toast.makeText(this,"Please Choose State",Toast.LENGTH_LONG).show();
                }
                if (department.equals("Please Choose Department")) {
                    focusView = editTextDepartment;
                    cancel = true;
                    Toast.makeText(this,"Please Choose Department",Toast.LENGTH_LONG).show();
                }
                if (station.equals("Please Choose Station")) {
                    focusView = editTextStation;
                    cancel = true;
                    Toast.makeText(this,"Please Choose Station",Toast.LENGTH_LONG).show();
                }
                if (password.trim().isEmpty()) {
                    focusView = editTextPassword;
                    cancel = true;
                    editTextPassword.setError(getString(R.string.err_field_required));
                }
                if (confirmPassword.trim().isEmpty()) {
                    focusView = editTextConfirmPwd;
                    cancel = true;
                    editTextConfirmPwd.setError(getString(R.string.err_field_required));
                }
                if (!password.equals(confirmPassword)) {
                    focusView = editTextConfirmPwd;
                    cancel = true;
                    editTextConfirmPwd.setError(getString(R.string.err_msg_confirm_password));
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    new RegisterAccount().execute();
                }
            }catch(Exception e){

            }
        } else {
            Toast.makeText(getApplicationContext(), "Please Add Your Photo.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNumber(String numberString) {
        String[] numberStrings;
        numberStrings = numberString.split("-");

        for (int i = 0; i < numberStrings.length; i++) {
            for (char c : numberStrings[i].toCharArray()) {
                if (!Character.isDigit(c)) return false;
            }

        }
        return true;
    }

    private class GetState extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            selected_agency = editTextAgency.getSelectedItem().toString();

        }

        @Override
        protected String doInBackground(String... urls) {
            try {
//                String tokenResponse = Utilities.SendTokenPostRequest();
//                JSONObject jsonObject = null;
//                jsonObject = new JSONObject(tokenResponse);
//                String token = jsonObject.getString("access_token");
//                String tokenType = jsonObject.getString("token_type");
//
//                return Utilities.SendGetRequest(Utilities.DISTRICT_STATION+ "?type=ddl_state", token, tokenType);

                String result = DataService.GetStateByAgency(selected_agency);

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
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && result != "ERROR") {
                try {
                    arrayListState = new ArrayList<>();
                    JSONArray jsonArr = new JSONArray(result);

                    for (int i = 0; i < jsonArr.length(); i++)
                    {
                        String value = jsonArr.getString(i);
                        arrayListState.add(value);
                    }
                    if(!arrayListState.isEmpty()){
                        ArrayAdapter<String> spinnerArrayAdapterState = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, arrayListState );
                        editTextState.setAdapter(spinnerArrayAdapterState);
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    private class GetAgency extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
//                String tokenResponse = Utilities.SendTokenPostRequest();
//                JSONObject jsonObject = null;
//                jsonObject = new JSONObject(tokenResponse);
//                String token = jsonObject.getString("access_token");
//                String tokenType = jsonObject.getString("token_type");
//
//                return Utilities.SendGetRequest(Utilities.DISTRICT_STATION+ "?type=ddl_agency", token, tokenType);
                String result = DataService.GetAgency();
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
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && result != "ERROR") {
                try {
                    arrayListAgency = new ArrayList<>();
                    JSONArray jsonArr = new JSONArray(result);

                    for (int i = 0; i < jsonArr.length(); i++)
                    {
                        String value = jsonArr.getString(i);
                        arrayListAgency.add(value);
                    }
                    if(!arrayListAgency.isEmpty()){
                        ArrayAdapter<String> spinnerArrayAdapterAgency = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, arrayListAgency );
                        editTextAgency.setAdapter(spinnerArrayAdapterAgency);
                        callState();
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    private class GetDepartment extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {


                String result = DataService.GetDepartmentByAgency(selected_agency);
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
//                String tokenResponse = Utilities.SendTokenPostRequest();
//                JSONObject jsonObject = null;
//                jsonObject = new JSONObject(tokenResponse);
//                String token = jsonObject.getString("access_token");
//                String tokenType = jsonObject.getString("token_type");
//
//                return Utilities.SendGetRequest(Utilities.DEPARTMENT+ "?agency="+selected_agency, token, tokenType);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }catch(Exception ex){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && result != "ERROR") {
                try {
                    arrayListDepartment = new ArrayList<>();
                    JSONArray arrayResult = new JSONArray(result);

                    for(int i=0;i<arrayResult.length();i++){
                        String value = arrayResult.getString(i);
                        arrayListDepartment.add(value);
                    }
                    if(!arrayListDepartment.isEmpty()){
                        spinnerArrayAdapterDepartment = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, arrayListDepartment );
                        editTextDepartment.setAdapter(spinnerArrayAdapterDepartment);
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    private class GetStation extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            selected_agency = editTextAgency.getSelectedItem().toString();
            selected_state = editTextState.getSelectedItem().toString();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                String result = DataService.GetStationByAgencyState(selected_agency,selected_state);
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
//                String tokenResponse = Utilities.SendTokenPostRequest();
//                JSONObject jsonObject = null;
//                jsonObject = new JSONObject(tokenResponse);
//                String token = jsonObject.getString("access_token");
//                String tokenType = jsonObject.getString("token_type");
//
//                return Utilities.SendGetRequest(Utilities.DISTRICT_STATION+ "?agency="+selected_agency+ "&state="+selected_state, token, tokenType);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }catch(Exception ex){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && result != "ERROR") {
                try {
                    arrayListStation = new ArrayList<>();
                    JSONArray arrayResult = new JSONArray(result);

                    for(int i=0;i<arrayResult.length();i++){
                        String value = arrayResult.getString(i);
                        arrayListStation.add(value);
                    }
                    if(!arrayListStation.isEmpty()){
                        spinnerArrayAdapterStation = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, arrayListStation );
                        editTextStation.setAdapter(spinnerArrayAdapterStation);
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    private class UploadPhoto extends AsyncTask<String, Void, String> {

        ProgressDialog asyncDialog = new ProgressDialog(AccountRegistrationActivity.this);
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
            try{
                String result = DataService.UploadFileWithBase64String("9",imagePath);
                if (result != null && result != "ERROR") {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getString("success").equalsIgnoreCase("true")){
                        return jsonObject.getString("data").toString();
                    }else{
                        return "ERROR";
                    }
                }else{
                    return "ERROR";
                }
            }catch(Exception ex){
                return "ERROR";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                asyncDialog.dismiss();
                if (result != null && result != "ERROR") {
                    uploadedPhoto = result;
                    URI uri = new URI(result);
                    photoPath = uri.getPath();
                    Log.d("photoPath",photoPath);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error occurred while uploading your image.", Toast.LENGTH_LONG).show();
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class RegisterAccount extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(AccountRegistrationActivity.this);

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
                String imageUploadPath = System.currentTimeMillis() + "-" + policeId + ".jpg";
//                FTPClient mFtpClient = Utilities.connnectingwithFTP(Utilities.IP_NoPort, "enuser", "1234");
//                Utilities.uploadFile(mFtpClient, new File(imagePath), imageUploadPath);

//                HashMap<String,String> accountData = new HashMap<>();
//                accountData.put(Utilities.PHOTO, "Upload/"+imageUploadPath);
//                accountData.put(Utilities.POLICE_ID, policeId);
//                accountData.put(Utilities.POLICE_NAME, fullName);
//                accountData.put(Utilities.IC_NO, icNo);
//                accountData.put(Utilities.PASSWORD, password);
//                accountData.put(Utilities.POLICE_AGENCY, agency);
//                accountData.put(Utilities.POLICE_DEPARTMENT, department);
//                accountData.put(Utilities.POLICE_STATE, state);
//                accountData.put(Utilities.POLICE_STATION, station);
//                accountData.put(Utilities.POLICE_MOBILE, hpNo);
//                accountData.put(Utilities.POLICE_RANK, rank);
//                accountData.put(Utilities.STATUS, "N");

                String result = DataService.NewRegister(policeId,icNo,fullName,hpNo,rank,agency,state,station,department,Utilities.getMD5Hash(password),photoPath);
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

        }catch(Exception e){

                return null;
        }
            /*HashMap<String,String> accountData = new HashMap<>();
            accountData.put(Utilities.PHOTO, imagePath);
            accountData.put(Utilities.POLICE_ID, policeId);
            accountData.put(Utilities.POLICE_NAME, fullName);
            accountData.put(Utilities.IC_NO, icNo);
            accountData.put(Utilities.PASSWORD, password);
            accountData.put(Utilities.POLICE_DEPARTMENT, department);
            accountData.put(Utilities.POLICE_STATION, station);
            accountData.put(Utilities.POLICE_MOBILE, hpNo);
            accountData.put(Utilities.POLICE_RANK, rank);

            Utilities utilities = new Utilities();
            return utilities.sendPostRequest(urls[0],accountData);*/
    }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                if (result != null && result != "ERROR") {
                    NewRegisterResponse respNewRegister = new Gson().fromJson(result, NewRegisterResponse.class);
                    if (respNewRegister.getId()>0) {
                        String name = respNewRegister.getFullname();
                        if (name != null) {
                            asyncDialog.dismiss();
                            NotificationUtils.showNotification(AccountRegistrationActivity.this,"VeriMyRc","Welcome to VeriMyRC Enforcement App! Your account has been successfully created. Access essential tools for ensuring safety and security.");
                            Toast.makeText(getApplicationContext(), "Thank you, " + name + ". Application is submitted, please wait for the approval.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            asyncDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Error occurred while submitting your application. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        asyncDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Existed pending approval application. Please wait for approval.", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    asyncDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error occurred while submitting your application. Please check your internet.", Toast.LENGTH_LONG).show();
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
                asyncDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error occurred while submitting your application. Please try again.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
