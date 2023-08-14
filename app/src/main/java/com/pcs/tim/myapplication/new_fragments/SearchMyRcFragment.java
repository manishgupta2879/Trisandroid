package com.pcs.tim.myapplication.new_fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.pcs.tim.myapplication.DataService;
import com.pcs.tim.myapplication.PartialViewCardStatus;
import com.pcs.tim.myapplication.PartialViewEmployer;
import com.pcs.tim.myapplication.PartialViewHealthStatus;
import com.pcs.tim.myapplication.PartialViewProfile;
import com.pcs.tim.myapplication.R;
import com.pcs.tim.myapplication.SearchMyRCActivity;
import com.pcs.tim.myapplication.Utilities;
import com.pcs.tim.myapplication.ViewDetailsResponse;
import com.pcs.tim.myapplication.ViewRemarksActivity;

import org.apache.commons.net.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class SearchMyRcFragment extends Fragment {

    ImageView buttonSearchMyRC;

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
    private EditText editTextSearchMyRC1;
    private EditText editTextSearchMyRC2;
    private EditText editTextSearchMyRC3;
    private TextView buttonAddRemark;
    private TextView buttonViewRemarks;
    //    private Button buttonViewPLKS;
    //  private Button buttonViewVaccine;
    private static final char space = '-';
    private TextInputEditText textViewUNCaseNo, textViewRefugeeGroup, textViewMarital, textViewBirthPlace, textViewFamilySize, textViewPassport,
            textViewEmergencyPerson, textViewEmergencyContact, textViewComRegNo, textViewApproveDate;

    String regID="", name = "", category = "", unhcr = "", dob = "", gender = "", address = "", country = "",
            religion = "", ethnicGroup = "", issueDate = "", expiredDate = "", employerName = "",
            employerContact = "", mobileNo = "", companyName = "", state = "", jobSector = "",
            workLocation = "",workDesc = "", approvalStatus = "", unhcrCaseNo = "", refugeeGroup = "",
            maritalStatus = "", birthPlace = "", familySize = "", passportNo = "", emergencyPerson = "",
            emergencyContact = "", companyRegNo = "", approvalDate = "", myRc = "" ;
    int lengthOfWork, lengthOfStay;
    double currentPay;
    byte[] photoBytes;
    String dbResult;
    boolean showButtonPLKS = false;

    private int log_id;
    private float lat = 0;
    private float lng = 0;
    String locationAddress = "No GPS";
    protected Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    SharedPreferences sharedPreferences;
    SearchView searchView;

    public SearchMyRcFragment() {
        // Required empty public constructor
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for (getActivity()) fragment
        View view= inflater.inflate(R.layout.activity_search_my_rc, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.MY_RC_SHARE_PREF, MODE_PRIVATE);

        textViewNotFound = (TextView)view.findViewById(R.id.textViewNotFound);
        editTextSearchMyRC1 = (EditText)view.findViewById(R.id.editTextMyRC1);
        editTextSearchMyRC2 = (EditText)view.findViewById(R.id.editTextMyRC2);
        editTextSearchMyRC3 = (EditText)view.findViewById(R.id.editTextMyRC3);
        frameLayout = (FrameLayout) view.findViewById(R.id.verification_result);
        textViewExpired = (TextView)view.findViewById(R.id.textViewCardExpired);
        textViewCardPending = (TextView)view.findViewById(R.id.textViewCardPending);
        textViewCardValid = (TextView)view.findViewById(R.id.textViewCardValid);
        buttonSearchMyRC = view.findViewById(R.id.buttonSearchMyRC);
        textViewMyRC = (TextView)view.findViewById(R.id.textViewMyrc);
        textViewName = (TextView)view.findViewById(R.id.textViewName);
        textViewAddress = (TextView)view.findViewById(R.id.textViewAddress);
        textViewCategory = (TextView)view.findViewById(R.id.textViewCategory);
        textViewCountry  = (TextView)view.findViewById(R.id.textViewCountry);
        textViewDateExpiry = (TextView)view.findViewById(R.id.textViewDateExpiry);
        textViewDateIssue = (TextView)view.findViewById(R.id.textViewDateIssue);
        textViewEthnic = (TextView)view.findViewById(R.id.textViewEthnic);
        textViewDob = (TextView)view.findViewById(R.id.textViewDOB);
        textViewReligion = (TextView)view.findViewById(R.id.textViewReligion);
        textViewUnhcr = (TextView)view.findViewById(R.id.textViewUNHCR);
        textViewGender = (TextView)view.findViewById(R.id.textViewGender);
        textViewState = (TextView)view.findViewById(R.id.textViewState);
        textViewMobile = (TextView)view.findViewById(R.id.textViewMobileNo);
        textViewComapnyName = (TextView)view.findViewById(R.id.textViewCompanyName);
        textViewEmployerName = (TextView)view.findViewById(R.id.textViewEmployerName);
        textViewEmployerContact = (TextView)view.findViewById(R.id.textViewEmployerContact);
        textViewJobSector = (TextView)view.findViewById(R.id.textViewJobSector);
        textViewWorkLocation = (TextView)view.findViewById(R.id.textViewWorkLocation);
        textViewWorkDesc = (TextView)view.findViewById(R.id.textViewWorkDescription);
        textViewLengthOfWork = (TextView)view.findViewById(R.id.textViewLengthOfWork);
        textViewLengthOfStay = (TextView)view.findViewById(R.id.textViewLengthOfStay);
        textViewCurrentPay = (TextView)view.findViewById(R.id.textViewCurrentPay);
        textViewApprovalStatus = (TextView)view.findViewById(R.id.textViewApprovalStatus);
        imageViewPhoto = (ImageView)view.findViewById(R.id.imageViewPhoto);
        buttonAddRemark =  view.findViewById(R.id.btnAddRemark);
        buttonViewRemarks = view.findViewById(R.id.btnViewRemark);
//        buttonViewPLKS = (Button)view.findViewById(R.id.btnViewPLKS);
        // buttonViewVaccine = (Button)view.findViewById(R.id.btnViewVaccine);
        textViewUNCaseNo = view.findViewById(R.id.textViewUNHCRCaseNo);
        textViewRefugeeGroup = view.findViewById(R.id.textViewRefugeeGroup);
        textViewMarital = view.findViewById(R.id.textViewMarital);
        textViewBirthPlace = view.findViewById(R.id.textViewBirthPlace);
        textViewFamilySize =view.findViewById(R.id.textViewFamilySize);
        textViewPassport =view.findViewById(R.id.textViewPassportNo);
        textViewEmergencyPerson = view.findViewById(R.id.textViewEmergencyPerson);
        textViewEmergencyContact =view.findViewById(R.id.textViewEmergencyContact);
        textViewComRegNo = view.findViewById(R.id.textViewCompanyRegNo);
        textViewApproveDate =view.findViewById(R.id.textViewDateApprove);

         searchView=view.findViewById(R.id.svMyRc);

        editTextSearchMyRC1.requestFocus();


        Toolbar toolbar = view.findViewById(R.id.searchMyRc_toolbar);
        //   toolbar.setVisibility(View.VISIBLE);// Assuming you have a Toolbar in your layout
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
        //show keyboard
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient((getActivity()));

        buttonViewRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRc = textViewMyRC.getText().toString();
                Intent intentRemark = new Intent(getActivity(), ViewRemarksActivity.class);
                intentRemark.putExtra(Utilities.MY_RC, myRc);
                startActivity(intentRemark);
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                textViewNotFound.setVisibility(View.GONE);
                if (checkMyRc(s)) {
                    myRc =s;
                           /* editTextSearchMyRC1.getText().toString() + space +
                             editTextSearchMyRC2.getText().toString() + space +
                             editTextSearchMyRC3.getText().toString()*/;
                    //hide keyboard
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextSearchMyRC1.getWindowToken(), 0);
//                    new RetrieveRefugeeData().execute();
                    new ViewRefugeeData().execute();

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
//        buttonViewPLKS.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentPLKS = new Intent(getActivity()(), ViewPLKSActivity.class);
//                intentPLKS.putExtra(Utilities.REG_ID, regID);
//                startActivity(intentPLKS);
//            }
//        });
    /*    buttonViewVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVaccine = new Intent(getActivity()(), ViewVaccination.class);
                intentVaccine.putExtra("myrc",textViewMyRC.getText().toString());
                intentVaccine.putExtra("phoneNo",textViewMobile.getText().toString());
                startActivity(intentVaccine);
            }
        });*/

//        buttonAddRemark.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
//                    String myRc = textViewMyRC.getText().toString();
//                    AddRemarksFragment addRemarksFragment = AddRemarksFragment.newInstance(myRc, log_id,regID);
//                    addRemarksFragment.show(getSupportFragmentManager(), getString(R.string.add_remark));
//                }else{
//                    ActivityCompat.requestPermissions(SearchMyRCActivity.(getActivity()),
//                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
//                            1);
//                }
//            }
//        });

        editTextSearchMyRC1.addTextChangedListener(new TextWatcher() {

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
                if(s.length() == 16)
                    editTextSearchMyRC2.requestFocus();

            }
        });

        editTextSearchMyRC2.addTextChangedListener(new TextWatcher() {

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
                if(s.length() == 6){
                    editTextSearchMyRC3.requestFocus();
                }
            }
        });
        buttonSearchMyRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textViewNotFound.setVisibility(View.GONE);
                if (checkMyRc("abc")) {
                    myRc = editTextSearchMyRC1.getText().toString()/* + space +
                             editTextSearchMyRC2.getText().toString() + space +
                             editTextSearchMyRC3.getText().toString()*/;
                    //hide keyboard
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextSearchMyRC1.getWindowToken(), 0);
//                    new RetrieveRefugeeData().execute();
                    new ViewRefugeeData().execute();

                }
            }
        });
        
        return view;
    }


    private boolean checkMyRc(String myRcText){
        if(myRcText.length() < 16)
        {
          //  editTextSearchMyRC1.setError("Please insert 16 characters");
            Toast.makeText(getActivity(), "Please insert 16 characters", Toast.LENGTH_SHORT).show();
            searchView.requestFocus();

        }/*else if (editTextSearchMyRC2.getText().toString().length() < 6){

            editTextSearchMyRC2.setError("Please insert 6 characters");
            editTextSearchMyRC2.requestFocus();

        }else if (editTextSearchMyRC3.getText().toString().length() < 7){
            editTextSearchMyRC3.setError("Please insert 7 characters");
            editTextSearchMyRC3.requestFocus();
        }*/
        else
            return true;
        return false;
    }

    private class CheckPLKSExist extends AsyncTask<String,Void,String> {

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
            try{
                String result = DataService.PostTrackLog(Long.parseLong(regID),"V",Long.parseLong(sharedPreferences.getString(Utilities.LOGIN_POLICE_ID, null)),
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
        ProgressDialog asyncDialog = new ProgressDialog(getActivity());

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

                String result = DataService.ViewRefugeeDetails("Myrc",String.valueOf(myRc));
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
                        if(issueDate!=null && !issueDate.equals("null")) {
                            Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(issueDate);
                            issueDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(date1);
                        }
                        expiredDate = respCard.getCardExpiredDate();
                        if(expiredDate!=null && !expiredDate.equals("null")) {
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

    private class RetrieveRefugeeData extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(getActivity());

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
            try{

                String tokenResponse = Utilities.SendTokenPostRequest();
                JSONObject jsonObject = new JSONObject(tokenResponse);
                String token = jsonObject.getString("access_token");
                String tokenType = jsonObject.getString("token_type");

                String result = Utilities.SendGetRequest(Utilities.REFUGEE + "?inputType=myrc" + "&value=" + URLEncoder.encode(String.valueOf(myRc), "UTF-8"), token, tokenType);

                JSONArray arrayResult = new JSONArray(result);
                JSONObject objectResult = arrayResult.getJSONObject(0);


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


                return result;

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
                // Log.i("Refugee Result", result);
                if(result != null) {



                    textViewNotFound.setVisibility(View.GONE);
                    //dd
                    buttonAddRemark.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.VISIBLE);

                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject refugeeJSON = jsonArray.getJSONObject(0);
                    byte[] base64Encoded = Base64.decodeBase64(refugeeJSON.getString(Utilities.PHOTO));
                    if (base64Encoded != null) {
                        Bitmap photo = BitmapFactory.decodeByteArray(base64Encoded, 0, base64Encoded.length);
                        imageViewPhoto.setImageBitmap(photo);
                    }

                    regID = refugeeJSON.getString("id").toString() != null ? refugeeJSON.getString("id") : "";
                    new CheckPLKSExist().execute();
                    name = refugeeJSON.getString("fullName");
                    myRc = refugeeJSON.getString("myrc");
                    category = refugeeJSON.getString("category");
                    unhcr = refugeeJSON.getString("unhcrID");
                    dob = refugeeJSON.getString("dob");
                    Date dobDate = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
                    dob = new SimpleDateFormat("dd-MM-yyyy").format(dobDate);
                    gender = refugeeJSON.getString("gender");
                    address = refugeeJSON.getString("address");
                    country = refugeeJSON.getString("countryOfOrigin");
                    religion = refugeeJSON.getString("religion");
                    ethnicGroup = refugeeJSON.getString("ethincGroup");
                    issueDate = refugeeJSON.getString("createdTime");
                    if(issueDate!=null && !issueDate.equals("null")) {
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(issueDate);
                        issueDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(date1);
                    }
                    expiredDate = refugeeJSON.getString("cardExpiredDate");
                    if(expiredDate!=null && !expiredDate.equals("null")) {
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(expiredDate);
                        expiredDate = new SimpleDateFormat("dd-MM-yyyy").format(date1);
                    }
                    employerName = refugeeJSON.getString("empContactName");
                    employerContact = refugeeJSON.getString("empContactNo");
                    mobileNo = refugeeJSON.getString("mobileNo");
                    companyName = refugeeJSON.getString("empName");
                    state = refugeeJSON.getString("state");
                    jobSector = refugeeJSON.getString("workSector");
                    workLocation = refugeeJSON.getString("workLocation");
                    workDesc = refugeeJSON.getString("workDescription");
                    lengthOfWork = Integer.parseInt(refugeeJSON.getString("lengthOfWork"));
                    lengthOfStay = Integer.parseInt(refugeeJSON.getString("lengthOfStay"));
                    currentPay = Double.parseDouble(refugeeJSON.getString("currentPay"));
                    approvalStatus = refugeeJSON.getString("isPrint");

                    unhcrCaseNo = refugeeJSON.getString("unhcrCaseNo");
                    refugeeGroup = refugeeJSON.getString("refugeeGroup");
                    maritalStatus = refugeeJSON.getString("maritalStatus");
                    birthPlace = refugeeJSON.getString("placeOfBirth");
                    familySize = refugeeJSON.getString("familySize");
                    passportNo = refugeeJSON.getString("passportNo");
                    emergencyPerson = refugeeJSON.getString("emergencyContact");
                    emergencyContact = refugeeJSON.getString("emergencyNo");
                    companyRegNo = refugeeJSON.getString("empRegNo");
                    approvalDate = refugeeJSON.getString("approvalTime");
                    if(approvalDate!=null && !approvalDate.equals("null")) {
                        Date approvalDate1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(approvalDate);
                        approvalDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(approvalDate1);
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
                                textViewCardPending.setVisibility(View.VISIBLE);
                                break;
                        }
                    } else {
                        approvalStatus = "Pending";
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

                } else {
                    textViewNotFound.setVisibility(View.VISIBLE);
                }
            }
            catch(Exception ex) {
                textViewNotFound.setVisibility(View.VISIBLE);
                ex.printStackTrace();
            }
        }
    }
}