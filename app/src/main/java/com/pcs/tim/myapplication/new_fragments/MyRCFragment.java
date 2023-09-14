package com.pcs.tim.myapplication.new_fragments;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Process;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.pcs.tim.myapplication.ApiKeyResponse;
import com.pcs.tim.myapplication.AppVersionResponse;
import com.pcs.tim.myapplication.DataService;
import com.pcs.tim.myapplication.FaceRecognitionActivity;
import com.pcs.tim.myapplication.MainActivity;
import com.pcs.tim.myapplication.MyRCVerification;
import com.pcs.tim.myapplication.new_activities.HomeActivity;
import com.pcs.tim.myapplication.new_fragments.MyRCFragment;
import com.pcs.tim.myapplication.NFCActivity;
import com.pcs.tim.myapplication.ProfileActivity;
import com.pcs.tim.myapplication.R;
import com.pcs.tim.myapplication.RemoteConfigResponse;
import com.pcs.tim.myapplication.SearchMenuActivity;
import com.pcs.tim.myapplication.SearchNameActivity;
import com.pcs.tim.myapplication.Utilities;
import com.pcs.tim.myapplication.VerificationResultActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class MyRCFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ZXingScannerView.ResultHandler {

    private boolean isScannerView = false;
    private ZXingScannerView scannerView;
    protected static final String TAG = "MyRC Verification";

    private float lat = 0;
    private float lng = 0;
    private String apikey = "";
    SharedPreferences sharedPreferences;

    //private TextView userLoginTxt;
    private String locationAddress;
    private String mAddressOutput;
    protected Location mLastLocation;
    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    private LocationCallback mLocationCallback;
    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates = false;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime, searchQuery;
    private MyRCFragment.AddressResultReceiver mResultReceiver;
    private FusedLocationProviderClient mFusedLocationClient;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    public RemoteConfigResponse rcResponse;
    LinearLayout iconsLay, logoLay;


    public MyRCFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_r_c, container, false);


        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setTitle("Fragment Title");
        }
        iconsLay = view.findViewById(R.id.iconsLay);
        logoLay = view.findViewById(R.id.logoLay);



        iconsLay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                return false;
            }
        });
        logoLay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                return false;
            }
        });
        Toolbar toolbar = view.findViewById(R.id.home_toolbar); // Assuming you have a Toolbar in your layout
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_back); // Set your back button icon
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle back button click here (e.g., pop the Fragment from the back stack)
                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }
                }
            });
        }


        Log.d("dddddddd", "onCreate: MyRCFragment");
        DataService.instance(getActivity());

        sharedPreferences = getActivity().getSharedPreferences(Utilities.MY_RC_SHARE_PREF, MODE_PRIVATE);

        //userLoginTxt = (TextView)findViewById(R.id.loginUser);
        if (sharedPreferences.getString(Utilities.LOGIN_POLICE_ID, null) == null) {
            logout();
        }
        new MyRCFragment.GetApiKey().execute();
        //welcomeTxt = "Logged in ID: " + sharedPreferences.getString(Utilities.LOGIN_POLICE_ID, null);
        //userLoginTxt.setText(welcomeTxt);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        scannerView = new ZXingScannerView(getActivity());
        mResultReceiver = new MyRCFragment.AddressResultReceiver(new Handler());

        EditText editQuery = (EditText) view.findViewById(R.id.edit_query);
        Button btnLogout = (Button) view.findViewById(R.id.buttonLogout);
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config);
        final FragmentManager fm = getFragmentManager();
        Fragment profileFragment = new ProfileFragment();
        Fragment searchMenuFragment=new SearchMenuFragment();
        Fragment searchUNHCRFragment = new SearchUNHCRFragment();
        Fragment searchNameFragment = new SearchNameFragment();

        FragmentTransaction transaction = fm.beginTransaction();

        firebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            firebaseRemoteConfig.fetchAndActivate();
                            rcResponse = new Gson().fromJson(firebaseRemoteConfig.getString("common_config"), RemoteConfigResponse.class);
                            Log.d("RemoteConfig", firebaseRemoteConfig.getString("common_config"));

                            DataService.instance().storeValueString(DataService.VERIMYRC_URL, rcResponse.getUrl());
                            DataService.instance().storeValueString(DataService.VERIMYRC_API_URL, rcResponse.getApiUrl());
                            //DataService.instance().storeValueString(DataService.VERIMYRC_API_URL,"www.dhillonfarm.com");
                            DataService.instance().storeValueString(DataService.VERIMYRC_ANDROID_VERSION, rcResponse.getAndroidVersion());
                            DataService.instance().storeValueString(DataService.VERIMYRC_ANDROID_PACKAGE_NAME, rcResponse.getAndroidPackageName());
                            new MyRCFragment.RequestToken().execute();

                            try {

                                if(isAdded()) {
                                    PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                                    String version = pInfo.versionName;

                                    if (!version.equals(rcResponse.getAndroidVersion())) {
                                        Log.d("check_version", version);
                                        Log.d("check_version", rcResponse.getAndroidVersion());
                                        getActivity().setTitle("v" + rcResponse.getAndroidVersion());
                                        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
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

                                }

                            } catch (PackageManager.NameNotFoundException e) {
                                Log.d("RemoteConfig", e.getMessage());
                                e.printStackTrace();
                            }


                        } else {

                        }
                    }
                });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Are you sure to logout?");
                adb.setIcon(R.mipmap.ic_tris_logo);
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });


                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                adb.show();
            }
        });

        ImageView btnSearch = (ImageView) view.findViewById(R.id.searchBtn);
        SearchView searchView = view.findViewById(R.id.sv);


        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchQuery = s;
                if (searchQuery.isEmpty() || searchQuery.length() < 5) {
                    //Toast.makeText(getApplicationContext(), "Please insert at least 3 characters to search for refugee.", Toast.LENGTH_LONG).show();
                    editQuery.setError(getString(R.string.search_name_err));
                } else {
                    Intent intent = new Intent(getActivity(), SearchNameActivity.class);
                    intent.putExtra("query", searchQuery);
                    startActivity(intent);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchQuery = editQuery.getText().toString();
                if (searchQuery.isEmpty() || searchQuery.length() < 5) {
                    //Toast.makeText(getApplicationContext(), "Please insert at least 3 characters to search for refugee.", Toast.LENGTH_LONG).show();
                    editQuery.setError(getString(R.string.search_name_err));
                } else {
                    Intent intent = new Intent(getActivity(), SearchNameActivity.class);
                    intent.putExtra("query", searchQuery);
                    startActivity(intent);
                }
            }
        });

//        LinearLayout logout = (LinearLayout) findViewById(R.id.logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder adb = new AlertDialog.Builder(MyRCFragment.this);
//                adb.setTitle("Are you sure to logout?");
//                adb.setIcon(R.mipmap.ic_tris_logo);
//                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        logout();
//                    } });
//
//
//                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    } });
//                adb.show();
//            }
//        });

        Button btnMyProfile = (Button) view.findViewById(R.id.buttonProfile);
        btnMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);*/

                transaction.replace(R.id.main_container, profileFragment);
                transaction.addToBackStack(null); // Optional: Add to back stack
                transaction.commit();
            }
        });
        if (getActivity().checkPermission(Manifest.permission.INTERNET, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
            Button buttonScanQR = (Button) view.findViewById(R.id.button_scan_qr);

            buttonScanQR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (getActivity().checkPermission(Manifest.permission.CAMERA, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
                        //qrScan.initiateScan();
                        getActivity().setContentView(scannerView);
                        scannerView.setResultHandler(MyRCFragment.this);
                        if (Build.MANUFACTURER.toLowerCase().contains("huawei"))
                            scannerView.setAspectTolerance(0.5f);
                        scannerView.startCamera();
                        isScannerView = true;

                    } else {
                        Toast.makeText(getActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                1);
                    }
                }
            });

            Button buttonSearch = (Button) view.findViewById(R.id.buttonSearch);
            Button buttonFaceRec = (Button) view.findViewById(R.id.button_face_rec);


            buttonFaceRec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Toast.makeText(activity, "In Progress", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), FaceRecognitionActivity.class);
                    startActivity(intent);
                }
            });

            buttonSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 /*   Intent intent = new Intent(getActivity(), SearchMenuActivity.class);
                    startActivity(intent);*/
                    transaction.replace(R.id.main_container, searchMenuFragment);
                    transaction.addToBackStack(null); // Optional: Add to back stack
                    transaction.commit();

                }
            });

            Button buttonFaceRecognition = (Button) view.findViewById(R.id.button_scan_face);

            buttonFaceRecognition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //edit by deepak
                  /*  Intent intent = new Intent(getActivity(), FaceRecognitionActivity.class);
                    startActivity(intent);*/

                    transaction.replace(R.id.main_container, profileFragment);
                    transaction.addToBackStack(null); // Optional: Add to back stack
                    transaction.commit();
                }
            });

           // Button buttonNFC = (Button) view.findViewById(R.id.button_NFC);

           /* buttonNFC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), NFCActivity.class);

                    startActivityForResult(intent, 000);
                }
            });*/
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.INTERNET},
                    1);
        }

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building the GoogleApiClient, LocationRequest, and
        // LocationSettingsRequest objects.
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
        checkLocationSettings();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Log.e("Locations", location.toString());
                }
            }

            ;
        };

        while (!(getActivity().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);


            if (getActivity().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                mLastLocation = location;
                                if (mLastLocation == null) {

                                    lat = 0;
                                    lng = 0;
                                    return;
                                } else {
                                    lat = (float) location.getLatitude();
                                    lng = (float) location.getLongitude();
                                }

                                if (!Geocoder.isPresent()) {
                                    Toast.makeText(getActivity(),
                                            R.string.no_geocoder_available,
                                            Toast.LENGTH_LONG).show();
                                    return;
                                }

                                // Start service and update UI to reflect new location
                                Utilities.startIntentService(getActivity(), mResultReceiver, mLastLocation);
                            }
                        });
            }
        }

        if(getActivity().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(),Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.d("success__", "onSuccess: ");
                            mLastLocation = location;
                            if( mLastLocation == null ) {

                                lat = 0;
                                lng = 0;
                                return;
                            }
                            else {
                                lat =  (float)location.getLatitude();
                                lng =  (float)location.getLongitude();
                            }

                            if (!Geocoder.isPresent()) {
                                Toast.makeText(getActivity(),
                                        R.string.no_geocoder_available,
                                        Toast.LENGTH_LONG).show();
                                return;
                            }

                            // Start service and update UI to reflect new location
                            Utilities.startIntentService(getActivity(),mResultReceiver,mLastLocation);
                        }
                    }
                    );
        }
        else{
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
        return view;
    }


    private void hideKeyboard() {
        View currentFocus = getActivity().getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
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
                        PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                        String version = pInfo.versionName;

                        if (!version.equals(respAppVersion.getVersion())) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
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
                    Toast.makeText(getActivity(), "No internet access.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
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
                    Toast.makeText(getActivity(), "Invalid credential. Please try again later.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void handleResult(Result result) {
        final String myResult = result.getText();
        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());

        if (myResult == null) {
            Toast.makeText(getActivity(), "MyRC Not Found", Toast.LENGTH_LONG).show();
        } else {
            //if qr contains data
            try {

                Intent intent = new Intent(getActivity(), VerificationResultActivity.class);
                intent.putExtra(Utilities.MY_RC, myResult);
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
                //if control comes here
                //that means the encoded format not matches
                //in this case you can display whatever data is available on the qrcode
                //to a toast
                Toast.makeText(getActivity(), myResult, Toast.LENGTH_LONG).show();
            }
        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Scan Result");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                scannerView.resumeCameraPreview(MyRCFragment.this);
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
    }

 /*   @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }*/

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        mRequestingLocationUpdates = true;
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                                "upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                                "not created.");
                        break;
                }
            }
        });
    }

    /**
     * The callback invoked when
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} is called. Examines the
     * {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 000) {

            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("toastMsg");
                // do something with the result
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();

            }
        } else {
//            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//            if (result != null) {
//                //if qrcode has nothing in it
//                if (result.getContents() == null) {
//                    Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
//                } else {
//                    //if qr contains data
//                    try {
//                        //converting the data to json
//                        String myRCResult = result.getContents();
//                        Intent intent = new Intent(this, VerificationResultActivity.class);
//                        intent.putExtra(Utilities.MY_RC, myRCResult);
//                        startActivity(intent);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        //if control comes here
//                        //that means the encoded format not matches
//                        //in this case you can display whatever data is available on the qrcode
//                        //to a toast
//                        Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
//                    }
//                }
//            } else {
            switch (requestCode) {
                // Check for the integer request code originally supplied to startResolutionForResult().
                case REQUEST_CHECK_SETTINGS:
                    switch (resultCode) {
                        case RESULT_OK:
                            Log.i(TAG, "User agreed to make required location settings changes.");
                            break;
                        case RESULT_CANCELED:
                            Log.i(TAG, "User chose not to make required location settings changes.");
                            break;
                    }
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES,
                mRequestingLocationUpdates);
        // ...
        super.onSaveInstanceState(outState);
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Associate searchable configuration with the SearchView
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.myProfile:
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Are you sure to logout?");
                adb.setIcon(R.mipmap.ic_tris_logo);
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                adb.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
        //noinspection SimplifiableIfStatement
//        if (id == R.id.logout) {
//            AlertDialog.Builder adb = new AlertDialog.Builder(this);
//            adb.setTitle("Are you sure to logout?");
//            adb.setIcon(R.mipmap.ic_tris_logo);
//            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    logout();
//                } });
//
//
//            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                } });
//            adb.show();
//
        //}

    }

    private void logout() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Utilities.MY_RC_SHARE_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MainActivity.MY_RC_RMB_ME, false);
        editor.putBoolean(Utilities.LOGGED_IN, false);
        editor.apply();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        //userLoginTxt.setText(welcomeTxt);

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        if(isScannerView){
            setContentView(scannerView);
            scannerView.startCamera();
            scannerView.setResultHandler(MyRCFragment.this);
        }
        if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(),Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            mLastLocation = location;
                            if( mLastLocation == null ) {

                                lat = 0;
                                lng = 0;
                                return;
                            }
                            else {
                                lat =  (float)location.getLatitude();
                                lng =  (float)location.getLongitude();
                            }

                            if (!Geocoder.isPresent()) {
                                Toast.makeText(getApplicationContext(),
                                        R.string.no_geocoder_available,
                                        Toast.LENGTH_LONG).show();
                                return;
                            }

                            // Start service and update UI to reflect new location
                            Utilities.startIntentService(getApplicationContext(),mResultReceiver,mLastLocation);
                        }
                    });
        }
    }*/

    private void startLocationUpdates() {
        if (getActivity().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED)
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        else
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
    }

    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            /*if(resultData != null) {

                mAddressOutput = resultData.getString(Utilities.RESULT_DATA_KEY);
                buttonAddRemark.setVisibility(View.VISIBLE);
                Log.i("Address Output", mAddressOutput);
                if(!mAddressOutput.equals(getString(R.string.service_not_available))  || !mAddressOutput.equals(""))
                    locationAddress = mAddressOutput;
                else
                    locationAddress = Float.toString(lat) + ", " + Float.toString(lng);
            }*/
            if (resultData != null) {

                mAddressOutput = resultData.getString(Utilities.RESULT_DATA_KEY);

                if (mAddressOutput !=null && !mAddressOutput.equals("Service not available") && !mAddressOutput.equals(""))
                    locationAddress = mAddressOutput;
                else {
                    try {


                        MyRCFragment.GetAddress getAddrTask = new MyRCFragment.GetAddress();
                        Log.d("Addr", Float.toString(lat));
                        Log.d("Addr", Float.toString(lng));
                        Log.d("Addr", apikey);
                        getAddrTask.execute("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + Float.toString(lat) + "," + Float.toString(lng) + "&key=" + apikey);

                        getAddrTask.get(15000, TimeUnit.MILLISECONDS);
                    } catch (Exception ex) {
                        locationAddress = Float.toString(lat) + ", " + Float.toString(lng);
                        ex.printStackTrace();
                    }
                }
            } else {
                Utilities.startIntentService(getActivity(), mResultReceiver, mLastLocation);
            }
        }
    }

    private class GetApiKey extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                String result = DataService.GetApiKey();
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

            try {
                ApiKeyResponse respApiKey = new Gson().fromJson(result, ApiKeyResponse.class);
                String get_api_key = respApiKey.getApiKey();
                if (!get_api_key.isEmpty()) {
                    apikey = get_api_key;
                } else {
                    apikey = "AIzaSyDATvuiJEVbGGE7Cz8hA1MHzosIdKB3Rbw";
                }

//                JSONArray jsonarray = new JSONArray(result);
//                for(int i=0; i < jsonarray.length(); i++) {
//                    if(jsonarray.getString(i).equals("apiKey")){
//                        String get_api_key = jsonarray.getString(i);
//
//                        if(!get_api_key.isEmpty())
//                            apikey = get_api_key;
//                        else {
//                            apikey = "AIzaSyDATvuiJEVbGGE7Cz8hA1MHzosIdKB3Rbw";
//                        }
//                    }else{
//                        apikey = "AIzaSyDATvuiJEVbGGE7Cz8hA1MHzosIdKB3Rbw";
//                    }
//                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class GetAddress extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            try {
                Log.d("resultGPS", result);
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonResult = jsonObject.getJSONArray("results");
                JSONObject formatted_address = jsonResult.getJSONObject(0);
                String address = formatted_address.getString("formatted_address");

                if (!address.isEmpty())
                    locationAddress = address;
                else {
                    if (lat > 0 || lng > 0)
                        locationAddress = Float.toString(lat) + ", " + Float.toString(lng);
                }

                editor.putString("locationAddress", locationAddress);
                editor.putFloat("lat", lat);
                editor.putFloat("lng", lng);
                editor.apply();
            } catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), "Error retrieving location", Toast.LENGTH_LONG).show();

                ex.printStackTrace();
                Utilities.startIntentService(getActivity(), mResultReceiver, mLastLocation);
            }
        }
    }

    private String scanQrCode() {
        return null;
    }
}