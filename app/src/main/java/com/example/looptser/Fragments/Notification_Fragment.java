package com.example.looptser.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.looptser.NotificationAdapter;
import com.example.looptser.R;
import com.example.looptser.notifications.notifications.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Notification_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notification_Fragment extends Fragment {

    FirebaseAuth auth;
    FirebaseDatabase database;

    RecyclerView notificationRecycler;
    NotificationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    ArrayList<Notification> notificationsList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Notification_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Notification_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Notification_Fragment newInstance(String param1, String param2) {
        Notification_Fragment fragment = new Notification_Fragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        notificationsList = new ArrayList<>();

        DatabaseReference reference = database.getReference("notifications").child("notificationsList");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot ) {
                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                String fUserUid = fUser.getUid();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    if(!fUserUid.equals(notification.getUserUid())) {
                        notificationsList.add(notification);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error ) { }
        });

        notificationRecycler = view.findViewById(R.id.notification_recycler);
        notificationRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationRecycler.setHasFixedSize(false);
        adapter = new NotificationAdapter(getContext(), notificationsList);
        notificationRecycler.setAdapter(adapter);

        return view;
    }
}