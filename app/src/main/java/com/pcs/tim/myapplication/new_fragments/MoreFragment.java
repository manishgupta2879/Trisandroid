package com.pcs.tim.myapplication.new_fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.pcs.tim.myapplication.MainActivity;
import com.pcs.tim.myapplication.R;
import com.pcs.tim.myapplication.Utilities;


public class MoreFragment extends Fragment {


    public MoreFragment() {
        // Required empty public constructor
    }

RelativeLayout btnLogout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_more, container, false);

        btnLogout=view.findViewById(R.id.btnLogoutLay);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Are you sure to logout?");
                adb.setIcon(R.mipmap.ic_tris_logo);
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    } });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    } });
                adb.show();
            }
        });


        return view;
    }

    private void logout(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Utilities.MY_RC_SHARE_PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MainActivity.MY_RC_RMB_ME,false);
        editor.putBoolean(Utilities.LOGGED_IN, false);
        editor.apply();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}