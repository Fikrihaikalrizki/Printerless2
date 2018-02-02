package com.example.fikrihaikal.printerless;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;


public class LoginSignup extends AppCompatActivity{
    // UI references.
    TextView tv_already;
    EditText singup_email,signup_password;
    Button signup;
    ProgressBar suprogress;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        tv_already = findViewById(R.id.tv_already);
        singup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_password);
        signup = findViewById(R.id.email_sign_up_button);
        suprogress = findViewById(R.id.signup_progress);
        tv_already.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = singup_email.getText().toString();
        String password = signup_password.getText().toString().trim();
        if (username.isEmpty()){
            singup_email.setError("Email is Require");
            singup_email.requestFocus();
            return;
        }
//        if (Patterns.EMAIL_ADDRESS.matcher(username).matches()){
//            etusername.setError("Please Enter a Valid Email");
//            etusername.requestFocus();
//            return;
//        }
        if (password.isEmpty()){
            signup_password.setError("Password is Require");
            signup_password.requestFocus();
            return;
        }
        if (password.length()<6){
            signup_password.setError("Password is too short");
            signup_password.requestFocus();
            return;
        }
        suprogress.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                suprogress.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    finish();
                    startActivity(new Intent(LoginSignup.this,ProfileSetup.class));
                    Toast.makeText(getApplicationContext(),"User Regiter Succesfull",Toast.LENGTH_SHORT).show();
                }else{
                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"You are already regiter",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

