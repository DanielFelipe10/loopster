package com.example.looptser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private List<Messages> userMessagesList;
    private FirebaseAuth fAuth;
    private DatabaseReference userRef;

    public MessagesAdapter(List<Messages> userMessagesList) {
        this.userMessagesList = userMessagesList;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView senderMessageText, receiverMessageText;
        public CircleImageView senderProfileImage, receiverProfileImage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageText = (TextView) itemView.findViewById(R.id.sender_chat_user_message);
            senderProfileImage = (CircleImageView) itemView.findViewById(R.id.sender_chat_user_image);
            receiverMessageText = (TextView) itemView.findViewById(R.id.receiver_chat_user_message);
            receiverProfileImage = (CircleImageView) itemView.findViewById(R.id.receiver_chat_user_image);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_chat_msg, parent, false);
        fAuth = FirebaseAuth.getInstance();
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String messageSenderId = fAuth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(position);

        String fromUserId = messages.getFrom();
        String fromMessageType = messages.getType();

        //get the profile image for both users
        userRef = FirebaseDatabase.getInstance().getReference().child("user");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userProfileImg = "imgUriProfile";

                //get the receiver profile image
                if(snapshot.child(fromUserId).hasChild(userProfileImg)) {
                    String userRefImg = snapshot.child(fromUserId).child(userProfileImg).getValue().toString();
                    Picasso.get().load(userRefImg).into(holder.receiverProfileImage);
                }

                //get the sender profile image
                if(snapshot.child(messageSenderId).hasChild(userProfileImg)) {
                    String userRefImg = snapshot.child(messageSenderId).child(userProfileImg).getValue().toString();
                    Picasso.get().load(userRefImg).into(holder.senderProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        //alternate send msg layout and receiver msg layout
        if (fromMessageType.equals("text")) {
            holder.receiverMessageText.setVisibility(View.INVISIBLE);
            holder.receiverProfileImage.setVisibility(View.INVISIBLE);
            holder.receiverMessageText.setText(messages.getMessage());
            if (fromUserId.equals(messageSenderId)) {
                holder.senderMessageText.setText(messages.getMessage());
            } else {
                holder.senderMessageText.setVisibility(View.INVISIBLE);
                holder.senderProfileImage.setVisibility(View.INVISIBLE);
                holder.receiverProfileImage.setVisibility(View.VISIBLE);
                holder.receiverMessageText.setVisibility(View.VISIBLE);
                holder.senderMessageText.setText(messages.getMessage());
            }
        }
    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }
}
