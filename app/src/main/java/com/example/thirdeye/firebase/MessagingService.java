package com.example.thirdeye.firebase;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

//import com.google.firebase.messaging.Constants;
import com.example.thirdeye.utilities.Constants;
import com.example.thirdeye.video_meeting.IncomingCallActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token){
    super.onNewToken(token);
        Log.d("FCM", "Token" + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remotemessage) {
        super.onMessageReceived(remotemessage);
        String type = remotemessage.getData().get(Constants.REMOTE_MSG_TYPE);
//        if(remotemessage.getNotification()!=null)
//        {
//            Log.d("FCM", "Remote msg received" + remotemessage.getNotification().getBody());
//        }
        if(type!=null)
        {
            if(type.equals(Constants.REMOTE_MSG_INVITATION))
            {
                Intent intent = new Intent(getApplicationContext(), IncomingCallActivity.class);
                intent.putExtra(Constants.KEY_USERNAME,remotemessage.getData().get(Constants.KEY_USERNAME));
                intent.putExtra(Constants.KEY_EMAIL,remotemessage.getData().get(Constants.KEY_EMAIL));
                intent.putExtra(Constants.KEY_USERNAME,remotemessage.getData().get(Constants.KEY_USERNAME));
                intent.putExtra(Constants.REMOTE_MSG_INVITER_TOKEN,remotemessage.getData().get(Constants.REMOTE_MSG_INVITER_TOKEN));
                intent.putExtra(Constants.REMOTE_MSG_MEETING_ROOM,remotemessage.getData().get(Constants.REMOTE_MSG_MEETING_ROOM));
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
            else if(type.equals(Constants.REMOTE_MSG_INVITATION_RESPONSE))
            {
                Intent intent = new Intent(Constants.REMOTE_MSG_INVITATION_RESPONSE);
                intent.putExtra(Constants.REMOTE_MSG_INVITATION_RESPONSE,remotemessage.getData().get(Constants.REMOTE_MSG_INVITATION_RESPONSE));
              LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        }
    }
}
