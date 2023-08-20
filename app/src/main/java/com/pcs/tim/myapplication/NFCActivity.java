package com.pcs.tim.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Process;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NFCActivity extends AppCompatActivity {

    public static final String TAG = "NfcDemo";
    public static final String DATA_TYPE = "*/*";

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    String nfc;
    boolean loggedIn = true;

    private TextView textViewNotFound;
    private TextView textViewExpired;
    private TextView textViewMyRC;
    private TextView textViewCardValid;
    private TextView textViewUnhcr;
    private TextView textViewDob;
    private TextView textViewGender;
    private TextView textViewReligion;
    private TextView textViewCountry;
    private TextView textViewEthnic;
    private TextView textViewAddress;
    private TextView textViewDateIssue;
    private TextView textViewDateExpiry;
    private ImageView imageViewPhoto;
    private Button buttonAddRemark;

    String name;
    String category;
    String unhcr;
    String dob;
    String gender;
    String address;
    String country;
    String religion;
    String ethnicGroup;
    String issueDateStr;
    String expiredDateStr;
    byte[] photoBytes;
    String dbResult;
    String myRc;
    Date issueDate, expiredDate;

    private int log_id;
    private float lat;
    private float lng;
    String locationAddress;
    private String mAddressOutput;
    protected Location mLastLocation;
   // private AddressResultReceiver mResultReceiver;
    private FusedLocationProviderClient mFusedLocationClient;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        setTitle(getText(R.string.title));
        sharedPreferences = getSharedPreferences(Utilities.MY_RC_SHARE_PREF, MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(Utilities.LOGGED_IN, false);
        textViewNotFound = (TextView) findViewById(R.id.textViewNotFound);
        textViewMyRC = (TextView) findViewById(R.id.textViewMyrc);
        textViewExpired = (TextView) findViewById(R.id.textViewCardExpired);
        textViewCardValid = (TextView) findViewById(R.id.textViewCardValid);
       // textViewCategory = (TextView) findViewById(R.id.textViewCategory);
        textViewCountry = (TextView) findViewById(R.id.textViewCountry);
        textViewDateExpiry = (TextView) findViewById(R.id.textViewDateExpiry);
       // textViewDateIssue = (TextView) findViewById(R.id.textViewDateIssue);
        //textViewEthnic = (TextView) findViewById(R.id.textViewEthnic);
       // textViewDob = (TextView) findViewById(R.id.textViewDOB);
      //  textViewReligion = (TextView) findViewById(R.id.textViewReligion);
        textViewUnhcr = (TextView) findViewById(R.id.textViewUNHCR);
        textViewGender = (TextView) findViewById(R.id.textViewGender);
       // imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
      //  buttonAddRemark = (Button) findViewById(R.id.btnAddRemark);

       // Button buttonViewRemarks = (Button) findViewById(R.id.btnViewRemark);

       // mResultReceiver = new AddressResultReceiver(new Handler());
       // mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

//        if(checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
//            mFusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            mLastLocation = location;
//                            if( mLastLocation == null ) {
//
//                                lat = 0;
//                                lng = 0;
//                                return;
//                            }
//                            else {
//                                lat =  (float)location.getLatitude();
//                                lng =  (float)location.getLongitude();
//                            }
//
//                            if (!Geocoder.isPresent()) {
//                                Toast.makeText(getApplicationContext(),
//                                        R.string.no_geocoder_available,
//                                        Toast.LENGTH_LONG).show();
//                                return;
//                            }
//
//                            // Start service and update UI to reflect new location
//                           // Utilities.startIntentService(getApplicationContext(),mResultReceiver,mLastLocation);
//                        }
//                    });
//        }
//        else{
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    1);
//        }

//        buttonViewRemarks.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String myRc = textViewMyRC.getText().toString();
//                Intent intentRemark = new Intent(getApplicationContext(), ViewRemarksActivity.class);
//                intentRemark.putExtra(Utilities.MY_RC, myRc);
//                startActivity(intentRemark);
//            }
//        });
//
//        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
//            buttonAddRemark.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String myRc = textViewMyRC.getText().toString();
//                    AddRemarksFragment addRemarksFragment = AddRemarksFragment.newInstance(myRc, log_id);
//                    addRemarksFragment.show(getSupportFragmentManager(), getString(R.string.add_remark));
//                }
//            });
//        } else {
//            ActivityCompat.requestPermissions(NFCActivity.this,
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
//                    1);
//        }

        if (loggedIn) {
            if(checkPermission(Manifest.permission.NFC, Process.myPid(),Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
                mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

                if (mNfcAdapter == null) {
                    // Stop here, we definitely need NFC
                    //Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.putExtra("toastMsg", "This device doesn't support NFC");
                    setResult(Activity.RESULT_OK, intent);

                    finish();
                    return;

                }
                mPendingIntent = PendingIntent.getActivity(this, 0,
                        new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE);

                IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

                try {
                    ndef.addDataType("*/*");
                } catch (IntentFilter.MalformedMimeTypeException e) {
                    e.printStackTrace();
                    //throw new RuntimeException("fail", e);
                }
                mFilters = new IntentFilter[]{
                        ndef,
                };

                // Setup a tech list for all NfcF tags
                mTechLists = new String[][]{new String[]{MifareClassic.class.getName()}};

                Intent intent = this.getIntent();

                if (!mNfcAdapter.isEnabled()) {
                    textViewNotFound.setText("NFC is disabled");
                    Toast.makeText(getApplicationContext(), "NFC is disabled.", Toast.LENGTH_LONG).show();
                }

                handleIntent(intent);
            } else {
                ActivityCompat.requestPermissions(NFCActivity.this,
                        new String[]{android.Manifest.permission.NFC},
                        1);
            }

        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        super.onPause();
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
        //new RetrieveRefugeeData().execute(Utilities.RETRIEVE_REFUGEE_DATA_BY_NFC_URL);

    }

    private void handleIntent(Intent intent) {
        try {
            if (intent != null) {
                String action = intent.getAction();
                if (action != null) {
                    Log.i("nfc", action);
                    if ((NfcAdapter.ACTION_TECH_DISCOVERED).equals(action)){
                    //if (action == "android.nfc.action.TECH_DISCOVERED") {
                        //String type = intent.getType();
                        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                        MifareClassic mfc = MifareClassic.get(tag);
                        byte[] data;
                        String content = "";

                        try {       //  5.1) Connect to card
                            mfc.connect();
                            boolean auth = false;
                            String cardData = null;
                            // 5.2) and get the number of sectors this card has..and loop thru these sectors
                            int secCount = mfc.getSectorCount();
                            int bCount = 0;
                            int bIndex = 0;
                            byte[] key = {0x01, 0x35, 0x56, 0x25, 0x70, (byte) 0xff};
                            Log.i("Key", key.toString());
                            for (int j = 0; j < 2; j++) {
                                // 6.1) authenticate the sector

                                //auth = mfc.authenticateSectorWithKeyA(j, MifareClassic.KEY_DEFAULT);
                                auth = mfc.authenticateSectorWithKeyA(j, key);
                                if (auth) {
                                    // 6.2) In each sector - get the block count
                                    bCount = mfc.getBlockCountInSector(j);
                                    bIndex = 0;
                                    // for (int i = 0; i < 1; i++) {
                                    bIndex = mfc.sectorToBlock(j);
                                    // 6.3) Read the block
                                    for (int k = 0; k < 3; k++) {
                                        data = mfc.readBlock(bIndex++);
                                        // 7) Convert the data into a string from Hex format.
                                        String result = new String(data);
                                        //String result = getHexString(data);
                                        Log.i(TAG, result);
                                        int i;
                                        for (i = 0; i < 8; i += 2) {
                                            content += Character.toString(result.charAt(i)) + Character.toString(result.charAt(i + 1));
                                        }
                                        switch (k) {

                                            case 0:
                                                //  = result.trim();
                                                myRc = new StringBuilder().append(result.substring(0, 3))
                                                        .append('-').append(result.substring(3, 9))
                                                        .append('-').append(result.substring(9)).toString();
                                                break;
                                            case 1:
                                                unhcr = result.trim();
                                                break;
                                            case 2:
                                                //gender = Character.toString(result.charAt(0));
                                                expiredDateStr = result.substring(0,8);
                                                issueDateStr = result.substring(8);
                                                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                                                try {
                                                    expiredDate = formatter.parse(expiredDateStr);
                                                    issueDate = formatter.parse(issueDateStr);
                                                    Calendar expiredDateCal = Calendar.getInstance();
                                                    expiredDateCal.setTime(expiredDate);
                                                    Calendar cal = Calendar.getInstance();

                                                    //Log.i("Expired & current", expiredDate.toString()+cal.toString());
                                                    if (cal.getTimeInMillis() > expiredDateCal.getTimeInMillis()) {
                                                        textViewExpired.setVisibility(View.VISIBLE);
                                                        Log.i("Expired", "true");
                                                    } else
                                                        textViewCardValid.setVisibility(View.VISIBLE);
                                                } catch (Exception e) {

                                                }
                                                //country = result.substring(9);
                                                break;
                                        }
                                    }
                                    // Toast.makeText(getApplicationContext(), myRc + " " + unhcr + " " + gender + " " + expiredDate + " " + country, Toast.LENGTH_LONG).show();


                                    // bIndex++;
                                    // }
                                }
                                else{
                                    textViewNotFound.setText("No MyRC. Please try again.");
                                    textViewNotFound.setVisibility(View.VISIBLE);

                                    mfc.close();
                                    return;
                                }

                            }
                            nfc = content.toUpperCase();
                            mfc.close();
                            textViewNotFound.setVisibility(View.GONE);
                            textViewMyRC.setText(myRc);
                            textViewUnhcr.setText(unhcr);
//                            if (gender.equals("F"))
//                                textViewGender.setText("Female");
//                            else
//                                textViewGender.setText("Male");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            textViewDateExpiry.setText(dateFormat.format(expiredDate));
                            textViewGender.setText(dateFormat.format(issueDate));
                            //textViewCountry.setText(country);
                            //if(isOnline)
                            //new RetrieveRefugeeData().execute();
                            if (Utilities.isNetworkAvailable(getApplicationContext())) {
                                // new RetrieveRefugeeData().execute();
                                Intent newintent = new Intent(this, VerificationResultActivity.class);
                                newintent.putExtra(Utilities.MY_RC, myRc);
                                startActivity(newintent);
                                finish();
                            }
                        } catch (IOException e) {
                            Log.e(TAG, e.getLocalizedMessage());
                        }
                        //     new NdefReaderTask().execute(tag);

                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        /*else {
            Log.d(TAG, "Wrong mime type: " + type);
        }*/


        // In case we would still use the Tech Discovered Intent
            /*Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }*/
    }

    private String getHexString(byte[] a) {
        String hexString = "";

        for (int i = 0; i < a.length; i++) {
            String thisByte = "".format("%02x", a[i]);
            hexString += thisByte;
        }

        return hexString;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

//    private class RetrieveRefugeeData extends AsyncTask<String, Void, String> {
//        ProgressDialog asyncDialog = new ProgressDialog(NFCActivity.this);
//        Connection connection;
//
//        @Override
//        protected void onPreExecute() {
//            //set message of the dialog
//            asyncDialog.setMessage(getString(R.string.loadingtype));
//            //show dialog
//            asyncDialog.show();
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... urls) {
//
//            try {
//                if(Utilities.hasActiveInternetConnection(getApplicationContext())) {
//                    connection = Utilities.getMySqlConnection();
//                    Statement statement = connection.createStatement();
//
//                    String query = "Select `myrc`, `unhcrid`, `fullName`, `category`, `gender`, `address`, " +
//                            "DATE_FORMAT(`dob`,'%d/%m/%Y') AS `dob`, `countryOfOrigin`, `religion`, `ethincGroup`, " +
//                            "`address`, DATE_FORMAT(`cardIssueTime`,'%d/%m/%Y') AS `issueDate`, " +
//                            "DATE_FORMAT(`cardExpiredDate`,'%d/%m/%Y') AS `expiredDate`, `photo` from `tb_register` " +
//                            "where `myrc` = '" + myRc + "'";
//
//                    ResultSet result = statement.executeQuery(query);
//                    if (result.next()) {
//
//                        myRc = result.getString("myrc");
//                        name = result.getString("fullName");
//                        category = result.getString("category");
//                        unhcr = result.getString("unhcrid");
//                        dob = result.getString("dob");
//                        gender = result.getString("gender");
//                        address = result.getString("address");
//                        country = result.getString("countryOfOrigin");
//                        religion = result.getString("religion");
//                        ethnicGroup = result.getString("ethincGroup");
//                        issueDate = result.getString("issueDate");
//                        expiredDate = result.getString("expiredDate");
//                        //nt blobLength = (int)result.getBlob("photo").length();
//                        photoBytes = result.getBytes("photo");
//
//                        query = "INSERT INTO `tb_log`(`police_id`, `myrc`, `remark`, `location`, `lat`, `lng`)"
//                                + " VALUES (?, ?, ?, ?, ?, ?)";
//
//                        // create the mysql insert preparedstatement
//                        PreparedStatement preparedStmt = connection.prepareStatement(query);
//                        preparedStmt.setString(1, sharedPreferences.getString(Utilities.LOGIN_POLICE_ID, null));
//                        preparedStmt.setString(2, myRc);
//                        preparedStmt.setString(3, "");
//                        preparedStmt.setString(4, locationAddress);
//                        preparedStmt.setFloat(5, lat);
//                        preparedStmt.setFloat(6, lng);
//
//                        // execute the preparedstatement
//                        log_id = preparedStmt.executeUpdate();
//
//                        Log.i("log id", Integer.toString(log_id));
//                        connection.close();
//                        dbResult = "success";
//                    } else {
//                        dbResult = "fail";
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            /*HashMap<String,String> refugeeData = new HashMap<>();
//            refugeeData.put(Utilities.NFC,nfc);
//            Utilities utilities = new Utilities();
//            return utilities.sendPostRequest(urls[0],refugeeData);*/
//            return dbResult;
//        }
//
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(String result) {
//            try {
//
//                Log.i("Refugee Result", result);
//
//                if (result.equals("success")) {
//                    textViewNotFound.setVisibility(View.GONE);
//
//                    /*JSONObject jsonObject = new JSONObject(result);
//
//                    String myrc = jsonObject.getString(Utilities.MY_RC);
//                    String name = jsonObject.getString(Utilities.FULL_NAME);
//                    String category = jsonObject.getString(Utilities.CATEGORY);
//                    String unhcr = jsonObject.getString(Utilities.UNHCRID);
//                    String dob = jsonObject.getString(Utilities.DOB);
//                    String gender = jsonObject.getString(Utilities.GENDER);
//                    String address = jsonObject.getString(Utilities.ADDRESS);
//                    String country = jsonObject.getString(Utilities.COUNTRY);
//                    String religion = jsonObject.getString(Utilities.RELIGION);
//                    String ethnicGroup = jsonObject.getString(Utilities.ETHNIC_GROUP);
//                    String issueDate = jsonObject.getString(Utilities.ISSUE_DATE);
//                    String expiredDate = jsonObject.getString(Utilities.EXPIRY_DATE);
//                    String photoString = jsonObject.getString(Utilities.PHOTO);
//*/
//                    Bitmap photo = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
//                    imageViewPhoto.setImageBitmap(photo);
//
//                    textViewMyRC.setText(myRc);
//                    textViewName.setText(name);
//                    textViewDateIssue.setText(issueDate);
//                    textViewEthnic.setText(ethnicGroup);
//                    textViewCategory.setText(category);
//                    textViewDateExpiry.setText(expiredDate);
//                    textViewAddress.setText(address);
//                    textViewGender.setText(gender);
//                    textViewDob.setText(dob);
//                    textViewCountry.setText(country);
//                    textViewUnhcr.setText(unhcr);
//                    textViewReligion.setText(religion);
//
//
//                    asyncDialog.dismiss();
//
//
//
//                } else {
//                    asyncDialog.dismiss();
//                    textViewNotFound.setVisibility(View.VISIBLE);
//                    textViewNotFound.setText(getString(R.string.nfc_not_found));
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    private class AddressResultReceiver extends ResultReceiver {
//        public AddressResultReceiver(Handler handler) {
//            super(handler);
//        }
//
//        @Override
//        protected void onReceiveResult(int resultCode, Bundle resultData) {
//
//            // Display the address string
//            // or an error message sent from the intent service.
//            if (resultData != null) {
//
//                mAddressOutput = resultData.getString(Utilities.RESULT_DATA_KEY);
//                buttonAddRemark.setVisibility(View.VISIBLE);
//
//                if (!mAddressOutput.equals(getString(R.string.service_not_available)))
//                    locationAddress = mAddressOutput;
//            }
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
