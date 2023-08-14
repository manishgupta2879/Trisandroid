package com.pcs.tim.myapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SearchMenuActivity extends AppCompatActivity {

    CardView buttonSearchMyRC;
    CardView buttonSearchUNHCR;
    CardView buttonSearchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_menu);

        buttonSearchMyRC = findViewById(R.id.buttonSearchByMyRC);
        buttonSearchName = findViewById(R.id.buttonSearchByName);
        buttonSearchUNHCR = findViewById(R.id.buttonSearchByUNHCR);

        buttonSearchMyRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SearchMyRCActivity.class);
                startActivity(intent);
            }
        });

        buttonSearchUNHCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SearchUNHCRActivity.class);
                startActivity(intent);
            }
        });

        buttonSearchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SearchNameActivity.class);
                startActivity(intent);
            }
        });
    }
}
