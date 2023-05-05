package com.example.looptser;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.looptser.Fragments.Chat_Fragment;
import com.example.looptser.Fragments.Home_Fragment;
import com.example.looptser.Fragments.Notification_Fragment;
import com.example.looptser.Fragments.Profile_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottonnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadFragment(new Home_Fragment());
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
}