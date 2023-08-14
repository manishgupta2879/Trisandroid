package com.pcs.tim.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.net.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;

public class Face1v1ResultActivity extends AppCompatActivity {

    String inputType;
    String inputValue;
    Double result;
    String photo;
    ImageView imageViewPhoto;
    ImageView imageViewMyrcPhoto;
    TextView textSimilarity;
    Button buttonViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face1v1_result);
        Intent intent = getIntent();
        result = intent.getDoubleExtra("result", 0);
        inputType = intent.getStringExtra("inputType");
        inputValue = intent.getStringExtra("inputValue");
        photo = intent.getStringExtra("photoPath");

        //bmp = BitmapFactory.decodeFile(photo);

        imageViewPhoto = (ImageView) findViewById(R.id.imageViewOriPhoto);
        imageViewMyrcPhoto = (ImageView)findViewById(R.id.imageViewComparedPhoto);
        textSimilarity = (TextView)findViewById(R.id.photoCompareResult);
        File photoFile = new File(photo);
        Picasso.with(this)
                .load(photoFile)
                .into(imageViewPhoto);
        //imageViewPhoto.setImageBitmap(bmp);
        String resultText = Double.toString(result)+ "% similar";
        textSimilarity.setText(resultText);

        new LoadMyrcPhoto().execute();

        buttonViewProfile = (Button)findViewById(R.id.buttonViewProfile);
        buttonViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VerificationResultActivity.class);
                intent.putExtra("inputType", inputType);
                intent.putExtra("inputValue", inputValue);
                startActivity(intent);
            }
        });
    }

    private class LoadMyrcPhoto extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(Face1v1ResultActivity.this);

        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setMessage(getString(R.string.loadingtype));
            //show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
//                Connection connection = Utilities.getMySqlConnection();
//                Statement statement = connection.createStatement();

//                if(inputType.equals("UNHCR"))
//                    inputType = "UNHCRID";
                String tokenResponse = Utilities.SendTokenPostRequest();
                JSONObject jsonObject = new JSONObject(tokenResponse);
                String token = jsonObject.getString("access_token");
                String tokenType = jsonObject.getString("token_type");

                return Utilities.SendGetRequest(Utilities.REFUGEE + "?inputType=" + inputType + "&value=" + URLEncoder.encode(String.valueOf(inputValue), "UTF-8"), token, tokenType);

               // JSONArray arrayResult = new JSONArray(result);
//                JSONObject objectResult = arrayResult.getJSONObject(0);
//                String query = "select `photo` from `tb_register` where `" + inputType+ "` = '" + inputValue + "'";
//
//                ResultSet result = statement.executeQuery(query);
//
//                if (result.next()) {
//                    myrcPhoto = result.getBytes("photo");
//                } else
//                    myrcPhoto = null;

            } catch (Exception e) {
                e.printStackTrace();
            }
           /* HashMap<String,String> data = new HashMap<>();
            data.put("id",id);
            data.put("password", password);

            return sendPostRequest(LOGIN_URL, data);*/


            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            asyncDialog.dismiss();
            try {
                if (result != null) {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    byte[] base64Encoded = Base64.decodeBase64(jsonObject.getString(Utilities.PHOTO));
                    if (base64Encoded != null) {
                        Bitmap photo = BitmapFactory.decodeByteArray(base64Encoded, 0, base64Encoded.length);
                        imageViewMyrcPhoto.setImageBitmap(photo);
                    }
//                Bitmap photo = BitmapFactory.decodeByteArray(myrcPhoto, 0, myrcPhoto.length);
//                imageViewMyrcPhoto.setImageBitmap(photo);
                } else {
                    buttonViewProfile.setEnabled(false);
                    Toast.makeText(getApplicationContext(), inputType + "not found. Please try again.", Toast.LENGTH_LONG).show();
                }
            }catch (Exception ex){}
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
