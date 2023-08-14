package com.pcs.tim.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.net.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Face1vManyResultActivity extends AppCompatActivity {

    ListView refugeeListView;
    ArrayList<Refugee> refugeeResultArrayList;
    ArrayList<Refugee> refugeeArrayList;
    FaceRecogResultListAdapter faceRecogResultListAdapter;
    ImageView imageView;
    TextView textViewNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face1v_many_result);

        refugeeArrayList = new ArrayList<>();
        refugeeListView = (ListView)findViewById(R.id.resultList);
        imageView = (ImageView) findViewById(R.id.imageViewPhoto);
        textViewNotFound = (TextView)findViewById(R.id.textViewNotFound);



        Toolbar toolbar =findViewById(R.id.search_refugee_toolbar);
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

        Intent intent = getIntent();

        String photo = intent.getStringExtra("photoPath");
        //Bitmap bmp = BitmapFactory.decodeFile(photo);
        File photoFile = new File(photo);
        Picasso.with(this)
                .load(photoFile)
                .into(imageView);
        //imageView.setImageBitmap(bmp);

        refugeeResultArrayList = (ArrayList<Refugee>) intent.getSerializableExtra("refugee_list");

        Refugee refugee =refugeeResultArrayList.get(0);
        Log.i("refugee", refugee.getMyRC() + Double.toString(refugee.getFaceRecogResult()));
        new RetriveRefugeeList().execute();
        //edit by deepak

   /*     refugeeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Refugee refugee = refugeeResultArrayList.get(position);
                String myRC = refugee.getMyRC();
                Intent intent = new Intent(getApplicationContext(), VerificationResultActivity.class);
                //intent.putExtra(Utilities.MY_RC, myRC);
                intent.putExtra("inputType", "myrc");
                intent.putExtra("inputValue", myRC);
                startActivity(intent);
            }
        });*/

    }

    private class RetriveRefugeeList extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(Face1vManyResultActivity.this);

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
            String dbResult = "fail";
            try {
                String myrc = "";
                String tokenResponse = Utilities.SendTokenPostRequest();
                JSONObject jsonObject = new JSONObject(tokenResponse);
                String token = jsonObject.getString("access_token");
                String tokenType = jsonObject.getString("token_type");

                if(refugeeResultArrayList.size()>1) {
                    for (int i = 0; i < refugeeResultArrayList.size(); i++) {
                        if (i != refugeeResultArrayList.size() - 1) {
                            myrc += "'" + refugeeResultArrayList.get(i).getMyRC() + "', ";
                        } else
                            myrc += "'" + refugeeResultArrayList.get(i).getMyRC() + "'";
                    }
                }
                else{
                    myrc = refugeeResultArrayList.get(0).getMyRC();
                }
                return Utilities.SendGetRequest(Utilities.REFUGEE + "?inputType=myrc" + "&value=" + URLEncoder.encode(String.valueOf(myrc), "UTF-8"), token, tokenType);

//                Connection connection = Utilities.getMySqlConnection();
//                Statement statement = connection.createStatement();

                //String myrc = "";
//                for (int i = 0; i < refugeeResultArrayList.size(); i++) {
//                    if (i != refugeeResultArrayList.size() - 1) {
//                        myrc += "'" + refugeeResultArrayList.get(i).getMyRC() + "', ";
//                    } else
//                        myrc += "'" + refugeeResultArrayList.get(i).getMyRC() + "'";
//
//                    Refugee refugee = refugeeResultArrayList.get(i);
//                    //Log.i("myrc", myrc);
//                    //if (!myrc.equals("")) {
//                        //String query = "Select `countryOfOrigin`,`unhcrid`, `fullName`, `photo` from `tb_register` where `myrc` IN (" + myrc + ")";
//                    String query = "Select `countryOfOrigin`,`unhcrid`, `fullName`, `photo` from `tb_register` where `myrc` = '" + refugee.getMyRC() + "'";
//                    Log.i("query", query);
//                        ResultSet result = statement.executeQuery(query);
//
//                        //int i = 0;
//                        if (result.next()) {
//                            dbResult = "success";
//
//                            refugee.setCountry(result.getString("countryOfOrigin"));
//                            refugee.setName(result.getString("fullName"));
//                            refugee.setPhoto(result.getBytes("photo"));
//                            refugee.setUnhcrId(result.getString("unhcrid"));
//                            Log.i("refugee", refugee.getName() + refugee.getMyRC());
//
//                            refugeeArrayList.add(refugee);
//                        }
//                    //}
//                }
//                    connection.close();
//                } else
//                    return "fail";

            } catch (Exception e) {
                System.err.println("Error");
                return null;
            }
            /*HashMap<String,String> refugeeData = new HashMap<>();
            refugeeData.put("name",name);
            Utilities utilities = new Utilities();
            return utilities.sendPostRequest(urls[0],refugeeData);*/
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                asyncDialog.dismiss();
                if (result != null) {
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject refugeeJSON = jsonArray.getJSONObject(i);
                        for (int j = 0; j < refugeeResultArrayList.size(); j++) {
                            Refugee refugee = refugeeResultArrayList.get(j);
                            if(refugee.getMyRC().equals(refugeeJSON.getString(Utilities.MY_RC))) {
                                byte[] base64Encoded = Base64.decodeBase64(refugeeJSON.getString(Utilities.PHOTO));
                                refugee.setCountry(refugeeJSON.getString(Utilities.COUNTRY));
                                refugee.setName(refugeeJSON.getString(Utilities.FULL_NAME));
                                refugee.setPhoto(base64Encoded);
                                refugee.setCategory(refugeeJSON.getString(Utilities.CATEGORY));
                                refugeeArrayList.add(refugee);
                            }
                            //refugeeResultArrayList.add(refugee);
                        }
                    }

                    faceRecogResultListAdapter = new FaceRecogResultListAdapter(getBaseContext(), R.layout.face_recog_result_list_item, refugeeArrayList);
                    refugeeListView.setAdapter(faceRecogResultListAdapter);
                    faceRecogResultListAdapter.notifyDataSetChanged();

                } else {
                    textViewNotFound.setVisibility(View.VISIBLE);
                }
            } catch (Exception ex) {
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
