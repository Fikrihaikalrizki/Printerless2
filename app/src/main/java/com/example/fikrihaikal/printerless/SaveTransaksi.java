package com.example.fikrihaikal.printerless;

/**
 * Created by Fikrihaikal on 31/01/2018.
 */

public class SaveTransaksi {
    String id;
    String namaUser;
    String jenis;
    String ketentuan;
    String banyak;
    String alamatPrint;
    String folderPrint;
    String filePrint;
    String keputusan;

    public SaveTransaksi(){

    }

    public SaveTransaksi(String id,String namaUser, String jenis, String ketentuan, String banyak, String alamatPrint,String folderPrint,String filePrint,String keputusan) {
        this.id = id;
        this.namaUser = namaUser;
        this.jenis = jenis;
        this.ketentuan = ketentuan;
        this.banyak = banyak;
        this.alamatPrint = alamatPrint;
        this.folderPrint = folderPrint;
        this.filePrint = filePrint;
        this.keputusan = keputusan;
    }

    public String getId() {
        return id;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public String getJenis() {
        return jenis;
    }

    public String getKetentuan() {
        return ketentuan;
    }

    public String getBanyak() {
        return banyak;
    }

    public String getAlamatPrint() {
        return alamatPrint;
    }

    public String getFolderPrint() { return folderPrint; }

    public String getFilePrint() {
        return filePrint;
    }

    public String getKeputusan() {
        return keputusan;
    }
}
