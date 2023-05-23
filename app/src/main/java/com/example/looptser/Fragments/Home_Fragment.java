package com.example.looptser.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.looptser.R;
import com.example.looptser.notifications.notifications.Notification;
import com.example.looptser.notifications.notifications.Notifications;
import com.example.looptser.posts.Post;
import com.example.looptser.posts.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Home_Fragment extends Fragment {

    private FirebaseAuth fAuth;
    private FirebaseDatabase database;
    private String currUserId;

    private ArrayList<Post> postsList;
    private ArrayList<Notification> notificationsList;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Home_Fragment() {}

    public static Home_Fragment newInstance(String param1, String param2) {
        Home_Fragment fragment = new Home_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_, container, false);

        fAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currUserId = fAuth.getCurrentUser().getUid();

        postsList = new ArrayList<>();
        notificationsList = new ArrayList<>();

        Button addPost = view.findViewById(R.id.postTest);

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference postReference = database.getReference().child("posts");
                DatabaseReference notificationReference = database.getReference().child("notifications");

                Calendar calendar = Calendar.getInstance();

                //get current date mm/dd/yyyy
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                String saveCurrentDate = currentDate.format(calendar.getTime());

                //get current time hh:mm:ss pm/am
                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                String saveCurrentTime = currentTime.format(calendar.getTime());

                //add new post
                Post post = new Post("http://img", "description example",currUserId, "david", "http://localhost",  saveCurrentDate);
                postsList.add(post);
                Posts posts = new Posts(postsList);

                postReference.setValue(posts).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

                // add new notification
                Notification notification = new Notification("david", currUserId, saveCurrentTime);
                notificationsList.add(notification);
                Notifications notifications = new Notifications(notificationsList);

                notificationReference.setValue(notifications).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });

        Button mName = view.findViewById(R.id.testLog);

        mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                getActivity().finish();
            }
        });

        return view;
    }
}