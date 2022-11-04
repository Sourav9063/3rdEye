package com.example.thirdeye.video_meeting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thirdeye.R;
import com.example.thirdeye.user_registration.User;
import com.example.thirdeye.utilities.Constants;
import com.example.thirdeye.video_meeting.network.ApiClient;
import com.example.thirdeye.video_meeting.network.ApiService;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomingCallActivity extends AppCompatActivity {
    MediaPlayer ringtone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);
        ringtone = MediaPlayer.create(IncomingCallActivity.this,R.raw.ringtone);
        ringtone.setLooping(true);
        getSupportActionBar().hide();
        TextView textUsername = findViewById(R.id.textUsername);
        TextView textEmail = findViewById(R.id.textEmail);
        TextView textFirstChar = findViewById(R.id.textFirstChar);
        String UserName = getIntent().getStringExtra(Constants.KEY_USERNAME);
        String Email = getIntent().getStringExtra(Constants.KEY_EMAIL);
        if(UserName!=null)
            textFirstChar.setText(UserName.substring(0,1));
        textEmail.setText(Email);
        textUsername.setText(UserName);
        ImageView imageInvitationAccepted = findViewById(R.id.imageAcceptInvitation);
        ImageView imageInvitationRejected = findViewById(R.id.imageRejectInvitation);

        imageInvitationAccepted.setOnClickListener(view -> sendInvitationResponse(Constants.REMOTE_MSG_INVITATION_ACCEPTED,getIntent().getStringExtra(Constants.REMOTE_MSG_INVITER_TOKEN)));
        imageInvitationRejected.setOnClickListener(view -> sendInvitationResponse(Constants.REMOTE_MSG_INVITATION_REJECTED,getIntent().getStringExtra(Constants.REMOTE_MSG_INVITER_TOKEN)));
        ringtone.start();


    }
    private void sendInvitationResponse(String type, String receiverToken)
    {
        try{
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);
            JSONObject data = new JSONObject();
            JSONObject body = new JSONObject();
            data.put(Constants.REMOTE_MSG_TYPE,Constants.REMOTE_MSG_INVITATION_RESPONSE);
            //Log.d("sharedPreference",preferenceManager.getString(Constants.KEY_USERNAME));

            data.put(Constants.REMOTE_MSG_INVITATION_RESPONSE,type);
            body.put(Constants.REMOTE_MSG_DATA,data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens);
            sendRemoteMessage(body.toString(),type);

        }
        catch(Exception e)
        {
            Toast.makeText(IncomingCallActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
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
                            if(type.equals(Constants.REMOTE_MSG_INVITATION_ACCEPTED)) {
                                Toast.makeText(IncomingCallActivity.this, "Invitation accepted", Toast.LENGTH_SHORT).show();
                            try{
                                URL serverURL = new URL("https://meet.jit.si");
                                JitsiMeetConferenceOptions conferenceOptions = new JitsiMeetConferenceOptions.Builder()
                                        .setServerURL(serverURL)
                                        .setWelcomePageEnabled(false)
                                        .setRoom(getIntent().getStringExtra(Constants.REMOTE_MSG_MEETING_ROOM)).build();
                                JitsiMeetActivity.launch(IncomingCallActivity.this,conferenceOptions);


                            }catch (Exception e)
                            {

                            }
                            }else {
                                Toast.makeText(IncomingCallActivity.this, "Invitation rejected", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }



                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(IncomingCallActivity.this,t.getMessage()+"send",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });


}
    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constants.REMOTE_MSG_INVITATION_RESPONSE);
            if(type!=null)
            {
                if(type.equals(Constants.REMOTE_MSG_INVITATION_CANCELLED))
                {

                    Toast.makeText(context, "Invitation Cancelled", Toast.LENGTH_SHORT).show();
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
        ringtone.release();
    }
    @Override
    protected void onPause() {

        super.onPause();
        ringtone.release();
    }
}