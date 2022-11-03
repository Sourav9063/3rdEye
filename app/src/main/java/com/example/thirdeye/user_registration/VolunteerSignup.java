package com.example.thirdeye.user_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.thirdeye.Home;
import com.example.thirdeye.R;
import com.example.thirdeye.utilities.Constants;
import com.example.thirdeye.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VolunteerSignup extends AppCompatActivity implements View.OnClickListener{
    private Button go_back,registerVolunteer;
    private FirebaseAuth mAuth;
    private TextInputLayout editName, editEmail, editPass;
    FirebaseDatabase db;
    DatabaseReference reference;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_volunteer_signup);
        go_back=findViewById(R.id.go_back_vol);
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        registerVolunteer= findViewById(R.id.sign_up_vol);
       registerVolunteer.setOnClickListener(this);
        editName= findViewById(R.id.name_vol);
        editEmail = findViewById(R.id.email_vol);
        editPass = findViewById(R.id.pass_vol);
        preferenceManager = new PreferenceManager(getApplicationContext());
//        go_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_up_vol:
                registerUser();
                break;
        }
    }

    private void registerUser(){
        String name = editName.getEditText().getText().toString().trim();
        String email = editEmail.getEditText().getText().toString().trim();
        String password = editPass.getEditText().getText().toString().trim();

        if(name.isEmpty()){
            editName.setError("Please provide us your name!");
            editName.requestFocus();
            return ;
        }
        if(email.isEmpty()){
            editEmail.setError("email is required.");
            editEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Please provide us a valid email.");
            editEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editPass.setError("Password is required.");
            editPass.requestFocus();
            return;
        }
        if(password.length()<6){
            editPass.setError("Minimum required password length is 6.");
            editPass.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(name,"volunteer", email,null);
                    db= FirebaseDatabase.getInstance();
                    reference=db.getReference("Volunteer");

                    FirebaseUser newUser = task.getResult().getUser();
                    reference.child(newUser.getUid()).setValue(user);
                    preferenceManager.putString(Constants.KEY_USER_ID, newUser.getUid());
                    preferenceManager.putString(Constants.KEY_ROLE,"volunteer");
                    preferenceManager.putBoolean(Constants.KEY_SIGNED_IN,true);
                    preferenceManager.putString(Constants.KEY_USERNAME, name);
                    preferenceManager.putString(Constants.KEY_EMAIL, email);
                    Toast.makeText(VolunteerSignup.this,"Registration Completed",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(VolunteerSignup.this, Home.class));
                }else{
                    Toast.makeText(VolunteerSignup.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}