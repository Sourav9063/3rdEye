package com.example.thirdeye.user_registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.thirdeye.Home;
import com.example.thirdeye.MainActivity;
import com.example.thirdeye.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUP extends AppCompatActivity {

    private Button sign_in, sign_up;
    private FirebaseAuth mAuth;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_u_p);
        sign_in = findViewById(R.id.sign_in);
        sign_up = findViewById(R.id.sign_up);
        mAuth = FirebaseAuth.getInstance();
        back=(ImageButton)findViewById(R.id.back_button_1);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!= null)
        {
            startActivity(new Intent(SignUP.this, Home.class));
            finish();
        }
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUP.this, SignUpOptions.class);
                startActivity(intent);
            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUP.this, SignInUser.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUP.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}