package com.pcs.tim.myapplication.new_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.pcs.tim.myapplication.R;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);




        Toolbar toolbar =findViewById(R.id.contactUs_toolbar);
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
    }
}