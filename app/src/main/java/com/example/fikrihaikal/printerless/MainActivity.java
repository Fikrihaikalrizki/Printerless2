package com.example.fikrihaikal.printerless;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    TextView tv_dontready;
    EditText login_email,login_password;
    Button login;
    ProgressBar progress;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_dontready = findViewById(R.id.tv_dontalready);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login = findViewById(R.id.email_sign_in_button);
        progress = findViewById(R.id.login_progress);
        mAuth = FirebaseAuth.getInstance();
        tv_dontready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(getApplicationContext(),LoginSignup.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin() {
        String username = login_email.getText().toString();
        String password = login_password.getText().toString();
        if (username.isEmpty()){
            login_email.setError("Email is Require");
            login_email.requestFocus();
            return;
        }
//        if (Patterns.EMAIL_ADDRESS.matcher(username).matches()){
//            etusername.setError("Please Enter a Valid Email");
//            etusername.requestFocus();
//            return;
//        }
        if (password.isEmpty()){
            login_password.setError("Password is Require");
            login_password.requestFocus();
            return;
        }
        if (password.length()<6){
            login_password.setError("Password is too short");
            login_password.requestFocus();
            return;
        }
        progress.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progress.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    finish();
                    Intent i = new Intent(MainActivity.this, MainPage.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this,MainPage.class));
        }
    }
}
