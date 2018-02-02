package com.example.fikrihaikal.printerless;

/**
 * Created by Fikrihaikal on 28/01/2018.
 */

public class SaveComProfile {
    String comID;
    String namajasa;
    String emailjasa;
    String nomorjasa;
    String alamatjasa;
    String kotajasa;

    public SaveComProfile(){

    }

    public SaveComProfile(String comID, String namajasa, String emailjasa, String nomorjasa, String alamatjasa, String kotajasa) {
        this.comID = comID;
        this.namajasa = namajasa;
        this.emailjasa = emailjasa;
        this.nomorjasa = nomorjasa;
        this.alamatjasa = alamatjasa;
        this.kotajasa = kotajasa;
    }

    public String getComID() {
        return comID;
    }

    public String getNamajasa() {
        return namajasa;
    }

    public String getEmailjasa() {
        return emailjasa;
    }

    public String getNomorjasa() {
        return nomorjasa;
    }

    public String getAlamatjasa() {
        return alamatjasa;
    }

    public String getKotajasa() {
        return kotajasa;
    }
}
