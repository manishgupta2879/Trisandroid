package com.pcs.tim.myapplication.new_activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pcs.tim.myapplication.DataService;
import com.pcs.tim.myapplication.R;
import com.pcs.tim.myapplication.Utilities;
import com.pcs.tim.myapplication.new_fragments.MoreFragment;
import com.pcs.tim.myapplication.new_fragments.MyProfileFragment;
import com.pcs.tim.myapplication.new_fragments.MyRCFragment;
import com.pcs.tim.myapplication.new_fragments.SearchMenuFragment;

public class HomeActivity extends AppCompatActivity {


    final Fragment rcFragment = new MyRCFragment();
    final Fragment profileFragment = new MyProfileFragment();
    private SharedPreferences sharePref;
    final Fragment moreFragment = new MoreFragment();
    String token;

    String[] permission = new String[]{
            Manifest.permission.POST_NOTIFICATIONS
    };

    boolean permission_post_notification = false;
    final Fragment searchMenuFragment = new SearchMenuFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = rcFragment;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        myToolbar.setTitle(" ");
        myToolbar.setVisibility(View.GONE);


        setSupportActionBar(myToolbar);
        sharePref = getSharedPreferences(Utilities.MY_RC_SHARE_PREF, MODE_PRIVATE);
        Log.d("dddddddd", "onCreate: homeActivity");

        token = FirebaseInstanceId.getInstance().getToken();

        if(token!=null){
            Log.d("fcmtoken__1", "onCreate: "+token);
        }else {
            Log.d("fcmtoken__1", "onCreate: cant get token");
        }

        if (!permission_post_notification) {
            requestPermissionsNotification();
        } else {

        }

        new sendFcmToken().execute(Utilities.LOGIN);

        fm.beginTransaction().add(R.id.main_container, moreFragment, "4").hide(moreFragment).commit();
        fm.beginTransaction().add(R.id.main_container, searchMenuFragment, "3").hide(searchMenuFragment).commit();
        fm.beginTransaction().add(R.id.main_container, profileFragment, "2").hide(profileFragment).commit();
        fm.beginTransaction().add(R.id.main_container, rcFragment, "1").commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, rcFragment)
                .commit();


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottom_nav_home:
                               /*  fm.beginTransaction().hide(active).show(rcFragment).commit();
                                 active = rcFragment;*/

                                Log.d("BottomNav____", "onNavigationItemSelected: bottom_nav_home");
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_container, rcFragment)
                                        .commit();
                                return true;

                            case R.id.bottom_nav_profile:
                               /*  fm.beginTransaction().hide(active).show(profileFragment).commit();
                                 active = profileFragment;*/
                                Log.d("BottomNav____", "onNavigationItemSelected: bottom_nav_profile");

                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_container, profileFragment)
                                        .commit();
                                return true;

                            case R.id.bottom_nav_search:
                                /* fm.beginTransaction().hide(active).show(searchMenuFragment).commit();
                                 active = searchMenuFragment;*/
                                Log.d("BottomNav____", "onNavigationItemSelected: bottom_nav_search");

                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_container, searchMenuFragment)
                                        .commit();
                                return true;


                            case R.id.bottom_nav_more:
                                 /*fm.beginTransaction().hide(active).show(moreFragment).commit();
                                 active = moreFragment;*/
                                Log.d("BottomNav____", "onNavigationItemSelected: bottom_nav_more");

                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_container, moreFragment)
                                        .commit();
                                return true;
                        }
                        return false;
                    }
                }
        );

    }

    public void requestPermissionsNotification() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, permission[0]) == PackageManager.PERMISSION_GRANTED) {

            permission_post_notification = true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

                } else {

                }
                requestPermissionLauncherNotification.launch(permission[0]);
            }
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncherNotification =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if(isGranted){
            permission_post_notification=true;
        }else{
            permission_post_notification=false;
            showPermissionDialog("Notification Permission");
        }
    });

    private void showPermissionDialog(String notification_permission) {

        Intent rIntent=new Intent();
        rIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri=Uri.fromParts("package",getPackageName(),null);
        rIntent.setData(uri);
        startActivity(rIntent);

    }

    private class sendFcmToken extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(HomeActivity.this);
        boolean successFlag = false;



     /*   @Override
        protected void onPreExecute() {
            asyncDialog.setMessage(getString(R.string.loadingtype));
            //show dialog
            asyncDialog.show();
            super.onPreExecute();
        }*/

        @Override
        protected String doInBackground(String... strings) {

            try {
                //    Log.d("fcmToken___doinBack", "doInBackground: "+id);
                String id = sharePref.getString(Utilities.LOGIN_ID, "");
                Log.d("fcmToken___doinBack", "doInBackground: " + id);
                String result = DataService.sendFcmToken(Integer.parseInt(id), token);
                Log.d("fcmToken___doinBack", "doInBackground: token  " + token);

                return "Success";
          /*      if (result != null && result != "ERROR") {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        successFlag = true;
                        return jsonObject.getString("data");
                    } else {
                        return jsonObject.getString("message");
                    }
                } else {
                    return "ERROR";
                }*/
            } catch (Exception e) {
                Log.d("fcmToken___error", "error: " + e.getMessage());
                e.printStackTrace();
                return null;
            }

        }


        @Override
        protected void onPostExecute(String s) {
            //show dialog

            super.onPostExecute(s);
        }

    }


}