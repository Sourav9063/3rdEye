package com.example.thirdeye;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.example.thirdeye.user_registration.SignUP;

public class MainActivity extends AppCompatActivity {
    private Button move;
    private int IGNORE_BATTERY_OPTIMIZATION_REQUEST = 1002;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        move=findViewById(R.id.Move);
       // askIgnoreOptimization();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,SignUP.class));
                finish();

            }
        },3000);
    }

    private void checkForBatteryOptimization()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            if(!powerManager.isIgnoringBatteryOptimizations(getPackageName()))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("warning");
                builder.setMessage("Battery Optimization is enabled. It may interrupt app running");
                builder.setPositiveButton("Disable",(dialog,which)->{
                    Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                   // startActivityForResult(intent,REQUEST_CODE_BATTERY_OPTIMIZATION);
                });
                builder.setNegativeButton("Cancel",(dialog,which) -> {
                    dialog.dismiss();
                });
                builder.create().show();
            }
        }
    }
//    private void askIgnoreOptimization() {
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            @SuppressLint("BatteryLife") Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//            intent.setData(Uri.parse("package:" + getPackageName()));
//            startActivityForResult(intent, IGNORE_BATTERY_OPTIMIZATION_REQUEST);
//        }
//
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//      askIgnoreOptimization();
//
//}

}