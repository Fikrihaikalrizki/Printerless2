package com.example.fikrihaikal.printerless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class selectCompany extends AppCompatActivity {
    public static final String COMPANY_EMAIL = "EMAIL";
    public static final String COMPANY_NAME = "NAME";
    ListView companyList;
    DatabaseReference databaseCompany;
    List<SaveComProfile> comProfileList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_company);
        companyList = findViewById(R.id.comListView);
        databaseCompany = FirebaseDatabase.getInstance().getReference("Company");
        comProfileList = new ArrayList<>();
        companyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                finish();
                SaveComProfile saveComProfile = comProfileList.get(position);
                Intent i  = new Intent(selectCompany.this,Addnew.class);
                i.putExtra(COMPANY_EMAIL,saveComProfile.getEmailjasa());
                i.putExtra(COMPANY_NAME,saveComProfile.getNamajasa());
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseCompany.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comProfileList.clear();
                for (DataSnapshot companySnapshot: dataSnapshot.getChildren()){
                    SaveComProfile saveComProfile = companySnapshot.getValue(SaveComProfile.class);
                    comProfileList.add(saveComProfile);
                }
                CompanyList adapter = new CompanyList(selectCompany.this,comProfileList);
                companyList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
