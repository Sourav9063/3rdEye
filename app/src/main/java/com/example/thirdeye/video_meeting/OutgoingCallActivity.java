package com.example.thirdeye.video_meeting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thirdeye.R;
import com.example.thirdeye.user_registration.User;
import com.example.thirdeye.utilities.Constants;
import com.example.thirdeye.utilities.PreferenceManager;
import com.example.thirdeye.video_meeting.network.ApiClient;
import com.example.thirdeye.video_meeting.network.ApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutgoingCallActivity extends AppCompatActivity{
        private String inviterCode = null;
        private String meetingType = "";
    private PreferenceManager preferenceManager;
    String meetingRoom = null;
    MediaPlayer ringing;
    private TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_call);
        preferenceManager = new PreferenceManager(getApplicationContext());

        getSupportActionBar().hide();
        ringing = MediaPlayer.create(OutgoingCallActivity.this,R.raw.ringing);
        ringing.setLooping(true);
        TextView textFirstChar = findViewById(R.id.textFirstChar);
        TextView textUsername = findViewById(R.id.textUsername);
        TextView textEmail = findViewById(R.id.textEmail);
        ImageView rejectCall = findViewById(R.id.imageStopInvitation);
        User user = (User) getIntent().getSerializableExtra("user");
        if(user!=null)
        {
            textFirstChar.setText(user.fullName.substring(0,1));
            textUsername.setText(user.fullName);
            textEmail.setText(user.email);
        }
        textToSpeech = new TextToSpeech(OutgoingCallActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    String text = "Calling "+ user.fullName;
                    textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
        rejectCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(user!=null) {
                   sendInvitationCancel(user.token);
                   onBackPressed();
               }
            }
        });
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful() && task.getResult()!=null) {
                            inviterCode = task.getResult();
                            if(user!=null)
                            {
                                initiateMeeting(user.token);
                            }
                        }

                    }
                });
     ringing.start();



    }
    private  void initiateMeeting(String receiverToken)
    {
     try{
         JSONArray tokens = new JSONArray();
         tokens.put(receiverToken);
         JSONObject data = new JSONObject();
         JSONObject body = new JSONObject();
         meetingRoom = preferenceManager.getString(Constants.KEY_USER_ID)+"_"+ UUID.randomUUID().toString().substring(0,5);
         data.put(Constants.REMOTE_MSG_TYPE,Constants.REMOTE_MSG_INVITATION);
         //Log.d("sharedPreference",preferenceManager.getString(Constants.KEY_USERNAME));
         data.put(Constants.KEY_USERNAME,preferenceManager.getString(Constants.KEY_USERNAME));
         data.put(Constants.KEY_EMAIL,preferenceManager.getString(Constants.KEY_EMAIL));
         data.put(Constants.REMOTE_MSG_INVITER_TOKEN,inviterCode);
         data.put(Constants.REMOTE_MSG_MEETING_ROOM,meetingRoom);
         body.put(Constants.REMOTE_MSG_DATA,data);
         body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens);
         sendRemoteMessage(body.toString(),Constants.REMOTE_MSG_INVITATION);
     }catch (Exception e){
         Toast.makeText(OutgoingCallActivity.this,e.getMessage()+"meeting"+preferenceManager.getString(Constants.KEY_EMAIL),Toast.LENGTH_SHORT).show();
         finish();
     }
    }


    private void sendRemoteMessage(String remoteMessageBody,String type)
    {
        ApiClient.getClient().create(ApiService.class).sendRemoteMessage(Constants.getRemoteMessageHeaders(),remoteMessageBody)
                .enqueue(new Callback<String>() {
                    @SuppressLint("SuspiciousIndentation")
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful())
                        {
                            if(type.equals(Constants.REMOTE_MSG_INVITATION))
                            Toast.makeText(OutgoingCallActivity.this,"Invitation sent successfully",Toast.LENGTH_SHORT).show();
                            else if(type.equals(Constants.REMOTE_MSG_INVITATION_REJECTED))
                            {
                                Toast.makeText(OutgoingCallActivity.this,"Invitation rejected",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(OutgoingCallActivity.this,t.getMessage()+"send",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }
    private void sendInvitationCancel( String receiverToken)
    {
        try{
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);
            JSONObject data = new JSONObject();
            JSONObject body = new JSONObject();
            data.put(Constants.REMOTE_MSG_TYPE,Constants.REMOTE_MSG_INVITATION_RESPONSE);
            //Log.d("sharedPreference",preferenceManager.getString(Constants.KEY_USERNAME));

            data.put(Constants.REMOTE_MSG_INVITATION_RESPONSE,Constants.REMOTE_MSG_INVITATION_CANCELLED);
            body.put(Constants.REMOTE_MSG_DATA,data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens);
            sendRemoteMessage(body.toString(),Constants.REMOTE_MSG_INVITATION_CANCELLED);

        }
        catch(Exception e)
        {
            Toast.makeText(OutgoingCallActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            finish();
        }

    }
    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constants.REMOTE_MSG_INVITATION_RESPONSE);
            if(type!=null)
            {
                if(type.equals(Constants.REMOTE_MSG_INVITATION_ACCEPTED))
                {
                    try {
                        URL serverURL = new URL("https://meet.jit.si");
                        JitsiMeetConferenceOptions conferenceOptions = new JitsiMeetConferenceOptions.Builder()
                                .setServerURL(serverURL)
                                .setRoom(meetingRoom).build();
                        JitsiMeetActivity.launch(OutgoingCallActivity.this,conferenceOptions);
                        Toast.makeText(context, "Invitation Accepted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }


                }
                else if(type.equals(Constants.REMOTE_MSG_INVITATION_REJECTED))
                {
                    Toast.makeText(context, "Invitation Rejected", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                invitationResponseReceiver, new IntentFilter(Constants.REMOTE_MSG_INVITATION_RESPONSE)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
                invitationResponseReceiver
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        ringing.release();
    }
}