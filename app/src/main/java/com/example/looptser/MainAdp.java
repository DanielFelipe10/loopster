package com.example.looptser;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdp extends RecyclerView.Adapter<MainAdp.ViewHolder> {
    ArrayList<Uri> arrayList;

    public MainAdp(ArrayList<Uri>arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main,parent,false);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iv_image.setImageURI(arrayList.get(position));
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageSwitcher iv_image;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView = itemView.findViewById(R.id.iv_image);
        }
    }
}
