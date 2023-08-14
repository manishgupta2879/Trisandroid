package com.pcs.tim.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by Tim on 6/29/2017.
 */

public class FetchAddressIntentService extends IntentService {

    protected ResultReceiver mReceiver;

    public  FetchAddressIntentService(){
        super("FetchAddressIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";

        mReceiver = intent.getParcelableExtra(Utilities.RECEIVER);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        // Get the location passed to this service through an extra.
        Location location = intent.getParcelableExtra(
                Utilities.LOCATION_DATA_EXTRA);

        // ...
        Log.i("GEOCODER", Boolean.toString(Geocoder.isPresent()));

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address.
                    10);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Utilities.FAILURE_RESULT, errorMessage);
        } else {

            ArrayList<String> addressFragments = null;
            int count = 0;
            while (addressFragments == null && count < addresses.size()) {
                Address address = addresses.get(count);
                if(address.getAddressLine(0) != null) {
                    addressFragments = new ArrayList<String>();

                    // Fetch the address lines using getAddressLine,
                    // join them, and send them to the thread.
                    for (int i = 0; i <= address.getMaxAddressLineIndex() - 1; i++) {
                        if (address.getAddressLine(i) != null)
                            addressFragments.add(address.getAddressLine(i));
                    }

                    Log.i(TAG, getString(R.string.address_found) + TextUtils.join(", ",
                            addressFragments) + "Latitude = " + location.getLatitude() +
                            ", Longitude = " +
                            location.getLongitude());
                    deliverResultToReceiver(Utilities.SUCCESS_RESULT,
                            TextUtils.join(", ",
                                    addressFragments));
                }
                count++;
            }


            /*deliverResultToReceiver(Utilities.SUCCESS_RESULT,
                    address.getAddressLine(0) + " " + address.getAddressLine(1));*/
        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Utilities.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}
