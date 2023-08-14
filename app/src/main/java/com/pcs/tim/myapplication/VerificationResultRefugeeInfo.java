package com.pcs.tim.myapplication;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class VerificationResultRefugeeInfo extends Fragment {


    private String myRc;
    TextView textViewMyrc;
    public VerificationResultRefugeeInfo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_verification_result_refugee_info, container, false);
        textViewMyrc = (TextView)rootView.findViewById(R.id.textViewMyrc);
        if(getArguments()!=null){
            String myrc = getArguments().getString("myrc");
            textViewMyrc.setText(myrc);
            Log.d("CHECK2",myrc);
        }else{
            textViewMyrc.setText("XXXXXXXX");
        }

        // Inflate the layout for this fragment
        return rootView;
    }

}
