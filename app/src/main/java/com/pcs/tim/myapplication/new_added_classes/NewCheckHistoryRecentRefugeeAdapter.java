package com.pcs.tim.myapplication.new_added_classes;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pcs.tim.myapplication.R;
import com.pcs.tim.myapplication.Remark;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewCheckHistoryRecentRefugeeAdapter extends RecyclerView.Adapter<NewCheckHistoryRecentRefugeeAdapter.RecentRefugeeViewHolder> {


    ArrayList<Remark> remarks;
    Context context;

    public NewCheckHistoryRecentRefugeeAdapter(ArrayList<Remark> remarks, Context context) {
        this.remarks = remarks;
        this.context = context;
    }

    @NonNull
    @Override
    public RecentRefugeeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recent_checkin_for_refugee_card,viewGroup,false);

        return new RecentRefugeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentRefugeeViewHolder holder, int i) {

        final Remark remark = remarks.get(i);

        if(remark!=null) {



            if (remark.getLocation() != null && !remark.getLocation().equals("null")) {

                String locString = remark.getLocation();


                SpannableStringBuilder spannable = new SpannableStringBuilder("Address " + locString);

                ForegroundColorSpan blueColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.themeColor));
                spannable.setSpan(blueColorSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



                holder.txtCheckedLocation.setText(spannable);

            }
            else {
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


            if(remark.getLat()!=0&&remark.getLng()!=0){
                SpannableStringBuilder spannableLat = new SpannableStringBuilder("Latitude " + remark.getLat());
                SpannableStringBuilder spannableLng = new SpannableStringBuilder("Longitude " + remark.getLng());

                ForegroundColorSpan blueColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.themeColor));
                spannableLat.setSpan(blueColorSpan, 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableLng.setSpan(blueColorSpan, 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



                holder.txtLatRef.setText(spannableLat);
                holder.txtLngRef.setText(spannableLng);
            }

            else{
                SpannableStringBuilder spannableLat = new SpannableStringBuilder("Latitude " + "");
                SpannableStringBuilder spannableLng = new SpannableStringBuilder("Longitude " + "");

                ForegroundColorSpan blueColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.themeColor));
                spannableLat.setSpan(blueColorSpan, 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableLng.setSpan(blueColorSpan, 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



                holder.txtLatRef.setText(spannableLat);
                holder.txtLngRef.setText(spannableLng);
            }

            SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat viewDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat viewtimeFormat = new SimpleDateFormat("hh:mm a");

            try {
                Date checkDate = sourceDateFormat.parse(remark.getCheckTime());
                Date checkTime =sourceDateFormat.parse(remark.getCheckTime());


                SpannableStringBuilder spannable = new SpannableStringBuilder("Date " + viewDateFormat.format(checkDate));
                SpannableStringBuilder spannableTime = new SpannableStringBuilder("Time " + viewtimeFormat.format(checkTime));

                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                ForegroundColorSpan blackColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.black));
                spannable.setSpan(blackColorSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableTime.setSpan(blackColorSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                spannable.setSpan(boldSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableTime.setSpan(boldSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

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

    class RecentRefugeeViewHolder extends RecyclerView.ViewHolder{

        TextView txtCheckedLocation ;
        TextView txtRemark;
        TextView txtCheckDate;
        TextView txtCheckTime;
        TextView txtLatRef;
        TextView txtLngRef;

        public RecentRefugeeViewHolder(@NonNull View v) {
            super(v);

            txtCheckedLocation = (TextView) v.findViewById(R.id.txtCheckedLocation);
            txtRemark = (TextView) v.findViewById(R.id.txtRemark);
            txtCheckDate = (TextView) v.findViewById(R.id.txtCheckDate);
            txtCheckTime = (TextView) v.findViewById(R.id.txtCheckTime);
            txtLatRef = (TextView) v.findViewById(R.id.latRef);
            txtLngRef = (TextView) v.findViewById(R.id.lngRef);
        }
    }
}
