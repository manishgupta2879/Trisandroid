package com.pcs.tim.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ViewRefugeeListAdapter extends ArrayAdapter {
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public ViewRefugeeListAdapter(Context ctx, int resourceId, ArrayList<ViewRefugee> refugees) {
        super(ctx, resourceId, refugees);
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
    }

    class ViewRefugeeListViewHolder{
        ImageView imageRefugee;
        TextView txtName;
        TextView txtMyRC;
        TextView txtCategory;
        TextView txtCountry;
        ViewRefugeeListViewHolder(View v){
            imageRefugee = (ImageView)v.findViewById(R.id.imageViewRefugee);
            txtName = (TextView) v.findViewById(R.id.textViewRefugeeName);
            txtMyRC = (TextView) v.findViewById(R.id.textViewRefugeeMyRC);
            txtCountry = (TextView) v.findViewById(R.id.textViewRefugeeCountry);
            txtCategory = (TextView) v.findViewById(R.id.textViewRefugeeCategory);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewRefugeeListAdapter.ViewRefugeeListViewHolder holder = null;
        if(row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         //   row = inflater.inflate(R.layout.search_list_item,parent,false);
            row = inflater.inflate(R.layout.new_search_my_rc_card,parent,false);
            holder = new ViewRefugeeListAdapter.ViewRefugeeListViewHolder(row);
            row.setTag(holder);

        }else{
            holder = (ViewRefugeeListAdapter.ViewRefugeeListViewHolder) row.getTag();
        }
        // this.position = position;
        /* create a new view of my layout and inflate it in the row */
        //convertView = (LinearLayout) inflater.inflate(resource, null);

//        if( position%2 == 0){
//            row.setBackgroundColor(Color.rgb(210, 225, 250));
//        }

        /* Extract the city's object to show */
        final ViewRefugee refugee = (ViewRefugee) getItem(position);

        //ImageView imageRefugee = (ImageView)convertView.findViewById(R.id.imageViewRefugee);


        if(refugee.getPhoto() != null && holder.imageRefugee!=null) {
            Glide.with(context)
                    .load(refugee.getPhoto())
                    .centerCrop()
                    .error(R.mipmap.ic_warning)
                    .into(holder.imageRefugee);
//            URL newurl = null;
//            try {
//                newurl = new URL(refugee.getPhoto());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                Bitmap bmp = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
//                holder.imageRefugee.setImageBitmap(bmp);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 4;  // downsample factor (16 pixels -> 1 pixel)
//            Bitmap photo = BitmapFactory.decodeByteArray(refugee.getPhoto() , 0 ,refugee.getPhoto().length,options);
//            holder.imageRefugee.setImageBitmap(photo);
        }

//        TextView txtName = (TextView) convertView.findViewById(R.id.textViewRefugeeName);
        holder.txtName.setText(refugee.getName());

//        TextView txtMyRC = (TextView) convertView.findViewById(R.id.textViewRefugeeMyRC);
        holder.txtMyRC.setText(refugee.getMyRC());


//        TextView txtUnhcr = (TextView) convertView.findViewById(R.id.textViewRefugeeUNHCR);
        holder.txtCategory.setText(refugee.getCategory());
        holder.txtCountry.setText(refugee.getCountry());

        return row;
    }
}
