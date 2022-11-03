package com.example.thirdeye;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.PowerManager;
import android.provider.Settings;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.thirdeye.sos.Sos_Page;

import com.example.thirdeye.utilities.Constants;
import com.example.thirdeye.utilities.PreferenceManager;
import com.example.thirdeye.video_meeting.AllUserActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

import com.example.thirdeye.user_registration.ProfileActivity;
import com.example.thirdeye.user_registration.SignInUser;
import com.example.thirdeye.user_registration.SignUP;
import com.example.thirdeye.user_registration.VolunteerSignup;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class Home extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private Button sos;
    private Button video;
    private PreferenceManager preferenceManager;
    FirebaseDatabase db;
    DatabaseReference UserReference, VolunteerReference;
    private int REQUEST_CODE_BATTERY_OPTIMIZATION = 1;
    private static final int IGNORE_BATTERY_OPTIMIZATION_REQUEST = 1002;
    private ImageButton menu;
    private FirebaseAuth mAuth;
    private ImageButton back;
    private Button volButton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if(getSupportActionBar()!= null)
//        {
//            getSupportActionBar().hide();
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferenceManager = new PreferenceManager(getApplicationContext());
        sos = (Button) findViewById(R.id.tri_sos);
        video = (Button) findViewById(R.id.tri_video);
       // checkForBatteryOptimization();
       // askIgnoreOptimization();
        sos=(Button)findViewById(R.id.tri_sos);
        video=(Button)findViewById(R.id.tri_video);
        menu= (ImageButton) findViewById(R.id.menu_button);
        mAuth=FirebaseAuth.getInstance();
        back=(ImageButton)findViewById(R.id.back_button);
        volButton=findViewById(R.id.volu_sign);
        volButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, VolunteerSignup.class));
            }
        });

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Sos_Page.class));
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, AllUserActivity.class));
            }
        });
        FirebaseMessaging.getInstance ().getToken ()
                .addOnCompleteListener ( task -> {
                    if (!task.isSuccessful ()) {
                        return;
                    }
                    if (null != task.getResult ()) {
                        String firebaseMessagingToken = Objects.requireNonNull ( task.getResult () );
                        sendFcmToken(firebaseMessagingToken);
                    }
                } );


    }


    private void sendFcmToken(String token)
    {

     db= FirebaseDatabase.getInstance();
        UserReference = db.getReference("Users");
       VolunteerReference = db.getReference("Volunteer");
       if(preferenceManager.getString(Constants.KEY_ROLE).equals("user"))
             UserReference.child(preferenceManager.getString(Constants.KEY_USER_ID)).child("token").setValue(token);
        if(preferenceManager.getString(Constants.KEY_ROLE).equals("volunteer"))
               VolunteerReference.child(preferenceManager.getString(Constants.KEY_USER_ID)).child("token").setValue(token);
    }
//    private void checkForBatteryOptimization()
//    {
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
//        {
//            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
//            if(!powerManager.isIgnoringBatteryOptimizations(getPackageName()))
//            {
//                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
//                builder.setTitle("warning");
//                builder.setMessage("Battery Optimization is enabled. It may interrupt app running");
//                builder.setPositiveButton("Disable",(dialog,which)->{
//                    Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
//                    startActivityForResult(intent,REQUEST_CODE_BATTERY_OPTIMIZATION);
//                });
//                builder.setNegativeButton("Cancel",(dialog,which) -> {
//                    dialog.dismiss();
//                });
//                builder.create().show();
//            }
//        }
//    }
//private void askIgnoreOptimization() {
//
//    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//        @SuppressLint("BatteryLife") Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//        intent.setData(Uri.parse("package:" + getPackageName()));
//        startActivityForResult(intent, IGNORE_BATTERY_OPTIMIZATION_REQUEST);
//    }

//}
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        askIgnoreOptimization();
//
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == REQUEST_CODE_BATTERY_OPTIMIZATION)
//        {
//            checkForBatteryOptimization();
//        }
//    }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, SignInUser.class));
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