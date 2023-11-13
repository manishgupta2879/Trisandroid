package com.pcs.tim.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Process;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.apache.commons.net.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class VerificationResultActivity extends AppCompatActivity {

    private String myRc,photoUrl;
    private TextView textViewNotFound;
    private TextView textViewName;
    private TextView textViewMyRC;
    private TextView textViewCategory;
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
    private TextView textViewExpired;
    private TextView buttonAddRemark;
   private TextView buttonViewRemarks;
    private Button buttonViewPLKS;
    private Button buttonViewVaccine;
    private TextView textViewState;
    private TextView textViewMobile;
    private LinearLayout approvalLay;
    private TextView approvalText;
    private ImageView icApproval;
    private TextView textViewComapnyName;
    private TextView textViewEmployerName;
    private TextView textViewEmployerContact;
    private TextView textViewJobSector;
    private TextView textViewWorkLocation;
    private TextView textViewWorkDesc;
    private TextView textViewLengthOfWork;
    private TextView textViewLengthOfStay;
    private TextView textViewCurrentPay;
    private TextView textViewApprovalStatus;
    private TextView textViewCardValid;
   // private TextView textViewCardPending;
    private TextInputEditText textViewUNCaseNo, textViewRefugeeGroup, textViewMarital, textViewBirthPlace, textViewFamilySize, textViewPassport,
            textViewEmergencyPerson, textViewEmergencyContact, textViewComRegNo, textViewApproveDate;

    String regID ="",name = "", category = "", unhcr = "", dob = "", gender = "", address = "", country = "",
            religion = "", ethnicGroup = "", issueDate = "", expiredDate = "", employerName = "",
            employerContact = "", mobileNo = "", companyName = "", state = "", jobSector = "",
            workLocation = "",workDesc = "", approvalStatus = "", unhcrCaseNo = "", refugeeGroup = "",
            maritalStatus = "", birthPlace = "", familySize = "", passportNo = "", emergencyPerson = "",
            emergencyContact = "", companyRegNo = "", approvalDate = "" ;

    int lengthOfWork = 0, lengthOfStay = 0;
    double currentPay = 0;
    String inputType;
    String inputValue;
    String qrDecrypt;
    Bitmap photo;
    private int log_id;
    private float lat =0;
    private float lng = 0;
    String locationAddress = "No GPS";
    SharedPreferences sharedPreferences;
    ImageView icPersonal,icCardStatus,icEmployer;
    LinearLayout personalLay,cardStatusLay,employerLay;

    boolean personalFlag=true,cardFlag=true,employerFlag=true;
    boolean offlineMode;
    boolean addRemark;
    boolean showButtonPLKS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_result);

        setTitle(getText(R.string.title));
        final Intent intent = getIntent();



        Toolbar toolbar =findViewById(R.id.verification_toolbar);
        //   toolbar.setVisibility(View.VISIBLE);// Assuming you have a Toolbar in your layout
        if (toolbar != null) {
            Log.d("toolbar__", "onCreate: Print");

            toolbar.setNavigationIcon(R.drawable.ic_back); // Set your back button icon
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle back button click here (e.g., pop the Fragment from the back stack)
                    onBackPressed();
                }
            });
        }

        offlineMode = intent.getBooleanExtra("offline", false);
        addRemark = intent.getBooleanExtra("addRemark", true);

        if((inputType = intent.getStringExtra("inputType"))!= null){
            inputValue = intent.getStringExtra("inputValue");
        }
        else{
            inputType = "MYRC";
            inputValue = intent.getStringExtra(Utilities.MY_RC);
            String passphrase = "T5r0i7S2!";
            byte[] pwd = md5(passphrase);
            byte[] keyBytes = Arrays.copyOf(pwd, 24);
            for (int j = 0, k = 16; j < 8;) {
                keyBytes[k++] = keyBytes[j++];
            }
            Log.d("toolbar__", "onCreate: abc");


            try {

                Cipher cipher = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
                SecretKeySpec myKey = new SecretKeySpec(keyBytes,"DESede");

                cipher.init(Cipher.DECRYPT_MODE, myKey);

                try {
                    byte[] encryptedPlainText = cipher.doFinal(Base64.decodeBase64(inputValue));

                    qrDecrypt = new String (encryptedPlainText, "UTF-8");
                    try {inputValue = qrDecrypt.substring(0, 18); }catch(Exception ex){}
                    try {expiredDate = qrDecrypt.substring(18,26); }catch(Exception ex){}
                    try {unhcr = qrDecrypt.substring(26,38); }catch(Exception ex){}

                } catch (IllegalBlockSizeException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        sharedPreferences = getSharedPreferences(Utilities.MY_RC_SHARE_PREF, MODE_PRIVATE);

        icApproval=findViewById(R.id.ic_approval);
        approvalText=findViewById(R.id.ic_approval_text);
        approvalLay=findViewById(R.id.approval_lay);

        icPersonal=findViewById(R.id.ic_personal);
        icCardStatus=findViewById(R.id.ic_card_status);
        icEmployer=findViewById(R.id.ic_employer);

        cardStatusLay=findViewById(R.id.cardStatusLay);
        employerLay=findViewById(R.id.employerLay);
        personalLay=findViewById(R.id.personalLay);
        textViewExpired = (TextView)findViewById(R.id.textViewCardExpired);
       // textViewCardPending = (TextView)findViewById(R.id.textViewCardPending);
        textViewCardValid = (TextView)findViewById(R.id.textViewCardValid);
        textViewNotFound = (TextView)findViewById(R.id.textViewNotFound);
        textViewMyRC = (TextView)findViewById(R.id.textViewMyrc);
        textViewName = (TextView)findViewById(R.id.textViewName);
        textViewAddress = (TextView)findViewById(R.id.textViewAddress);
        textViewCategory = (TextView)findViewById(R.id.textViewCategory);
        textViewCountry  = (TextView)findViewById(R.id.textViewCountry);
        textViewDateExpiry = (TextView)findViewById(R.id.textViewDateExpiry);
        textViewDateIssue = (TextView)findViewById(R.id.textViewDateIssue);
        textViewEthnic = (TextView)findViewById(R.id.textViewEthnic);
        textViewDob = (TextView)findViewById(R.id.textViewDOB);
        textViewReligion = (TextView)findViewById(R.id.textViewReligion);
        textViewUnhcr = (TextView)findViewById(R.id.textViewUNHCR);
        textViewGender = (TextView)findViewById(R.id.textViewGender);
        imageViewPhoto = (ImageView)findViewById(R.id.imageViewPhoto);
        buttonAddRemark =  findViewById(R.id.btnAddRemark);
      buttonViewRemarks = findViewById(R.id.btnViewRemark);
        buttonViewPLKS = (Button)findViewById(R.id.btnViewPLKS);
        buttonViewVaccine = (Button)findViewById(R.id.btnViewVaccine);
        textViewState = (TextView)findViewById(R.id.textViewState);
        textViewMobile = (TextView)findViewById(R.id.textViewMobileNo);
        textViewComapnyName = (TextView)findViewById(R.id.textViewCompanyName);
        textViewEmployerName = (TextView)findViewById(R.id.textViewEmployerName);
        textViewEmployerContact = (TextView)findViewById(R.id.textViewEmployerContact);
        textViewJobSector = (TextView)findViewById(R.id.textViewJobSector);
        textViewWorkLocation = (TextView)findViewById(R.id.textViewWorkLocation);
        textViewWorkDesc = (TextView)findViewById(R.id.textViewWorkDescription);
        textViewLengthOfWork = (TextView)findViewById(R.id.textViewLengthOfWork);
        textViewLengthOfStay = (TextView)findViewById(R.id.textViewLengthOfStay);
        textViewCurrentPay = (TextView)findViewById(R.id.textViewCurrentPay);
        textViewApprovalStatus = (TextView)findViewById(R.id.textViewApprovalStatus);
        textViewUNCaseNo = findViewById(R.id.textViewUNHCRCaseNo);
        textViewRefugeeGroup = findViewById(R.id.textViewRefugeeGroup);
        textViewMarital = findViewById(R.id.textViewMarital);
        textViewBirthPlace = findViewById(R.id.textViewBirthPlace);
        textViewFamilySize = findViewById(R.id.textViewFamilySize);
        textViewPassport = findViewById(R.id.textViewPassportNo);
        textViewEmergencyPerson = findViewById(R.id.textViewEmergencyPerson);
        textViewEmergencyContact = findViewById(R.id.textViewEmergencyContact);
        textViewComRegNo =findViewById(R.id.textViewCompanyRegNo);
        textViewApproveDate = findViewById(R.id.textViewDateApprove);


        icPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (personalFlag) {

                    icPersonal.setImageResource(R.drawable.ic_expand);
                    personalLay.setVisibility(View.GONE);
                    personalFlag =false;

                } else {
                    icPersonal.setImageResource(R.drawable.ic_collapse);
                    personalLay.setVisibility(View.VISIBLE);
                    personalFlag =true;
                }
            }
        });


        icEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (employerFlag) {

                    icEmployer.setImageResource(R.drawable.ic_expand);
                    employerLay.setVisibility(View.GONE);
                    employerFlag =false;

                } else {
                    icEmployer.setImageResource(R.drawable.ic_collapse);
                    employerLay.setVisibility(View.VISIBLE);
                    employerFlag =true;
                }
            }
        });


        icCardStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardFlag) {

                    icCardStatus.setImageResource(R.drawable.ic_expand);
                    cardStatusLay.setVisibility(View.GONE);
                    cardFlag =false;

                } else {
                    icCardStatus.setImageResource(R.drawable.ic_collapse);
                    cardStatusLay.setVisibility(View.VISIBLE);
                    cardFlag =true;
                }
            }
        });

        if(!addRemark)
            buttonAddRemark.setEnabled(false);
//edit by deepak
        buttonViewRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRemark = new Intent(getApplicationContext(), ViewRemarksActivity.class);
                intentRemark.putExtra(Utilities.MY_RC, myRc);
                intentRemark.putExtra(Utilities.FULL_NAME, name);
                intentRemark.putExtra(Utilities.PHOTO, photoUrl);
                startActivity(intentRemark);
            }
        });

     /*   buttonViewPLKS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPLKS = new Intent(getApplicationContext(), ViewPLKSActivity.class);
                intentPLKS.putExtra(Utilities.REG_ID, regID);
                startActivity(intentPLKS);
            }
        });*/
     /*   buttonViewVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVaccine = new Intent(getApplicationContext(), ViewVaccination.class);
               intentVaccine.putExtra("myrc",textViewMyRC.getText().toString());
               intentVaccine.putExtra("phoneNo",textViewMobile.getText().toString());
                startActivity(intentVaccine);
            }
        });*/

        buttonAddRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
                    String myRc = textViewMyRC.getText().toString();
                    AddRemarksFragment addRemarksFragment = AddRemarksFragment.newInstance(myRc, log_id,regID);
                    addRemarksFragment.show(getSupportFragmentManager(), getString(R.string.add_remark));
                }else{
                    ActivityCompat.requestPermissions(VerificationResultActivity.this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);
                }
            }
        });

        if(offlineMode) {
            LinearLayout offlinelayout = (LinearLayout)findViewById(R.id.offlineLayout);
            TextView textViewShowMyrc = (TextView)findViewById(R.id.textViewShowMyrc);
            TextView textViewShowDOB = (TextView)findViewById(R.id.textViewShowDOB);
            TextView textViewShowExpiry = (TextView) findViewById(R.id.textViewShowExpiry);
            TextView textViewShowUn = (TextView) findViewById(R.id.textViewShowUn);
            TextView textViewMatchMyrc = (TextView)findViewById(R.id.textViewMatchMyrc);
            String[] myrc = inputValue.split("-");

            offlinelayout.setVisibility(View.VISIBLE);

            try {

                DateFormat myrcFormat = new SimpleDateFormat("yyMMdd");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                if (myrc.length == 3 && qrDecrypt.length() == 38) {
                    String dob = myrc[1];
                    Date birthdate = null;
                    if (myrc[0].length() == 3 && myrc[1].length() == 6 && myrc[2].length() == 7) {

                        birthdate = (Date) myrcFormat.parse(dob);
                        textViewShowMyrc.setText(inputValue);
                        textViewShowDOB.setText(dateFormat.format(birthdate));
                        textViewShowUn.setText(unhcr);
                        Date date1 = new SimpleDateFormat("yyyyMMdd").parse(expiredDate);
                        Calendar expiredDateCal = Calendar.getInstance();
                        expiredDateCal.setTime(date1);
                        Calendar cal = Calendar.getInstance();
                        Log.d("CurrentTime", String.valueOf(cal.getTimeInMillis()));//1569573188564
                        Log.d("ExpiredTime", String.valueOf(expiredDateCal.getTimeInMillis()));//1565798400000
                        if (cal.getTimeInMillis() > expiredDateCal.getTimeInMillis()) {
                            textViewMatchMyrc.setTextColor(Color.RED);
                            textViewMatchMyrc.setText("Card Expired");

                        } else {
                            textViewMatchMyrc.setText("Card Valid");
                        }
                        textViewShowExpiry.setText(dateFormat.format(date1));
//                        textViewMatchMyrc.setText("Card Valid");
                    }
                } else {
                    textViewMatchMyrc.setCompoundDrawables(getResources().getDrawable(R.mipmap.ic_warning), null, null, null);
                    textViewMatchMyrc.setText("Card Invalid");
                    textViewMatchMyrc.setBackgroundColor(Color.RED);
                }
            }catch(Exception e){
                textViewMatchMyrc.setText("Card Invalid");
                textViewMatchMyrc.setBackgroundColor(Color.RED);
                e.printStackTrace();
            }

        }
        else {
//            new RetrieveRefugeeData().execute();
            new ViewRefugeeData().execute();
        }

    }

    private static final byte[] md5(String s) {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(s.getBytes("UTF-8"));

            return messageDigest;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        // Write your code here
        if(offlineMode) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            //setContentView(R.layout.activity_my_rc_verification);
            finish();
        }else {
            super.onBackPressed();
            finish();
        }
        //

    }

    private class CheckPLKSExist extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                String tokenResponse = Utilities.SendTokenPostRequest();
                JSONObject jsonObject = new JSONObject(tokenResponse);
                String token = jsonObject.getString("access_token");
                String tokenType = jsonObject.getString("token_type");

                String result = Utilities.SendGetRequest(Utilities.PLKS + "?regID=" +regID+"&searchExist=true", token, tokenType);

                return result;

            }catch (Exception e){

                return null;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(result!=null){
                try {
                    JSONObject jObjResult = new JSONObject(result);
                    showButtonPLKS = jObjResult.getBoolean("result");
//                    buttonViewPLKS.setEnabled(showButtonPLKS);
//                    if(showButtonPLKS){
//                        if(jObjResult.getString("applicationStatus").equals("A")){
//                            buttonViewPLKS.setBackgroundColor(Color.rgb(0,163,0));
//                        }else{
//                            buttonViewPLKS.setBackgroundColor(Color.rgb(255,71,26));
//                        }
//                    }else{
//                        buttonViewPLKS.setBackgroundColor(Color.rgb(169,169,169));
//                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class AddTrackLog extends AsyncTask<String,Void,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try{//Long.parseLong(
                String result = DataService.PostTrackLog(Long.parseLong(regID),"V",sharedPreferences.getString(Utilities.LOGIN_POLICE_ID, null),
                        sharedPreferences.getString("locationAddress", "No GPS"), sharedPreferences.getFloat("lat", 0)
                        ,sharedPreferences.getFloat("lng", 0),"");
                if (result != null && result != "ERROR") {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getString("success").equalsIgnoreCase("true")){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }


            }catch (Exception e){
                Log.d("error__track", "doInBackground: "+e.getMessage());
                return false;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ViewRefugeeData extends AsyncTask<String, Void, String>{
        ProgressDialog asyncDialog = new ProgressDialog(VerificationResultActivity.this);

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

                String result = DataService.ViewRefugeeDetails("Myrc",inputValue);
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
                //System.err.println("Error");
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                asyncDialog.dismiss();
                if (result != null && result != "ERROR") {

                    try {
                        textViewNotFound.setVisibility(View.GONE);
                        buttonAddRemark.setVisibility(View.VISIBLE);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    ViewDetailsResponse respViewDetail = new Gson().fromJson(result, ViewDetailsResponse.class);
                        PartialViewProfile respProfile = respViewDetail.getProfile();
                        PartialViewEmployer respEmployer = respViewDetail.getEmployer();
                        PartialViewCardStatus respCard = respViewDetail.getCardStatus();
                        PartialViewHealthStatus respHealth = respViewDetail.getHealthStatus();

                        photoUrl=respViewDetail.getPhoto();

                        byte[] base64Encoded = Base64.decodeBase64(respViewDetail.getPhoto());
                        if (base64Encoded != null) {
                             photo = BitmapFactory.decodeByteArray(base64Encoded, 0, base64Encoded.length);
                            imageViewPhoto.setImageBitmap(photo);
                        }

                        regID = respProfile.getId() != null ? respProfile.getId().toString() : "";
                        new CheckPLKSExist().execute();
                        new AddTrackLog().execute();
                        name = respProfile.getFullName();
                        myRc = respProfile.getMyrc();
                        category = respProfile.getCategory();
                        unhcr = respProfile.getUnhcrId();
                        dob = respProfile.getDob();
                    Date dobDate = null;
                    try {
                        dobDate = new SimpleDateFormat("dd/MM/yyyy").parse(dob);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    dob = new SimpleDateFormat("dd-MM-yyyy").format(dobDate);
                        gender = respProfile.getGender();
                        address = respProfile.getAddress();
                        country = respProfile.getCountryOfOrigin();
                        religion = respProfile.getReligion();
                        ethnicGroup = respProfile.getEthnicGroup();
                        issueDate = respCard.getRegisterDate();
                        if(issueDate!=null && !issueDate.equals("null")) {
                            Date date1 = null;
                            try {
                                date1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(issueDate);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            issueDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(date1);
                        }
                        expiredDate = respCard.getCardExpiredDate();
                        if(expiredDate!=null && !expiredDate.equals("null")) {
                            Date date1 = null;
                            try {
                                date1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(expiredDate);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            expiredDate = new SimpleDateFormat("dd-MM-yyyy").format(date1);
                        }
                        employerName = respEmployer.getEmpContactName();
                        employerContact = respEmployer.getEmpContactNo();
                        mobileNo = respProfile.getMobileNo();
                        companyName = respEmployer.getEmpName();
                        state = respProfile.getState();
                        jobSector = respEmployer.getWorkSector();
                        workLocation = respEmployer.getWorkLocation();
                        workDesc = respEmployer.getWorkDescription();
                        lengthOfWork = Integer.parseInt(String.valueOf(respEmployer.getLengthOfWork()!= null ? respEmployer.getLengthOfWork() : 0));
                        lengthOfStay = Integer.parseInt(String.valueOf(respProfile.getLengthOfStay()!=null ? respProfile.getLengthOfStay() : 0));
                        if(respEmployer.getCurrentPay()!=null && !respEmployer.getCurrentPay().equals("null")){
                            currentPay = Double.parseDouble(String.valueOf(respEmployer.getCurrentPay()));
                        }else{
                            currentPay = 0.0;
                        }
                        approvalStatus = respCard.getCardStatus();

                        unhcrCaseNo = respProfile.getUnhcrCaseNo();
                        refugeeGroup = respProfile.getRefugeeGroup();
                        maritalStatus = respProfile.getMaritalStatus();
                        birthPlace = respProfile.getPlaceOfBirth();
                        familySize = String.valueOf(respProfile.getFamilySize());
                        passportNo = respProfile.getPassportNo();
                        emergencyPerson = respProfile.getEmergencyContact();
                        emergencyContact = respProfile.getEmergencyNo();
                        companyRegNo = respEmployer.getEmpRegNo();
//                        approvalDate = respCard.getCollectCardDate();
//                        if(approvalDate!=null && !approvalDate.equals("null")) {
//                            Date approvalDate1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(approvalDate);
//                            approvalDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(approvalDate1);
//                            textViewApproveDate.setText(approvalDate);
//                        }

                        textViewMyRC.setText(myRc);
                        textViewName.setText(name);
                        textViewDateIssue.setText(issueDate);
                        textViewEthnic.setText(ethnicGroup);
                        textViewCategory.setText(category);
                        if(!expiredDate.isEmpty() && !expiredDate.equals("null"))
                            textViewDateExpiry.setText(expiredDate);
                        textViewAddress.setText(address);


                        String dobGender =gender+"   "+dob;
                        Log.d("approvalStatus__", "onPostExecute: "+approvalStatus);

                        textViewGender.setText(dobGender);
                     //   textViewDob.setText(dob);
                        textViewCountry.setText(country);
                        textViewUnhcr.setText(unhcr);
                        textViewReligion.setText(religion);
                        textViewState.setText(state);
                        textViewMobile.setText(mobileNo);
                        textViewComapnyName.setText(companyName);
                        textViewEmployerName.setText(employerName);
                        textViewEmployerContact.setText(employerContact);
                        textViewJobSector.setText(jobSector);
                        textViewWorkLocation.setText(workLocation);
                        textViewWorkDesc.setText(workDesc);
                        if (lengthOfWork != 0)
                            textViewLengthOfWork.setText(Integer.toString(lengthOfWork));
                        if (lengthOfStay != 0)
                            textViewLengthOfStay.setText(Integer.toString(lengthOfStay));
                        if (currentPay != 0)
                            textViewCurrentPay.setText("RM " + (String.format("%.2f", currentPay)));




                        if (approvalStatus != null && !approvalStatus.isEmpty()) {
                            switch (approvalStatus) {
                                case "REJECTED":
                                    Log.d("stattttt5", "onPostExecute: "+respCard.getCardStatus());

                                    approvalText.setText(respCard.getCardStatus());
                                    approvalStatus = "REJECTED";
                                    textViewExpired.setText(approvalStatus);
                                    textViewExpired.setVisibility(View.VISIBLE);
                                    break;
                                case "EXPIRED":
                                    Log.d("stattttt4", "onPostExecute: "+respCard.getCardStatus());
                                    approvalLay.setBackgroundColor(Color.parseColor("#ef1d0d"));

                                    Log.d("stattttt", "onPostExecute: "+respCard.getCardStatus());
                                    approvalText.setText(respCard.getCardStatus());
                                    Drawable drawable2 = ContextCompat.getDrawable(VerificationResultActivity.this, R.drawable.baseline_dangerous_24);

                                    icApproval.setImageDrawable(drawable2);
                                    break;
                                case "APPROVED":
                                    approvalStatus = "APPROVED";
                                    Log.d("stattttt3", "onPostExecute: "+respCard.getCardStatus());

                                    approvalText.setText(respCard.getCardStatus());
                                    if (expiredDate != null && !expiredDate.isEmpty() && !expiredDate.equals("null")) {
                                        Date dateExpired = null;
                                        try {
                                            dateExpired = new SimpleDateFormat("dd-MM-yyyy").parse(expiredDate);
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        }
                                        Calendar expiredDateCal = Calendar.getInstance();
                                        expiredDateCal.setTime(dateExpired);
                                        Calendar cal = Calendar.getInstance();

                                        //Log.i("Expired & current", expiredDate.toString()+cal.toString());
                                        if (cal.getTimeInMillis() > expiredDateCal.getTimeInMillis()) {
                                            textViewExpired.setVisibility(View.VISIBLE);
                                            //Log.i("Expired", "true");
                                        } else
                                            textViewCardValid.setVisibility(View.VISIBLE);
                                    } else {
                                        Log.d("stattttt2", "onPostExecute: "+respCard.getCardStatus());

                                        approvalText.setText(respCard.getCardStatus());
                                        textViewCardValid.setText("APPROVED");
                                        textViewCardValid.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case "ACTIVE":
                                    approvalLay.setBackgroundColor(Color.parseColor("#5ecd2e"));

                                    Log.d("stattttt", "onPostExecute: "+respCard.getCardStatus());
                                    approvalText.setText(respCard.getCardStatus());
                                    Drawable drawable = ContextCompat.getDrawable(VerificationResultActivity.this, R.drawable.baseline_check_circle_24);

                                    icApproval.setImageDrawable(drawable);
                                    //textViewCardPending.setVisibility(View.VISIBLE);
                                    break;
                                default:
                                    approvalStatus = "PENDING";

                                    approvalLay.setBackgroundColor(Color.parseColor("#F8C76F"));

                                    Log.d("stattttt", "onPostExecute: "+respCard.getCardStatus());
                                    approvalText.setText(respCard.getCardStatus());
                                    Drawable drawable1 = ContextCompat.getDrawable(VerificationResultActivity.this, R.drawable.new_ic_pending);

                                    icApproval.setImageDrawable(drawable1);
                                    //textViewCardPending.setVisibility(View.VISIBLE);
                                    break;
                            }
                        } else {
                            approvalStatus = "PENDING";
                           // textViewCardPending.setVisibility(View.VISIBLE);
                        }
                        textViewApprovalStatus.setText(approvalStatus);
                        textViewUNCaseNo.setText(unhcrCaseNo);
                        textViewRefugeeGroup.setText(refugeeGroup);
                        textViewMarital.setText(maritalStatus);
                        textViewBirthPlace.setText(birthPlace);
                        textViewFamilySize.setText(familySize);
                        textViewPassport.setText(passportNo);
                        textViewEmergencyPerson.setText(emergencyPerson);
                        textViewEmergencyContact.setText(emergencyContact);
                        textViewComRegNo.setText(companyRegNo);
                    } catch (Exception e) {


                        Log.d("error__vr", "onPostExecute: "+e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    textViewNotFound.setVisibility(View.VISIBLE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                textViewNotFound.setVisibility(View.VISIBLE);
            }
        }
    }

    private class RetrieveRefugeeData extends AsyncTask<String, Void, String> {
                ProgressDialog asyncDialog = new ProgressDialog(VerificationResultActivity.this);

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
                        String tokenResponse = Utilities.SendTokenPostRequest();
                        JSONObject jsonObject = new JSONObject(tokenResponse);
                        String token = jsonObject.getString("access_token");
                        String tokenType = jsonObject.getString("token_type");

                        String result = Utilities.SendGetRequest(Utilities.REFUGEE + "?inputType=" + URLEncoder.encode(String.valueOf(inputType), "UTF-8") + "&value=" + URLEncoder.encode(String.valueOf(inputValue), "UTF-8"), token, tokenType);

                        JSONArray arrayResult = new JSONArray(result);
                        JSONObject objectResult = arrayResult.getJSONObject(0);

                        if (addRemark) {
                            locationAddress = sharedPreferences.getString("locationAddress", "No GPS");
                            lat = sharedPreferences.getFloat("lat", 0);
                            lng = sharedPreferences.getFloat("lng", 0);
                            HashMap<String, String> data = new HashMap<>();
                            data.put(Utilities.POLICE_ID, sharedPreferences.getString(Utilities.LOGIN_POLICE_ID, null));
                            data.put(Utilities.MY_RC, objectResult.getString(Utilities.MY_RC));
                            data.put(Utilities.LOG_REMARK, "");
                            data.put(Utilities.LOCATION, locationAddress);
                            data.put(Utilities.LATITUDE, Float.toString(lat));
                            data.put(Utilities.LONGITUDE, Float.toString(lng));
                            Utilities.sendPostRequest(Utilities.LOG, data, token, tokenType);
                        }

                        return result;

//                        Connection connection = Utilities.getMySqlConnection();
//                Statement statement = connection.createStatement();
//
//                String query = "Select `myrc`, `unhcrid`,`unhcrCaseNo`, `refugeeGroup`, `fullName`, `category`, `gender`, `address`, " +
//                        "DATE_FORMAT(`dob`,'%d/%m/%Y') AS `dob`, `countryOfOrigin`, `religion`, `maritalStatus`, `ethincGroup`, " +
//                        "`address`, `createdTime`, `placeOfBirth`, `familySize`, `passportNo`, `emergencyContact`, `emergencyNo`, `empRegNo`, " +
//                        "DATE_FORMAT(`cardExpiredDate`,'%d/%m/%Y') AS `expiredDate`, `photo`, `state`, `approvalTime`, " +
//                        "`workLocation`, `mobileNo`, `empName`, `empContactName`, `empContactNo`, `workSector`, " +
//                        "`workDescription`, `lengthOfWork`, `lengthOfStay`, `currentPay`, `isApprove` from `tb_register` " +
//                        "where `" + inputType + "` = '"+ inputValue +"'";
//                  String query = "Select " +
//                       "DATE_FORMAT(`dob`,'%d/%m/%Y') AS `dob`, " +
//                       "DATE_FORMAT(`cardExpiredDate`,'%d/%m/%Y') AS `expiredDate`, * from `tb_register` " +
//                       "where `" + inputType + "` = '"+ inputValue +"'";
                        // Log.i("query",query);

//                ResultSet result = statement.executeQuery(query);
//                if (result.next()){
//                    Log.i("Result", result.toString());
//
//                    name = result.getString("fullName");
//                    myRc = result.getString("myrc");
//                    category = result.getString("category");
//                    unhcr = result.getString("unhcrid");
//                    dob = result.getString("dob");
//                    gender = result.getString("gender");
//                    address = result.getString("address");
//                    country = result.getString("countryOfOrigin");
//                    religion = result.getString("religion");
//                    ethnicGroup = result.getString("ethincGroup");
//                    issueDate = result.getString("createdTime");
//                    Date date1= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(issueDate);
//                    issueDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(date1);
//                    expiredDate = result.getString("expiredDate");
//                    //nt blobLength = (int)result.getBlob("photo").length();
//                    photoBytes = result.getBytes("photo");
//                    employerName = result.getString("empContactName");
//                    employerContact = result.getString("empContactNo");
//                    mobileNo = result.getString("mobileNo");
//                    companyName = result.getString("empName");
//                    state = result.getString("state");
//                    jobSector = result.getString("workSector");
//                    workLocation = result.getString("workLocation");
//                    workDesc = result.getString("workDescription");
//                    lengthOfWork = result.getInt("lengthOfWork");
//                    lengthOfStay = result.getInt("lengthOfStay");
//                    currentPay = result.getDouble("currentPay");
//                    approvalStatus = result.getString("isApprove");
////                    if(approvalStatus != null){
////                        if(approvalStatus.equals("0"))
////                            approvalStatus = "Rejected";
////                        else
////                            approvalStatus = "Approved";
////                    }
////                    else
////                        approvalStatus = "Pending";
//                    unhcrCaseNo = result.getString("unhcrCaseNo");
//                    refugeeGroup = result.getString("refugeeGroup");
//                    maritalStatus = result.getString("maritalStatus");
//                    birthPlace = result.getString("placeOfBirth");
//                    familySize = result.getString("familySize");
//                    passportNo = result.getString("passportNo");
//                    emergencyPerson = result.getString("emergencyContact");
//                    emergencyContact = result.getString("emergencyNo");
//                    companyRegNo = result.getString("empRegNo");
//                    approvalDate = result.getString("approvalTime");
//                    dbResult = "success";

//                    if(addRemark) {
//                        locationAddress = sharedPreferences.getString("locationAddress", "No GPS");
//                        lat = sharedPreferences.getFloat("lat", 0);
//                        lng = sharedPreferences.getFloat("lng", 0);
//                        query = "INSERT INTO `tb_log`(`police_id`, `myrc`, `remark`, `location`, `lat`, `lng`)"
//                                + " VALUES (?, ?, ?, ?, ?, ?)";
//
//                        // create the mysql insert preparedstatement
//                        PreparedStatement preparedStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
//                        preparedStmt.setString(1, sharedPreferences.getString(Utilities.LOGIN_POLICE_ID, null));
//                        preparedStmt.setString(2, myRc);
//                        preparedStmt.setString(3, "");
//                        preparedStmt.setString(4, locationAddress);
//                        preparedStmt.setFloat(5, lat);
//                        preparedStmt.setFloat(6, lng);
//
//                       // Log.i("insert query", preparedStmt.toString());
//                        // execute the preparedstatement
//                        preparedStmt.executeUpdate();
//
//                        //connection.commit();
//                        ResultSet rs = preparedStmt.getGeneratedKeys();
//                        if (rs.next())
//                            log_id = rs.getInt(1);
//                       // Log.i("insert query id", Integer.toString(log_id));
//                    }
                        //connection.close();
                    } catch (Exception e) {
                        System.err.println("Error");
                        e.printStackTrace();
                        return null;
                    }
                }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {

                locationAddress = sharedPreferences.getString(locationAddress, null);
                lat = sharedPreferences.getFloat("lat", 0);
                lng = sharedPreferences.getFloat("lng", 0);
                buttonAddRemark.setVisibility(View.VISIBLE);

                asyncDialog.dismiss();

                if (result != null) {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject refugeeJSON = jsonArray.getJSONObject(0);
                    byte[] base64Encoded = Base64.decodeBase64(refugeeJSON.getString(Utilities.PHOTO));
                    if (base64Encoded != null) {
                        Bitmap photo = BitmapFactory.decodeByteArray(base64Encoded, 0, base64Encoded.length);
                        imageViewPhoto.setImageBitmap(photo);
                    }

                    regID = refugeeJSON.getString("id").toString() != null ? refugeeJSON.getString("id") : "";
                    new CheckPLKSExist().execute();
                    name = refugeeJSON.getString("fullName") != null? refugeeJSON.getString("fullName") : "";
                    myRc = refugeeJSON.getString("myrc") != null ? refugeeJSON.getString("myrc") : "" ;
                    category = refugeeJSON.getString("category") != null ? refugeeJSON.getString("category") : "";
                    unhcr = refugeeJSON.getString("unhcrID") != null ? refugeeJSON.getString("unhcrID") : "";
                    dob = refugeeJSON.getString("dob") != null ? refugeeJSON.getString("dob") : "";
                    Date dobDate = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
                    dob = new SimpleDateFormat("dd MMMM yyyy").format(dobDate);
                    gender = refugeeJSON.getString("gender") != null ? refugeeJSON.getString("gender") : "";
                    address = refugeeJSON.getString("address") != null ? refugeeJSON.getString("address") : "";
                    country = refugeeJSON.getString("countryOfOrigin") != null ? refugeeJSON.getString("countryOfOrigin") : "";
                    religion = refugeeJSON.getString("religion") != null ? refugeeJSON.getString("religion") : "";
                    ethnicGroup = refugeeJSON.getString("ethincGroup") != null ? refugeeJSON.getString("ethincGroup") : "";
                    issueDate = refugeeJSON.getString("createdTime") != null ? refugeeJSON.getString("createdTime") :null;
                    if(issueDate!=null && !issueDate.equals("null")) {
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(issueDate);
                        issueDate = new SimpleDateFormat("dd MMMM yyyy h:mm:ss a").format(date1);
                    }
                    expiredDate = refugeeJSON.getString("cardExpiredDate") != null ? refugeeJSON.getString("cardExpiredDate") : null;
                    if(expiredDate!=null && !expiredDate.equals("null")) {
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(expiredDate);
                        expiredDate = new SimpleDateFormat("dd MMMM yyyy").format(date1);
                    }
                    employerName = refugeeJSON.getString("empContactName") != null ? refugeeJSON.getString("empContactName") : "";
                    employerContact = refugeeJSON.getString("empContactNo") != null ? refugeeJSON.getString("empContactNo") :  "";
                    mobileNo = refugeeJSON.getString("mobileNo") != null ? refugeeJSON.getString("mobileNo") : "";
                    companyName = refugeeJSON.getString("empName") != null ? refugeeJSON.getString("empName") : "";
                    state = refugeeJSON.getString("state") != null ? refugeeJSON.getString("state") : "";
                    jobSector = refugeeJSON.getString("workSector") != null ? refugeeJSON.getString("workSector") : "";
                    workLocation = refugeeJSON.getString("workLocation") != null ? refugeeJSON.getString("workLocation") : "";
                    workDesc = refugeeJSON.getString("workDescription") != null ? refugeeJSON.getString("workDescription") : "";
                    lengthOfWork = refugeeJSON.getString("lengthOfWork") != null ? Integer.parseInt(refugeeJSON.getString("lengthOfWork")) : Integer.parseInt("0") ;
                    lengthOfStay = refugeeJSON.getString("lengthOfStay") != null ? Integer.parseInt(refugeeJSON.getString("lengthOfStay")) : Integer.parseInt("0");
                    currentPay = refugeeJSON.getString("currentPay") != null ? Double.parseDouble(refugeeJSON.getString("currentPay")) : Double.parseDouble("0");
                    approvalStatus = refugeeJSON.getString("isPrint") != null ? refugeeJSON.getString("isPrint") : "";
//                    if (approvalStatus != null) {
//                        if (approvalStatus.equals("0"))
//                            approvalStatus = "Rejected";
//                        else
//                            approvalStatus = "Approved";
//                    } else
//                        approvalStatus = "Pending";
                    unhcrCaseNo = refugeeJSON.getString("unhcrCaseNo") != null ? refugeeJSON.getString("unhcrCaseNo") : "";
                    refugeeGroup = refugeeJSON.getString("refugeeGroup") != null ? refugeeJSON.getString("refugeeGroup") : "";
                    maritalStatus = refugeeJSON.getString("maritalStatus") != null ? refugeeJSON.getString("maritalStatus") : "";
                    birthPlace = refugeeJSON.getString("placeOfBirth") != null ? refugeeJSON.getString("placeOfBirth") : "";
                    familySize = refugeeJSON.getString("familySize") != null ? refugeeJSON.getString("familySize") : "";
                    passportNo = refugeeJSON.getString("passportNo") != null ? refugeeJSON.getString("passportNo") : "";
                    emergencyPerson = refugeeJSON.getString("emergencyContact") != null ? refugeeJSON.getString("emergencyContact") : "";
                    emergencyContact = refugeeJSON.getString("emergencyNo") != null ? refugeeJSON.getString("emergencyNo") : "";
                    companyRegNo = refugeeJSON.getString("empRegNo") != null ? refugeeJSON.getString("empRegNo") : "";
                    approvalDate = refugeeJSON.getString("approvalTime") != null ? refugeeJSON.getString("approvalTime") : null;
                    if(approvalDate!=null && !approvalDate.equals("null")) {
                        Date approvalDate1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(approvalDate);
                        approvalDate = new SimpleDateFormat("dd-MMMM-yyyy h:mm:ss a").format(approvalDate1);
                        textViewApproveDate.setText(approvalDate);
                    }
                    textViewMyRC.setText(myRc);
                    textViewName.setText(name);
                    textViewDateIssue.setText(issueDate);
                    textViewEthnic.setText(ethnicGroup);
                    textViewCategory.setText(category);
                    if(!expiredDate.isEmpty() && !expiredDate.equals("null"))
                        textViewDateExpiry.setText(expiredDate);
                    textViewAddress.setText(address);
                    textViewGender.setText(gender);
                    textViewDob.setText(dob);
                    textViewCountry.setText(country);
                    textViewUnhcr.setText(unhcr);
                    textViewReligion.setText(religion);
                    textViewState.setText(state);
                    textViewMobile.setText(mobileNo);
                    textViewComapnyName.setText(companyName);
                    textViewEmployerName.setText(employerName);
                    textViewEmployerContact.setText(employerContact);
                    textViewJobSector.setText(jobSector);
                    textViewWorkLocation.setText(workLocation);
                    textViewWorkDesc.setText(workDesc);
                    if (lengthOfWork != 0)
                        textViewLengthOfWork.setText(Integer.toString(lengthOfWork));
                    if (lengthOfStay != 0)
                        textViewLengthOfStay.setText(Integer.toString(lengthOfStay));
                    if (currentPay != 0)
                        textViewCurrentPay.setText("RM " + (String.format("%.2f", currentPay)));

                    if (approvalStatus != null && !approvalStatus.isEmpty()) {
                        switch (approvalStatus) {
                            case "false":
                                approvalStatus = "Rejected";
                                textViewExpired.setText(approvalStatus);
                                textViewExpired.setVisibility(View.VISIBLE);
                                break;
                            case "true":
                                approvalStatus = "Approved";
                                if (expiredDate != null && !expiredDate.isEmpty() && !expiredDate.equals("null")) {
                                    Date dateExpired = new SimpleDateFormat("dd-MM-yyyy").parse(expiredDate);
                                    Calendar expiredDateCal = Calendar.getInstance();
                                    expiredDateCal.setTime(dateExpired);
                                    Calendar cal = Calendar.getInstance();

                                    //Log.i("Expired & current", expiredDate.toString()+cal.toString());
                                    if (cal.getTimeInMillis() > expiredDateCal.getTimeInMillis()) {
                                        textViewExpired.setVisibility(View.VISIBLE);
                                        //Log.i("Expired", "true");
                                    } else
                                        textViewCardValid.setVisibility(View.VISIBLE);
                                } else {
                                    textViewCardValid.setText("Approved");
                                    textViewCardValid.setVisibility(View.VISIBLE);
                                }
                                break;
                            default:
                                approvalStatus = "Pending";
                                //textViewCardPending.setVisibility(View.VISIBLE);
                                break;
                        }
                    } else {
                        approvalStatus = "Pending";
                        //textViewCardPending.setVisibility(View.VISIBLE);
                    }
                    textViewApprovalStatus.setText(approvalStatus);
                    textViewUNCaseNo.setText(unhcrCaseNo);
                    textViewRefugeeGroup.setText(refugeeGroup);
                    textViewMarital.setText(maritalStatus);
                    textViewBirthPlace.setText(birthPlace);
                    textViewFamilySize.setText(familySize);
                    textViewPassport.setText(passportNo);
                    textViewEmergencyPerson.setText(emergencyPerson);
                    textViewEmergencyContact.setText(emergencyContact);
                    textViewComRegNo.setText(companyRegNo);

                } else {
                    textViewNotFound.setVisibility(View.VISIBLE);
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
