package com.example.generazionepassword;

import android.graphics.Bitmap;

public class GenerazionePasswordController implements HomeGenerazionePassword{

    private String password;

    public GenerazionePasswordController(){
        this.password = new String();
    }

    @Override
    public void generaPassword(Bitmap immagine) {
        //prendo l'immagine
        //la elaboro e produco la password

        this.password = "";
    }

    @Override
    public String getPassword() {
        return password;
    }
}
