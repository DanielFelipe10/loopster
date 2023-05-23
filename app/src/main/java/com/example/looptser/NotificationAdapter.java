package com.example.looptser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.looptser.notifications.notifications.Notification;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    Context context;
    ArrayList<Notification> notificationArrayList;

    public NotificationAdapter(Context context, ArrayList<Notification> notificationsList) {
        this.context = context;
        this.notificationArrayList = notificationsList;
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Load the notification_item_row.xml layout to the context
        View v = LayoutInflater.from(context).inflate(R.layout.notification_item_row, parent, false);
        return new NotificationAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        //Load the data from the array for each notification_item_row.xml item
        Notification notification = notificationArrayList.get(position);

        holder.notificationName.setText(notification.getUserName());
        holder.notificationTime.setText(notification.getTime());
        holder.notificationImage.setImageResource(R.drawable.icon_loopster);
    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView notificationName;
        TextView notificationTime;
        CircleImageView notificationImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            notificationName = itemView.findViewById(R.id.notification_username);
            notificationTime = itemView.findViewById(R.id.notification_time);
            notificationImage = itemView.findViewById(R.id.notification_logo);
        }
    }
}
