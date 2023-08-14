package com.pcs.tim.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewVaccination extends AppCompatActivity {

    private ScrollView scrollPage;
    private LinearLayout vaccinePage;
    private TextView  textViewNotFoundVaccine;
    private TextView textViewMyRC;
    private TextView textViewPhoneNo;
    private TextView textViewStatus;
    private TextView textViewRegisterDate;
    private TextView textViewVaccineType;
    private TextView textViewApplyVaccineLocation;
    private LinearLayout firstDoseScheduleLinearLayout;
    private TextView textViewFirstDoseScheduleTime;
    private LinearLayout firstDoseApplyLinearLayout;
    private TextView textViewFirstDoseApplyTime;
    private LinearLayout secondDoseScheduleLinearLayout;
    private TextView textViewSecondDoseScheduleTime;
    private LinearLayout secondDoseApplyLinearLayout;
    private TextView textViewSecondDoseApplyTime;
    String myrc="";
    String phoneNo="";
    ViewVaccineResponse respVaccine;
    String displayMyRC= "",displayPhoneNo= "",displayStatus= "",displayRegisterDate= "",displayVaccineType= "",displayApplyVaccineLocation= "",
    displayFirstDoseScheduleTime= "",displayFirstDoseApplyTime= "",displaySecondDoseScheduleTime= "",displaySecondDoseApplyTime="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vaccination);

//        scrollPage = (ScrollView)findViewById(R.id.scrollPage);
        vaccinePage = (LinearLayout)findViewById(R.id.vaccinePage);
        textViewNotFoundVaccine = (TextView)findViewById(R.id.textViewNotFoundVaccine);
        textViewMyRC = (TextView)findViewById(R.id.textViewMyRC);
        textViewPhoneNo = (TextView)findViewById(R.id.textViewPhoneNo);
        textViewStatus = (TextView)findViewById(R.id.textViewStatus);
        textViewRegisterDate = (TextView)findViewById(R.id.textViewRegisterDate);
        textViewVaccineType  = (TextView)findViewById(R.id.textViewVaccineType);
        textViewApplyVaccineLocation  = (TextView)findViewById(R.id.textViewApplyVaccineLocation);
        firstDoseScheduleLinearLayout  = (LinearLayout)findViewById(R.id.firstDoseScheduleLinearLayout);
        textViewFirstDoseScheduleTime  = (TextView)findViewById(R.id.textViewFirstDoseScheduleTime);
        firstDoseApplyLinearLayout  = (LinearLayout)findViewById(R.id.firstDoseApplyLinearLayout);
        textViewFirstDoseApplyTime  = (TextView) findViewById(R.id.textViewFirstDoseApplyTime);
        secondDoseScheduleLinearLayout  = (LinearLayout) findViewById(R.id.secondDoseScheduleLinearLayout);
        textViewSecondDoseScheduleTime  = (TextView) findViewById(R.id.textViewSecondDoseScheduleTime);
        secondDoseApplyLinearLayout  = (LinearLayout) findViewById(R.id.secondDoseApplyLinearLayout);
        textViewSecondDoseApplyTime  = (TextView) findViewById(R.id.textViewSecondDoseApplyTime);

        Intent intent = getIntent();
        myrc = intent.getStringExtra("myrc");
        phoneNo = intent.getStringExtra("phoneNo");
        if (myrc != null && !myrc.isEmpty() && !myrc.equals("null") && phoneNo!=null && !phoneNo.isEmpty() && !phoneNo.equals("null")){
            new ViewVaccineData().execute();
        }else{
           // scrollPage.setBackgroundColor(Integer.parseInt("#ffffff"));
            vaccinePage.setVisibility(View.GONE);
            textViewNotFoundVaccine.setVisibility(View.VISIBLE);
        }
    }

    private class ViewVaccineData extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(ViewVaccination.this);

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

                String result = DataService.ViewVaccineData(myrc,phoneNo);

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
            if (result != null && result != "ERROR") {
                try{

                    respVaccine = new Gson().fromJson(result, ViewVaccineResponse.class);

                    textViewMyRC.setText(respVaccine.getMyrc());

                    textViewPhoneNo.setText(respVaccine.getPhoneNo());

                    if(respVaccine.getVaccineRegisterDate()!=null && !respVaccine.getVaccineRegisterDate().equals("null")){
                        Date vaccineregDate1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(respVaccine.getVaccineRegisterDate());
                        displayRegisterDate = new SimpleDateFormat("yyyy-MM-dd h:mm a").format(vaccineregDate1);
                        textViewRegisterDate.setText(displayRegisterDate);
                    }
                    textViewStatus.setText(respVaccine.getStatus());
                    if(respVaccine.getIsApproval() == 0){
                        textViewStatus.setTextColor(Color.RED);
                    }else if (respVaccine.getIsApproval() == 1){
                        textViewStatus.setTextColor(Color.GREEN);
                    }else
                    {
                        textViewStatus.setTextColor(Color.parseColor("#F9AA33"));
                    }

                    textViewVaccineType.setText(respVaccine.getVaccineType());
                    textViewApplyVaccineLocation.setText(respVaccine.getScheduleLocation() + ", " + respVaccine.getScheduleLocationState());

                    if(respVaccine.getFirstDoseScheduleTime()!=null && !respVaccine.getFirstDoseScheduleTime().equals("null")){
                        Date firstdosescheduletime1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(respVaccine.getFirstDoseScheduleTime());
                        displayFirstDoseScheduleTime = new SimpleDateFormat("yyyy-MM-dd h:mm a").format(firstdosescheduletime1);
                        textViewFirstDoseScheduleTime.setText(displayFirstDoseScheduleTime);
                    }

                    if(respVaccine.getIsFirstDoseApply()==1){
                        firstDoseApplyLinearLayout.setVisibility(View.VISIBLE);
                        if(respVaccine.getFirstDoseApplyTime()!=null && !respVaccine.getFirstDoseApplyTime().equals("null")){
                            Date firstdoseapplytime1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(respVaccine.getFirstDoseApplyTime());
                            displayFirstDoseApplyTime = new SimpleDateFormat("yyyy-MM-dd h:mm a").format(firstdoseapplytime1);
                            textViewFirstDoseApplyTime.setText(displayFirstDoseApplyTime);
                        }
                    }else{
                        firstDoseApplyLinearLayout.setVisibility(View.GONE);
                    }

                    if(respVaccine.getSecondDoseScheduleTime()!=null && !respVaccine.getSecondDoseScheduleTime().equals("null")){
                        Date secondosescheduletime1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(respVaccine.getSecondDoseScheduleTime());
                        displaySecondDoseScheduleTime = new SimpleDateFormat("yyyy-MM-dd h:mm a").format(secondosescheduletime1);
                        textViewSecondDoseScheduleTime.setText(displaySecondDoseScheduleTime);
                    }

                    if(respVaccine.getIsSecondDoseApply()==1){
                        secondDoseApplyLinearLayout.setVisibility(View.VISIBLE);
                        if(respVaccine.getSecondDoseApplyTime()!=null && !respVaccine.getSecondDoseApplyTime().equals("null")){
                            Date seconddoseapplytime1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").parse(respVaccine.getSecondDoseApplyTime());
                            displaySecondDoseApplyTime = new SimpleDateFormat("yyyy-MM-dd h:mm a").format(seconddoseapplytime1);
                            textViewSecondDoseApplyTime.setText(displaySecondDoseApplyTime);
                        }
                    }else{
                        secondDoseApplyLinearLayout.setVisibility(View.GONE);
                    }

                }catch(Exception e){
//                    scrollPage.setBackgroundColor(Color.WHITE);
//                        Log.d("ViewVaccination",e.getMessage());
                    vaccinePage.setVisibility(View.GONE);
                    textViewNotFoundVaccine.setVisibility(View.VISIBLE);
                }

            }else{
//                scrollPage.setBackgroundColor(Color.WHITE);
                vaccinePage.setVisibility(View.GONE);
                textViewNotFoundVaccine.setVisibility(View.VISIBLE);
            }

        }
    }
}
