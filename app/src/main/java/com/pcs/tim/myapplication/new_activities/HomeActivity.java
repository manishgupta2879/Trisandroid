package com.pcs.tim.myapplication.new_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pcs.tim.myapplication.R;
import com.pcs.tim.myapplication.new_fragments.MoreFragment;
import com.pcs.tim.myapplication.new_fragments.MyProfileFragment;
import com.pcs.tim.myapplication.new_fragments.MyRCFragment;
import com.pcs.tim.myapplication.new_fragments.SearchMenuFragment;

public class HomeActivity extends AppCompatActivity {


    final Fragment rcFragment = new MyRCFragment();
    final Fragment profileFragment = new MyProfileFragment();

    final Fragment moreFragment = new MoreFragment();

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

        Log.d("dddddddd", "onCreate: homeActivity");

        fm.beginTransaction().add(R.id.main_container, moreFragment, "4").hide(moreFragment).commit();
        fm.beginTransaction().add(R.id.main_container, searchMenuFragment, "3").hide(searchMenuFragment).commit();
        fm.beginTransaction().add(R.id.main_container, profileFragment, "2").hide(profileFragment).commit();
        fm.beginTransaction().add(R.id.main_container, rcFragment, "1").commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, rcFragment)
                .commit();

/*
         BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav_home:
                        fm.beginTransaction().hide(active).show(rcFragment).commit();
                        active = rcFragment;
                        return true;

                    case R.id.bottom_nav_profile:
                        fm.beginTransaction().hide(active).show(profileFragment).commit();
                        active = profileFragment;
                        return true;

                    case R.id.bottom_nav_search:
                        fm.beginTransaction().hide(active).show(myRCFragment).commit();
                        active = myRCFragment;
                        return true;


                    case R.id.bottom_nav_more:
                        fm.beginTransaction().hide(active).show(moreFragment).commit();
                        active = moreFragment;
                        return true;
                }
                return false;
            }
        };*/

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
}