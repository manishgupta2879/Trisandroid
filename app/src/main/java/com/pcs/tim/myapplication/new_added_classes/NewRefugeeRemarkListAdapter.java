package com.pcs.tim.myapplication.new_added_classes;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pcs.tim.myapplication.R;
import com.pcs.tim.myapplication.Remark;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewRefugeeRemarkListAdapter extends RecyclerView.Adapter<NewRefugeeRemarkListAdapter.RefugeeRemarkViewHolder> {
    ArrayList<Remark> remarks;
    Context context;

    public NewRefugeeRemarkListAdapter(ArrayList<Remark> remarks, Context context) {
        this.remarks = remarks;
        this.context = context;
    }

    @NonNull
    @Override
    public RefugeeRemarkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_refugee_remark_list_item,viewGroup,false);
        return new RefugeeRemarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RefugeeRemarkViewHolder holder, int i) {
        final Remark remark = remarks.get(i);

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
    }

    @Override
    public int getItemCount() {
        return remarks.size();
    }

    class RefugeeRemarkViewHolder extends RecyclerView.ViewHolder{

        TextView txtPoliceId ;
        TextView txtCheckedLocation ;
        TextView txtRemark;
        TextView txtCheckDate;
        public RefugeeRemarkViewHolder(@NonNull View v) {
            super(v);
            txtPoliceId = (TextView) v.findViewById(R.id.textViewPoliceID);
            txtCheckedLocation= (TextView) v.findViewById(R.id.textViewCheckedLocation);
            txtRemark  = (TextView) v.findViewById(R.id.textViewRemark);
            txtCheckDate  = (TextView) v.findViewById(R.id.textViewCheckDate);
        }
    }{

    }
}
