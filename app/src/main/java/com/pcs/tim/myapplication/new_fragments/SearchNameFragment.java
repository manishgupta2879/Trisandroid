package com.pcs.tim.myapplication.new_fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pcs.tim.myapplication.DataService;
import com.pcs.tim.myapplication.R;
import com.pcs.tim.myapplication.SearchNameActivity;
import com.pcs.tim.myapplication.VerificationResultActivity;
import com.pcs.tim.myapplication.ViewRefugee;
import com.pcs.tim.myapplication.ViewRefugeeListAdapter;
import com.pcs.tim.myapplication.ViewSearchRefugeeResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SearchNameFragment extends Fragment {
    ListView refugeeNameListView;
    ArrayList<ViewRefugee> refugeeArrayList;
    ImageView buttonSearchName;
    EditText editTextName;
    String query;
    SearchView searchView;
    ViewRefugeeListAdapter refugeeListAdapter;
    TextView textViewNotFound;

    public SearchNameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.activity_search_name, container, false);


        Intent intent = getActivity().getIntent();
        query = intent.getStringExtra("query");

        refugeeArrayList = new ArrayList<>();
        refugeeNameListView = (ListView) view.findViewById(R.id.listViewRefugeeNames);
        textViewNotFound = (TextView)view.findViewById(R.id.textViewNotFound);

         searchView=view.findViewById(R.id.svName);

        Toolbar toolbar = view.findViewById(R.id.searchMyName_toolbar);
        //   toolbar.setVisibility(View.VISIBLE);// Assuming you have a Toolbar in your layout
        if (toolbar != null) {

            toolbar.setNavigationIcon(R.drawable.ic_back); // Set your back button icon
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle back button click here (e.g., pop the Fragment from the back stack)
                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }
                }
            });
        }
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
            }
        });
        refugeeNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewRefugee refugee = refugeeArrayList.get(position);
                String myRC = refugee.getMyRC();
                Intent intent = new Intent(getActivity(), VerificationResultActivity.class);
                intent.putExtra("inputType", "myrc");
                intent.putExtra("inputValue", myRC);
                //intent.putExtra(Utilities.MY_RC, myRC);
                startActivity(intent);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

               query = s;//editTextName.getText().toString();

                if (query.length() < 5) {
                   // editTextName.setError(getString(R.string.search_name_err));
                    searchView.requestFocus();
                    Toast.makeText(getActivity(), R.string.search_name_err, Toast.LENGTH_SHORT).show();
                   // searchView.set
                } else {
                    textViewNotFound.setVisibility(View.GONE);
                    refugeeArrayList.clear();
                    new SearchRefugeeName().execute();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        buttonSearchName = (ImageView)view.findViewById(R.id.buttonSearchName);
        editTextName = (EditText)view.findViewById(R.id.editTextName);
        buttonSearchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (Utilities.hasActiveInternetConnection(getActivity())) {
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
        
        return view;
    }



    private class SearchRefugee extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(getActivity());

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
        ProgressDialog asyncDialog = new ProgressDialog(getActivity());

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

                    refugeeListAdapter = new ViewRefugeeListAdapter(getActivity(),R.layout.search_list_item,refugeeArrayList);
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