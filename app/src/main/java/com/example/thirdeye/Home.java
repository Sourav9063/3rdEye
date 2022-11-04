package com.example.thirdeye;

import static android.Manifest.permission.RECORD_AUDIO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.thirdeye.sos.Sos_Page;

import com.example.thirdeye.text_detection.TextDetectionActivity;
import com.example.thirdeye.utilities.Constants;
import com.example.thirdeye.utilities.PreferenceManager;
import com.example.thirdeye.video_meeting.AllUserActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import com.example.thirdeye.user_registration.ProfileActivity;
import com.example.thirdeye.user_registration.SignInUser;
import com.example.thirdeye.user_registration.SignUP;
import com.example.thirdeye.user_registration.VolunteerSignup;
import com.google.firebase.auth.FirebaseAuth;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

public class Home extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private Button sos;
    private Button video;
    private Button obstacleButton;
    private PreferenceManager preferenceManager;
    FirebaseDatabase db;
    DatabaseReference UserReference, VolunteerReference;
    private int REQUEST_CODE_BATTERY_OPTIMIZATION = 1;
    private static final int IGNORE_BATTERY_OPTIMIZATION_REQUEST = 1002;
    private ImageButton menu;
    private FirebaseAuth mAuth;
    private ImageButton back;
    private ImageButton mic;
    private Button volButton;
    private Button textDetection;
    private SpeechRecognizer speechRecognizer;
    private Intent intentRecognizer;
    private TextToSpeech textToSpeech;


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
        textDetection=(Button)findViewById(R.id.tri_text);
        menu= (ImageButton) findViewById(R.id.menu_button);
        mAuth=FirebaseAuth.getInstance();
        back=(ImageButton)findViewById(R.id.back_button);
        volButton=findViewById(R.id.volu_sign);
        mic = findViewById(R.id.mic);
        obstacleButton=findViewById(R.id.obstacle);

        obstacleButton.setOnClickListener(new MyOwnListener());
        //speech

        //obstacle


        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);
        intentRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        textToSpeech = new TextToSpeech(Home.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    String text = "Hello, Hope you are doing fine. Tap on the upper middle Screen and Speak, Text, Volunteer, obstacle  or Emergency to get Service.";
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String result = "";
                if (data != null) {
                    result = data.get(0);
                    if(result.contains("text")) {
                        Intent intent = new Intent(Home.this, TextDetectionActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("Text Detection", true);
                        startActivity(intent);
                        finish();
                    }
                    else if(result.contains("volunteer"))
                    {
                        Intent intent = new Intent(Home.this, AllUserActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("All user", true);
                        startActivity(intent);
                        finish();
                    }
                    else if(result.contains("emergency"))
                    {
                        Intent intent = new Intent(Home.this, Sos_Page.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("sos", true);
                        startActivity(intent);
                        finish();
                    }
                    else if(result.contains("obstacle")){
                        openObstacle();
                    }
                    else{
                        textToSpeech.speak("Please try again",TextToSpeech.QUEUE_FLUSH,null);
                    }

                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        textDetection.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, TextDetectionActivity.class));
        });
        mic.setOnClickListener(view -> {
            if(textToSpeech.isSpeaking()== false) {
                //textView.setText(String.valueOf(textToSpeech.isSpeaking()) );
                speechRecognizer.startListening(intentRecognizer);
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
                sendFcmToken(null);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this, SignUP.class));
                finish();
                break;

        }
        return super.onOptionsItemSelected(menuItem);
    }

    public class MyOwnListener implements View.OnClickListener
    {
        // ...

        @Override
        public void onClick(View v)
        {
          openObstacle();

//      Intent intent =new Intent("org.tensorflow.lite.examples.objectdetection.activities.MainActivity");
//      startActivity(intent);

        }
    }

    void openObstacle(){
        System.out.println("Button clicked//////////////////////////////////////////////////////////////////////////////////////////////////////////");
        String packageName = "org.tensorflow.lite.examples.classification";
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if(intent != null) {
            System.out.println("Button clicked//////////////////////////////////////////////////////////////////////////////////////////////////////////");
            startActivity(intent);

        }
        else {
            Intent intent2 = new Intent(Intent.ACTION_VIEW , Uri.parse("https://github.com/Sourav9063/obstacles_detection/releases/download/test/app-support-debug.apk"));
            startActivity(intent2);
        }

    }
}