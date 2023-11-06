package com.pcs.tim.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pcs.tim.myapplication.new_activities.RefugeeLiveLocationActivity;
import com.pcs.tim.myapplication.new_added_classes.NewCheckHistoryCurrentLoactionPoliceAdapter;
import com.pcs.tim.myapplication.new_added_classes.NewCheckHistoryCurrentLocationRefugee;
import com.pcs.tim.myapplication.new_added_classes.NewCheckHistoryRecentPoliceAdapter;
import com.pcs.tim.myapplication.new_added_classes.NewCheckHistoryRecentRefugeeAdapter;
import com.pcs.tim.myapplication.new_added_classes.NewRefugeeRemarkListAdapter;
import com.pcs.tim.myapplication.new_added_classes.NewRemarkListAdapter;
import com.pcs.tim.myapplication.new_fragments.MyProfileFragment;

import org.apache.commons.net.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewRemarksActivity extends AppCompatActivity {

    //NewRemarkListAdapter remarkListAdapter;

    NewCheckHistoryCurrentLoactionPoliceAdapter currentRemarkPoliceAdapter;
    NewCheckHistoryRecentRefugeeAdapter recentRefugeeAdapter;
    NewCheckHistoryCurrentLocationRefugee currentRefugeeAdapter;
    NewCheckHistoryRecentPoliceAdapter recentRemarkPoliceAdapter;
    NewRefugeeRemarkListAdapter refugeeRemarkListAdapter;
    ArrayList<Remark> remarkArrayList;
    ArrayList<Remark> remarkArrayListfirst;
    ArrayList<Remark> refugeeRemarkArrayList;
    ArrayList<Remark> refugeeRemarkArrayListFirst;
    String myRc, enforcementId, photoUrl,loginId;
    RecyclerView currentRecycler, recentRecycler;
    CardView refugeeLay, policeLay;
    ScrollView scrollView;
    SharedPreferences sharedPreferences;
    TextView textViewNotFound, policeNametxt, enforcementIdtxt, refugeeNametxt, refugeeMyRcIdtxt,getLiveLocation;
    ImageView imgRefugee;
    Bitmap enforcementPhoto;
    LinearLayout currentLay,recentLay;
    ImageView imgpolice;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_check_history_police);
        Intent intent = getIntent();
        sharedPreferences = getSharedPreferences(Utilities.MY_RC_SHARE_PREF, MODE_PRIVATE);
        myRc = intent.getStringExtra(Utilities.MY_RC);
        enforcementId = intent.getStringExtra(Utilities.POLICE_ID);
        photoUrl = intent.getStringExtra(Utilities.PHOTO);
        loginId= sharedPreferences.getString(Utilities.LOGIN_ID,"");

        Log.d("Remark query", myRc + enforcementId);


        Toolbar toolbar = findViewById(R.id.checkHistory_toolbar);
        //   toolbar.setVisibility(View.VISIBLE);// Assuming you have a Toolbar in your layout
        if (toolbar != null) {

            toolbar.setNavigationIcon(R.drawable.ic_back); // Set your back button icon
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle back button click here (e.g., pop the Fragment from the back stack)
                    onBackPressed();
                }
            });
        }

        remarkArrayList = new ArrayList<>();
        remarkArrayListfirst = new ArrayList<>();
        refugeeRemarkArrayList = new ArrayList<>();
        refugeeRemarkArrayListFirst = new ArrayList<>();
        currentRecycler = findViewById(R.id.currentRecycler);
        recentRecycler = findViewById(R.id.recentRecycler);
        policeNametxt = findViewById(R.id.fullNamePoliceTxt);
        enforcementIdtxt = findViewById(R.id.enforcementIdTxt);
        refugeeMyRcIdtxt = findViewById(R.id.refugeeMyRcIdtxt);
        refugeeNametxt = findViewById(R.id.refugeeNametxt);
        imgpolice=findViewById(R.id.imgpolice);
        policeLay = findViewById(R.id.policeLay);
        refugeeLay = findViewById(R.id.refugeeLay);
        scrollView = findViewById(R.id.scroll);
        imgRefugee = findViewById(R.id.imgRefugee);
        currentLay = findViewById(R.id.currentLay);
        recentLay = findViewById(R.id.recentLay);
        getLiveLocation = findViewById(R.id.getLiveLocation);


        currentRecycler.setLayoutManager(new LinearLayoutManager(this));
        recentRecycler.setLayoutManager(new LinearLayoutManager(this));
        textViewNotFound = (TextView) findViewById(R.id.textViewNotFound);



       // Log.d("police_id__", "onClick: "+remark.getPoliceId());


        getLiveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(ViewRemarksActivity.this, RefugeeLiveLocationActivity.class);
                intent1.putExtra("lat",refugeeRemarkArrayListFirst.get(0).getLat());
                intent1.putExtra("lng",refugeeRemarkArrayListFirst.get(0).getLng());
                intent1.putExtra("regId",refugeeRemarkArrayListFirst.get(0).getRegId());
                startActivity(intent1);
            }
        });


        new GetPhoto().execute();


        textViewNotFound.setVisibility(View.GONE);
        remarkArrayList.clear();
        remarkArrayListfirst.clear();
        refugeeRemarkArrayList.clear();
        refugeeRemarkArrayListFirst.clear();
        new RetrieveRemarks().execute();
        if (myRc != null && !myRc.isEmpty()) {

        }else {
            new RetrieveRemarksNew().execute();
        }

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

                        imgpolice.setImageBitmap(enforcementPhoto);
                }

                else {
                    Toast.makeText(getBaseContext(), "Error occurred loading your photo.", Toast.LENGTH_LONG).show();
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
private  class RetrieveRemarksNew extends  AsyncTask<String,Void,String>{
    ProgressDialog asyncDialog = new ProgressDialog(ViewRemarksActivity.this);

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

        String result="";
        try{


             if (enforcementId != null && !enforcementId.isEmpty()) {


                Log.d("accesss___1", "doInBackground: enforcementId "+enforcementId);
                result = DataService.GetEnforcementTrackLogNew(enforcementId);

                Log.d("accesss___1", "doInBackground: result "+result);
                if (result != null && result != "ERROR") {

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        return jsonObject.getString("data");
                    } else {
                        Log.d("scrollView___", "doInBackground: 1");
                      //  scrollView.setVisibility(View.GONE);
                        recentLay.setVisibility(View.GONE);
                        return "ERROR";

                    }
                } else {
                    recentLay.setVisibility(View.GONE);
                    //scrollView.setVisibility(View.GONE);
                    Log.d("scrollView___", "doInBackground: 2");

                    return "ERROR";
                }
            }
            return null;
        }catch(Exception e) {
            recentLay.setVisibility(View.GONE);

            //  scrollView.setVisibility(View.GONE);
            Log.d("scrollView___", "doInBackground: 3");
            Log.d("scrollView___", e.toString());

            return null;
        }




    }

    @Override
    protected void onPostExecute(String result) {
        try{
            asyncDialog.dismiss();
            scrollView.setVisibility(View.VISIBLE);
            recentLay.setVisibility(View.VISIBLE);
            if (result != null && result != "ERROR") {
                 if (enforcementId != null && !enforcementId.isEmpty()) {

                     EnforcementTrackLogResponse[] enforcementTrack = new Gson().fromJson(result, EnforcementTrackLogResponse[].class);
                     List<EnforcementTrackLogResponse> etlrList = new ArrayList<>(Arrays.asList(enforcementTrack));
                     for (int i = 0; i < etlrList.size(); i++) {
                         Remark remark = new Remark(String.valueOf(etlrList.get(i).getEnforcementId()), etlrList.get(i).getRemark(),
                                 etlrList.get(i).getMyrc(), etlrList.get(i).getLocation(), etlrList.get(i).getCreatedTime(),
                                 etlrList.get(i).getFullName(), etlrList.get(i).getPhotoURL(), etlrList.get(i).getCountryOfOrigin(),
                                 etlrList.get(i).getCardExpiredDate(), etlrList.get(i).getCardStatus(),Float.parseFloat(String.valueOf(etlrList.get(i).getLat())),Float.parseFloat(String.valueOf(etlrList.get(i).getLng())),etlrList.get(i).getRegId());
                         remarkArrayList.add(remark);
                     }

                     if (myRc != null && !myRc.isEmpty() && refugeeRemarkArrayList.size() > 0) {}
                     else{
                         recentRemarkPoliceAdapter = new NewCheckHistoryRecentPoliceAdapter(getBaseContext(), remarkArrayList);
                         recentRecycler.setAdapter(recentRemarkPoliceAdapter);
                         recentRemarkPoliceAdapter.notifyDataSetChanged();
                     }
                 }

            }else {
                asyncDialog.dismiss();
               // textViewNotFound.setVisibility(View.VISIBLE);
                Log.d("scrollView___", "doInBackground: 4");
                recentLay.setVisibility(View.GONE);

              //  scrollView.setVisibility(View.GONE);
            }
        }
        catch (Exception ex) {
            Log.d("error__", "onPostExecute: "+ex);
            Log.d("scrollView___", "doInBackground: 5");

            ex.printStackTrace();
          //  textViewNotFound.setVisibility(View.VISIBLE);
            recentLay.setVisibility(View.GONE);
           // scrollView.setVisibility(View.GONE);
        }

    }
}
    private class RetrieveRemarks extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(ViewRemarksActivity.this);

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
            String result = "";
            try {

                if (myRc != null && !myRc.isEmpty()) {
                    recentLay.setVisibility(View.VISIBLE);
                    result = DataService.GetMyRCTrackLog(myRc);
                    Log.d("accesss___1", "doInBackground: myRc");

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
                } else if (enforcementId != null && !enforcementId.isEmpty()) {
                    Log.d("accesss___1", "doInBackground: enforcementId "+enforcementId);
                    result = DataService.GetEnforcementTrackLog(enforcementId);

                    Log.d("accesss___1", "doInBackground: result "+result);
                    if (result != null && result != "ERROR") {

                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                            return jsonObject.getString("data");
                        } else {
                            Log.d("scrollView___", "doInBackground: 6");

                            scrollView.setVisibility(View.GONE);
                            return "ERROR";

                        }
                    } else {
                        scrollView.setVisibility(View.GONE);
                        Log.d("scrollView___", "doInBackground: 7");

                        return "ERROR";
                    }
                }
                return null;

            } catch (Exception e) {
                Log.d("scrollView___", "doInBackground: 8");

                scrollView.setVisibility(View.GONE);
                return null;
            }


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                asyncDialog.dismiss();
              //  textViewNotFound.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                Log.d("resultRemark", result);
                if (result != null && result != "ERROR") {
                    if (myRc != null && !myRc.isEmpty()) {
                        JSONObject jsonObject = new JSONObject(result);

                        PartialViewTrackLogs[] trackLogModel = new Gson().fromJson(jsonObject.getString("trackLogs"), PartialViewTrackLogs[].class);
                        List<PartialViewTrackLogs> tlmList = new ArrayList<>(Arrays.asList(trackLogModel));

                        Log.d("ArraySize__", "onPostExecute: "+tlmList.size());

                        for (int i = 0; i < tlmList.size(); i++) {
                            if (tlmList.get(i).getTrackType().equals("M")) {
                                Log.d("ArraySize__", "onPostExecute: "+refugeeRemarkArrayList.size());
                                Remark remark = new Remark(String.valueOf(tlmList.get(i).getEnforcementId()), "MyRegistered Check-in",
                                        jsonObject.getString("myrc"), tlmList.get(i).getLocation(), tlmList.get(i).getCreatedTime(),
                                        jsonObject.getString("fullName"), jsonObject.getString("photoURL"), jsonObject.getString("countryOfOrigin"),
                                        jsonObject.getString("cardExpiredDate"), jsonObject.getString("cardStatus"),tlmList.get(i).getLat(),tlmList.get(i).getLng(),tlmList.get(i).getRegId());
                                refugeeRemarkArrayList.add(remark);


                            }
                            Log.d("ArraySize__", "onPostExecute: "+refugeeRemarkArrayList.size());
                        }

                        refugeeRemarkArrayListFirst.add(refugeeRemarkArrayList.get(0));
                        refugeeRemarkArrayList.remove(0);

                        for (int i = 0; i < tlmList.size(); i++) {


                            if (tlmList.get(i).getTrackType().equals("M") ) {
                               /* Remark remark = new Remark(String.valueOf(tlmList.get(i).getEnforcementId()), "MyRegistered Check-in",
                                        jsonObject.getString("myrc"), tlmList.get(i).getLocation(), tlmList.get(i).getCreatedTime(),
                                        jsonObject.getString("fullName"), jsonObject.getString("photoURL"), jsonObject.getString("countryOfOrigin"),
                                        jsonObject.getString("cardExpiredDate"), jsonObject.getString("cardStatus"),tlmList.get(i).getLat(),tlmList.get(i).getLng());
                                refugeeRemarkArrayListFirst.add(remark);*/
                                refugeeLay.setVisibility(View.VISIBLE);
                                getLiveLocation.setVisibility(View.VISIBLE);

                                Log.d("abc___", "onPostExecute: " + myRc);
                                String enforcementId = myRc;
                                byte[] base64Encoded = Base64.decodeBase64(photoUrl);
                                if (base64Encoded != null) {
                                    Bitmap photo = BitmapFactory.decodeByteArray(base64Encoded, 0, base64Encoded.length);
                                    imgRefugee.setImageBitmap(photo);
                                }

                                // Create a SpannableStringBuilder
                                SpannableStringBuilder spannable = new SpannableStringBuilder("MyRC " + enforcementId);

                                ForegroundColorSpan blueColorSpan = new ForegroundColorSpan(ContextCompat.getColor(getBaseContext(), R.color.themeColor));
                                spannable.setSpan(blueColorSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                refugeeMyRcIdtxt.setText(spannable);
                                refugeeNametxt.setText(jsonObject.getString("fullName"));
                                break;
                            }

                        }


                    }
                    else if (enforcementId != null && !enforcementId.isEmpty()) {
                        EnforcementTrackLogResponse[] enforcementTrack = new Gson().fromJson(result, EnforcementTrackLogResponse[].class);
                        List<EnforcementTrackLogResponse> etlrList = new ArrayList<>(Arrays.asList(enforcementTrack));
                       /* for (int i = 1; i < etlrList.size(); i++) {
                            Remark remark = new Remark(String.valueOf(etlrList.get(i).getEnforcementId()), etlrList.get(i).getRemark(),
                                    etlrList.get(i).getMyrc(), etlrList.get(i).getLocation(), etlrList.get(i).getCreatedTime(),
                                    etlrList.get(i).getFullName(), etlrList.get(i).getPhotoURL(), etlrList.get(i).getCountryOfOrigin(),
                                    etlrList.get(i).getCardExpiredDate(), etlrList.get(i).getCardStatus(),Float.parseFloat(String.valueOf(etlrList.get(i).getLat())),Float.parseFloat(String.valueOf(etlrList.get(i).getLng())));
                            remarkArrayList.add(remark);
                        }*/

                        Remark remark = new Remark(String.valueOf(etlrList.get(0).getEnforcementId()), etlrList.get(0).getRemark(),
                                etlrList.get(0).getMyrc(), etlrList.get(0).getLocation(), etlrList.get(0).getCreatedTime(),
                                etlrList.get(0).getFullName(), etlrList.get(0).getPhotoURL(), etlrList.get(0).getCountryOfOrigin(),
                                etlrList.get(0).getCardExpiredDate(), etlrList.get(0).getCardStatus(),Float.parseFloat(String.valueOf(etlrList.get(0).getLat())),Float.parseFloat(String.valueOf(etlrList.get(0).getLng())),etlrList.get(0).getRegId());
                        remarkArrayListfirst.add(remark);

                        policeLay.setVisibility(View.VISIBLE);

                        policeNametxt.setText(sharedPreferences.getString(Utilities.LOGIN_POLICE_NAME,"null"));


                        SpannableStringBuilder spannable = new SpannableStringBuilder("Enforcement ID " + enforcementId);

                        ForegroundColorSpan blueColorSpan = new ForegroundColorSpan(ContextCompat.getColor(getBaseContext(), R.color.themeColor));
                        spannable.setSpan(blueColorSpan, 0, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                        enforcementIdtxt.setText(" "+enforcementId);

                    }
                    if (myRc != null && !myRc.isEmpty() && refugeeRemarkArrayList.size() > 0) {
                        //  refugeeRemarkListAdapter = new RefugeeRemarkListAdapter(getBaseContext(),R.layout.new_refugee_remark_list_item,refugeeRemarkArrayList);
                        recentRefugeeAdapter = new NewCheckHistoryRecentRefugeeAdapter(refugeeRemarkArrayList, getBaseContext());

                        currentRefugeeAdapter = new NewCheckHistoryCurrentLocationRefugee(refugeeRemarkArrayListFirst, ViewRemarksActivity.this);
                        recentRecycler.setAdapter(recentRefugeeAdapter);
                        currentRecycler.setAdapter(currentRefugeeAdapter);
                        Log.d("dddddd", "onPostExecute: " + refugeeRemarkArrayList.size());
                    } else {
                        Log.d("dddddd", "onPostExecute: ");
                        //edit by deepak
                        //  remarkListAdapter = new RemarkListAdapter(getBaseContext(),R.layout.remark_list_item,remarkArrayList);
                        //  remarkListAdapter = new NewRemarkListAdapter(remarkArrayList,getBaseContext());
               //         recentRemarkPoliceAdapter = new NewCheckHistoryRecentPoliceAdapter(getBaseContext(), remarkArrayList);
                        currentRemarkPoliceAdapter = new NewCheckHistoryCurrentLoactionPoliceAdapter(getBaseContext(), remarkArrayListfirst);
                        currentRecycler.setAdapter(currentRemarkPoliceAdapter);
         //               recentRecycler.setAdapter(recentRemarkPoliceAdapter);
                        currentRemarkPoliceAdapter.notifyDataSetChanged();
      //                  recentRemarkPoliceAdapter.notifyDataSetChanged();

                    }



                } else {
                    asyncDialog.dismiss();
                    textViewNotFound.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                    Log.d("scrollView___", "doInBackground: 9");

                }
            } catch (Exception ex) {
                Log.d("error__", "onPostExecute: "+ex);
                ex.printStackTrace();
                textViewNotFound.setVisibility(View.VISIBLE);
                Log.d("scrollView___", "doInBackground: 10");

                scrollView.setVisibility(View.GONE);
            }
        }
    }
}
