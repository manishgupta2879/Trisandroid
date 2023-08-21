package com.pcs.tim.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class FaceRecognitionActivity extends AppCompatActivity {
    ImageView imageViewPhoto;
    File output = null;
    String country, gender,yearOfBirth = "";
    String userChoosenTask;
    //boolean cameraPermission = false;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    static final char space = '-';
    Bitmap refugeePhoto;
    RadioButton rbMyrc;
    RadioButton rbUnid;
    RadioButton rbVsMany;
    EditText inputMyRc;
    EditText inputUnId;
    Spinner inputYear;
    String inputType;
    String inputValue;
    List<String> countryList;
    Spinner countrySpinner;
    ArrayAdapter<String> dataAdapter;
        String mImagePath, yearOfBirthFrom = "", yearOfBirthTo = "";
    Uri photoURI;
    boolean permission = false;
    File photoFile;
    Bitmap croppedImage;
    ArrayList<Bitmap> croppedImgList;
   // RecyclerView croppedImgListView;
    ArrayList<String> imagePathList;
    ImageListAdapter imageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);

        Spinner genderSpinner;
        Button buttonVerify;

        PackageManager pm = getPackageManager();
        int hasPerm = pm.checkPermission(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                getPackageName());
        if (hasPerm != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FaceRecognitionActivity.this,
                    new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        else
            permission = true;

        countryList = new ArrayList<String>();
        croppedImgList = new ArrayList<>();
        imagePathList = new ArrayList<>();

//        new GetCountryList().execute();
//        countrySpinner = (Spinner)findViewById(R.id.selectedCountry);
        genderSpinner = (Spinner)findViewById(R.id.selectedGender);
//        LinearLayout countryLayout = (LinearLayout)findViewById(R.id.countrySpinnerLayout);
        LinearLayout genderLayout = (LinearLayout)findViewById(R.id.genderSpinnerLayout);
        LinearLayout yearLayout = (LinearLayout)findViewById(R.id.yearInputTextLayout);
        inputMyRc = (EditText)findViewById(R.id.inputMyRc);
        inputUnId = (EditText)findViewById(R.id.inputUnId);
        inputYear = (Spinner)findViewById(R.id.inputYear);
        rbMyrc = (RadioButton)findViewById(R.id.rbMyRc);
        rbUnid = (RadioButton)findViewById(R.id.rbUnId);
        rbVsMany = (RadioButton)findViewById(R.id.rbVsMany);
        buttonVerify = (Button)findViewById(R.id.buttonVerify);
        //croppedImgListView = (RecyclerView) findViewById(R.id.croppedImageListView);



        //


        Toolbar toolbar =findViewById(R.id.searchRefugee_toolbar);
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

       /* croppedImgListView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), croppedImgListView ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                //Toast.makeText(getApplicationContext(), "position:" + Integer.toString(position) + "child count:" + Integer.toString(croppedImgListView.getAdapter().getItemCount()),Toast.LENGTH_LONG).show();

                for (int i = 0; i < croppedImgListView.getAdapter().getItemCount(); i++){
                    if(position == i ){
                        if(croppedImgListView.getChildViewHolder(view) != null)
                            croppedImgListView.getChildViewHolder(view).itemView.setBackgroundColor(Color.rgb(50,205,50));
                        //croppedImgListView.getChildAt(position).setBackgroundColor(Color.rgb(50,205,50));
                        if(imagePathList.size() - 1 >= position)
                            mImagePath = imagePathList.get(position);
                        else{
                            Toast.makeText(getApplicationContext(), "Something went wrong, please try another photo.", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(getApplicationContext(), Integer.toString(position) + " " +mImagePath ,Toast.LENGTH_LONG).show();
                    }
                    else{
                        if(croppedImgListView.findViewHolderForAdapterPosition(i) != null)
                            croppedImgListView.findViewHolderForAdapterPosition(i).itemView.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }})
        );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        croppedImgListView.setLayoutManager(mLayoutManager);
        croppedImgListView.setItemAnimator(new DefaultItemAnimator());*/

        rbVsMany.setChecked(true);
//        yearLayout.setVisibility(View.VISIBLE);
//        genderLayout.setVisibility(View.VISIBLE);
        rbMyrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rbMyrc.setChecked(true);
                rbVsMany.setChecked(false);
                rbUnid.setChecked(false);
                inputMyRc.setVisibility(View.VISIBLE);
                inputUnId.setVisibility(View.GONE);
                yearLayout.setVisibility(View.GONE);
//                countryLayout.setVisibility(View.GONE);
                genderLayout.setVisibility(View.GONE);
            }
        });

        rbUnid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbUnid.setChecked(true);
                rbVsMany.setChecked(false);
                rbMyrc.setChecked(false);
                inputUnId.setVisibility(View.VISIBLE);
                inputMyRc.setVisibility(View.GONE);
                yearLayout.setVisibility(View.GONE);
//                countryLayout.setVisibility(View.GONE);
                genderLayout.setVisibility(View.GONE);
            }
        });

        rbVsMany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbVsMany.setChecked(true);
                rbMyrc.setChecked(false);
                rbUnid.setChecked(false);
                inputUnId.setVisibility(View.GONE);
                inputMyRc.setVisibility(View.GONE);
                yearLayout.setVisibility(View.VISIBLE);
//                countryLayout.setVisibility(View.VISIBLE);
                genderLayout.setVisibility(View.VISIBLE);
            }
        });
        imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
        imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(permission) {

                    selectImage();
                }
                else{
                    ActivityCompat.requestPermissions(FaceRecognitionActivity.this,
                            new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }
            }
        });

        inputUnId.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //inputMyRc.removeTextChangedListener(this);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
               /* if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }*/
                // Insert char where needed.
                if (s.length() == 4) {

                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if(c == space){
                        s.delete(s.length() - 1, s.length());
                    }
                    else if(TextUtils.split(s.toString(), String.valueOf(space)).length <= 2) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }

            }
        });

        inputMyRc.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //inputMyRc.removeTextChangedListener(this);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
               /* if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }*/
                // Insert char where needed.
                if (s.length() == 4) {

                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if(c == space){
                        s.delete(s.length() - 1, s.length());
                    }
                    else if(TextUtils.split(s.toString(), String.valueOf(space)).length <= 2) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }
                else if(s.length() == 11){

                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if(c == space){
                        s.delete(s.length() - 1, s.length());
                    } else if (TextUtils.split(s.toString(), String.valueOf(space)).length <= 2) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }

            }
        });

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (rbMyrc.isChecked()) {
                        inputType = "MYRC";
                        inputValue = inputMyRc.getText().toString();
                        if (inputValue.trim().equals("")) {
                            inputMyRc.setError("MyRC No. cannot be empty.");
                            inputMyRc.requestFocus();
                        } else
                            Log.d("cameraCheck__", "onClick: 1");

                        new FaceRecognition().execute();
                    }
                    if (rbUnid.isChecked()) {
                        inputType = "UNHCR";
                        inputValue = inputUnId.getText().toString();
                        if (inputValue.trim().equals("")) {
                            inputUnId.setError("MyRC No. cannot be empty.");
                            inputUnId.requestFocus();
                        } else
                            Log.d("cameraCheck__", "onClick: 2");

                        new FaceRecognition().execute();
                    }
                    if (rbVsMany.isChecked()) {
//                        country = countrySpinner.getSelectedItem().toString();
                        gender = genderSpinner.getSelectedItem().toString();
                        getYearOfBirth();
                        Log.d("cameraCheck__", "onClick: 3");
                        new FaceRecognitionVsMany().execute();
//                        if (!inputYear.getText().toString().equals("")) {
//                            yearOfBirth = inputYear.getText().toString();
//                            if (Integer.parseInt(yearOfBirth) < 1930 || Integer.parseInt(yearOfBirth) > Calendar.getInstance().get(Calendar.YEAR)) {
//                                inputYear.setError("Invalid year of birth");
//                                inputYear.requestFocus();
//                            }
//                            new FaceRecognitionVsMany().execute();
//                        } else
//                            new FaceRecognitionVsMany().execute();
                    }
                }
        });
    }

    public void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(FaceRecognitionActivity.this);
        builder.setTitle("Choose Options");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (permission)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (permission)
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
            photoURI = FileProvider.getUriForFile(FaceRecognitionActivity.this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, REQUEST_CAMERA);
        }catch (IOException e){
            e.printStackTrace();
        }
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
        try {
           // Bitmap thumbnail = null;


            ContentResolver cr = getContentResolver();
            cr.notifyChange(photoURI, null);
            mImagePath = output.getAbsolutePath();

            Bitmap bitmap = Utilities.getImage(cr,photoURI, mImagePath);
            imageViewPhoto.setImageBitmap(bitmap);

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
                imagePathList.add(mImagePath);
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

                croppedImgList.clear();
                imagePathList.clear();
                imageListAdapter = null;

                for (int i = 0; i < faces.size(); i++) {
                    Face thisFace = faces.valueAt(i);
                    float x1 = thisFace.getPosition().x;
                    float y1 = thisFace.getPosition().y;
                    float x2 = x1 + thisFace.getWidth();
                    float y2 = y1 + thisFace.getHeight();
                    if(x1>=0 && y1 >= 0) {
                        tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
                        Bitmap croppedImage = Bitmap.createBitmap(bitmap, (int) x1 + 1, (int) y1 + 1, (int) thisFace.getWidth() + 1, (int) thisFace.getHeight() + 1);
                        croppedImgList.add(croppedImage);
                    }
                }
                Log.d("xxx",String.valueOf(croppedImgList.size()));

                imageListAdapter = new ImageListAdapter(croppedImgList, null);
                //croppedImgListView.setAdapter(imageListAdapter);
//                imageListAdapter.notifyDataSetChanged();

                imageViewPhoto.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));

            }
//
//            thumbnail = Utilities.compressImage(mImagePath);
//            refugeePhoto = thumbnail;

            if(croppedImgList.size() > 0){

                for(int i = 0; i < croppedImgList.size(); i++){
                    FileOutputStream fo;
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                    File destination = new File(getFilesDir().getAbsolutePath(),
                        System.currentTimeMillis() + Integer.toString(i) + ".jpg");
                    croppedImgList.get(i).compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    mImagePath = destination.getAbsolutePath();
                    Log.d("mImagePath",mImagePath);
                    imagePathList.add(mImagePath);
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
                mImagePath = imagePathList.get(0);
              /*  if(croppedImgListView.findViewHolderForAdapterPosition(0)!=null){
                    croppedImgListView.findViewHolderForAdapterPosition(0).itemView.setBackgroundColor(Color.rgb(50,205,50));
                }*/
            }else {
                Toast.makeText(getApplicationContext(), "No face detected, please use another photo.", Toast.LENGTH_LONG).show();
                return;
            }

        }
        catch (Exception e)
        {
            Log.d("xxxxerror",e.getMessage());
            Toast.makeText(this, "Failed to load image, please try again", Toast.LENGTH_SHORT).show();
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = Long.toString(System.currentTimeMillis());
//        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DCIM), "Camera");
//        File storageDir = new File(getFilesDir(), "DCIM");
//        File storageDir = new File(context.getCacheDir());

//        File storageDir = new File(getCacheDir(), Environment.DIRECTORY_DCIM);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir /* directory */
//        );
//        output = image.getAbsoluteFile();

        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "VeriMyRC");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("VeriMyRC", "failed to create directory");
        }

        // Return the file target for the photo based on filename
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

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        try {
            Bitmap bm = null;
            if (data != null) {
                try {
//                File destination = new File(Environment.getExternalStorageDirectory(),
//                        System.currentTimeMillis() + ".jpg");

                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    Uri selectedImageURI = data.getData();
                    mImagePath = Utilities.getRealPathFromURI(getApplicationContext(), selectedImageURI);

                    ContentResolver cr = getContentResolver();
                    cr.notifyChange(selectedImageURI, null);

                    Bitmap bitmap = Utilities.getImage(cr, selectedImageURI, mImagePath);
                    imageViewPhoto.setImageBitmap(bitmap);

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
                        imagePathList.add(mImagePath);
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

                        croppedImgList.clear();
                        imagePathList.clear();
                        imageListAdapter = null;

                        for (int i = 0; i < faces.size(); i++) {
                            Face thisFace = faces.valueAt(i);
                            float x1 = thisFace.getPosition().x;
                            float y1 = thisFace.getPosition().y;
                            float x2 = x1 + thisFace.getWidth();
                            float y2 = y1 + thisFace.getHeight();
                            if (x1 >= 0 && y1 >= 0) {
                                tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
                                Bitmap croppedImage = Bitmap.createBitmap(bitmap, (int) x1 + 1, (int) y1 + 1, (int) thisFace.getWidth() + 1, (int) thisFace.getHeight() + 1);
                                croppedImgList.add(croppedImage);
                            }
                        }
                        //imageListAdapter.notifyDataSetChanged();
                        imageListAdapter = new ImageListAdapter(croppedImgList, null);
                        //croppedImgListView.setAdapter(imageListAdapter);
                        imageViewPhoto.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));

                    }
//
//            thumbnail = Utilities.compressImage(mImagePath);
//            refugeePhoto = thumbnail;

                    if (croppedImgList.size() > 0) {

                        for (int i = 0; i < croppedImgList.size(); i++) {
                            FileOutputStream fo;
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            File destination = new File(getFilesDir().getAbsolutePath(),
                                    System.currentTimeMillis() + Integer.toString(i) + ".jpg");
                            croppedImgList.get(i).compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            mImagePath = destination.getAbsolutePath();
                            imagePathList.add(mImagePath);
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
                        mImagePath = imagePathList.get(0);
                      /*  if (croppedImgListView.findViewHolderForAdapterPosition(0) != null) {
                            croppedImgListView.findViewHolderForAdapterPosition(0).itemView.setBackgroundColor(Color.rgb(50, 205, 50));
                        }*/
                    } else {
                        Toast.makeText(getApplicationContext(), "No face detected, please use another photo.", Toast.LENGTH_LONG).show();
                        return;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch(Exception ex){
            Toast.makeText(this, "Failed to load image, please try again", Toast.LENGTH_SHORT).show();
        }
       // refugeePhoto = bm;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permission = true;
                } else {
                    permission = false;
                    Toast.makeText(FaceRecognitionActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private class GetCountryList extends  AsyncTask<String, Void, String> {
       // ProgressDialog asyncDialog = new ProgressDialog(FaceRecognitionActivity.this);

        @Override
        protected void onPreExecute() {
            //set message of the dialog
         //   asyncDialog.setMessage(getString(R.string.loadingtype));
            //show dialog
           // asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            try {
                String tokenResponse = Utilities.SendTokenPostRequest();
                JSONObject jsonObject = new JSONObject(tokenResponse);
                String token = jsonObject.getString("access_token");
                String tokenType = jsonObject.getString("token_type");
                return Utilities.SendGetRequest(Utilities.REFUGEE + "?list=country", token, tokenType);

//                Connection connection = Utilities.getMySqlConnection();
//                Statement statement = connection.createStatement();
//
//                String query = "SELECT * FROM `tb_country_code` WHERE `isEnabled` = 1";
//
//                ResultSet result = statement.executeQuery(query);
//                while (result.next()) {
//                    countryList.add(result.getString("countryName"));
//                }
//                connection.close();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                //Log.i("Register", result);
              //  asyncDialog.dismiss();
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                if(result!=null){
                    JSONObject jsonObject = new JSONObject(result);
                    String[] countryArray = jsonObject.getString("Result").split(",");
                    countryList = Arrays.asList(countryArray);
                    dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.country_spinner_item, countryList);
                    countrySpinner.setAdapter(dataAdapter);
                    dataAdapter.notifyDataSetChanged();
                }
            }
            catch(Exception ex) {
                Toast.makeText(getApplicationContext(), "Please check your internet connection",Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
    }

    private class FaceRecognition extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(FaceRecognitionActivity.this);

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
//
//                if(Utilities.uploadFile(mFtpClient, new File(mImagePath), imageUploadPath)) {
//                    HashMap<String, String> verifyData = new HashMap<>();
//                    verifyData.put("SourceImageURL", "http://" + "192.168.0.100" + "/UPLOAD/" + imageUploadPath);
//                    verifyData.put("InputType", inputType);
//                    verifyData.put("InputValue", inputValue);
//                    Utilities utilities = new Utilities();
//                   // Log.i("URL", "http://" + Utilities.IP + "/HIKWS.asmx/_CompareFace1v1");
//                    String result = Utilities.sendPostRequest("http://" + Utilities.IP_NoPort + "/HIKWS.asmx/CompareFace1v1", verifyData, null, null);
//                    result = result.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
//                    result = result.replace("<string xmlns=\"http://tris.my/webservice\">", "");
//                    result = result.replace("</string>", "");
//                    //Log.i("Face Recog Result", result);
//
//                    return result;
//                }
                //Log.i("Image Path", mImagePath);
                String result = DataService.UploadFileWithBase64String("2",mImagePath);
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


                        Intent intent = new Intent(getApplicationContext(), Face1v1ResultActivity.class);
                        intent.putExtra("photoPath", mImagePath);
                        intent.putExtra("result", similarity);
                        intent.putExtra("inputType",inputType);
                        intent.putExtra("inputValue", inputValue);

                        startActivity(intent);
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

    private class FaceRecognitionVsMany extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(FaceRecognitionActivity.this);

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
                String result = DataService.UploadFileWithBase64String("2",mImagePath);
                Log.d("yyy",result);
                if (result != null && result != "ERROR") {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getString("success").equalsIgnoreCase("true")){
                        String dataImage = jsonObject.getString("data");


                        HashMap<String, String> verifyData = new HashMap<>();
                        verifyData.put("SourceImageURL", dataImage);
//                verifyData.put("Country", country);
//                        if(gender.equals("ANY")){
//                            gender = "";
//                        }
//                        verifyData.put("Gender", gender);
//
//                        verifyData.put("DobFrom",yearOfBirthFrom);
//
//                        verifyData.put("DobTo",yearOfBirthTo);

                        verifyData.put("MaxResult","5");
                        //verifyData.put("YearOfBirth",yearOfBirth);
                        Utilities utilities = new Utilities();

//                        String resultVerifyFace1VMany = Utilities.sendPostRequest("http://" + Utilities.IP_NoPort + "/HIKWS.asmx/CompareFace1vManyTV", verifyData, null, null);
                        String resultVerifyFace1VMany = Utilities.sendPostRequest("http://" + Utilities.IP_NoPort + "/HIKWS.asmx/CompareFaceAllv2", verifyData, null, null);

                        resultVerifyFace1VMany = resultVerifyFace1VMany.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
                        resultVerifyFace1VMany = resultVerifyFace1VMany.replace("<string xmlns=\"http://tris.my/webservice\">", "");
                        resultVerifyFace1VMany = resultVerifyFace1VMany.replace("</string>", "");


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
                Log.d("xxx",result);
                if (result != null && result != "ERROR") {

                    JSONArray jsonArray = new JSONArray(result);
                    ArrayList<Refugee> refugeeArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        refugeeArrayList.add(new Refugee(jsonObject.getString("myrc"), jsonObject.getDouble("similarity")));

                    }

                    // ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // refugeePhoto.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    //byte[] byteArray = stream.toByteArray();

                    Intent intent = new Intent(getApplicationContext(), Face1vManyResultActivity.class);
                    intent.putExtra("photoPath", mImagePath);
                    intent.putExtra("refugee_list", refugeeArrayList);

                    startActivity(intent);
                }


            } catch (Exception ex) {
                asyncDialog.dismiss();
                Toast.makeText(getApplicationContext(),"No result found.", Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
    }

    private void getYearOfBirth(){
        int yearRange = inputYear.getSelectedItemPosition();
        Calendar now = Calendar.getInstance();
        switch (yearRange){
            case 0:
                yearOfBirthFrom = "";
                yearOfBirthTo = "";
                break;
            case 1:
                yearOfBirthFrom = Integer.toString(now.get(Calendar.YEAR) - 20);
                yearOfBirthTo = Integer.toString(now.get(Calendar.YEAR) - 13);
                break;
            case 2:
                yearOfBirthFrom = Integer.toString(now.get(Calendar.YEAR) - 25);
                yearOfBirthTo = Integer.toString(now.get(Calendar.YEAR) - 21);
                break;
            case 3:
                yearOfBirthFrom = Integer.toString(now.get(Calendar.YEAR) - 30);
                yearOfBirthTo = Integer.toString(now.get(Calendar.YEAR) - 26);
                break;
            case 4:
                yearOfBirthFrom = Integer.toString(now.get(Calendar.YEAR) - 35);
                yearOfBirthTo = Integer.toString(now.get(Calendar.YEAR) - 31);
                break;
            case 5:
                yearOfBirthFrom = Integer.toString(now.get(Calendar.YEAR) - 40);
                yearOfBirthTo = Integer.toString(now.get(Calendar.YEAR) - 36);
                break;
            case 6:
                yearOfBirthFrom = Integer.toString(now.get(Calendar.YEAR) - 50);
                yearOfBirthTo = Integer.toString(now.get(Calendar.YEAR) - 41);
                break;
            case 7:
                yearOfBirthFrom = Integer.toString(now.get(Calendar.YEAR) - 60);
                yearOfBirthTo = Integer.toString(now.get(Calendar.YEAR) - 51);
                break;
            case 8:
                yearOfBirthFrom = Integer.toString(now.get(Calendar.YEAR) - 100);
                yearOfBirthTo = Integer.toString(now.get(Calendar.YEAR) - 61);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //imageListAdapter.notifyDataSetChanged();
    }
}
