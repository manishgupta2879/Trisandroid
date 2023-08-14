package com.pcs.tim.myapplication.new_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pcs.tim.myapplication.R;
import com.pcs.tim.myapplication.SearchMyRCActivity;
import com.pcs.tim.myapplication.SearchNameActivity;
import com.pcs.tim.myapplication.SearchUNHCRActivity;


public class SearchMenuFragment extends Fragment {


    CardView buttonSearchMyRC;
    CardView buttonSearchUNHCR;
    CardView buttonSearchName;
    public SearchMenuFragment() {
        // Required empty public constructor
    }

  

 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.activity_search_menu, container, false);


        final FragmentManager fm = getFragmentManager();
        Fragment searchMyRcFragment = new SearchMyRcFragment();
        Fragment searchUNHCRFragment=new SearchUNHCRFragment();
        Fragment searchNameFragment=new SearchNameFragment();

        FragmentTransaction transaction =fm.beginTransaction();
        buttonSearchMyRC = view.findViewById(R.id.buttonSearchByMyRC);
        buttonSearchName = view.findViewById(R.id.buttonSearchByName);
        buttonSearchUNHCR = view.findViewById(R.id.buttonSearchByUNHCR);




        Toolbar toolbar = view.findViewById(R.id.searchMenu_toolbar);
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

        buttonSearchMyRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(getActivity(), SearchMyRCActivity.class);
                startActivity(intent);*/

                transaction.replace(R.id.main_container, searchMyRcFragment);
                transaction.addToBackStack(null); // Optional: Add to back stack
                transaction.commit();
            }
        });

        buttonSearchUNHCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(getActivity(), SearchUNHCRActivity.class);
                startActivity(intent);*/
                transaction.replace(R.id.main_container, searchUNHCRFragment);
                transaction.addToBackStack(null); // Optional: Add to back stack
                transaction.commit();
            }
        });

        buttonSearchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(getActivity(), SearchNameActivity.class);
                startActivity(intent);*/
                transaction.replace(R.id.main_container, searchNameFragment);
                transaction.addToBackStack(null); // Optional: Add to back stack
                transaction.commit();
            }
        });
        return  view;
    }
}