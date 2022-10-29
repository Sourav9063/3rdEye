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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInUser extends AppCompatActivity implements View.OnClickListener{
    private Button signIn;
    private TextInputLayout loginEmail,loginPass;
    private FirebaseAuth mAuth;



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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!= null)
        {
            startActivity(new Intent(SignInUser.this,Home.class));
            finish();
        }
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
                      startActivity(new Intent(SignInUser.this, Home.class));
                      Toast.makeText(SignInUser.this,"Welcome",Toast.LENGTH_SHORT).show();
                 }else{
                     Toast.makeText(SignInUser.this,"Failed to login! Please check your credentials",Toast.LENGTH_LONG).show();
                 }
            }
        });
    }
}