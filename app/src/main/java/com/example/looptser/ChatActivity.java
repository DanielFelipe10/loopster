package com.example.looptser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    CircleImageView profileImg;
    ImageView goBackArrow;
    TextView nameText;

    String reciveUserName, reciveUserUid, reciveUserImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        reciveUserName = getIntent().getStringExtra("userName");
        reciveUserUid = getIntent().getStringExtra("userUid");
        reciveUserImg = getIntent().getStringExtra("userImg");

        profileImg = findViewById(R.id.contact_img);
        goBackArrow = findViewById(R.id.contact_back_arrow);
        nameText = findViewById(R.id.contact_name);

        Picasso.get().load(reciveUserImg).into(profileImg);
        nameText.setText(reciveUserName);

        goBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}