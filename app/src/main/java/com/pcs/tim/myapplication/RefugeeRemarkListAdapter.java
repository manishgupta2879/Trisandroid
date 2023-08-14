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
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class RefugeeRemarkListAdapter extends ArrayAdapter {
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public RefugeeRemarkListAdapter(Context ctx, int resourceId, ArrayList<Remark> remarks) {
        super(ctx, resourceId, remarks);
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
    }

    class ViewRefugeeRemarkListViewHolder{
        TextView txtPoliceId ;
        TextView txtCheckedLocation ;
        TextView txtRemark;
        TextView txtCheckDate;
        ViewRefugeeRemarkListViewHolder(View v){
            txtPoliceId = (TextView) v.findViewById(R.id.textViewPoliceID);
            txtCheckedLocation= (TextView) v.findViewById(R.id.textViewCheckedLocation);
            txtRemark  = (TextView) v.findViewById(R.id.textViewRemark);
            txtCheckDate  = (TextView) v.findViewById(R.id.textViewCheckDate);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        RefugeeRemarkListAdapter.ViewRefugeeRemarkListViewHolder holder = null;

        if(row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.new_refugee_remark_list_item,parent,false);
            holder = new RefugeeRemarkListAdapter.ViewRefugeeRemarkListViewHolder(row);
            row.setTag(holder);

        }else{
            holder = (RefugeeRemarkListAdapter.ViewRefugeeRemarkListViewHolder) row.getTag();
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

            if (holder.txtPoliceId != null)
                holder.txtPoliceId.setText(remark.getPoliceId());

            if (remark.getLocation() != null && !remark.getLocation().equals("null"))
                holder.txtCheckedLocation.setText(remark.getLocation());
            else
                holder.txtCheckedLocation.setText("");

            holder.txtRemark.setMovementMethod(new ScrollingMovementMethod());
            if(remark.getRemark()!=null)
                holder.txtRemark.setText(remark.getRemark());
            else
                holder.txtRemark.setText("");

            SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat viewDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

            try {
                Date checkTime = sourceDateFormat.parse(remark.getCheckTime());
                holder.txtCheckDate.setText(viewDateFormat.format(checkTime));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return row;
    }
}
