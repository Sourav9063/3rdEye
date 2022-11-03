 package com.example.thirdeye.user_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.thirdeye.Home;
import com.example.thirdeye.MainActivity;
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

import java.lang.ref.Reference;

 public class SignUpUser extends AppCompatActivity implements View.OnClickListener {
    private Button registerUser;
    private FirebaseAuth mAuth;
    private TextInputLayout editName, editEmail, editPass;
    private ImageButton back;
    FirebaseDatabase db;
    DatabaseReference reference;
     private PreferenceManager preferenceManager;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_sign_up_user);
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        registerUser= findViewById(R.id.sign_up_user);
        registerUser.setOnClickListener(this);
        editName= findViewById(R.id.name);
        editEmail = findViewById(R.id.email);
        editPass = findViewById(R.id.password);
        back=(ImageButton)findViewById(R.id.back_button_3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpUser.this, SignUpOptions.class);
                SignUpUser.this.startActivity(intent);
            }
        });


        preferenceManager = new PreferenceManager(getApplicationContext());
//        go_back_user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

    }
    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.sign_up_user:
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
                    User user = new User(name,"user", email, null);
                    db= FirebaseDatabase.getInstance();
                    reference= db.getReference("Users");

                    FirebaseUser newUser = task.getResult().getUser();
                    reference.child(newUser.getUid()).setValue(user);
                    preferenceManager.putString(Constants.KEY_USER_ID, newUser.getUid());
                    Log.d("user",newUser.getUid().toString());
                    preferenceManager.putString(Constants.KEY_ROLE,"user");
                    preferenceManager.putBoolean(Constants.KEY_SIGNED_IN,true);
                    preferenceManager.putString(Constants.KEY_USERNAME, name);
                    preferenceManager.putString(Constants.KEY_EMAIL, email);
                    Toast.makeText(SignUpUser.this,"user registered successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpUser.this, Home.class));

                }
                else
                {
                    Toast.makeText(SignUpUser.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}