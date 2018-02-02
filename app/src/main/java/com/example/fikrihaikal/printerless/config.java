package com.example.fikrihaikal.printerless;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Fikrihaikal on 28/01/2018.
 */

public class config {
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    String namaFolder =  currentFirebaseUser.getUid();
    public String STORAGE_PATH_UPLOADS = namaFolder+"/";
    public String DATABASE_PATH_UPLOADS = "uploads";
}
