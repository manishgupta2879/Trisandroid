package com.pcs.tim.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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

public class EditProfileActivity extends AppCompatActivity {
    EditText editTextName;
    EditText editTextIc;
    EditText editTextRank;
    EditText editTextHp;
    Spinner editTextDepartment;
    Spinner editTextStation;
    Spinner editTextState;
    Spinner editTextAgency;
    EditText editTextPassword;
    EditText editTextConfirmPwd;
    Button buttonSubmit;
    TextView buttonEditPhoto;
    ImageView imageViewPhoto;

    ArrayList<String> arrayListAgency, arrayListState, arrayListStation, arrayListDepartment;
    ArrayAdapter<String> spinnerArrayAdapterStation, spinnerArrayAdapterDepartment;

    File output = null;
    String id;
    String policeId;
    String icNo;
    String fullName;
    String rank;
    String hpNo;
    String department;
    String agency;
    String state;
    String station;
    String password, confirmPassword, status;
    String changedPhoto = "";
    String photoPath = "";
    String imagePath,newImagePath;
    String userChoosenTask;
    boolean cameraPermission = false;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    static final char space = '-';
    Bitmap policePhoto;
    Uri photoURI;
    boolean isPhotoChanged = false;
    SharedPreferences sharedPreferences;
    String selected_agency = "", selected_state = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        sharedPreferences = getSharedPreferences(Utilities.MY_RC_SHARE_PREF, MODE_PRIVATE);

        editTextConfirmPwd = (EditText) findViewById(R.id.input_confirm_password);
        editTextDepartment = (Spinner) findViewById(R.id.input_department);
        editTextHp = (EditText) findViewById(R.id.input_hp);
        editTextIc = (EditText) findViewById(R.id.input_ic);
        editTextName = (EditText) findViewById(R.id.input_name);
        editTextPassword = (EditText) findViewById(R.id.input_password);
        editTextRank = (EditText) findViewById(R.id.input_rank);
        editTextStation = (Spinner) findViewById(R.id.input_station);
        editTextAgency = (Spinner) findViewById(R.id.input_agency);
        editTextState = (Spinner) findViewById(R.id.input_state);
        buttonSubmit = (Button) findViewById(R.id.buttonSave);
        buttonEditPhoto = findViewById(R.id.btnEditPhoto);
        imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);

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
        disableEditText(editTextIc);
        new GetPhoto().execute();
        DisplayPersonalDetails();


        Toolbar toolbar =findViewById(R.id.editProfile_toolbar);
        //   toolbar.setVisibility(View.VISIBLE);// Assuming you have a Toolbar in your layout
        if (toolbar != null) {

            toolbar.setNavigationIcon(R.drawable.ic_back); // Set your back button icon
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle back button click here (e.g., pop the Fragment from the back stack)
                    onBackPressed();
                }
            });
        }

        try {
            callAgency();

            callState();
        } catch (Exception e) {

        }

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
                if (editTextState != null && editTextState.getSelectedItem() != null) {
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
//                int index = arg0.getSelectedItemPosition();
//                Toast.makeText(getBaseContext(),
//                        "You have selected item : " + arrayListAgency.get(index),
//                        Toast.LENGTH_SHORT).show();
                if (editTextAgency != null && editTextAgency.getSelectedItem() != null) {
                    int index = arg0.getSelectedItemPosition();
                    selected_state = arrayListState.get(index);
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
        buttonEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(EditProfileActivity.this,
                        new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
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

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    private void DisplayPersonalDetails() {
        editTextConfirmPwd.setText(password);
        editTextPassword.setText(password);
//        editTextDepartment.setText(department);
        editTextHp.setText(hpNo);
        editTextIc.setText(icNo);
        editTextName.setText(fullName);
        editTextRank.setText(rank);
    }

    public void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cameraIntent() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // File dir =
            //    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

            //output = new File(dir, System.currentTimeMillis() + ".jpg");
            photoURI = FileProvider.getUriForFile(EditProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile());
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (IOException e) {
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
            Bitmap thumbnail;
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            File destination = new File(getFilesDir().getAbsolutePath(),
                    System.currentTimeMillis() + ".jpg");
//            File destination = new File(Environment.getExternalStorageDirectory(),
//                    System.currentTimeMillis() + ".jpg");

            ContentResolver cr = getContentResolver();
            cr.notifyChange(photoURI, null);
            imagePath = output.getAbsolutePath();

            Bitmap bitmap = Utilities.getImage(cr, photoURI, imagePath);
            imageViewPhoto.setImageBitmap(bitmap);

            //imagePath = destination.getAbsolutePath();
            FileOutputStream fo;
            thumbnail = Utilities.compressImage(imagePath);
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            imagePath = destination.getAbsolutePath();
            Log.i("imagePath on device", imagePath);
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

            // imageFileLength = destination.length() / 1024;
            policePhoto = thumbnail;
            new UploadPhoto().execute();
            //imageViewPhoto.setImageBitmap(thumbnail);
        } catch (Exception e) {
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
                    Toast.makeText(EditProfileActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void submitApplication() {
        boolean cancel = false;
        View focusView = null;

        if (policePhoto != null) {

            icNo = editTextIc.getText().toString();
            fullName = editTextName.getText().toString();
            rank = editTextRank.getText().toString();
            hpNo = editTextHp.getText().toString();
            department = editTextDepartment.getSelectedItem().toString();
            station = editTextStation.getSelectedItem().toString();
            agency = editTextAgency.getSelectedItem().toString();
            state = editTextState.getSelectedItem().toString();
            password = editTextPassword.getText().toString();
            confirmPassword = editTextConfirmPwd.getText().toString();

            editTextConfirmPwd.setError(null);
            editTextHp.setError(null);
            editTextIc.setError(null);
            editTextName.setError(null);
            editTextPassword.setError(null);
            editTextRank.setError(null);

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
            if (department.trim().isEmpty()) {
                focusView = editTextDepartment;
                cancel = true;
                Toast.makeText(this, "Please choose Department", Toast.LENGTH_SHORT).show();
            }
            if (agency.trim().isEmpty()) {
                focusView = editTextAgency;
                cancel = true;
                Toast.makeText(this, "Please choose Agency", Toast.LENGTH_SHORT).show();
            }
            if (state.trim().isEmpty()) {
                focusView = editTextState;
                cancel = true;
                Toast.makeText(this, "Please choose State", Toast.LENGTH_SHORT).show();
            }
            if (station.trim().isEmpty()) {
                focusView = editTextStation;
                cancel = true;
                Toast.makeText(this, "Please choose Station", Toast.LENGTH_SHORT).show();
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
                new UpdateAccount().execute();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please add your photo.", Toast.LENGTH_LONG).show();
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

    private class UploadPhoto extends AsyncTask<String, Void, String> {

        ProgressDialog asyncDialog = new ProgressDialog(EditProfileActivity.this);

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
                Log.d("imagePathEdit__", newImagePath);
                String result = DataService.UploadFileWithBase64String("9", newImagePath);
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
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred while uploading your image.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class UpdateAccount extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(EditProfileActivity.this);
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
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
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

    private void callAgency() {
        new GetAgency().execute();
    }

    private void callState() {
        new GetState().execute();
    }

    private void callStation() {
        new GetStation().execute();
    }

    private void callDepartment() {
        new GetDepartment().execute();
    }

    private class GetPhoto extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            try {
                policePhoto = Utilities.getBitmapFromURL(sharedPreferences.getString(Utilities.LOGIN_POLICE_PHOTO, ""));
                if (policePhoto != null)
                    return "OK";
                return null;
            } catch (Exception e) {

                return null;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                if (result.equals("OK")) {
                    if (policePhoto != null)
                        imageViewPhoto.setImageBitmap(policePhoto);
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred loading your photo.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class GetState extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
//            selected_agency = editTextAgency.getSelectedItem().toString();
//            Log.d("selected_agency",selected_agency);
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
//                return Utilities.SendGetRequest(Utilities.DISTRICT_STATION+ "?type=ddl_state", token, tokenType);

                String result = DataService.GetStateByAgency(agency);
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
            Log.d("statedata", result);
            if (result != null && result != "ERROR") {
                try {
                    arrayListState = new ArrayList<>();
                    JSONArray jsonArr = new JSONArray(result);

                    for (int i = 0; i < jsonArr.length(); i++) {
                        String value = jsonArr.getString(i);
                        arrayListState.add(value);
                    }
                    if (!arrayListState.isEmpty()) {
                        ArrayAdapter<String> spinnerArrayAdapterState = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, arrayListState);
                        editTextState.setAdapter(spinnerArrayAdapterState);
                        if (!state.trim().isEmpty()) {
                            int spinnerPosition = spinnerArrayAdapterState.getPosition(state);
                            editTextState.setSelection(spinnerPosition);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    private class GetAgency extends AsyncTask<String, Void, String> {
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
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        return jsonObject.getString("data");
                    } else {
                        return "ERROR";
                    }
                } else {
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

                    for (int i = 0; i < jsonArr.length(); i++) {
                        String value = jsonArr.getString(i);
                        arrayListAgency.add(value);
                    }
                    if (!arrayListAgency.isEmpty()) {
                        ArrayAdapter<String> spinnerArrayAdapterAgency = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, arrayListAgency);
                        //edit by deepak
                       // spinnerArrayAdapterAgency.setDropDownViewResource();
                        editTextAgency.setAdapter(spinnerArrayAdapterAgency);

                        if (!agency.trim().isEmpty()) {
                            int spinnerPosition = spinnerArrayAdapterAgency.getPosition(agency);
                            editTextAgency.setSelection(spinnerPosition);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    private class GetDepartment extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            selected_agency = editTextAgency.getSelectedItem().toString();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                String result = DataService.GetDepartmentByAgency(selected_agency);
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
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                arrayListDepartment = new ArrayList<>();
                JSONArray arrayResult = new JSONArray(result);

                for (int i = 0; i < arrayResult.length(); i++) {
                    String value = arrayResult.getString(i);
                    arrayListDepartment.add(value);
                }
                if (!arrayListDepartment.isEmpty()) {
                    spinnerArrayAdapterDepartment = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, arrayListDepartment);
                    editTextDepartment.setAdapter(spinnerArrayAdapterDepartment);
                    if (!department.trim().isEmpty()) {
                        int spinnerPosition = spinnerArrayAdapterDepartment.getPosition(department);
                        editTextDepartment.setSelection(spinnerPosition);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class GetStation extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            selected_agency = editTextAgency.getSelectedItem().toString();
            selected_state = editTextState.getSelectedItem().toString();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {

                String result = DataService.GetStationByAgencyState(selected_agency, selected_state);
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
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && result != "ERROR") {
                try {
                    arrayListStation = new ArrayList<>();
                    JSONArray arrayResult = new JSONArray(result);

                    for (int i = 0; i < arrayResult.length(); i++) {
                        String value = arrayResult.getString(i);
                        arrayListStation.add(value);
                    }
                    if (!arrayListStation.isEmpty()) {
                        spinnerArrayAdapterStation = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, arrayListStation);
                        editTextStation.setAdapter(spinnerArrayAdapterStation);
                        if (!station.trim().isEmpty()) {
                            int spinnerPosition = spinnerArrayAdapterStation.getPosition(station);
                            editTextStation.setSelection(spinnerPosition);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
