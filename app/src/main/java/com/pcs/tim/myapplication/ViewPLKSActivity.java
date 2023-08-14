package com.pcs.tim.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ViewPLKSActivity extends AppCompatActivity {

    private TextView textViewNotFound;
    private LinearLayout PLKSPage;
    private ImageView imageViewPLKSStatus;
    private TextView textViewFormSubmitDateLabel;
    private TextView textViewFormSubmitDateValue;
    private TextView textViewCurrentProgressLabel;
    private TextView textViewCurrentProgressValue;
    private TextView textViewMyrc;
    private TextView textViewWithPassport;
    private TextView textViewPassportNo;
    private TextView textViewPassportExpiry;
    private TextView textViewPassportPlaceOfIssue;
    private TextView textViewJobSector;
    private TextView textViewPosition;
    private TextView textViewSalary;
    private TextView textViewDurationOfPassApplied;
    private TextView textViewMedicalReportDate;
    private TextView textViewFWIGProvider;
    private TextView textViewFWIGPolicyNo;
    private TextView textViewFWHSPolicyNo;
    private TextView textViewEmployerCompanyName;
    private TextView textViewEmployerCompanyRegNo;
    private TextView textViewEmployerCompanyDirectorName;
    private TextView textViewEmployerCompanyAddress;
    private TextView textViewEmployerState;
    private TextView textViewEmployerPhoneNo;
    private TextView textViewEmployerFaxNo;
    private TextView textViewEmployerEmail;
    private TextView textViewEmployerContactPerson;
    private TextView textViewEmployerContactPersonPhoneNo;
    private TextView textViewEmployerContactPersonPosition;
    String regID="";

    String FormSubmitDateLabel= "",FormSubmitDateValue="",CurrentProgressLabel="",CurrentProgressValue="",Myrc="",PassportNo="",PassportExpiry="",PassportPlaceOfIssue="",
            JobSector="",Position="",Salary="",DurationOfPassApplied="",MedicalReportDate="",FWIGProvider="",FWIGPolicyNo="",FWHSPolicyNo="",EmployerCompanyName="",EmployerCompanyRegNo="",
            EmployerCompanyDirectorName="",EmployerCompanyAddress="",EmployerState="",EmployerPhoneNo="",EmployerFaxNo="",EmployerEmail="",EmployerContactPerson="",
            EmployerContactPersonPhoneNo="",EmployerContactPersonPosition="",workPermitExpiryDate="",applicationStatus="",createdTime="",lastUpdTime="";
    Boolean withPassport=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plks);

        textViewNotFound = (TextView)findViewById(R.id.textViewNotFound);
        PLKSPage = (LinearLayout)findViewById(R.id.PLKSPage);
        imageViewPLKSStatus = (ImageView)findViewById(R.id.imageViewPLKSStatus);
        textViewFormSubmitDateLabel = (TextView)findViewById(R.id.textViewFormSubmitDateLabel);
        textViewFormSubmitDateValue= (TextView)findViewById(R.id.textViewFormSubmitDateValue);
        textViewCurrentProgressLabel= (TextView)findViewById(R.id.textViewCurrentProgressLabel);
        textViewCurrentProgressValue= (TextView)findViewById(R.id.textViewCurrentProgressValue);
        textViewMyrc= (TextView)findViewById(R.id.textViewMyrc);
        textViewWithPassport= (TextView)findViewById(R.id.textViewWithPassport);
        textViewPassportNo= (TextView)findViewById(R.id.textViewPassportNo);
        textViewPassportExpiry= (TextView)findViewById(R.id.textViewPassportExpiry);
        textViewPassportPlaceOfIssue= (TextView)findViewById(R.id.textViewPassportPlaceOfIssue);
        textViewJobSector= (TextView)findViewById(R.id.textViewJobSector);
        textViewPosition = (TextView)findViewById(R.id.textViewPosition);
        textViewSalary= (TextView)findViewById(R.id.textViewSalary);
        textViewDurationOfPassApplied= (TextView)findViewById(R.id.textViewDurationOfPassApplied);
        textViewMedicalReportDate= (TextView)findViewById(R.id.textViewMedicalReportDate);
        textViewFWIGProvider= (TextView)findViewById(R.id.textViewFWIGProvider);
        textViewFWIGPolicyNo= (TextView)findViewById(R.id.textViewFWIGPolicyNo);
        textViewFWHSPolicyNo= (TextView)findViewById(R.id.textViewFWHSPolicyNo);
        textViewEmployerCompanyName= (TextView)findViewById(R.id.textViewEmployerCompanyName);
        textViewEmployerCompanyRegNo= (TextView)findViewById(R.id.textViewEmployerCompanyRegNo);
        textViewEmployerCompanyDirectorName= (TextView)findViewById(R.id.textViewEmployerCompanyDirectorName);
        textViewEmployerCompanyAddress= (TextView)findViewById(R.id.textViewEmployerCompanyAddress);
        textViewEmployerState= (TextView)findViewById(R.id.textViewEmployerState);
        textViewEmployerPhoneNo= (TextView)findViewById(R.id.textViewEmployerPhoneNo);
        textViewEmployerFaxNo= (TextView)findViewById(R.id.textViewEmployerFaxNo);
        textViewEmployerEmail= (TextView)findViewById(R.id.textViewEmployerEmail);
        textViewEmployerContactPerson= (TextView)findViewById(R.id.textViewEmployerContactPerson);
        textViewEmployerContactPersonPhoneNo= (TextView)findViewById(R.id.textViewEmployerContactPersonPhoneNo);
        textViewEmployerContactPersonPosition= (TextView)findViewById(R.id.textViewEmployerContactPersonPosition);

        Intent intent = getIntent();
        regID = intent.getStringExtra(Utilities.REG_ID);

        if (regID != null && !regID.isEmpty() && !regID.equals("null")){
            new RetrievePLKSData().execute();
        }
    }

    private class RetrievePLKSData extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(ViewPLKSActivity.this);

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

//                String tokenResponse = Utilities.SendTokenPostRequest();
//                JSONObject jsonObject = new JSONObject(tokenResponse);
//                String token = jsonObject.getString("access_token");
//                String tokenType = jsonObject.getString("token_type");
//
//                String result = Utilities.SendGetRequest(Utilities.PLKS + "?regID=" +regID, token, tokenType);
                String result = DataService.GetPLKS(Integer.parseInt(regID));
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

        @Override
        protected void onPostExecute(String result) {
            asyncDialog.dismiss();
            if(result!=null && !result.equals("null")){
                try{

                    ViewPLKS respViewPLKS = new Gson().fromJson(result, ViewPLKS.class);
                    PartialViewWorker respWorker = respViewPLKS.getPlksDetail();
                    PartialViewPLKSEmployer respEmployer = respViewPLKS.getEmployer();

                    Myrc = "";
                    if(respWorker.getWithPassport()==0){
                        withPassport =false;
                    }else{
                        withPassport =true;
                    }
                    PassportNo = respWorker.getPassportNo();
                    PassportExpiry = respWorker.getPassportExpiryDate();
                    PassportPlaceOfIssue = respWorker.getPassportPlaceOfIssue();
                    JobSector = respWorker.getSector();
                    Position = respWorker.getPosition();
                    Salary = respWorker.getSalary();
                    if(respWorker.getDurationOfPassApply()!=null){
                        DurationOfPassApplied = String.valueOf(respWorker.getDurationOfPassApply());
                    }else{
                        DurationOfPassApplied = "";
                    }
                    MedicalReportDate=respWorker.getMedicalReportDate();
                    FWIGProvider=respWorker.getIgProvider();
                    FWIGPolicyNo=respWorker.getIgPolicyNo();
                    FWHSPolicyNo=respWorker.getFwhsPolicyNo();
                    workPermitExpiryDate=respWorker.getWorkPermitExpiryDate();
                    applicationStatus=respWorker.getApplicationStatus();
                    createdTime=respWorker.getCreatedTime();
                    lastUpdTime=respWorker.getCreatedTime();

                    EmployerCompanyName=respEmployer.getCompName();
                    EmployerCompanyRegNo=respEmployer.getCompRegNo();
                    EmployerCompanyDirectorName=respEmployer.getEmployerName();
                    EmployerCompanyAddress=respEmployer.getAddress();
                    EmployerState=respEmployer.getState();
                    EmployerPhoneNo=respEmployer.getTelNo();
                    EmployerFaxNo=respEmployer.getFaxNo();
                    EmployerEmail=respEmployer.getEmail();
                    EmployerContactPerson=respEmployer.getContactPerson();
                    EmployerContactPersonPhoneNo = respEmployer.getContactPersonTelNo();
                    EmployerContactPersonPosition = respEmployer.getPosition();

                    if(applicationStatus!=null && !applicationStatus.equals("null")) {
                        if(applicationStatus.equals("A")){
                            imageViewPLKSStatus.setImageResource(R.drawable.ic_check);
                            if(workPermitExpiryDate!=null && !workPermitExpiryDate.equals("null")){
                                Date workPermitExpiryDate1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(workPermitExpiryDate);
                                workPermitExpiryDate = new SimpleDateFormat("dd MMM yyyy").format(workPermitExpiryDate1);
                                textViewCurrentProgressValue.setText(workPermitExpiryDate);
                                textViewCurrentProgressValue.setTextColor(Color.GREEN);
                                textViewCurrentProgressLabel.setText("Work Permit Expiry Date:");
                            }
                            textViewFormSubmitDateLabel.setText("");
                            textViewFormSubmitDateValue.setText("APPROVED");
                            textViewFormSubmitDateLabel.setVisibility(View.GONE);
                            textViewFormSubmitDateValue.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
                            textViewFormSubmitDateValue.setTypeface(null, Typeface.BOLD);

                        }else if (applicationStatus.equals("R")){
                            imageViewPLKSStatus.setImageResource(R.drawable.ic_reject);
                            if(lastUpdTime!=null && !lastUpdTime.equals("null")){
                                Date lastUpdTime1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(lastUpdTime);
                                lastUpdTime = new SimpleDateFormat("dd MMM yyyy").format(lastUpdTime1);
                                textViewCurrentProgressValue.setText(lastUpdTime);
                                textViewCurrentProgressValue.setTextColor(Color.BLACK);
                                textViewCurrentProgressLabel.setText("Updated Date:");
                            }
                            textViewFormSubmitDateLabel.setText("");
                            textViewFormSubmitDateValue.setText("REJECTED");
                            textViewFormSubmitDateLabel.setVisibility(View.GONE);
                            textViewFormSubmitDateValue.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
                            textViewFormSubmitDateValue.setTypeface(null, Typeface.BOLD);
                        }else{
                            imageViewPLKSStatus.setImageResource(R.drawable.ic_waiting_approval);
                            if(createdTime!=null && !createdTime.equals("null")){
                                Date createdTime1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(createdTime);
                                createdTime = new SimpleDateFormat("dd MMM yyyy").format(createdTime1);
                                textViewFormSubmitDateValue.setText(createdTime);
                                textViewFormSubmitDateValue.setTextColor(Color.RED);
                            }
                            textViewFormSubmitDateLabel.setText("Form Submit Date:");
                            textViewCurrentProgressLabel.setText("Current Progress:");
                            textViewCurrentProgressValue.setText("Document Review");
                        }
                    }

                    if(withPassport){
                        textViewWithPassport.setText("YES");
                        textViewPassportNo.setText(PassportNo);
                        textViewPassportPlaceOfIssue.setText(PassportPlaceOfIssue);
                        if(PassportExpiry!=null && !PassportExpiry.equals("null")) {
                            Date PassportExpiry1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(PassportExpiry);
                            PassportExpiry = new SimpleDateFormat("dd MMM yyyy").format(PassportExpiry1);
                            textViewPassportExpiry.setText(PassportExpiry);
                        }
                    }else{
                        textViewWithPassport.setText("NO");
                        textViewPassportNo.setText("");
                        textViewPassportPlaceOfIssue.setText("");
                        textViewPassportExpiry.setText("");
                    }

                    textViewJobSector.setText(JobSector);
                    textViewPosition.setText(Position);
                    double currentSalary = Salary != null ? Double.parseDouble(Salary) : Double.parseDouble("0");
                    textViewSalary.setText("RM " + (String.format("%.2f", currentSalary)));
                    textViewDurationOfPassApplied.setText(DurationOfPassApplied);
                    if(MedicalReportDate!=null && !MedicalReportDate.equals("null")) {
                        Date MedicalReportDate1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(MedicalReportDate);
                        MedicalReportDate = new SimpleDateFormat("dd MMM yyyy").format(MedicalReportDate1);
                        textViewMedicalReportDate.setText(MedicalReportDate);
                    }
                    textViewFWIGProvider.setText(FWIGProvider);
                    textViewFWIGPolicyNo.setText(FWIGPolicyNo);
                    textViewFWHSPolicyNo.setText(FWHSPolicyNo);
                    textViewEmployerCompanyName.setText(EmployerCompanyName);
                    textViewEmployerCompanyRegNo.setText(EmployerCompanyRegNo);
                    textViewEmployerCompanyDirectorName.setText(EmployerCompanyDirectorName);
                    textViewEmployerCompanyAddress.setText(EmployerCompanyAddress);
                    textViewEmployerState.setText(EmployerState);
                    textViewEmployerPhoneNo.setText(EmployerPhoneNo);
                    textViewEmployerFaxNo.setText(EmployerFaxNo);
                    textViewEmployerEmail.setText(EmployerEmail);
                    textViewEmployerContactPerson.setText(EmployerContactPerson);
                    textViewEmployerContactPersonPhoneNo.setText(EmployerContactPersonPhoneNo);
                    textViewEmployerContactPersonPosition.setText(EmployerContactPersonPosition);


//                    JSONObject jsonObj = new JSONObject(result);

//                    Myrc = jsonObj.getString("MyRC");
//                    withPassport = jsonObj.getBoolean("withPassport");
//                    PassportNo=jsonObj.getString("passportNo");
//                    PassportExpiry=jsonObj.getString("passportExpiryDate");
//                    PassportPlaceOfIssue=jsonObj.getString("passportPlaceOfIssue");
//                    JobSector=jsonObj.getString("sector");
//                    Position =jsonObj.getString("position");
//                    Salary=jsonObj.getString("salary");
//                    DurationOfPassApplied=jsonObj.getString("durationOfPassApply");
//                    MedicalReportDate=jsonObj.getString("medicalReportDate");
//                    FWIGProvider=jsonObj.getString("igProvider");
//                    FWIGPolicyNo=jsonObj.getString("igPolicyNo");
//                    FWHSPolicyNo=jsonObj.getString("fwhsPolicyNo");
//                    workPermitExpiryDate=jsonObj.getString("workPermitExpiryDate");
//                    applicationStatus=jsonObj.getString("applicationStatus");
//                    createdTime=jsonObj.getString("createdTime");
//                    lastUpdTime=jsonObj.getString("lastUpdTime");
//                    EmployerCompanyName=jsonObj.getString("compName");
//                    EmployerCompanyRegNo=jsonObj.getString("compRegNo");
//                    EmployerCompanyDirectorName=jsonObj.getString("employerName");
//                    EmployerCompanyAddress=jsonObj.getString("address");
//                    EmployerState=jsonObj.getString("state");
//                    EmployerPhoneNo=jsonObj.getString("telNo");
//                    EmployerFaxNo=jsonObj.getString("faxNo");
//                    EmployerEmail=jsonObj.getString("email");
//                    EmployerContactPerson=jsonObj.getString("contactPerson");
//                    EmployerContactPersonPhoneNo = jsonObj.getString("contactPersonTelNo");
//                    EmployerContactPersonPosition = jsonObj.getString("contactPersonposition");
//
//                    if(applicationStatus!=null && !applicationStatus.equals("null")) {
//                        if(applicationStatus.equals("A")){
//                            imageViewPLKSStatus.setImageResource(R.drawable.ic_check);
//                            if(workPermitExpiryDate!=null && !workPermitExpiryDate.equals("null")){
//                                Date workPermitExpiryDate1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(workPermitExpiryDate);
//                                workPermitExpiryDate = new SimpleDateFormat("dd MMM yyyy").format(workPermitExpiryDate1);
//                                textViewCurrentProgressValue.setText(workPermitExpiryDate);
//                                textViewCurrentProgressValue.setTextColor(Color.GREEN);
//                                textViewCurrentProgressLabel.setText("Work Permit Expiry Date:");
//                            }
//                            textViewFormSubmitDateLabel.setText("");
//                            textViewFormSubmitDateValue.setText("APPROVED");
//                            textViewFormSubmitDateLabel.setVisibility(View.GONE);
//                            textViewFormSubmitDateValue.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
//                            textViewFormSubmitDateValue.setTypeface(null, Typeface.BOLD);
//
//                        }else if (applicationStatus.equals("R")){
//                            imageViewPLKSStatus.setImageResource(R.drawable.ic_reject);
//                            if(lastUpdTime!=null && !lastUpdTime.equals("null")){
//                                Date lastUpdTime1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(lastUpdTime);
//                                lastUpdTime = new SimpleDateFormat("dd MMM yyyy").format(lastUpdTime1);
//                                textViewCurrentProgressValue.setText(lastUpdTime);
//                                textViewCurrentProgressValue.setTextColor(Color.BLACK);
//                                textViewCurrentProgressLabel.setText("Updated Date:");
//                            }
//                            textViewFormSubmitDateLabel.setText("");
//                            textViewFormSubmitDateValue.setText("REJECTED");
//                            textViewFormSubmitDateLabel.setVisibility(View.GONE);
//                            textViewFormSubmitDateValue.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
//                            textViewFormSubmitDateValue.setTypeface(null, Typeface.BOLD);
//                        }else{
//                            imageViewPLKSStatus.setImageResource(R.drawable.ic_waiting_approval);
//                            if(createdTime!=null && !createdTime.equals("null")){
//                                Date createdTime1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(createdTime);
//                                createdTime = new SimpleDateFormat("dd MMM yyyy").format(createdTime1);
//                                textViewFormSubmitDateValue.setText(createdTime);
//                                textViewFormSubmitDateValue.setTextColor(Color.RED);
//                            }
//                            textViewFormSubmitDateLabel.setText("Form Submit Date:");
//                            textViewCurrentProgressLabel.setText("Current Progress:");
//                            textViewCurrentProgressValue.setText("Document Review");
//                        }
//                    }
//                    textViewMyrc.setText(Myrc);
//                    if(withPassport){
//                        textViewWithPassport.setText("YES");
//                        textViewPassportNo.setText(PassportNo);
//                        textViewPassportPlaceOfIssue.setText(PassportPlaceOfIssue);
//                        if(PassportExpiry!=null && !PassportExpiry.equals("null")) {
//                            Date PassportExpiry1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(PassportExpiry);
//                            PassportExpiry = new SimpleDateFormat("dd MMM yyyy").format(PassportExpiry1);
//                            textViewPassportExpiry.setText(PassportExpiry);
//                        }
//                    }else{
//                        textViewWithPassport.setText("NO");
//                        textViewPassportNo.setText("");
//                        textViewPassportPlaceOfIssue.setText("");
//                        textViewPassportExpiry.setText("");
//                    }
//                    textViewJobSector.setText(JobSector);
//                    textViewPosition.setText(Position);
//                    double currentSalary = Salary != null ? Double.parseDouble(Salary) : Double.parseDouble("0");
//                    textViewSalary.setText("RM " + (String.format("%.2f", currentSalary)));
//                    textViewDurationOfPassApplied.setText(DurationOfPassApplied);
//                    if(MedicalReportDate!=null && !MedicalReportDate.equals("null")) {
//                        Date MedicalReportDate1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(MedicalReportDate);
//                        MedicalReportDate = new SimpleDateFormat("dd MMM yyyy").format(MedicalReportDate1);
//                        textViewMedicalReportDate.setText(MedicalReportDate);
//                    }
//                    textViewFWIGProvider.setText(FWIGProvider);
//                    textViewFWIGPolicyNo.setText(FWIGPolicyNo);
//                    textViewFWHSPolicyNo.setText(FWHSPolicyNo);
//                    textViewEmployerCompanyName.setText(EmployerCompanyName);
//                    textViewEmployerCompanyRegNo.setText(EmployerCompanyRegNo);
//                    textViewEmployerCompanyDirectorName.setText(EmployerCompanyDirectorName);
//                    textViewEmployerCompanyAddress.setText(EmployerCompanyAddress);
//                    textViewEmployerState.setText(EmployerState);
//                    textViewEmployerPhoneNo.setText(EmployerPhoneNo);
//                    textViewEmployerFaxNo.setText(EmployerFaxNo);
//                    textViewEmployerEmail.setText(EmployerEmail);
//                    textViewEmployerContactPerson.setText(EmployerContactPerson);
//                    textViewEmployerContactPersonPhoneNo.setText(EmployerContactPersonPhoneNo);
//                    textViewEmployerContactPersonPosition.setText(EmployerContactPersonPosition);

                }catch(Exception e){

                }

            }else{
                PLKSPage.setVisibility(View.GONE);
                textViewNotFound.setVisibility(View.VISIBLE);
            }

        }
    }
}
