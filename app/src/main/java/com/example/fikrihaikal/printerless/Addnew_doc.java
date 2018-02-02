package com.example.fikrihaikal.printerless;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Properties;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class Addnew_doc extends AppCompatActivity {
    private static final int CHOSE_FILE = 123;
    int columnIndex;
    Uri URI;
    DatabaseReference databaseTransaksi;
    StorageReference mStorageReference;
    String judul = "Printing : ",attachment;
    String dataPath;
    String namaFolder,namaFile,namaUser;
    long elapsed;
    String ukuranya,jumlahnya;
    TextView title;
    EditText jumlahCetak;
    Button pilihFile,send_request,pilihTempat;
    Spinner ukuranCetak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew_doc);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        databaseTransaksi = FirebaseDatabase.getInstance().getReference("Transaksi");
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        namaFolder =  currentFirebaseUser.getUid();
        namaUser = currentFirebaseUser.getEmail();
        elapsed = System.currentTimeMillis();
        namaFile = Long.toString(elapsed);
        title = findViewById(R.id.JenisTitle);
        pilihFile = findViewById(R.id.picDoc);
        send_request = findViewById(R.id.send);
        jumlahCetak = findViewById(R.id.banyak);
        ukuranCetak = findViewById(R.id.ukuran);
        pilihTempat = findViewById(R.id.tempat);
        Intent i = getIntent();
        String message = i.getStringExtra(Addnew.EXTRA_MESSAGE);
        title.setText(judul+message);
        String email = i.getStringExtra(Addnew.EXTRA_EMAIL);
        String name = i.getStringExtra(Addnew.EXTRA_NAME);
        pilihTempat.setText(name);
        ukuranCetak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ukuranya = ukuranCetak.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMail();
            }
        });
        pilihFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDocChosser();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOSE_FILE && resultCode == RESULT_OK && data != null && data.getData() != null){
            if (data.getData() != null) {
                //uploading the file 
                uploadFile(data.getData());
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }        
    }

    private void uploadFile(Uri data) {
        StorageReference sRef = mStorageReference.child(namaFolder+"/"+ namaFile + ".pdf");
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Addnew_doc.this,"Upload Sukses",Toast.LENGTH_SHORT).show();
                        pilihFile.setText("Uploaded");
                        Upload upload = new Upload(namaFolder, taskSnapshot.getDownloadUrl().toString());
                        //mDatabaseReference.child(mDatabaseReference.push().getKey()).setValue(upload);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        pilihFile.setText((int) progress + "% Uploading...");
                    }
                });

    }

    private void SendMail() {
        Intent i = getIntent();
        String message = i.getStringExtra(Addnew.EXTRA_MESSAGE);
        String email = i.getStringExtra(Addnew.EXTRA_EMAIL);
        String pengirim = namaUser;
        String jenisPrint = message;
        String ketentuanPrint = ukuranya;
        String banyakPrint = jumlahCetak.getText().toString();
        String alamatPrint = email;
        String folderPrint = namaFolder;
        String filePrint = namaFile;
        String keputusan = "Pending";
        if (banyakPrint.isEmpty()){
            jumlahCetak.setError("Masukan Banyak");
            jumlahCetak.requestFocus();
            return;
        }else{
            String id = databaseTransaksi.push().getKey();
            SaveTransaksi saveTransaksi = new SaveTransaksi(id,pengirim,jenisPrint,ketentuanPrint,banyakPrint,alamatPrint,folderPrint,filePrint,keputusan);
            databaseTransaksi.child(id).setValue(saveTransaksi);
            Toast.makeText(Addnew_doc.this,"Database Updated",Toast.LENGTH_SHORT).show();
        }
    }

    private void showDocChosser(){
        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent,"ChooseFile"), CHOSE_FILE);

    }
}
