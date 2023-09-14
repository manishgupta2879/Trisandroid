package com.pcs.tim.myapplication;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRemarksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRemarksFragment extends DialogFragment {
    private static final String MY_RC = "my_rc";
    private static final String LOG_ID = "log_id";

    SharedPreferences sharedPreferences;
    private float lat;
    private float lng;
    private String policeId;
    private String myRc;
    private String registerID;
    private int log_id;
    private String remark;
    private String address;

    private String mAddressOutput;
    protected Location mLastLocation;
   // private AddressResultReceiver mResultReceiver;

    EditText editTextRemark;
    EditText editTextLocation;
    Button buttonAddRemark;

    public AddRemarksFragment() {
    }

    public static AddRemarksFragment newInstance(String myRc, int log_id, String regID) {
        AddRemarksFragment fragment = new AddRemarksFragment();
        Bundle args = new Bundle();
        args.putString(MY_RC, myRc);
        args.putString("reg_ID", regID);
        args.putInt(LOG_ID,log_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            myRc = getArguments().getString(MY_RC);
            registerID = getArguments().getString("reg_ID");
            log_id = getArguments().getInt(LOG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_remarks, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        buttonAddRemark = (Button)view.findViewById(R.id.btnSubmitRemark);
        editTextRemark = (EditText)view.findViewById(R.id.editTextRemark);
        editTextLocation = (EditText)view.findViewById(R.id.editTextCurrentLocation);
        editTextLocation.setEnabled(false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.MY_RC_SHARE_PREF, Context.MODE_PRIVATE);
        mAddressOutput = sharedPreferences.getString("locationAddress", null);
        policeId = sharedPreferences.getString(Utilities.LOGIN_POLICE_ID, null);
        lat = sharedPreferences.getFloat("lat",0);
        lng = sharedPreferences.getFloat("lng",0);

        if( mAddressOutput == null )
            editTextLocation.setEnabled(true);
        else
            editTextLocation.setText(mAddressOutput);
        buttonAddRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextRemark.setError(null);
                remark = editTextRemark.getText().toString();
                address = editTextLocation.getText().toString();

                if(remark.trim().isEmpty()) {
                    editTextRemark.setError(getString(R.string.err_field_required));
                    editTextRemark.requestFocus();
                }
                else
                if(address.trim().isEmpty()){
                    editTextLocation.setError(getString(R.string.err_field_required));
                    editTextLocation.requestFocus();
                }
                else
                    new InsertLog().execute();
            }
        });

       // mResultReceiver = new AddressResultReceiver(new Handler());
       // FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        /*if(getActivity().checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(),Process.myUid()) == PackageManager.PERMISSION_GRANTED) {

                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                mLastLocation = location;
//                                Log.i("Location", location.toString());
                                if (mLastLocation == null) {
                                    editTextLocation.setEnabled(true);
                                    lat = 0;
                                    lng = 0;
                                    return;
                                } else {
                                    lat = (float) location.getLatitude();
                                    lng = (float) location.getLongitude();

                                }

                                if (!Geocoder.isPresent()) {
                                    Toast.makeText(getContext(),
                                            R.string.no_geocoder_available,
                                            Toast.LENGTH_LONG).show();
                                    return;
                                }

                                // Start service and update UI to reflect new location
                                //Utilities utilities = new Utilities();
                                Utilities.startIntentService(getContext(), mResultReceiver, mLastLocation);
                            }
                        });


        }
        else{
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            Log.i("Location", "not permitted");
        }
*/
    }

    public  void onStart(){
        super.onStart();
    }
    public void onResume()
    {
        super.onResume();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Window window = getDialog().getWindow();
        window.setLayout((int)(width * 0.8),(int)(height * 0.55));
        window.setGravity(Gravity.CENTER);
    }

    /*protected void startIntentService() {
        Intent intent = new Intent(getContext(), FetchAddressIntentService.class);
        intent.putExtra(Utilities.RECEIVER, mResultReceiver);
        intent.putExtra(Utilities.LOCATION_DATA_EXTRA, mLastLocation);
        getActivity().startService(intent);
    }*/

    /*private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            if(resultData != null) {

                mAddressOutput = resultData.getString(Utilities.RESULT_DATA_KEY);
                buttonAddRemark.setVisibility(View.VISIBLE);

                if(!mAddressOutput.equals(getString(R.string.service_not_available)) && !mAddressOutput.equals(""))
                    editTextLocation.setText(mAddressOutput);
                else{
                    new GetAddress().execute("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + Float.toString(lat) + "," + Float.toString(lng));
                }
            }else
                editTextLocation.setEnabled(true);
        }
    }
*/
    private void displayAddressOutput(){
        editTextLocation.setVisibility(View.VISIBLE);
        editTextLocation.setText(mAddressOutput);
    }

    /*private class GetAddress extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(getContext());

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
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }
        @Override
        protected void onPostExecute(String result) {
            try {
                asyncDialog.dismiss();
                Log.i("Log Remark Address", result);

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonResult = jsonObject.getJSONArray("results");
                JSONObject formatted_address = jsonResult.getJSONObject(0);
                String address = formatted_address.getString("formatted_address");

                if(!address.isEmpty())
                    editTextLocation.setText(address);
                else
                    editTextLocation.setText(Float.toString(lat) + ", " + Float.toString(lng));
            }
            catch(Exception ex) {
                ex.printStackTrace();
                dismiss();
            }
        }
    }
*/
    private class InsertLog extends AsyncTask<String, Void, Boolean> {
        ProgressDialog asyncDialog = new ProgressDialog(getContext());
        int returned_id;
        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setMessage(getString(R.string.loadingtype));
            //show dialog
            asyncDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                String result = DataService.PostTrackLog(Long.parseLong(registerID),"V",sharedPreferences.getString(Utilities.LOGIN_POLICE_ID, null),
                        sharedPreferences.getString("locationAddress", "No GPS"), sharedPreferences.getFloat("lat", 0)
                        ,sharedPreferences.getFloat("lng", 0),remark);
                if (result != null && result != "ERROR") {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getString("success").equalsIgnoreCase("true")){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Boolean result) {
            try {
                    asyncDialog.dismiss();
                    if(!result) {
                        dismiss();
                       Toast.makeText(getContext(), "Something went wrong, failed to add remark.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        dismiss();
                        Toast.makeText(getContext(), "Successfully add remark.", Toast.LENGTH_LONG).show();
                    }
            }
            catch(Exception ex) {
                ex.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong, failed to add remark.", Toast.LENGTH_LONG).show();
            }
        }
    }

}
