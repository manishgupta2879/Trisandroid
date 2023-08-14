package com.pcs.tim.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    Bitmap enforcementPhoto;
    SharedPreferences sharedPreferences;
    TextView editPersonalDetails, checkHistory, personalDetails, enforcementName;
    ImageView enforcementPhotoView;
    TextInputEditText  tvID, tvName, tvStation, tvIC, tvMobile, tvAgency, tvDept, tvState, tvRank;
    String agency, dept, mobile, id, station, name, ic, state, rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences(Utilities.MY_RC_SHARE_PREF, MODE_PRIVATE);

        enforcementName= (TextView)findViewById(R.id.nameLabel);
        enforcementPhotoView = (ImageView)findViewById(R.id.enforcementPhotoView);
        editPersonalDetails = (TextView)findViewById(R.id.editDetails);
        checkHistory = (TextView)findViewById(R.id.checkHistory);
        tvName = findViewById(R.id.textViewName);
        tvAgency =findViewById(R.id.textViewAgency);
        tvIC =findViewById(R.id.textViewIC);
        tvID = findViewById(R.id.textViewPoliceID);
        tvStation = findViewById(R.id.textViewStation);
        tvMobile = findViewById(R.id.textViewMobileNo);
        tvDept = findViewById(R.id.textViewDept);
        tvState =findViewById(R.id.textViewState);
        tvRank =findViewById(R.id.textViewRank);

        editPersonalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        checkHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewRemarksActivity.class);
                Log.d("policeID", id);
                intent.putExtra(Utilities.POLICE_ID, id);
                startActivity(intent);
            }
        });

        DisplayPersonalDetails();
        new GetPhoto().execute();
    }

    @Override
    protected void onResume(){
        super.onResume();
        DisplayPersonalDetails();
        new GetPhoto().execute();
    }
    private void DisplayPersonalDetails(){
        agency = sharedPreferences.getString(Utilities.LOGIN_POLICE_AGENCY,"");
        name = sharedPreferences.getString(Utilities.LOGIN_POLICE_NAME,"");
        dept = sharedPreferences.getString(Utilities.LOGIN_POLICE_DEPT,"");
        mobile = sharedPreferences.getString(Utilities.LOGIN_POLICE_MOBILE,"");
        id = sharedPreferences.getString(Utilities.LOGIN_POLICE_ID,"");
        station = sharedPreferences.getString(Utilities.LOGIN_POLICE_STATION,"");
        ic = sharedPreferences.getString(Utilities.LOGIN_POLICE_IC,"");
        state = sharedPreferences.getString(Utilities.LOGIN_POLICE_STATE,"");
        rank = sharedPreferences.getString(Utilities.LOGIN_POLICE_RANK,"");

        enforcementName.setText(name);
        tvName.setText(name);
        tvAgency.setText(agency);
        tvIC.setText(ic);
        tvID.setText(id);
        tvStation.setText(station);
        tvMobile.setText(mobile);
        tvDept.setText(dept);
        tvState.setText(state);
        tvRank.setText(rank);
    }

    private class GetPhoto extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            Log.d("photo",sharedPreferences.getString(Utilities.LOGIN_POLICE_PHOTO,""));
            try {
                enforcementPhoto = Utilities.getBitmapFromURL(sharedPreferences.getString(Utilities.LOGIN_POLICE_PHOTO,""));

                if(enforcementPhoto!=null)
                    return "OK";
                return null;
            }catch(Exception e){

                return null;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                if (result.equals("OK")) {
                    if(enforcementPhoto != null)
                        enforcementPhotoView.setImageBitmap(enforcementPhoto);
                }

                else {
                    Toast.makeText(getApplicationContext(), "Error occurred loading your photo.", Toast.LENGTH_LONG).show();
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
