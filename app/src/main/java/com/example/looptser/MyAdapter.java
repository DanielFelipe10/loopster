package com.example.looptser;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.looptser.users.Users;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//Get the actual context and reuse the user_item_row.xml file
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<Users> usersArrayList;

    public MyAdapter(Context context, ArrayList<Users> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Load the user_item_row.xml layout to the context
        View v = LayoutInflater.from(context).inflate(R.layout.user_item_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Load the data from the array to each user_item_row.xml
        Users users = usersArrayList.get(position);
        holder.userName.setText(users.getName());
        Picasso.get().load(users.getImgUriProfile()).into(holder.userImg);

        // Open user chat
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userUid", users.getUid());
                intent.putExtra("userName", users.getName());
                intent.putExtra("userImg", users.getImgUriProfile());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImg;
        TextView userName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userImg = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
        }
    }
}