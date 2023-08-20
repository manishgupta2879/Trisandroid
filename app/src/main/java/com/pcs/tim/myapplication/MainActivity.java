package com.pcs.tim.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.pcs.tim.myapplication.new_activities.FaceRegisterActivity;
import com.pcs.tim.myapplication.new_activities.HomeActivity;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    public final static String MY_RC_RMB_ME = "MyRC Remember Me";

    private final static String LOGIN_URL = "http://tris.wmpos.my/DB/login.php";
    static final int REGISTER_REQUEST = 1;

    private SharedPreferences sharePref;
    private String id;
    private String password;
    private String dbResult;
    private EditText editTextID;
    private EditText editTextPassword;
    private CheckBox checkBoxRmb;
    private boolean isScannerView = false;
    private ZXingScannerView scannerView;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    public RemoteConfigResponse rcResponse;
    String token;
   /* String[] permissions=new String[]{
            Manifest.permission.POST_NOTIFICATIONS
    };*/

    boolean permission_post_notification=false;
    ImageView icPass;

    private boolean showPass=false;
    long cacheExpiration = 3600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataService.instance(this.getApplicationContext());
        sharePref = getSharedPreferences(Utilities.MY_RC_SHARE_PREF, MODE_PRIVATE);

        boolean rememberMe = sharePref.getBoolean(MY_RC_RMB_ME, false);
        FirebaseApp.initializeApp(this);

        // Get the FCM token
        token = FirebaseInstanceId.getInstance().getToken();
        if(token!=null){
            Log.d("fcmtoken__", "onCreate: "+token);
        }else {
            Log.d("fcmtoken__", "onCreate: cant get token");
        }
        editTextID = (EditText) findViewById(R.id.editTextID);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        // You can now use the 'token' as needed, such as sending it to your server.
        //   Log.d("FCM Token", token);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        icPass = findViewById(R.id.ic_pass);

        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config);


        firebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            firebaseRemoteConfig.fetchAndActivate();
                            rcResponse = new Gson().fromJson(firebaseRemoteConfig.getString("common_config"), RemoteConfigResponse.class);
                            Log.d("RemoteConfig", firebaseRemoteConfig.getString("common_config"));

                            DataService.instance().storeValueString(DataService.VERIMYRC_URL, rcResponse.getUrl());
                            //edit by deepak
                            DataService.instance().storeValueString(DataService.VERIMYRC_API_URL, rcResponse.getApiUrl());
                            // DataService.instance().storeValueString(DataService.VERIMYRC_API_URL, "www.dhillonfarm.com");
                            DataService.instance().storeValueString(DataService.VERIMYRC_ANDROID_VERSION, rcResponse.getAndroidVersion());
                            DataService.instance().storeValueString(DataService.VERIMYRC_ANDROID_PACKAGE_NAME, rcResponse.getAndroidPackageName());
                            new RequestToken().execute();

                            try {

                                PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                                String version = pInfo.versionName;

                                Log.d("RemoteConfig__", "onComplete: version " + version + " firebase version " + rcResponse.getAndroidVersion());

                                if (!version.equals(rcResponse.getAndroidVersion())) {
                                    Log.d("check_version", version);
                                    Log.d("check_version", rcResponse.getAndroidVersion());
                                    setTitle("v" + rcResponse.getAndroidVersion());
                                    AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                                    adb.setTitle("Latest version available");
                                    adb.setMessage("Please update to latest version");
                                    adb.setIcon(R.mipmap.ic_tris_logo);
                                    adb.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
//                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Utilities.PACKAGE_NAME)));
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(rcResponse.getAndroidPackageName())));
                                            } catch (
                                                    android.content.ActivityNotFoundException anfe) {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(rcResponse.getAndroidPackageName())));
                                            }
                                        }
                                    });
                                    adb.setCancelable(false);
                                    AlertDialog alert = adb.create();
                                    alert.show();
                                }

                            } catch (PackageManager.NameNotFoundException e) {
                                Log.d("RemoteConfig", e.getMessage());
                                e.printStackTrace();
                            }


                        } else {

                        }
                    }
                });


        if (rememberMe) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }


     /*   if (!permission_post_notification) {

            requestPermissionNotification();
        }*/




        //new GetAppVersion().execute();

        Utilities utilities = new Utilities();
        Log.i("Internet", Boolean.toString(utilities.isOnline(this)));
        if (rememberMe) {
            editTextID = (EditText) findViewById(R.id.editTextID);
            editTextPassword = (EditText) findViewById(R.id.editTextPassword);

            // editTextPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            checkBoxRmb = (CheckBox) findViewById(R.id.checkBoxRememberMe);
            editTextID.setText(DataService.instance().fetchValueString(Utilities.LOGIN_POLICE_ID));
            editTextPassword.setText(DataService.instance().fetchValueString(Utilities.LOGIN_POLICE_PWD));
            checkBoxRmb.setChecked(true);
//            Intent intent = new Intent(this, MyRCVerification.class);
//            startActivity(intent);
//            finish();
        }

        Button textView = findViewById(R.id.textViewRegister);
        SpannableString content = new SpannableString(getResources().getString(R.string.register_account));
        textView.setText(content);
        scannerView = new ZXingScannerView(this);


        TextView buttonOffline = findViewById(R.id.buttonOffline);
        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(Manifest.permission.CAMERA, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
                    //qrScan.initiateScan();
                    setContentView(scannerView);
                    scannerView.setResultHandler(MainActivity.this);
                    if (Build.MANUFACTURER.toLowerCase().contains("huawei"))
                        scannerView.setAspectTolerance(0.5f);
                    scannerView.startCamera();
                    isScannerView = true;

                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            1);
                }
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        icPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (showPass) {
                        editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_open_eye); // Replace with your drawable resource
                        icPass.setImageDrawable(drawable);
                        showPass=false;
                    } else {
                        editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_close_eye); // Replace with your drawable resource
                        icPass.setImageDrawable(drawable);
                        showPass=true;

                    }

            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AccountRegistrationActivity.class);
                startActivityForResult(intent, REGISTER_REQUEST);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        //new GetAppVersion().execute();
    }

   /* public void requestPermissionNotification(){
        if(ContextCompat.checkSelfPermission(MainActivity.this,permissions[0])== PackageManager.PERMISSION_GRANTED){
            permission_post_notification=true;
        }
        else{
        *//*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)){
                    Log.d("message___", "requestPermissionNotification: "+"1st dont Allow");
                }
                else{
                    Log.d("message___", "requestPermissionNotification: "+"2nd dont Allow");
                }

            }*//*
            requestPermissionLauncherNotification.launch(permissions[0]);
        }
    }*/

    @Override
    public void handleResult(Result result) {
        final String myResult = result.getText();
        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());

        if (myResult == null) {
            Toast.makeText(this, "MyRC Not Found", Toast.LENGTH_LONG).show();
        } else {
            //if qr contains data
            try {

                Intent intent = new Intent(this, VerificationResultActivity.class);
                intent.putExtra(Utilities.MY_RC, myResult);
                intent.putExtra("offline", true);
                startActivity(intent);
                finish();

            } catch (Exception e) {
                e.printStackTrace();
                //if control comes here
                //that means the encoded format not matches
                //in this case you can display whatever data is available on the qrcode
                //to a toast
                Toast.makeText(this, myResult, Toast.LENGTH_LONG).show();
            }
        }
    }

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Scan Result");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                scannerView.resumeCameraPreview(MyRCVerification.this);
//            }
//        });
//        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myResult));
//                startActivity(browserIntent);
//            }
//        });
//        builder.setMessage(result.getText());
//        AlertDialog alert1 = builder.create();
//        alert1.show();


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            //if qrcode has nothing in it
//            if (result.getContents() == null) {
//                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
//            } else {
//                //if qr contains data
//                try {
//                    //converting the data to json
//                    String myRCResult = result.getContents();
//                    Intent intent = new Intent(this, VerificationResultActivity.class);
//                    intent.putExtra(Utilities.MY_RC, myRCResult);
//                    intent.putExtra("offline",true);
//                    startActivity(intent);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    //if control comes here
//                    //that means the encoded format not matches
//                    //in this case you can display whatever data is available on the qrcode
//                    //to a toast
//                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//
//    }

  /*  private ActivityResultLauncher<String> requestPermissionLauncherNotification=
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{
                if(isGranted){
                    permission_post_notification=true;

                }
                else{
                    permission_post_notification=false;
                //    showPermissionDialog("notification Permission");
                }
            });*/


    public  void showPermissionDialog(String permission_desc){
        new androidx.appcompat.app.AlertDialog.Builder(
                MainActivity.this
        ).setTitle("allert For Permission")
                .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent=new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri=Uri.fromParts("package",getPackageName(),null);
                        intent.setData(uri);
                        startActivity(intent);
                        dialogInterface.dismiss();

                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })

                .show();
    }

    //@TargetApi(23)
    private void login() {
        checkBoxRmb = (CheckBox) findViewById(R.id.checkBoxRememberMe);
        editTextID = (EditText) findViewById(R.id.editTextID);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        id = editTextID.getText().toString();
        password = editTextPassword.getText().toString();

        if (!id.trim().isEmpty() && !password.trim().isEmpty()) {
            /*if(checkPassword(id, password)){

                if(checkBoxRmb.isChecked()){
                    SharedPreferences.Editor editor= sharePref.edit();
                    editor.putBoolean(MY_RC_RMB_ME,true);
                    editor.apply();
                }
                Intent intent = new Intent(this, MyRCVerification.class);
                startActivity(intent);
                finish();
            }*/
            if (checkPermission()) {
                new UserLoginTask().execute(Utilities.LOGIN);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.INTERNET},
                        1);
            }

        } else {
            editTextID.setError("Invalid ID or Password");
            editTextPassword.setError("Invalid ID or Password");
        }
    }

    private boolean checkPermission() {
        return checkPermission(android.Manifest.permission.INTERNET, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED;
    }

    private class RequestToken extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                boolean success = DataService.RequestToken();
//                JSONObject jsonObject = new JSONObject(tokenResult);
//                if(jsonObject.getString("success").equalsIgnoreCase("true")){
//                    return jsonObject.getString("data");
//                }else{
//                    return "ERROR";
//                }
                return success;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Boolean result) {
            try {
                if (result) {

                    try {
//                        Gson gson = new Gson();
//                        RequestTokenResponse resp = gson.fromJson(result, RequestTokenResponse.class);
//                        DataService.accessToken = resp.getAccessToken();
//                        DataService.refreshToken = resp.getRefreshToken();
//                        Toast.makeText(getApplicationContext(), DataService.accessToken + " " + DataService.refreshToken , Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid credential. Please try again later.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }


    private class GetAppVersion extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                String versionResult = DataService.GetAppVersion();

                if (versionResult != null && versionResult != "ERROR") {
                    JSONObject jsonObject = new JSONObject(versionResult);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        return jsonObject.getString("data");
                    } else {
                        return "ERROR";
                    }
                } else {
                    return "ERROR";
                }
//                    JSONObject jsonObject = new JSONObject(versionResult);
//
//                    return jsonObject.getString("version");

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {

                if (result != null && result != "ERROR") {

                    try {
                        AppVersionResponse respAppVersion = new Gson().fromJson(result, AppVersionResponse.class);
                        PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                        String version = pInfo.versionName;

                        if (!version.equals(respAppVersion.getVersion())) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                            adb.setTitle("Latest version available");
                            adb.setMessage("Please update to latest version");
                            adb.setIcon(R.mipmap.ic_tris_logo);
                            adb.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Utilities.PACKAGE_NAME)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Utilities.APP_URL)));
                                    }
                                }
                            });
                            adb.setCancelable(false);
                            AlertDialog alert = adb.create();
                            alert.show();
                        }

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No internet access.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }


    private class sendFcmToken extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(MainActivity.this);
        boolean successFlag = false;



     /*   @Override
        protected void onPreExecute() {
            asyncDialog.setMessage(getString(R.string.loadingtype));
            //show dialog
            asyncDialog.show();
            super.onPreExecute();
        }*/

        @Override
        protected String doInBackground(String... strings) {

            try {
                //    Log.d("fcmToken___doinBack", "doInBackground: "+id);
                String id = sharePref.getString(Utilities.LOGIN_ID, "");
                Log.d("fcmToken___doinBack", "doInBackground: " + id);
                String result = DataService.sendFcmToken(Integer.parseInt(id), token);
                Log.d("fcmToken___doinBack", "doInBackground: token  " + token);

                return "Success";
          /*      if (result != null && result != "ERROR") {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        successFlag = true;
                        return jsonObject.getString("data");
                    } else {
                        return jsonObject.getString("message");
                    }
                } else {
                    return "ERROR";
                }*/
            } catch (Exception e) {
                Log.d("fcmToken___error", "error: " + e.getMessage());
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            //show dialog

            super.onPostExecute(s);
        }

    }

    private class UserLoginTask extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(MainActivity.this);
        boolean successFlag = false;

        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setMessage(getString(R.string.loadingtype));
            //show dialog
            asyncDialog.show();
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {

            try {
//                String tokenResponse = Utilities.SendTokenPostRequest();
//                JSONObject jsonObject = new JSONObject(tokenResponse);
//                String token = jsonObject.getString("access_token");
//                String tokenType = jsonObject.getString("token_type");
//                HashMap<String,String> data = new HashMap<>();
//                data.put(Utilities.POLICE_ID , id);
//                data.put(Utilities.PASSWORD, Utilities.getMD5Hash(password));
//                return Utilities.sendPostRequest(Utilities.LOGIN, data, token, tokenType);

                Log.d("resultssdbc", Utilities.getMD5Hash(password));
                Log.d("resultssdbc", String.valueOf(id));
                String result = DataService.EnforcementLogin(id, Utilities.getMD5Hash(password));
                Log.d("resultssdbc", result);
                if (result != null && result != "ERROR") {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        successFlag = true;
                        return jsonObject.getString("data");
                    } else {
                        return jsonObject.getString("message");
                    }
                } else {
                    return "ERROR";
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String result) {

            asyncDialog.dismiss();
            try {
                Log.i("resultss", result);

                SharedPreferences.Editor editor = sharePref.edit();

                if (successFlag) {
                    if (result != null && result != "ERROR") {
                        Log.d("resultss", "1");
                        EnforcementLoginResponse respEnforcementLogin = new Gson().fromJson(result, EnforcementLoginResponse.class);

                        String id = respEnforcementLogin.getId().toString();
                        String police_id = respEnforcementLogin.getPoliceId();
                        String police_photo = respEnforcementLogin.getPhoto();

                        String police_name = respEnforcementLogin.getFullname();
                        String police_station = respEnforcementLogin.getStation();
                        String police_agency = respEnforcementLogin.getAgency();
                        String police_rank = respEnforcementLogin.getPoliceRank();
                        String police_state = respEnforcementLogin.getState();
                        String police_dept = respEnforcementLogin.getDepartment();
                        String police_hp = respEnforcementLogin.getMobileNo();
                        String police_ic = respEnforcementLogin.getIcNumber();
                        String status = respEnforcementLogin.getStatus();

                        String remark = respEnforcementLogin.getRemark();

                        if (status.equalsIgnoreCase("Y")) {
                            if (checkBoxRmb.isChecked()) {

                                editor.putBoolean(MY_RC_RMB_ME, true);

                            }
                            if (police_photo != null && !police_photo.isEmpty())
                                editor.putString(Utilities.LOGIN_POLICE_PHOTO, police_photo);
                            editor.putString(Utilities.LOGIN_POLICE_AGENCY, police_agency);
                            editor.putString(Utilities.LOGIN_POLICE_DEPT, police_dept);
                            editor.putString(Utilities.LOGIN_POLICE_IC, police_ic);
                            editor.putString(Utilities.LOGIN_POLICE_NAME, police_name);
                            editor.putString(Utilities.LOGIN_POLICE_STATION, police_station);
                            editor.putString(Utilities.LOGIN_POLICE_MOBILE, police_hp);
                            editor.putString(Utilities.LOGIN_POLICE_STATE, police_state);
                            editor.putString(Utilities.LOGIN_POLICE_RANK, police_rank);
                            editor.putString(Utilities.LOGIN_POLICE_ID, police_id);
                            editor.putString(Utilities.LOGIN_ID, id);
                            editor.putString(Utilities.LOGIN_POLICE_PWD, password);
                            editor.putString(Utilities.LOGIN_POLICE_STATUS, status);
                            editor.putBoolean(Utilities.LOGGED_IN, true);
                            editor.apply();


                            if (checkPermission()) {
                                Log.d("fcmToken___cp", "onPostExecute: checkPermission");
                                new sendFcmToken().execute(Utilities.LOGIN);
                            } else {
                                Log.d("fcmToken___else", "onPostExecute: else");
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.INTERNET},
                                        1);
                            }
                            Intent intent = new Intent(getApplicationContext(), FaceRegisterActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (status.equalsIgnoreCase(("W"))) {
                            Toast.makeText(getApplicationContext(), "Your account is rejected. Remark :" + remark, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Account is yet to be approved or username and password not match.", Toast.LENGTH_LONG).show();
                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                }

//                else {
//                    editTextID.setError(getString(R.string.err_incorrect_password_or_id));
//                    editTextPassword.setError(getString(R.string.err_incorrect_password_or_id));
//                    editTextID.requestFocus();
//                }
                /*JSONObject jsonObject = new JSONObject(result);
                SharedPreferences.Editor editor = sharePref.edit();

                if (jsonObject.getBoolean("success")) {
                    if(checkBoxRmb.isChecked()) {

                        editor.putBoolean(MY_RC_RMB_ME, true);

                    }
                    editor.putString(Utilities.LOGIN_POLICE_ID, id);
                    editor.putBoolean(Utilities.LOGGED_IN, true);
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), MyRCVerification.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    editTextID.setError(jsonObject.getString("error"));
                    editTextPassword.setError(jsonObject.getString("error"));
                    editTextID.requestFocus();
                }*/
            } catch (Exception ex) {
                Log.e("MyRCLoginddd", ex.toString());
                if (result == null) {
                    Toast.makeText(getApplicationContext(), "Account is yet to be approved or username and password not match.", Toast.LENGTH_LONG).show();
                } else if (result.isEmpty())
                    Toast.makeText(getApplicationContext(), "Server unavailable currently, please try again later", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(getApplicationContext(), "Account is yet to be approved or username and password not match.", Toast.LENGTH_LONG).show();
                }
            }

            // }

        }
    }


    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    @Override
    public void onBackPressed() {
        // Write your code here
        if (isScannerView) {
            scannerView.stopCamera();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            //setContentView(R.layout.activity_my_rc_verification);
            isScannerView = false;
            finish();
        } else
            super.onBackPressed();
        //

    }
}

