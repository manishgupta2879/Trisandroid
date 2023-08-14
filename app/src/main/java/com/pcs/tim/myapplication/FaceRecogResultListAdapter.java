package com.pcs.tim.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tim on 16-Jul-17.
 */

public class FaceRecogResultListAdapter extends ArrayAdapter {
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public FaceRecogResultListAdapter(Context ctx, int resourceId, ArrayList<Refugee> refugees) {
        super(ctx, resourceId, refugees);
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
    }

    class RefugeeFaceRecogResultListViewHolder{
        ImageView imageRefugee;
        TextView txtName;
        Button buttonViewDetails;
       /* TextView txtMyRC;
        TextView txtCategory;
        TextView txtCountry;
        TextView txtResult;*/
        RefugeeFaceRecogResultListViewHolder(View v){
            imageRefugee = (ImageView)v.findViewById(R.id.imageViewRefugee);
            txtName = (TextView) v.findViewById(R.id.textViewRefugeeName);
            buttonViewDetails=v.findViewById(R.id.buttonViewDetails);
        //    txtMyRC = (TextView) v.findViewById(R.id.textViewRefugeeMyRC);
        //    txtCountry = (TextView) v.findViewById(R.id.textViewRefugeeCountry);
          //  txtCategory = (TextView) v.findViewById(R.id.textViewRefugeeCategory);
          //  txtResult = (TextView) v.findViewById(R.id.textResult);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        // this.position = position;
        /* create a new view of my layout and inflate it in the row */
//        convertView = (LinearLayout) inflater.inflate(resource, null);

        View row = convertView;
        RefugeeFaceRecogResultListViewHolder holder =null;
        if(row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //row = inflater.inflate(R.layout.face_recog_result_list_item,parent,false);
            row = inflater.inflate(R.layout.new_face_recog_result_list_item,parent,false);
            holder = new RefugeeFaceRecogResultListViewHolder(row);
            row.setTag(holder);
        }else{
            holder = (RefugeeFaceRecogResultListViewHolder) row.getTag();
        }
//        if( position%2 == 0){
//            row.setBackgroundColor(Color.rgb(210, 225, 250));
//        }

        /* Extract the city's object to show */
        final Refugee refugee = (Refugee) getItem(position);

//        ImageView imageRefugee = (ImageView)convertView.findViewById(R.id.imageViewRefugee);

        if(refugee.getPhoto() != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;  // downsample factor (16 pixels -> 1 pixel)
            Bitmap photo = BitmapFactory.decodeByteArray(refugee.getPhoto(), 0 ,refugee.getPhoto().length,options);
            holder.imageRefugee.setImageBitmap(photo);
        }

        holder.buttonViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), VerificationResultActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.putExtra(Utilities.MY_RC, myRC);
                intent.putExtra("inputType", "myrc");
                intent.putExtra("inputValue", refugee.getMyRC());
                context.startActivity(intent);
            }
        });

//        TextView txtName = (TextView) convertView.findViewById(R.id.textViewRefugeeName);
        holder.txtName.setText(refugee.getName());

//        TextView txtMyRC = (TextView) convertView.findViewById(R.id.textViewRefugeeMyRC);
       // holder.txtMyRC.setText("MyRC : " + refugee.getMyRC());


//        TextView txtUnhcr = (TextView) convertView.findViewById(R.id.textViewRefugeeUNHCR);
       // holder.txtCategory.setText("Category : " +refugee.getCategory());

//        TextView txtCountry = (TextView) convertView.findViewById(R.id.textViewRefugeeCountry);
      //  holder.txtCountry.setText("Country of Origin : " + refugee.getCountry());

//        TextView txtResult = (TextView) convertView.findViewById(R.id.textResult);
        /*if(refugee.getFaceRecogResult()>=80)
            holder.txtResult.setTextColor(Color.rgb(0,153,0));
        else if (refugee.getFaceRecogResult()>=60)
            holder.txtResult.setTextColor(Color.rgb(255,140,0));
        else
            holder.txtResult.setTextColor(Color.RED);
        holder.txtResult.setText(Double.toString(refugee.getFaceRecogResult()) + "%");*/
        return row;
    }
}
