package com.pcs.tim.myapplication.new_added_classes;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pcs.tim.myapplication.R;
import com.pcs.tim.myapplication.Remark;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewCheckHistoryCurrentLoactionPoliceAdapter extends RecyclerView.Adapter<NewCheckHistoryCurrentLoactionPoliceAdapter.CurrentLoactionPoliceViewHolder> {


    Context context;
    ArrayList<Remark> remarks;

    public NewCheckHistoryCurrentLoactionPoliceAdapter(Context context, ArrayList<Remark> remarks) {
        this.context = context;
        this.remarks = remarks;
    }

    @NonNull
    @Override
    public CurrentLoactionPoliceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.current_location_for_police_card, viewGroup, false);
        return new CurrentLoactionPoliceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentLoactionPoliceViewHolder holder, int i) {
        final Remark remark = remarks.get(i);


        if(remark!=null) {

          /*  if (holder.txtPoliceId != null)
                holder.txtPoliceId.setText(remark.getPoliceId());*/

            if (remark.getLocation() != null && !remark.getLocation().equals("null"))
            {
                String locString = remark.getLocation();


                SpannableStringBuilder spannable = new SpannableStringBuilder("Address " + locString);

                ForegroundColorSpan blueColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.themeColor));
                spannable.setSpan(blueColorSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



                holder.txtCheckedLocation.setText(spannable);
            }else {

                SpannableStringBuilder spannable = new SpannableStringBuilder("Address " + " ");

                ForegroundColorSpan blueColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.themeColor));
                spannable.setSpan(blueColorSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



                holder.txtCheckedLocation.setText(spannable);
            }

            holder.txtRemark.setMovementMethod(new ScrollingMovementMethod());
            if(remark.getRemark()!=null) {
                SpannableStringBuilder spannable = new SpannableStringBuilder("Remark " + remark.getRemark());

                ForegroundColorSpan blueColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.themeColor));
                spannable.setSpan(blueColorSpan, 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



                holder.txtRemark.setText(spannable);
            }
            else {

                SpannableStringBuilder spannable = new SpannableStringBuilder("Remark " + "");

                ForegroundColorSpan blueColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.themeColor));
                spannable.setSpan(blueColorSpan, 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



                holder.txtRemark.setText(spannable);
            }

     //       if(remark.getLat()!=0&& remark.getLng()!=0){
             //   Log.d("latlong__", "onBindViewHolder:  found latlong "+remark.getLat()+" long "+remark.getLng());
          /*      holder.mapView.onCreate(null);

                holder.mapView.getMapAsync(new OnMapReadyCallback() {

                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                      *//*  Log.d("latlong_1_", "onBindViewHolder:  found latlong ");

                        LatLng TARGET_LOCATION =new LatLng(28.7041,77.1025);
                        googleMap.addMarker(new MarkerOptions().position(TARGET_LOCATION));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(TARGET_LOCATION));
                        Log.d("latlong_1_", "onBindViewHolder2:  found latlong ");*//*
                    }
                });*/

          /*  }else{
                Log.d("latlong__", "onBindViewHolder: not found latlong");
            }*/

            SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat viewDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat viewtimeFormat = new SimpleDateFormat("hh:mm a");

            try {
                Date checkDate = sourceDateFormat.parse(remark.getCheckTime());
                Date checkTime =sourceDateFormat.parse(remark.getCheckTime());


                SpannableStringBuilder spannable = new SpannableStringBuilder("Date " + viewDateFormat.format(checkDate));
                SpannableStringBuilder spannableTime = new SpannableStringBuilder("Time " + viewtimeFormat.format(checkTime));

                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);


                spannable.setSpan(boldSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableTime.setSpan(boldSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                holder.txtCheckDate.setText(spannable);
                holder.txtCheckTime.setText(spannableTime);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        return remarks.size();
    }

    class CurrentLoactionPoliceViewHolder extends RecyclerView.ViewHolder {
        TextView txtCheckedLocation;
        TextView txtRemark;
        TextView txtCheckDate;
        TextView txtCheckTime;
        View mapView;



        public CurrentLoactionPoliceViewHolder(@NonNull View v) {
            super(v);

            txtCheckedLocation = (TextView) v.findViewById(R.id.textViewCheckedLocation);
            txtRemark = (TextView) v.findViewById(R.id.textViewRemark);
            txtCheckDate = (TextView) v.findViewById(R.id.textViewCheckDate);
            txtCheckTime = (TextView) v.findViewById(R.id.txtCheckTime);
            mapView=v.findViewById(R.id.mapFragment);
        }
    }


}
