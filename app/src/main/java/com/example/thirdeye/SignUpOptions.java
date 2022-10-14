package com.example.thirdeye;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpOptions extends AppCompatActivity {
    private Button go_back_option, sign_up_options,sign_up_volunteer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_options);
        go_back_option = findViewById(R.id.go_back_option);
        sign_up_options = findViewById(R.id.sign_up_user);
        sign_up_volunteer = findViewById(R.id.button8);
        sign_up_volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpOptions.this, VolunteerSignup.class);
                startActivity(intent);
            }
        });
        sign_up_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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