package com.example.looptser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.example.looptser.users.Users;
import com.example.looptser.utils.DialogError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseStorage mStorage;

    private TextView goToLoginActivity;
    private ImageButton goBack;
    private Button signupBtn;

    private EditText userName;
    private EditText userEmail;
    private EditText userPassword;
    private EditText userConfirmPassword;

    ProgressDialog progressDialog;
    String userProfileImg;
    String userBackgroundImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Espera por favor...");
        progressDialog.setCancelable(false);

        Auth auth = new Auth();
        DialogError dialog = new DialogError();

        // firebase instances
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();

        // btn actions
        goToLoginActivity = findViewById(R.id.signup_go_login);
        goBack = findViewById(R.id.signup_go_back);
        signupBtn = findViewById(R.id.signup_btn_action);

        // input values
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
                 progressDialog.show();
                 String name = userName.getText().toString().trim();
                 String email = userEmail.getText().toString().trim();
                 String password = userPassword.getText().toString().trim();
                 String confirmPassword = userConfirmPassword.getText().toString().trim();

                if (auth.validateSignupData(name, email, password, confirmPassword)) {
                    createAccount(email, password, name);
                } else {
                    progressDialog.dismiss();
                    dialog.show(getSupportFragmentManager(), "Datos invalidos");
                }
            }
        });
    }

    private void createAccount(String email, String password, String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;

                            userProfileImg = "https://firebasestorage.googleapis.com/v0/b/looptser.appspot.com/o/user_default_profile_img.png?alt=media&token=ffcc28de-897b-48b9-b1cb-96c15f5a9ec6";
                            userBackgroundImg = "https://firebasestorage.googleapis.com/v0/b/looptser.appspot.com/o/user_default_bg.png?alt=media&token=d46bec9f-42e5-4a15-a166-c85167fc57ee";

                            DatabaseReference reference = mDatabase.getReference().child("user").child(user.getUid());
                            StorageReference storageReference = mStorage.getReference().child("uplod").child(user.getUid());

                            Users users = new Users(user.getUid(), name, email, userProfileImg, userBackgroundImg);

                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        updateUI(user);
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignupActivity.this, "Error in creating user.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
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