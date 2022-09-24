package com.example.thirdeye;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpUser extends AppCompatActivity {
    private Button go_back_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        go_back_user=findViewById(R.id.go_back_user);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);
//        go_back_user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
    }
}