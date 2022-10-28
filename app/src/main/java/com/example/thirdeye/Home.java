package com.example.thirdeye;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.thirdeye.sos.Sos_Page;

public class Home extends AppCompatActivity {
    private Button sos;
    private Button video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
}