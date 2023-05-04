package com.example.looptser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.looptser.auth.Auth;
import com.example.looptser.utils.DialogError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;

    private TextView goToLoginActivity;
    private ImageButton goBack;
    private Button signupBtn;

    private EditText userName;
    private EditText userEmail;
    private EditText userPassword;
    private EditText userConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Auth auth = new Auth();
        DialogError dialog = new DialogError();

        mAuth = FirebaseAuth.getInstance();

        goToLoginActivity = findViewById(R.id.signup_go_login);
        goBack = findViewById(R.id.signup_go_back);
        signupBtn = findViewById(R.id.signup_btn_action);

        userName = findViewById(R.id.signup_input_user);
        userEmail = findViewById(R.id.signup_input_email);
        userPassword = findViewById(R.id.signup_input_password);
        userConfirmPassword = findViewById(R.id.signup_input_confirm_password);


        goToLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String name = userName.getText().toString().trim();
                 String email = userEmail.getText().toString().trim();
                 String password = userPassword.getText().toString().trim();
                 String confirmPassword = userConfirmPassword.getText().toString().trim();

                if (auth.validateSignupData(name, email, password, confirmPassword)) {
                    createAccount(email, password);
                } else {
                    dialog.show(getSupportFragmentManager(), "Datos invalidos");
                }
            }
        });
    }



    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }



    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}