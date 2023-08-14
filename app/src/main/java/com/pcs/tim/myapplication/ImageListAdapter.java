package com.pcs.tim.myapplication;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by PCS on 23/4/2018.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {
    private ArrayList<Bitmap> imageList;
    private final OnItemClickListener listener;
    View itemView;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageViewRefugee);
        }

        public void bind(final int position, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(position);
                    itemView.setBackgroundColor(Color.rgb(50,205,50));
                }
            });
        }
    }


    public ImageListAdapter(ArrayList<Bitmap> imageList, OnItemClickListener listener) {
        this.imageList = imageList;
        this.listener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Bitmap photo = imageList.get(position);
        holder.imageView.setImageBitmap(photo);
        holder.setIsRecyclable(false);

        if(listener!=null)
            holder.bind(position, listener);

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
