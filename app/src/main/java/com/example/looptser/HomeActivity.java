package com.example.looptser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.looptser.Fragments.Chat_Fragment;
import com.example.looptser.Fragments.Home_Fragment;
import com.example.looptser.Fragments.Notification_Fragment;
import com.example.looptser.Fragments.Profile_Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    private FirebaseStorage mStorage;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;

    private String userName, userEmail;
    private Bitmap uBitPerfil;
    private Bitmap uBitBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottonnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadFragment(new Home_Fragment());

        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        String currUserUid = user.getUid();

        mStorage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        StorageReference currStorageProfileImgRef = mStorage.getReference().child("users").child(currUserUid).child(currUserUid+"profile.jpg");
        StorageReference currStorageBgImgRef = mStorage.getReference().child("users").child(currUserUid).child(currUserUid+"bg.jpg");
        StorageReference storageProfileImgRef = mStorage.getReference().child("user_default_profile_img.png");
        StorageReference storageBgImgRef = mStorage.getReference().child("user_default_bg.png");

        try {
            //USER PROFILE IMAGE
            final File UserProfileFile = File.createTempFile(currStorageProfileImgRef.getName(), "jpg");
            currStorageProfileImgRef.getFile(UserProfileFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(UserProfileFile.getAbsolutePath());
                    setuBitPerfil(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // File not found
                    try {
                        getUserDefaultProfileImg("user_default_bg.png", storageProfileImgRef);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            //USER BG IMAGE
            final File UserBgFile = File.createTempFile(currUserUid+"bg", "jpg");
            currStorageBgImgRef.getFile(UserBgFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(UserBgFile.getAbsolutePath());
                    setuBitBg(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // File not found
                    try {
                        getUserDefaultBgImg("user_default_bg.png", storageBgImgRef);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        //    GET USER INFO
        DatabaseReference userRef = mDatabase.getReference().child("user").child(currUserUid);

        userRef.child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    userName = task.getResult().getValue().toString();
                }
            }
        });

        userRef.child("email").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    userEmail = task.getResult().getValue().toString();
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.dashbord:
                fragment = new Home_Fragment();
                break;
            case R.id.notification:
                fragment = new Notification_Fragment();
                break;
            case R.id.chat:
                fragment = new Chat_Fragment();
                break;
            case R.id.profile:
                fragment = new Profile_Fragment();
                break;
        }

        if (fragment != null) {
            loadFragment(fragment);
        }
        return true;
    }

    void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout, fragment).commit();
    }

    void getUserDefaultProfileImg(String name, StorageReference storageProfileImgRef) throws IOException {
        final File UserProfileFile = File.createTempFile(name, "png");
        storageProfileImgRef.getFile(UserProfileFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(UserProfileFile.getAbsolutePath());
                setuBitPerfil(bitmap);
            }
        });
    }

    void getUserDefaultBgImg(String name, StorageReference storageBgImgRef) throws IOException {
        final File UserBgFile = File.createTempFile(name, "png");
        storageBgImgRef.getFile(UserBgFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(UserBgFile.getAbsolutePath());
                setuBitBg(bitmap);
            }
        });
    }

    public Bitmap getuBitPerfil() {
        return uBitPerfil;
    }

    public void setuBitPerfil(Bitmap uBitPerfil) {
        this.uBitPerfil = uBitPerfil;
    }

    public Bitmap getuBitBg() {
        return uBitBg;
    }

    public void setuBitBg(Bitmap uBitBg) {
        this.uBitBg = uBitBg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}