package com.example.looptser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button openPrivateActivities;
    private TextView openSignupActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openSignupActivity = findViewById(R.id.login_register);
        openPrivateActivities = findViewById(R.id.login_action_btn);

        openSignupActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        openPrivateActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Here goes the authentication and redirection
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}