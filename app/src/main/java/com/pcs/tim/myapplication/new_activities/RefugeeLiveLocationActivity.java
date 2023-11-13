package com.pcs.tim.myapplication.new_activities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pcs.tim.myapplication.R;

public class RefugeeLiveLocationActivity extends FragmentActivity implements OnMapReadyCallback {
    private boolean isFetchingData = false;
    private DatabaseReference refugeeLocationRef;
    private GoogleMap googleMap;
    private Marker refugeeMarker;

    float lat,lng;
    long regId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refugee_live_location);
        Toolbar toolbar = findViewById(R.id.live_location_toolbar);
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

        lat = getIntent().getFloatExtra("lat", 0.0F); // 0.0 is the default value
         lng = getIntent().getFloatExtra("lng", 0.0F);
         regId = getIntent().getLongExtra("regId",0);

        // Initialize Firebase Realtime Database reference
        String firebaseUrl = "https://geofence-bbfdb-default-rtdb.firebaseio.com/";
        FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseUrl);
        refugeeLocationRef = database.getReference("RefugeLocation");

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Start fetching live location when the map is ready
        startFetchingLiveLocation();
    }

    private void startFetchingLiveLocation() {
        isFetchingData = true;
        getLiveLocation();
    }

    // Fetch and display live location
    private void getLiveLocation() {
       // long regID = 180;//GlobalParams.RegID; // Assuming you have regID available

        if (regId != 0) {


            refugeeLocationRef.orderByChild("regid").equalTo(regId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Log.d("liveLoc__", "onChildAdded: ");
                    if (snapshot.exists()) {
                        String latString = snapshot.child("lat").getValue(String.class);
                        String lngString = snapshot.child("lng").getValue(String.class);
                        Log.d("lat_Long_data", "onChildAdded: "+latString+" lng "+lngString+" regid "+regId);

                        if (latString != null && lngString != null) {
                            Double lat = Double.parseDouble(latString);
                            Double lng = Double.parseDouble(lngString);
                            LatLng newPosition = new LatLng(lat, lng);
                            // Update the map to display the new location
                            addLocationOnMap(newPosition);


                        }


                     /*   for (DataSnapshot refugeeSnapshot : snapshot.getChildren()) {

                            String abc=refugeeSnapshot.toString();
                            String latString = refugeeSnapshot.child("lat").getValue(String.class);
                            String lngString = refugeeSnapshot.child("lng").getValue(String.class);

                            Log.d("lat_Long_data", "onChildAdded: "+abc+" lng "+lngString+" regid "+regId);

                            if (latString != null && lngString != null) {
                                Double lat = Double.parseDouble(latString);
                                Double lng = Double.parseDouble(lngString);
                                LatLng newPosition = new LatLng(lat, lng);
                                // Update the map to display the new location
                                updateLocationOnMap(newPosition);


                            }
                        }*/
                    } else {
                        Log.d("lat_Long_data", "onDataChange: in else "+lat+" lng "+lng+" regid "+regId);

                        if (lat != 0.0 && lng != 0.0) {
                            LatLng newPosition = new LatLng(lat, lng);
                            // Update the map to display the new location
                            addLocationOnMap(newPosition);
                        }
                        // Handle the case when no data is found
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Log.d("liveLoc__", "onChildChanged: ");

                    if (snapshot.exists()) {
                        String latString = snapshot.child("lat").getValue(String.class);
                        String lngString = snapshot.child("lng").getValue(String.class);
                        Log.d("lat_Long_data", "onChildAdded: "+latString+" lng "+lngString+" regid "+regId);

                        if (latString != null && lngString != null) {
                            Double lat = Double.parseDouble(latString);
                            Double lng = Double.parseDouble(lngString);
                            LatLng newPosition = new LatLng(lat, lng);
                            // Update the map to display the new location
                            updateLocationOnMap(newPosition);


                        }
                    } else {
                        Log.d("lat_Long_data", "onDataChange: in else "+lat+" lng "+lng+" regid "+regId);

                        if (lat != 0.0 && lng != 0.0) {
                            LatLng newPosition = new LatLng(lat, lng);
                            // Update the map to display the new location
                            addLocationOnMap(newPosition);
                        }
                        // Handle the case when no data is found
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Log.d("liveLoc__", "onChildRemoved: ");
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Log.d("liveLoc__", "onChildMoved: ");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("liveLoc__", "onCancelled: "+ error.getMessage());
                }
            });

/*
            refugeeLocationRef.orderByChild("regid").equalTo(regId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot refugeeSnapshot : dataSnapshot.getChildren()) {
                            String latString = refugeeSnapshot.child("lat").getValue(String.class);
                            String lngString = refugeeSnapshot.child("lng").getValue(String.class);

                            Log.d("lat_Long_data", "onDataChange: "+lat+" lng "+lng+" regid "+regId);

                            if (latString != null && lngString != null) {
                                Double lat = Double.parseDouble(latString);
                                Double lng = Double.parseDouble(lngString);
                                LatLng newPosition = new LatLng(lat, lng);
                                // Update the map to display the new location
                                updateLocationOnMap(newPosition);


                            }
                        }
                    } else {
                        Log.d("lat_Long_data", "onDataChange: in else "+lat+" lng "+lng+" regid "+regId);

                        if (lat != 0.0 && lng != 0.0) {
                            LatLng newPosition = new LatLng(lat, lng);
                            // Update the map to display the new location
                            updateLocationOnMap(newPosition);
                        }
                        // Handle the case when no data is found
                    }
                }



                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors, if any
                }
            });*/
        } else {
            // Handle the case when regID is 0
            showAlertDialog("RegID", "No RegID is available for this refugee");
        }
    }

    private void addLocationOnMap(LatLng newPosition) {


        // Check if a marker already exists
        if (refugeeMarker != null) {
            // Remove the existing marker from the map
            refugeeMarker.remove();
        }

        // Add a new marker on the map
        refugeeMarker = googleMap.addMarker(new MarkerOptions()
                .position(newPosition)
                .title("Refugee is here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        // Optionally, move the camera to the new location
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 15));
    }



    private void updateLocationOnMap(LatLng newPosition) {


        // Check if a marker already exists
        if (refugeeMarker != null) {
            // Remove the existing marker from the map
            refugeeMarker.remove();
        }

        // Add a new marker on the map
        refugeeMarker = googleMap.addMarker(new MarkerOptions()
                .position(newPosition)
                .title("Refugee is here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        // Optionally, move the camera to the new location
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(newPosition));
    }


    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
}
