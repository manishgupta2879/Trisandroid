package com.pcs.tim.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.commons.net.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchNameActivity extends AppCompatActivity {

    ListView refugeeNameListView;
    ArrayList<ViewRefugee> refugeeArrayList;
    ImageView buttonSearchName;
    EditText editTextName;
    String query;
    ViewRefugeeListAdapter refugeeListAdapter;
    TextView textViewNotFound;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_name);

        Intent intent = getIntent();
        query = intent.getStringExtra("query");

        refugeeArrayList = new ArrayList<>();
        refugeeNameListView = (ListView) findViewById(R.id.listViewRefugeeNames);
        textViewNotFound = (TextView)findViewById(R.id.textViewNotFound);


        refugeeNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewRefugee refugee = refugeeArrayList.get(position);
                String myRC = refugee.getMyRC();
                Intent intent = new Intent(getApplicationContext(), VerificationResultActivity.class);
                intent.putExtra("inputType", "myrc");
                intent.putExtra("inputValue", myRC);
                //intent.putExtra(Utilities.MY_RC, myRC);
                startActivity(intent);
            }
        });

        buttonSearchName = (ImageView)findViewById(R.id.buttonSearchName);
        editTextName = (EditText)findViewById(R.id.editTextName);
        buttonSearchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if (Utilities.hasActiveInternetConnection(getApplicationContext())) {
                    query = editTextName.getText().toString();

                    if (query.length() < 5) {
                        editTextName.setError(getString(R.string.search_name_err));
                        editTextName.requestFocus();
                    } else {
                        textViewNotFound.setVisibility(View.GONE);
                        refugeeArrayList.clear();
                        new SearchRefugeeName().execute();
                    }

               // }
            }
        });
        if(query!=null && !query.isEmpty()){
            editTextName.setText(query);
            textViewNotFound.setVisibility(View.GONE);
            refugeeArrayList.clear();
            new SearchRefugeeName().execute();
        }
    }

    private class SearchRefugee extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(SearchNameActivity.this);

        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setMessage(getString(R.string.loadingtype));
            //show dialog
            asyncDialog.setCancelable(false);
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try{
                String result = DataService.SearchRefugee(query,"","","","");
                if (result != null && result != "ERROR") {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getString("success").equalsIgnoreCase("true")){
                        return jsonObject.getString("data");
                    }else{
                        return "ERROR";
                    }
                }else{
                    return "ERROR";
                }

//                return Utilities.SendGetRequest(Utilities.REFUGEE + "?query=" + URLEncoder.encode(String.valueOf(query), "UTF-8") + "&searchnameonly=false", token, tokenType);
            }catch(Exception e){
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
                if (result != null && result != "ERROR") {
                    asyncDialog.dismiss();

                    List<ViewSearchRefugeeResponse> respRefugee = Arrays.asList(new Gson().fromJson(result, ViewSearchRefugeeResponse[].class));
//                    JSONArray jsonArray = new JSONArray(result);
//
                    for (int i = 0; i < respRefugee.size(); i++) {
                        ViewRefugee vrefugee = new ViewRefugee(respRefugee.get(i).getFullName(),respRefugee.get(i).getMyrc(),respRefugee.get(i).getPhoto(),respRefugee.get(i).getCountryOfOrigin(),respRefugee.get(i).getCategory());
                        // Log.i("photo bytes", new String(base64Encoded));
                        //Log.i("photo", refugeeJSON.getString(Utilities.PHOTO));
                        refugeeArrayList.add(vrefugee);
                    }
//
//                    refugeeListAdapter = new RefugeeListAdapter(getBaseContext(),R.layout.search_list_item,refugeeArrayList);
//                    refugeeNameListView.setAdapter(refugeeListAdapter);
//                    refugeeListAdapter.notifyDataSetChanged();

                }
                else{
                    asyncDialog.dismiss();
                    textViewNotFound.setVisibility(View.VISIBLE);
                }
            }
            catch(Exception ex) {
                asyncDialog.dismiss();

                textViewNotFound.setVisibility(View.VISIBLE);
                ex.printStackTrace();
            }
        }
    }

    private class SearchRefugeeName extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(SearchNameActivity.this);

        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setMessage(getString(R.string.loadingtype));
            //show dialog
            asyncDialog.setCancelable(false);
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try{
                try{
                    String result = DataService.SearchRefugee(query,"","","","");
                    if (result != null && result != "ERROR") {
                        JSONObject jsonObject = new JSONObject(result);
                        if(jsonObject.getString("success").equalsIgnoreCase("true")){
                            return jsonObject.getString("data");
                        }else{
                            return "ERROR";
                        }
                    }else{
                        return "ERROR";
                    }

//                return Utilities.SendGetRequest(Utilities.REFUGEE + "?query=" + URLEncoder.encode(String.valueOf(query), "UTF-8") + "&searchnameonly=false", token, tokenType);
                }catch(Exception e){
                    return null;
                }
            }catch(Exception e){
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
                if (result != null) {
                    asyncDialog.dismiss();
                    JSONArray jsonArray = new JSONArray(result);
                    List<ViewSearchRefugeeResponse> respRefugee = Arrays.asList(new Gson().fromJson(result, ViewSearchRefugeeResponse[].class));
                    for (int i = 0; i < jsonArray.length(); i++) {

                        ViewRefugee vrefugee = new ViewRefugee(respRefugee.get(i).getFullName(),
                                respRefugee.get(i).getMyrc(),respRefugee.get(i).getPhoto(),
                                respRefugee.get(i).getCountryOfOrigin(),respRefugee.get(i).getCategory());
                        refugeeArrayList.add(vrefugee);
                    }

                    refugeeListAdapter = new ViewRefugeeListAdapter(getBaseContext(),R.layout.search_list_item,refugeeArrayList);
                    refugeeNameListView.setAdapter(refugeeListAdapter);
                    refugeeListAdapter.notifyDataSetChanged();

                }
                else{
                    asyncDialog.dismiss();
                    textViewNotFound.setVisibility(View.VISIBLE);
                }
            }
            catch(Exception ex) {
                asyncDialog.dismiss();

                textViewNotFound.setVisibility(View.VISIBLE);
                ex.printStackTrace();
            }
        }
    }
}
