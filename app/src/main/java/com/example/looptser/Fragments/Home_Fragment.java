package com.example.looptser.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.looptser.PostAdapter;
import com.example.looptser.R;
import com.example.looptser.notifications.notifications.Notification;
import com.example.looptser.posts.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home_Fragment extends Fragment {

    private FirebaseAuth fAuth;
    private FirebaseDatabase database;
    private String currUserId;

    private RecyclerView postRecycler;
    private PostAdapter adapter;
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

        DatabaseReference reference = database.getReference("posts").child("postsList");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot ) {
                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                String fUserUid = fUser.getUid();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    if(!fUserUid.equals(post.getUserUid())) {
                        postsList.add(post);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error ) { }
        });

        Button mName = view.findViewById(R.id.testLog);

        mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                getActivity().finish();
            }
        });

        postRecycler = view.findViewById(R.id.recyclerView);
        postRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        postRecycler.setHasFixedSize(false);
        adapter = new PostAdapter(getContext(), postsList);
        postRecycler.setAdapter(adapter);

        return view;
    }
}