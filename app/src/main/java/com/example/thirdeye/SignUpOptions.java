package com.example.thirdeye;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpOptions extends AppCompatActivity {
    private Button go_back, sign_up,sign_up_volunteer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_options);
        go_back = findViewById(R.id.go_back);
        sign_up = findViewById(R.id.sign_up_user);
        sign_up_volunteer = findViewById(R.id.sign_up_volunteer);
        sign_up_volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpOptions.this, VolunteerSignup.class);
                startActivity(intent);
            }
        });

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

    }
}