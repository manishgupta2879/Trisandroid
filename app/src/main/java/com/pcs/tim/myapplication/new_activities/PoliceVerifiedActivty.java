package com.pcs.tim.myapplication.new_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pcs.tim.myapplication.EditProfileActivity;
import com.pcs.tim.myapplication.R;
import com.pcs.tim.myapplication.new_added_classes.NotificationUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PoliceVerifiedActivty extends AppCompatActivity {
    TextView nametxt;
    ImageView policeImage;
    Button viewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_verified_activty);
        policeImage = findViewById(R.id.imageViewRefugee);
        viewBtn = findViewById(R.id.buttonViewDetails);
        nametxt = findViewById(R.id.textViewRefugeeName);

        Intent intent = getIntent();

        NotificationUtils.showNotification(PoliceVerifiedActivty.this, "VeriMyRc", "You have successfully logged in");


        try {

            String photo = intent.getStringExtra("photoPath");
            String name = intent.getStringExtra("policeName");
            //Bitmap bmp = BitmapFactory.decodeFile(photo);
            File photoFile = new File(photo);
            Picasso.with(this)
                    .load(photoFile)
                    .into(policeImage);

            nametxt.setText(name);

            viewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (Exception e) {
            Log.d("exception__", "onCreate: " + e);
        }


    }
}