package com.pcs.tim.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Tim on 6/30/2017.
 */

public class RemarkListAdapter extends ArrayAdapter {
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public RemarkListAdapter(Context ctx, int resourceId, ArrayList<Remark> remarks) {
        super(ctx, resourceId, remarks);
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
    }

    class ViewRemarkListViewHolder{
        ImageView imgViewRefugee;
        TextView txtPoliceId ;
        TextView txtFullName ;
        TextView txtCountry;
        TextView txtStatus ;
        TextView txtDate;
        TextView txtCheckedLocation ;
        TextView txtMyrc;
        TextView txtRemark;
        ViewRemarkListViewHolder(View v){
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        RemarkListAdapter.ViewRemarkListViewHolder holder = null;

        if(row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.remark_list_item,parent,false);
            holder = new RemarkListAdapter.ViewRemarkListViewHolder(row);
            row.setTag(holder);

        }else{
            holder = (RemarkListAdapter.ViewRemarkListViewHolder) row.getTag();
        }
        // this.position = position;
        /* create a new view of my layout and inflate it in the row */
//        convertView = (LinearLayout) inflater.inflate(resource, null);

//        if( position%2 == 0){
//            row.setBackgroundColor(Color.rgb(210, 225, 250));
//        }
        /* Extract the city's object to show */
        final Remark remark = (Remark) getItem(position);

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
                        Intent intent = new Intent(getContext(), VerificationResultActivity.class);
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
        return row;
    }
}
