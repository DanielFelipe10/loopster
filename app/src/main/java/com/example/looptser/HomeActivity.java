package com.example.looptser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.looptser.Fragments.Chat_Fragment;
import com.example.looptser.Fragments.Home_Fragment;
import com.example.looptser.Fragments.Notification_Fragment;
import com.example.looptser.Fragments.Profile_Fragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;

    private Bitmap uBitPerfil;
    private Bitmap uBitBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottonnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadFragment(new Home_Fragment());

        mStorage = FirebaseStorage.getInstance();

        StorageReference currstorageProfileImgRef = mStorage.getReference().child("users");
        StorageReference currstorageBgImgRef = mStorage.getReference().child("users");
        StorageReference storageProfileImgRef = mStorage.getReference().child("user_default_profile_img.png");
        StorageReference storageBgImgRef = mStorage.getReference().child("user_default_bg.png");

        // set the user images (profile / bg)
        try {
            final File profileFile = File.createTempFile("user_default_profile_img", "png");
            storageProfileImgRef.getFile(profileFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(profileFile.getAbsolutePath());
                    setuBitPerfil(bitmap);
                }
            });
            final File bgFile = File.createTempFile("user_default_bg", "png");
            storageBgImgRef.getFile(profileFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(profileFile.getAbsolutePath());
                    setuBitBg(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}