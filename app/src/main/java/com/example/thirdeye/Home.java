package com.example.thirdeye;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.example.thirdeye.sos.Sos_Page;

public class Home extends AppCompatActivity {
    private Button sos;
    private Button video;
    private Button menu;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if(getSupportActionBar()!= null)
//        {
//            getSupportActionBar().hide();
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sos=(Button)findViewById(R.id.tri_sos);
        video=(Button)findViewById(R.id.tri_video);

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Sos_Page.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.app_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}