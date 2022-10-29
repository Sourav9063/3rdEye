package com.example.thirdeye.user_registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.thirdeye.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}