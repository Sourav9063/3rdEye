package com.example.thirdeye;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.thirdeye.sos.Sos_Page;
import com.example.thirdeye.user_registration.ProfileActivity;
import com.example.thirdeye.user_registration.SignUP;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private Button sos;
    private Button video;
    private ImageButton menu;
    private FirebaseAuth mAuth;
    private ImageButton back;

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
        menu= (ImageButton) findViewById(R.id.menu_button);
        mAuth=FirebaseAuth.getInstance();
        back=(ImageButton)findViewById(R.id.back_button);

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Sos_Page.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void showPopup(View view) {
        PopupMenu popupMenu=new PopupMenu(this,view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.app_menu);
        popupMenu.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.item_1:
                startActivity(new Intent(Home.this, ProfileActivity.class));
                break;
            case R.id.item_2:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this, SignUP.class));
                finish();
                break;

        }
        return super.onOptionsItemSelected(menuItem);
    }



}