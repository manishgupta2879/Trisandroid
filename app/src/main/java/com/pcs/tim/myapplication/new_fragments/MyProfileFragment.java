package com.pcs.tim.myapplication.new_fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.pcs.tim.myapplication.EditProfileActivity;
import com.pcs.tim.myapplication.ProfileActivity;
import com.pcs.tim.myapplication.R;
import com.pcs.tim.myapplication.Utilities;
import com.pcs.tim.myapplication.ViewRefugee;
import com.pcs.tim.myapplication.ViewRemarksActivity;


public class MyProfileFragment extends Fragment {

    Bitmap enforcementPhoto;
    SharedPreferences sharedPreferences;
    TextView editPersonalDetails, checkHistory, personalDetails, enforcementName;
    ImageView enforcementPhotoView;
    TextInputEditText tvID, tvName, tvStation, tvIC, tvMobile, tvAgency, tvDept, tvState, tvRank;
    String agency, dept, mobile, id, station, name, ic, state, rank;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_profile, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.MY_RC_SHARE_PREF, MODE_PRIVATE);

        enforcementName= view.findViewById(R.id.nameLabel);
        enforcementPhotoView = (ImageView)view.findViewById(R.id.enforcementPhotoView);
        editPersonalDetails = (TextView)view.findViewById(R.id.editDetails);
        checkHistory = (TextView)view.findViewById(R.id.checkHistory);
        tvName = view.findViewById(R.id.textViewName);
        tvAgency =view.findViewById(R.id.textViewAgency);
        tvIC =view.findViewById(R.id.textViewIC);
        tvID = view.findViewById(R.id.textViewPoliceID);
        tvStation = view.findViewById(R.id.textViewStation);
        tvMobile = view.findViewById(R.id.textViewMobileNo);
        tvDept = view.findViewById(R.id.textViewDept);
        tvState =view.findViewById(R.id.textViewState);
        tvRank =view.findViewById(R.id.textViewRank);

        editPersonalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        checkHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewRemarksActivity.class);
                Log.d("policeID", id);
                intent.putExtra(Utilities.POLICE_ID, id);
                startActivity(intent);
            }
        });

        DisplayPersonalDetails();
        new GetPhoto().execute();
        return view;
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
                    Toast.makeText(getActivity(), "Error occurred loading your photo.", Toast.LENGTH_LONG).show();
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}