package com.example.thirdeye.video_meeting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.example.thirdeye.R;
import com.example.thirdeye.user_registration.User;
import com.example.thirdeye.video_meeting.adapter.UsersAdapter;
import com.example.thirdeye.video_meeting.listeners.UserListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AllUserActivity extends AppCompatActivity implements UserListener {
   private List<User> users;
   private UsersAdapter usersAdapter;
    FirebaseDatabase db;
    DatabaseReference reference;
    private TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        textToSpeech = new TextToSpeech(AllUserActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    textToSpeech.speak("Volunteer Service",TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
        RecyclerView userRecyclerView = findViewById(R.id.userRecyclerView);
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(users,this);
        userRecyclerView.setAdapter(usersAdapter);
        getVolunteer();
    }
    private void getVolunteer(){
         db = FirebaseDatabase.getInstance();
         reference= db.getReference().child("Volunteer");
         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 users.clear();
                 for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                     User user = dataSnapshot.getValue(User.class);

                     users.add(user);

                     usersAdapter.notifyDataSetChanged();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
    }

    @Override
    public void initiateVideoMeeting(User user) {
        Intent intent = new Intent(getApplicationContext(),OutgoingCallActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);

        Toast.makeText(this, "video meeting with "+user.fullName, Toast.LENGTH_SHORT).show();
    }
}