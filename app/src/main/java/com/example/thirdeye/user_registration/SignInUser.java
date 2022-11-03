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
import com.example.thirdeye.R;
import com.example.thirdeye.utilities.Constants;
import com.example.thirdeye.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignInUser extends AppCompatActivity implements View.OnClickListener{
    private Button signIn;
    private TextInputLayout loginEmail,loginPass;
    private FirebaseAuth mAuth;

    private PreferenceManager preferenceManager;
    FirebaseDatabase db;
    DatabaseReference reference;

    private ImageButton back;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_user);
        signIn = findViewById(R.id.sign_in_sign);
        signIn.setOnClickListener(this);

        loginEmail = findViewById(R.id.emailLogin);
        loginPass= findViewById(R.id.passwordLogin);
        mAuth = FirebaseAuth.getInstance();

        preferenceManager = new PreferenceManager(getApplicationContext());

        back=(ImageButton)findViewById(R.id.back_button);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!= null)
        {
            startActivity(new Intent(SignInUser.this,Home.class));
            finish();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInUser.this, SignUP.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_in_sign:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = loginEmail.getEditText().getText().toString().trim();
        String password = loginPass.getEditText().getText().toString().trim();
        if(email.isEmpty()){
            loginEmail.setError("email is required.");
            loginEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            loginEmail.setError("Please provide us a valid email.");
            loginEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            loginPass.setError("Password is required.");
            loginPass.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful()){
                     db= FirebaseDatabase.getInstance();
                     String uid = task.getResult().getUser().getUid();
                     reference= db.getReference();
                     //DatabaseReference uidRef = reference.child("Users").child(uid);
                     Log.d("uid", "onComplete: " + uid);
                     reference.child("Users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                         @Override
                         public void onComplete(@NonNull Task<DataSnapshot> task) {
                             if (!task.isSuccessful()&& task.getResult()==null) {
                                 Log.e("firebase", "Error getting data", task.getException());
                             }
                             else if(task.getResult().getValue()!=null) {

                                 User user = task.getResult().getValue(User.class);
                                 Toast.makeText(SignInUser.this,"Welcome "+ user.fullName,Toast.LENGTH_SHORT).show();

                                preferenceManager.putString(Constants.KEY_USER_ID, uid);
                                preferenceManager.putBoolean(Constants.KEY_SIGNED_IN,true);
                                preferenceManager.putString(Constants.KEY_USERNAME, user.fullName);
                                preferenceManager.putString(Constants.KEY_EMAIL, user.email);
                                preferenceManager.putString(Constants.KEY_ROLE, user.role);
                                startActivity(new Intent(SignInUser.this, Home.class));


                                 Log.d("firebase", String.valueOf(task.getResult().getValue()));
                             }
                         }
                     });
                     reference.child("Volunteer").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                         @Override
                         public void onComplete(@NonNull Task<DataSnapshot> task) {
                             if (!task.isSuccessful()) {
                                 Log.e("firebase", "Error getting data", task.getException());
                             }
                             else if(task.getResult().getValue()!=null) {

                                 User user = task.getResult().getValue(User.class);
                                 Toast.makeText(SignInUser.this,"Welcome "+ user.fullName,Toast.LENGTH_SHORT).show();

                                 preferenceManager.putString(Constants.KEY_USER_ID, uid);
                                 preferenceManager.putBoolean(Constants.KEY_SIGNED_IN,true);
                                 preferenceManager.putString(Constants.KEY_USERNAME, user.fullName.toString());
                                 preferenceManager.putString(Constants.KEY_EMAIL, user.email);
                                 preferenceManager.putString(Constants.KEY_ROLE, user.role);
                                 startActivity(new Intent(SignInUser.this, Home.class));


                                 Log.d("firebase", String.valueOf(task.getResult().getValue()));
                             }
                         }
                     });

                 }else{
                     Toast.makeText(SignInUser.this,"Failed to login! Please check your credentials",Toast.LENGTH_LONG).show();
                 }
            }
        });
    }
}