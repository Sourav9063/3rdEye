package com.example.thirdeye.utilities;

import java.util.HashMap;

public class Constants {
    public static final  String KEY_PREFERENCE_NAME = "videoMeetingPreference";
    public static final String KEY_USER_ID ="user_id";
    public static final String KEY_USERNAME = "fullName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ROLE = "role";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_SIGNED_IN = "isSignedIn";
    public static final String KEY_FCM_TOKEN ="fcm_token";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";

    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_TYPE = "type";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";
    public static final String REMOTE_MSG_INVITATION = "invitation";
    public static  final String REMOTE_MSG_INVITER_TOKEN ="inviterToken";
    public static final String REMOTE_MSG_INVITATION_RESPONSE = "invitationResponse";
    public static final String REMOTE_MSG_INVITATION_ACCEPTED = "accepted";
    public static final String REMOTE_MSG_INVITATION_REJECTED = "rejected";
    public static final String REMOTE_MSG_INVITATION_CANCELLED = "cancelled";
    public static final String REMOTE_MSG_MEETING_ROOM = "meetingRoom";
    public static HashMap<String,String> getRemoteMessageHeaders(){
        HashMap<String,String> headers = new HashMap<>();
        headers.put(
                Constants.REMOTE_MSG_AUTHORIZATION,
              "key=AAAA9zyujoQ:APA91bEYD_JwHy48Icuu59AOLgcchK1Edg-T30XsjsZuZ5NHjc_pnv7EJDEnoK5zm_FRj6mUFITiGYgkEINaiLYJN5Wyr5S3rPAZ4sRh8YsfpijmLzxCXv_uSBn_sBAHHfjr9jOLaKdo"
        );
        headers.put(Constants.REMOTE_MSG_CONTENT_TYPE,"application/json");
        return headers;
    }
}
