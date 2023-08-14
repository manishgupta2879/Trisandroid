package com.pcs.tim.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by Tim on 6/22/2017.
 */

public class RefugeeListAdapter extends ArrayAdapter{
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public RefugeeListAdapter(Context ctx, int resourceId, ArrayList<Refugee> refugees) {
        super(ctx, resourceId, refugees);
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
    }

    class RefugeeListViewHolder{
        ImageView imageRefugee;
        TextView txtName;
        TextView txtMyRC;
        TextView txtCategory;
        TextView txtCountry;
        RefugeeListViewHolder(View v){
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
        RefugeeListViewHolder holder = null;
        if(row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.search_list_item,parent,false);
            holder = new RefugeeListViewHolder(row);
            row.setTag(holder);

        }else{
            holder = (RefugeeListViewHolder) row.getTag();
        }
        // this.position = position;
        /* create a new view of my layout and inflate it in the row */
        //convertView = (LinearLayout) inflater.inflate(resource, null);

//        if( position%2 == 0){
//            row.setBackgroundColor(Color.rgb(210, 225, 250));
//        }

        /* Extract the city's object to show */
        final Refugee refugee = (Refugee) getItem(position);

        //ImageView imageRefugee = (ImageView)convertView.findViewById(R.id.imageViewRefugee);

        if(refugee.getPhoto() != null) {
            //byte[] photoBytes = refugee.getPhotoString().getBytes();
            //Log.i("photo bytes", new String(refugee.getPhoto()));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;  // downsample factor (16 pixels -> 1 pixel)
            Bitmap photo = BitmapFactory.decodeByteArray(refugee.getPhoto() , 0 ,refugee.getPhoto().length,options);
            holder.imageRefugee.setImageBitmap(photo);
        }

//        TextView txtName = (TextView) convertView.findViewById(R.id.textViewRefugeeName);
        holder.txtName.setText("Name : " + refugee.getName());

//        TextView txtMyRC = (TextView) convertView.findViewById(R.id.textViewRefugeeMyRC);
        holder.txtMyRC.setText("MyRC : " + refugee.getMyRC());


//        TextView txtUnhcr = (TextView) convertView.findViewById(R.id.textViewRefugeeUNHCR);
        holder.txtCategory.setText("Category : " + refugee.getCategory());
        holder.txtCountry.setText("Country of Origin : " + refugee.getCountry());

        return row;
    }

}
