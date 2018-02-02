package com.example.fikrihaikal.printerless;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Fikrihaikal on 28/01/2018.
 */

public class CompanyList extends ArrayAdapter<SaveComProfile> {
    private Activity conActivity;
    private List<SaveComProfile> comList;

    public CompanyList(Activity conActivity,List<SaveComProfile> comList){
        super(conActivity,R.layout.list_layout_company,comList);
        this.conActivity = conActivity;
        this.comList = comList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = conActivity.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout_company,null,true);
        TextView namaCompany = listViewItem.findViewById(R.id.namaCompany);
        TextView alamatCompany = listViewItem.findViewById(R.id.alamatCompany);
        SaveComProfile saveComProfile = comList.get(position);
        namaCompany.setText(saveComProfile.getNamajasa());
        alamatCompany.setText(saveComProfile.getAlamatjasa());
        return listViewItem;
    }
}
