package com.pcs.tim.myapplication.new_added_classes;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pcs.tim.myapplication.R;
import com.pcs.tim.myapplication.Remark;
import com.pcs.tim.myapplication.RemarkListAdapter;
import com.pcs.tim.myapplication.VerificationResultActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewRemarkListAdapter extends RecyclerView.Adapter<NewRemarkListAdapter.RemarkViewHolder> {


    ArrayList<Remark> remarks;
    private Context context;

    public NewRemarkListAdapter(ArrayList<Remark> remarks, Context context) {
        this.remarks = remarks;
        this.context = context;
    }

    @NonNull
    @Override
    public RemarkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_refugee_remark_list_item,viewGroup,false);
        return new NewRemarkListAdapter.RemarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RemarkViewHolder holder, int i) {




        // this.position = position;
        /* create a new view of my layout and inflate it in the row */
//        convertView = (LinearLayout) inflater.inflate(resource, null);

//        if( position%2 == 0){
//            row.setBackgroundColor(Color.rgb(210, 225, 250));
//        }
        /* Extract the city's object to show */
        final Remark remark = remarks.get(i);

        if(remark!=null) {

            if(remark.getPhoto() != null) {

                Glide.with(context)
                        .load(remark.getPhoto())
                        .centerCrop()
                        .error(R.mipmap.ic_warning)
                        .into(holder.imgViewRefugee);
            }


            if (holder.txtPoliceId != null)
                holder.txtPoliceId.setText(remark.getPoliceId());


            if (holder.txtFullName != null)
                holder.txtFullName.setText(remark.getRefugeeName());


            if (holder.txtCountry != null)
                holder.txtCountry.setText(remark.getCountryOfOrigin());


            if (holder.txtStatus != null)
                holder.txtStatus.setText(remark.getCardStatus());

            if(remark.getCardStatus().equals("EXPIRED")){
                holder.txtStatus.setTextColor(Color.RED);
            }else  if(holder.txtStatus.equals("PENDING")){
                holder.txtStatus.setTextColor(Color.parseColor("#F9AA33"));
            }
            else{
                holder.txtStatus.setTextColor(Color.GREEN);
            }



            SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat viewDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

            try {
                Date checkTime = sourceDateFormat.parse(remark.getCardExpiredDate());
                holder.txtDate.setText(viewDateFormat.format(checkTime));
            }catch (Exception ex){
                ex.printStackTrace();
            }


            if (remark.getLocation() != null && !remark.getLocation().equals("null"))
                holder.txtCheckedLocation.setText(remark.getLocation());
            else
                holder.txtCheckedLocation.setText("");

            if (holder.txtMyrc != null) {
                holder.txtMyrc.setText(remark.getMyRc());
                holder.txtMyrc.setPaintFlags(holder.txtMyrc.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

                holder.txtMyrc.setOnClickListener(new View.OnClickListener() {
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
            }

            holder.txtRemark.setMovementMethod(new ScrollingMovementMethod());
            if(remark.getRemark()!=null)
                holder.txtRemark.setText(remark.getRemark());
            else
                holder.txtRemark.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return remarks.size();
    }

    class  RemarkViewHolder extends RecyclerView.ViewHolder{
        ImageView imgViewRefugee;
        TextView txtPoliceId ;
        TextView txtFullName ;
        TextView txtCountry;
        TextView txtStatus ;
        TextView txtDate;
        TextView txtCheckedLocation ;
        TextView txtMyrc;
        TextView txtRemark;
        public RemarkViewHolder(@NonNull View v) {
            super(v);
            imgViewRefugee  = (ImageView) v.findViewById(R.id.imageViewRefugee);
            txtPoliceId = (TextView) v.findViewById(R.id.textViewPoliceID);
            txtFullName = v.findViewById(R.id.textViewRefugeeName);
            txtCountry  = v.findViewById(R.id.textViewCountryOfOrigin);
            txtStatus = v.findViewById(R.id.textViewCardStatus);
            txtDate  = (TextView) v.findViewById(R.id.textViewCardExpired);
            txtCheckedLocation= (TextView) v.findViewById(R.id.textViewCheckedLocation);
            txtMyrc  = (TextView) v.findViewById(R.id.textViewMyrc);
            txtRemark  = (TextView) v.findViewById(R.id.textViewRemark);
        }
    }
}
