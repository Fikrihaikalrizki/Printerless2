package com.example.fikrihaikal.printerless;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileSetup extends AppCompatActivity {
    private static final int CHOSE_IMAGE = 101;
    ImageView imgprofile;
    TextView tvverified;
    EditText txtnama;
    Button btnsave;
    ProgressBar loadimage;
    Uri uriProfile;
    String profileImageUrl;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        mAuth = FirebaseAuth.getInstance();
        imgprofile = findViewById(R.id.imgprofile);
        txtnama = findViewById(R.id.txtname);
        btnsave = findViewById(R.id.btnsave);
        loadimage = findViewById(R.id.loadimage);
        tvverified = findViewById(R.id.tvverivied);
        loadUser();
        imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChosser();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }
    private void loadUser() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.getPhotoUrl() != null){
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(imgprofile);
            }if (user.getDisplayName() != null){
                txtnama.setText(user.getDisplayName());
            }if(user.isEmailVerified()){
                tvverified.setText("Email is Verified");
            }else{
                tvverified.setText("Email is Not Verified, Click to verified");
                tvverified.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ProfileSetup.this,"Verification Email Sent",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriProfile =  data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfile);
                imgprofile.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void saveUser() {
        String displayname = txtnama.getText().toString();
        if (displayname.isEmpty()){
            txtnama.setError("Name is Require");
            txtnama.requestFocus();
            return;
        }
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && profileImageUrl != null ){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayname)
                    .setPhotoUri(Uri.parse(profileImageUrl)).build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        finish();
                        startActivity(new Intent(ProfileSetup.this,MainPage.class));
                        Toast.makeText(ProfileSetup.this,"Profile Updated",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void uploadImage() {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if (uriProfile != null){
            loadimage.setVisibility(View.VISIBLE);
            mStorageRef.putFile(uriProfile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    loadimage.setVisibility(View.GONE);
                    profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadimage.setVisibility(View.GONE);
                            Toast.makeText(ProfileSetup.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void showImageChosser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Profile Image"),CHOSE_IMAGE);
    }
}
