package com.example.fikrihaikal.printerless;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Fikrihaikal on 31/01/2018.
 */

public class TransaksiList extends ArrayAdapter<SaveTransaksi> {
    private Activity tranAc;
    private List<SaveTransaksi> traList;

    public TransaksiList(Activity tranAc, List<SaveTransaksi> traList){
        super(tranAc,R.layout.list_layout_transaksi,traList);
        this.tranAc = tranAc;
        this.traList = traList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = tranAc.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout_transaksi,null,true);
        TextView  dari = listViewItem.findViewById(R.id.tranDari);
        TextView jenisnya = listViewItem.findViewById(R.id.jenis);
        TextView status = listViewItem.findViewById(R.id.status);
        SaveTransaksi saveTransaksi = traList.get(position);
        String keputusan = saveTransaksi.getKeputusan();
        dari.setText(saveTransaksi.getNamaUser());
        jenisnya.setText(saveTransaksi.getJenis());
        if (keputusan.equalsIgnoreCase("Pending")){
            status.setTextColor(Color.RED);
        }else{
            status.setTextColor(Color.GREEN);
        }
        status.setText(keputusan);
        return listViewItem;
    }
}
