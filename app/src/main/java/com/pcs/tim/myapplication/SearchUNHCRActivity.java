package com.pcs.tim.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Process;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.apache.commons.net.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SearchUNHCRActivity extends AppCompatActivity {

    ImageView buttonSearchUNHCR;

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
    private TextView textViewExpired;
    private TextView textViewState;
    private TextView textViewMobile;
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
    private TextView textViewCardValid, textViewCardPending;
    private ImageView imageViewPhoto;
    private FrameLayout frameLayout;
    private EditText editTextSearchUNHCR1;
    private EditText editTextSearchUNHCR2;
    private TextView buttonAddRemark;
//    private Button buttonViewPLKS;
    //  private Button buttonViewVaccine;
    private static final char space = '-';
    private TextInputEditText textViewUNCaseNo, textViewRefugeeGroup, textViewMarital, textViewBirthPlace, textViewFamilySize, textViewPassport,
            textViewEmergencyPerson, textViewEmergencyContact, textViewComRegNo, textViewApproveDate;

    String regID = "", name = "", category = "", unhcr = "", dob = "", gender = "", address = "", country = "",
            religion = "", ethnicGroup = "", issueDate = "", expiredDate = "", employerName = "",
            employerContact = "", mobileNo = "", companyName = "", state = "", jobSector = "",
            workLocation = "", workDesc = "", approvalStatus = "", unhcrCaseNo = "", refugeeGroup = "",
            maritalStatus = "", birthPlace = "", familySize = "", passportNo = "", emergencyPerson = "",
            emergencyContact = "", companyRegNo = "", approvalDate = "", myRc = "";
    int lengthOfWork, lengthOfStay;
    double currentPay;
    byte[] photoBytes;
    String dbResult;
    private int log_id;
    boolean showButtonPLKS = false;
    private float lat;
    private float lng;
    String locationAddress = "No GPS";
    protected Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    SharedPreferences sharedPreferences;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_unhcr);

        sharedPreferences = getSharedPreferences(Utilities.MY_RC_SHARE_PREF, MODE_PRIVATE);

        textViewNotFound = (TextView) findViewById(R.id.textViewNotFound);
        editTextSearchUNHCR1 = (EditText) findViewById(R.id.editTextUNHCR1);
        editTextSearchUNHCR2 = (EditText) findViewById(R.id.editTextUNHCR2);

        frameLayout = (FrameLayout) findViewById(R.id.verification_result);
        buttonSearchUNHCR = findViewById(R.id.buttonSearchUNHCR);
        textViewMyRC = (TextView) findViewById(R.id.textViewMyrc);
        textViewCardPending = (TextView) findViewById(R.id.textViewCardPending);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        textViewCategory = (TextView) findViewById(R.id.textViewCategory);
        textViewCountry = (TextView) findViewById(R.id.textViewCountry);
        textViewDateExpiry = (TextView) findViewById(R.id.textViewDateExpiry);
        textViewDateIssue = (TextView) findViewById(R.id.textViewDateIssue);
        textViewEthnic = (TextView) findViewById(R.id.textViewEthnic);
        textViewDob = (TextView) findViewById(R.id.textViewDOB);
        textViewReligion = (TextView) findViewById(R.id.textViewReligion);
        textViewUnhcr = (TextView) findViewById(R.id.textViewUNHCR);
        textViewGender = (TextView) findViewById(R.id.textViewGender);
        imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
        textViewExpired = (TextView) findViewById(R.id.textViewCardExpired);
        textViewCardValid = (TextView) findViewById(R.id.textViewCardValid);
        textViewState = (TextView) findViewById(R.id.textViewState);
        textViewMobile = (TextView) findViewById(R.id.textViewMobileNo);
        textViewComapnyName = (TextView) findViewById(R.id.textViewCompanyName);
        textViewEmployerName = (TextView) findViewById(R.id.textViewEmployerName);
        textViewEmployerContact = (TextView) findViewById(R.id.textViewEmployerContact);
        textViewJobSector = (TextView) findViewById(R.id.textViewJobSector);
        textViewWorkLocation = (TextView) findViewById(R.id.textViewWorkLocation);
        textViewWorkDesc = (TextView) findViewById(R.id.textViewWorkDescription);
        textViewLengthOfWork = (TextView) findViewById(R.id.textViewLengthOfWork);
        textViewLengthOfStay = (TextView) findViewById(R.id.textViewLengthOfStay);
        textViewCurrentPay = (TextView) findViewById(R.id.textViewCurrentPay);
        textViewApprovalStatus = (TextView) findViewById(R.id.textViewApprovalStatus);
          buttonAddRemark =  findViewById(R.id.btnAddRemark);
        textViewUNCaseNo = findViewById(R.id.textViewUNHCRCaseNo);
        textViewRefugeeGroup = findViewById(R.id.textViewRefugeeGroup);
        textViewMarital = findViewById(R.id.textViewMarital);
        textViewBirthPlace = findViewById(R.id.textViewBirthPlace);
        textViewFamilySize = findViewById(R.id.textViewFamilySize);
        textViewPassport = findViewById(R.id.textViewPassportNo);
        textViewEmergencyPerson = findViewById(R.id.textViewEmergencyPerson);
        textViewEmergencyContact = findViewById(R.id.textViewEmergencyContact);
        textViewComRegNo = findViewById(R.id.textViewCompanyRegNo);
        textViewApproveDate = findViewById(R.id.textViewDateApprove);

        //show keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        TextView buttonViewRemarks =  findViewById(R.id.btnViewRemark);
//        buttonViewPLKS = (Button)findViewById(R.id.btnViewPLKS);
        //  buttonViewVaccine = (Button)findViewById(R.id.btnViewVaccine);
        buttonViewRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myRc = textViewMyRC.getText().toString();
                    Intent intentRemark = new Intent(getApplicationContext(), ViewRemarksActivity.class);
                    intentRemark.putExtra(Utilities.MY_RC, myRc);
                    startActivity(intentRemark);
            }
        });
//        buttonViewPLKS.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentPLKS = new Intent(getApplicationContext(), ViewPLKSActivity.class);
//                intentPLKS.putExtra(Utilities.REG_ID, regID);
//                startActivity(intentPLKS);
//            }
//        });

      /*  buttonViewVaccine.setOnClickListener(new View.OnClickListener() {
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
                    if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
                        String myRc = textViewMyRC.getText().toString();
                        AddRemarksFragment addRemarksFragment = AddRemarksFragment.newInstance(myRc, log_id,regID);
                        addRemarksFragment.show(getSupportFragmentManager(), getString(R.string.add_remark));
                    } else {
                        ActivityCompat.requestPermissions(SearchUNHCRActivity.this,
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                1);
                    }
            }
        });

        editTextSearchUNHCR1.addTextChangedListener(new TextWatcher() {

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
                if (s.length() == 12)
                    editTextSearchUNHCR2.requestFocus();
            }
        });

        buttonSearchUNHCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textViewNotFound.setVisibility(View.GONE);
//dd
                //  unhcr = editTextSearchUNHCR1.getText().toString() + space + editTextSearchUNHCR2.getText().toString();
                unhcr = editTextSearchUNHCR1.getText().toString() ;

                if (checkUNHCR()) {
                    //hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextSearchUNHCR1.getWindowToken(), 0);
                    new ViewRefugeeData().execute();
                }
            }
        });
    }

    private boolean checkUNHCR() {
        if (editTextSearchUNHCR1.getText().toString().length() < 12) {
            editTextSearchUNHCR1.setError("Please insert 11 characters.");
            editTextSearchUNHCR1.requestFocus();
        }
       /* else if(editTextSearchUNHCR2.getText().toString().length() < 8)
        {
            editTextSearchUNHCR2.setError("Please insert 8 characters.");
            editTextSearchUNHCR2.requestFocus();
        }*/
        else
            return true;
        return false;
    }

    private class CheckPLKSExist extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String tokenResponse = Utilities.SendTokenPostRequest();
                JSONObject jsonObject = new JSONObject(tokenResponse);
                String token = jsonObject.getString("access_token");
                String tokenType = jsonObject.getString("token_type");

                String result = Utilities.SendGetRequest(Utilities.PLKS + "?regID=" + regID + "&searchExist=true", token, tokenType);

                return result;

            } catch (Exception e) {

                return null;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
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

    private class AddTrackLog extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                String result = DataService.PostTrackLog(Long.parseLong(regID), "V", Long.parseLong(sharedPreferences.getString(Utilities.LOGIN_POLICE_ID, null)),
                        sharedPreferences.getString("locationAddress", "No GPS"), sharedPreferences.getFloat("lat", 0)
                        , sharedPreferences.getFloat("lng", 0), "");
                if (result != null && result != "ERROR") {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }


            } catch (Exception e) {

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

    private class ViewRefugeeData extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(SearchUNHCRActivity.this);

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

                String result = DataService.ViewRefugeeDetails("UnhcrId", unhcr);
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


//                JSONArray arrayResult = new JSONArray(result);
//                JSONObject objectResult = arrayResult.getJSONObject(0);


//                String tokenResponse = Utilities.SendTokenPostRequest();
//                JSONObject jsonObject = new JSONObject(tokenResponse);
//                String token = jsonObject.getString("access_token");
//                String tokenType = jsonObject.getString("token_type");
//                locationAddress = sharedPreferences.getString("locationAddress", "No GPS");
//                lat = sharedPreferences.getFloat("lat", 0);
//                lng = sharedPreferences.getFloat("lng", 0);
//                HashMap<String, String> data = new HashMap<>();
//                data.put(Utilities.POLICE_ID, sharedPreferences.getString(Utilities.LOGIN_POLICE_ID, null));
//                data.put(Utilities.MY_RC, objectResult.getString(Utilities.MY_RC));
//                data.put(Utilities.LOG_REMARK, "");
//                data.put(Utilities.LOCATION, locationAddress);
//                data.put(Utilities.LATITUDE, Float.toString(lat));
//                data.put(Utilities.LONGITUDE, Float.toString(lng));
//                Utilities.sendPostRequest(Utilities.LOG, data, token, tokenType);


            } catch (Exception e) {
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

                        //dd
                         buttonAddRemark.setVisibility(View.VISIBLE);
                        frameLayout.setVisibility(View.VISIBLE);


                        ViewDetailsResponse respViewDetail = new Gson().fromJson(result, ViewDetailsResponse.class);
                        PartialViewProfile respProfile = respViewDetail.getProfile();
                        PartialViewEmployer respEmployer = respViewDetail.getEmployer();
                        PartialViewCardStatus respCard = respViewDetail.getCardStatus();
                        PartialViewHealthStatus respHealth = respViewDetail.getHealthStatus();

                        byte[] base64Encoded = Base64.decodeBase64(respViewDetail.getPhoto());
                        if (base64Encoded != null) {
                            Bitmap photo = BitmapFactory.decodeByteArray(base64Encoded, 0, base64Encoded.length);
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
                        Date dobDate = new SimpleDateFormat("dd/MM/yyyy").parse(dob);
                        dob = new SimpleDateFormat("dd-MM-yyyy").format(dobDate);
                        gender = respProfile.getGender();
                        address = respProfile.getAddress();
                        country = respProfile.getCountryOfOrigin();
                        religion = respProfile.getReligion();
                        ethnicGroup = respProfile.getEthnicGroup();
                        issueDate = respCard.getRegisterDate();
                        if (issueDate != null && !issueDate.equals("null")) {
                            Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(issueDate);
                            issueDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(date1);
                        }
                        expiredDate = respCard.getCardExpiredDate();
                        if (expiredDate != null && !expiredDate.equals("null")) {
                            Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(expiredDate);
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
                        lengthOfWork = Integer.parseInt(String.valueOf(respEmployer.getLengthOfWork() != null ? respEmployer.getLengthOfWork() : 0));
                        lengthOfStay = Integer.parseInt(String.valueOf(respProfile.getLengthOfStay() != null ? respProfile.getLengthOfStay() : 0));
                        if (respEmployer.getCurrentPay() != null && !respEmployer.getCurrentPay().equals("null")) {
                            currentPay = Double.parseDouble(String.valueOf(respEmployer.getCurrentPay()));
                        } else {
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
                        if (!expiredDate.isEmpty() && !expiredDate.equals("null"))
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
                                case "REJECTED":
                                    approvalStatus = "REJECTED";
                                    textViewExpired.setText(approvalStatus);
                                    textViewExpired.setVisibility(View.VISIBLE);
                                    break;
                                case "EXPIRED":
                                    approvalStatus = "EXPIRED";
                                    textViewExpired.setText(approvalStatus);
                                    textViewExpired.setVisibility(View.VISIBLE);
                                    break;
                                case "APPROVED":
                                    approvalStatus = "APPROVED";
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
                                        textViewCardValid.setText("APPROVED");
                                        textViewCardValid.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                default:
                                    approvalStatus = "PENDING";
                                    textViewCardPending.setVisibility(View.VISIBLE);
                                    break;
                            }
                        } else {
                            approvalStatus = "PENDING";
                            textViewCardPending.setVisibility(View.VISIBLE);
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

//    private class RetrieveRefugeeData extends AsyncTask<String, Void, String> {
//        ProgressDialog asyncDialog = new ProgressDialog(SearchUNHCRActivity.this);
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
//            try{
//                String tokenResponse = Utilities.SendTokenPostRequest();
//                JSONObject jsonObject = new JSONObject(tokenResponse);
//                String token = jsonObject.getString("access_token");
//                String tokenType = jsonObject.getString("token_type");
//
//                String result = Utilities.SendGetRequest(Utilities.REFUGEE + "?inputType=unhcr" + "&value=" + URLEncoder.encode(String.valueOf(unhcr), "UTF-8"), token, tokenType);
//
//                JSONArray arrayResult = new JSONArray(result);
//                JSONObject objectResult = arrayResult.getJSONObject(0);
//
//
//                locationAddress = sharedPreferences.getString("locationAddress", "No GPS");
//                lat = sharedPreferences.getFloat("lat", 0);
//                lng = sharedPreferences.getFloat("lng", 0);
//                HashMap<String, String> data = new HashMap<>();
//                data.put(Utilities.POLICE_ID, sharedPreferences.getString(Utilities.LOGIN_POLICE_ID, null));
//                data.put(Utilities.MY_RC, objectResult.getString(Utilities.MY_RC));
//                data.put(Utilities.LOG_REMARK, "");
//                data.put(Utilities.LOCATION, locationAddress);
//                data.put(Utilities.LATITUDE, Float.toString(lat));
//                data.put(Utilities.LONGITUDE, Float.toString(lng));
//                Utilities.sendPostRequest(Utilities.LOG, data, token, tokenType);
//
//                return result;
//            }catch(Exception e) {
//                return null;
//            }
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            try {
//                asyncDialog.dismiss();
//                if(result!=null) {
//                    Log.i("REFUGEE",result);
//                    textViewNotFound.setVisibility(View.GONE);
//                    buttonAddRemark.setVisibility(View.VISIBLE);
//                    frameLayout.setVisibility(View.VISIBLE);
//
//                    JSONArray jsonArray = new JSONArray(result);
//                    JSONObject refugeeJSON = jsonArray.getJSONObject(0);
//                    byte[] base64Encoded = Base64.decodeBase64(refugeeJSON.getString(Utilities.PHOTO));
//                    if (base64Encoded != null) {
//                        Bitmap photo = BitmapFactory.decodeByteArray(base64Encoded, 0, base64Encoded.length);
//                        imageViewPhoto.setImageBitmap(photo);
//                    }
//
//                    regID = refugeeJSON.getString("id").toString() != null ? refugeeJSON.getString("id") : "";
//                    new CheckPLKSExist().execute();
//                    name = refugeeJSON.getString("fullName").toString() !=null ?refugeeJSON.getString("fullName") : "";
//                    myRc = refugeeJSON.getString("myrc");
//                    category = refugeeJSON.getString("category");
//                    unhcr = refugeeJSON.getString("unhcrID");
//                    dob = refugeeJSON.getString("dob");
//                    Date dobDate = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
//                    dob = new SimpleDateFormat("dd-MM-yyyy").format(dobDate);
//                    gender = refugeeJSON.getString("gender");
//                    address = refugeeJSON.getString("address");
//                    country = refugeeJSON.getString("countryOfOrigin");
//                    religion = refugeeJSON.getString("religion");
//                    ethnicGroup = refugeeJSON.getString("ethincGroup");
//                    issueDate = refugeeJSON.getString("createdTime");
//                    if(issueDate!=null && !issueDate.equals("null")) {
//                        Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(issueDate);
//                        issueDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(date1);
//                    }
//                    expiredDate = refugeeJSON.getString("cardExpiredDate");
//                    if(expiredDate!=null && !expiredDate.equals("null")) {
//                        Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(expiredDate);
//                        expiredDate = new SimpleDateFormat("dd-MM-yyyy").format(date1);
//                    }
//                    employerName = refugeeJSON.getString("empContactName");
//                    employerContact = refugeeJSON.getString("empContactNo");
//                    mobileNo = refugeeJSON.getString("mobileNo");
//                    companyName = refugeeJSON.getString("empName");
//                    state = refugeeJSON.getString("state");
//                    jobSector = refugeeJSON.getString("workSector");
//                    workLocation = refugeeJSON.getString("workLocation");
//                    workDesc = refugeeJSON.getString("workDescription");
//                    if(refugeeJSON.getString("lengthOfWork")!=null && !refugeeJSON.getString("lengthOfWork").equals("null")){
//                        lengthOfWork =Integer.parseInt(refugeeJSON.getString("lengthOfWork"));
//                    }else{
//                        lengthOfWork = 0;
//                    }
//                    lengthOfStay = Integer.parseInt(refugeeJSON.getString("lengthOfStay"));
//                    currentPay = Double.parseDouble(refugeeJSON.getString("currentPay"));
//                    approvalStatus = refugeeJSON.getString("isPrint");
//
//                    unhcrCaseNo = refugeeJSON.getString("unhcrCaseNo");
//                    refugeeGroup = refugeeJSON.getString("refugeeGroup");
//                    maritalStatus = refugeeJSON.getString("maritalStatus");
//                    birthPlace = refugeeJSON.getString("placeOfBirth");
//                    familySize = refugeeJSON.getString("familySize");
//                    passportNo = refugeeJSON.getString("passportNo");
//                    emergencyPerson = refugeeJSON.getString("emergencyContact");
//                    emergencyContact = refugeeJSON.getString("emergencyNo");
//                    companyRegNo = refugeeJSON.getString("empRegNo");
//                    approvalDate = refugeeJSON.getString("approvalTime");
//                    if(approvalDate!=null && !approvalDate.equals("null")) {
//                        Date approvalDate1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(approvalDate);
//                        approvalDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(approvalDate1);
//                        textViewApproveDate.setText(approvalDate);
//                    }
//                    textViewMyRC.setText(myRc);
//                    textViewName.setText(name);
//                    textViewDateIssue.setText(issueDate);
//                    textViewEthnic.setText(ethnicGroup);
//                    textViewCategory.setText(category);
//                    if(!expiredDate.isEmpty() && !expiredDate.equals("null"))
//                        textViewDateExpiry.setText(expiredDate);
//                    textViewAddress.setText(address);
//                    textViewGender.setText(gender);
//                    textViewDob.setText(dob);
//                    textViewCountry.setText(country);
//                    textViewUnhcr.setText(unhcr);
//                    textViewReligion.setText(religion);
//                    textViewState.setText(state);
//                    textViewMobile.setText(mobileNo);
//                    textViewComapnyName.setText(companyName);
//                    textViewEmployerName.setText(employerName);
//                    textViewEmployerContact.setText(employerContact);
//                    textViewJobSector.setText(jobSector);
//                    textViewWorkLocation.setText(workLocation);
//                    textViewWorkDesc.setText(workDesc);
//                    if (lengthOfWork != 0)
//                        textViewLengthOfWork.setText(Integer.toString(lengthOfWork));
//                    if (lengthOfStay != 0)
//                        textViewLengthOfStay.setText(Integer.toString(lengthOfStay));
//                    if (currentPay != 0)
//                        textViewCurrentPay.setText("RM " + (String.format("%.2f", currentPay)));
//
//                    if (approvalStatus != null && !approvalStatus.isEmpty()) {
//                        switch (approvalStatus) {
//                            case "false":
//                                approvalStatus = "Rejected";
//                                textViewExpired.setText(approvalStatus);
//                                textViewExpired.setVisibility(View.VISIBLE);
//                                break;
//                            case "true":
//                                approvalStatus = "Approved";
//                                if (expiredDate != null && !expiredDate.isEmpty() && !expiredDate.equals("null")) {
//                                    Date dateExpired = new SimpleDateFormat("dd-MM-yyyy").parse(expiredDate);
//                                    Calendar expiredDateCal = Calendar.getInstance();
//                                    expiredDateCal.setTime(dateExpired);
//                                    Calendar cal = Calendar.getInstance();
//
//                                    if (cal.getTimeInMillis() > expiredDateCal.getTimeInMillis()) {
//                                        textViewExpired.setVisibility(View.VISIBLE);
//                                    } else
//                                        textViewCardValid.setVisibility(View.VISIBLE);
//                                } else {
//                                    textViewCardValid.setText("Approved");
//                                    textViewCardValid.setVisibility(View.VISIBLE);
//                                }
//                                break;
//                            default:
//                                approvalStatus = "Pending";
//                                textViewCardPending.setVisibility(View.VISIBLE);
//                                break;
//                        }
//                    } else {
//                        approvalStatus = "Pending";
//                        textViewCardPending.setVisibility(View.VISIBLE);
//                    }
//                    textViewApprovalStatus.setText(approvalStatus);
//                    textViewUNCaseNo.setText(unhcrCaseNo);
//                    textViewRefugeeGroup.setText(refugeeGroup);
//                    textViewMarital.setText(maritalStatus);
//                    textViewBirthPlace.setText(birthPlace);
//                    textViewFamilySize.setText(familySize);
//                    textViewPassport.setText(passportNo);
//                    textViewEmergencyPerson.setText(emergencyPerson);
//                    textViewEmergencyContact.setText(emergencyContact);
//                    textViewComRegNo.setText(companyRegNo);
//
//                } else {
//                    textViewNotFound.setVisibility(View.VISIBLE);
//                }
//            }
//            catch(Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }

}
