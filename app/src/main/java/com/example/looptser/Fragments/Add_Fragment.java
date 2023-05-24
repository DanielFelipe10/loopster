package com.example.looptser.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.looptser.HomeActivity;
import com.example.looptser.R;
import com.example.looptser.notifications.notifications.Notification;
import com.example.looptser.notifications.notifications.Notifications;
import com.example.looptser.posts.Post;
import com.example.looptser.posts.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Add_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Add_Fragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    private ArrayList<Post> postsList;
    private ArrayList<Notification> notificationsList;

    private Uri path;
    private Button publishBtn;
    private EditText postDescription;
    private ImageView postImage;

    private String currUserId, userName, userProfile;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Add_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Add_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Add_Fragment newInstance(String param1, String param2) {
        Add_Fragment fragment = new Add_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_add_, container, false);

        HomeActivity homeActivity = (HomeActivity) getActivity();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        currUserId = auth.getCurrentUser().getUid();

        notificationsList = new ArrayList<>();
        postsList = new ArrayList<>();

        userName = homeActivity.getUserName();
        userProfile = homeActivity.getUserProfileUri();

        publishBtn = view.findViewById(R.id.add_publish);
        postDescription = view.findViewById(R.id.add_input);
        postImage = view.findViewById(R.id.add_img);

        DatabaseReference postReference = database.getReference("posts").child("postsList");
        DatabaseReference notificationReference = database.getReference("notifications").child("notificationsList");

        //get all posts
        postReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot ) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    postsList.add(post);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error ) { }
        });

        // get all notifications
        notificationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot ) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    notificationsList.add(notification);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error ) { }
        });

        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len = postsList.size()+1;
                String parseLen = Integer.toString(len);
                StorageReference storageReference = storage.getReference().child("users").child(currUserId).child("posts").child(parseLen+".jpg");

                Calendar calendar = Calendar.getInstance();

                //get current date mm/dd/yyyy
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                String saveCurrentDate = currentDate.format(calendar.getTime());

                //get current time hh:mm:ss pm/am
                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                String saveCurrentTime = currentTime.format(calendar.getTime());

                storageReference.putFile(path).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference postReferenceAdd = database.getReference().child("posts");
                                DatabaseReference notificationReferenceAdd = database.getReference().child("notifications");

                                String finalImgUri = uri.toString();
                                String description = postDescription.getText().toString();

                                //add new post
                                Post post = new Post(finalImgUri, description,currUserId, userName, userProfile,  saveCurrentDate);
                                postsList.add(post);
                                Posts posts = new Posts(postsList);

                                postReferenceAdd.setValue(posts).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });

                                // add new notification
                                Notification notification = new Notification(userName, userProfile,currUserId, saveCurrentTime);
                                notificationsList.add(notification);
                                Notifications notifications = new Notifications(notificationsList);

                                notificationReferenceAdd.setValue(notifications).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage();
            }
        });

        return view;
    }

    public void updateImage(){
        Intent intentUP = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentUP.setType("image/");
        startActivityForResult(intentUP.createChooser(intentUP,"Seleccione"),10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            path = data.getData();
            postImage.setImageURI(path);
        }
    }
}