package com.pcs.tim.myapplication.new_added_classes;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pcs.tim.myapplication.R;
import com.pcs.tim.myapplication.Remark;
import com.pcs.tim.myapplication.VerificationResultActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewCheckHistoryRecentPoliceAdapter extends RecyclerView.Adapter<NewCheckHistoryRecentPoliceAdapter.RecentPoliceViewHolder> {

    Context context;
    ArrayList<Remark> remarks;

    public NewCheckHistoryRecentPoliceAdapter(Context context, ArrayList<Remark> remarks) {
        this.context = context;
        this.remarks = remarks;
    }

    @NonNull
    @Override
    public RecentPoliceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recent_checkin_for_police_card,viewGroup,false);
        return new RecentPoliceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentPoliceViewHolder holder, int i) {

        final Remark remark = remarks.get(i);

        if(remark!=null) {

          /*  if (holder.txtPoliceId != null)
                holder.txtPoliceId.setText(remark.getPoliceId());*/



            if (remark.getMyRc() != null) {


                SpannableStringBuilder spannable = new SpannableStringBuilder("MyRc " + remark.getMyRc());

                ForegroundColorSpan blueColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.themeColor));
                spannable.setSpan(blueColorSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                UnderlineSpan underlineSpan = new UnderlineSpan();
                spannable.setSpan(underlineSpan, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



                holder.textViewCheckedMyRc.setText(spannable);
            } else {

                SpannableStringBuilder spannable = new SpannableStringBuilder("MyRc " + "");

                ForegroundColorSpan blueColorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.themeColor));
                spannable.setSpan(blueColorSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                holder.textViewCheckedMyRc.setText(spannable);
            }



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

            holder.recentRefugeeCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VerificationResultActivity.class);
                    intent.putExtra("inputType", "myrc");
                    intent.putExtra("inputValue", remark.getMyRc());
                    intent.putExtra("addRemark", false);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });


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
            }        }

    }

    @Override
    public int getItemCount() {
        return remarks.size();
    }

    class RecentPoliceViewHolder extends RecyclerView.ViewHolder{

        TextView txtCheckedLocation;
        TextView txtRemark,textViewCheckedMyRc;
        TextView txtCheckDate;
        TextView   txtCheckTime ;
        CardView recentRefugeeCard;

        public RecentPoliceViewHolder(@NonNull View v) {
            super(v);

            txtCheckedLocation = (TextView) v.findViewById(R.id.textViewCheckedLocation);
            txtRemark = (TextView) v.findViewById(R.id.textViewRemark);
            txtCheckDate = (TextView) v.findViewById(R.id.textViewCheckDate);
            txtCheckTime = (TextView) v.findViewById(R.id.txtCheckTime);
            textViewCheckedMyRc = (TextView) v.findViewById(R.id.textViewCheckedMyRc);
            recentRefugeeCard =v.findViewById(R.id.recentRefugeeCard);
        }
    }
}
