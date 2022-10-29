package com.example.thirdeye.user_registration;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.thirdeye.R;

public class SignUpOptions extends AppCompatActivity {
    private Button  sign_up_options,sign_up_volunteer;
    private ImageView go_back_option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_options);
//        go_back_option= findViewById(R.id.back_button);
        sign_up_options = findViewById(R.id.sign_up_user);
        sign_up_volunteer = findViewById(R.id.button8);
        sign_up_volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent7 = new Intent(SignUpOptions.this, VolunteerSignup.class);
                startActivity(intent7);
//                Intent intent2 = new Intent(SignUpOptions.this, SignUpUser.class);
//                SignUpOptions.this.startActivity(intent2);
            }
        });
        sign_up_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent7 = new Intent(SignUpOptions.this, VolunteerSignup.class);
//                startActivity(intent7);
                Intent intent2 = new Intent(SignUpOptions.this, SignUpUser.class);
                SignUpOptions.this.startActivity(intent2);
            }
        });
//        go_back_option.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               finish();
//            }
//        });

    }
}