package com.example.fikrihaikal.printerless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Addnew extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "MESSAGE";
    public static final String EXTRA_EMAIL = "EMAIL";
    public static final String EXTRA_NAME = "NAME";
    Spinner jenisOpt;
    Button choose;
    String idpilih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew);
        Intent i = getIntent();
        final String email = i.getStringExtra(selectCompany.COMPANY_EMAIL);
        final String name = i.getStringExtra(selectCompany.COMPANY_NAME);
        jenisOpt = findViewById(R.id.jenisOpt);
        jenisOpt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idpilih = jenisOpt.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        choose = findViewById(R.id.btnChoose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (idpilih){
                    case "Document":
                        finish();
                        Toast.makeText(getApplicationContext(), idpilih, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Addnew.this, Addnew_doc.class);
                        i.putExtra(EXTRA_MESSAGE,"Document");
                        i.putExtra(EXTRA_EMAIL,email);
                        i.putExtra(EXTRA_NAME,name);
                        startActivity(i);
                        break;
                }
            }
        });

    }
}
