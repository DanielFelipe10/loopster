package com.example.looptser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.looptser.posts.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    Context context;
    ArrayList<Post> postArrayList;

    public PostAdapter(Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Load the post_item_row.xml layout to the context
        View v = LayoutInflater.from(context).inflate(R.layout.post_item_row, parent, false);
        return new PostAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.MyViewHolder holder, int position) {
        //Load the data from the array for each post_item_row.xml item
        Post post = postArrayList.get(position);

        holder.postDescription.setText(post.getPostDescription());
        holder.postDate.setText(post.getDate());
        Picasso.get().load(post.getPostImage()).into(holder.postImage);
        Picasso.get().load(post.getUserImage()).into(holder.postProfileImage);
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        TextView postDescription, postDate;
        CircleImageView postProfileImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.post_img);
            postDescription = itemView.findViewById(R.id.post_description);
            postProfileImage = itemView.findViewById(R.id.post_user_image);
            postDate = itemView.findViewById(R.id.post_date);
        }
    }
}
