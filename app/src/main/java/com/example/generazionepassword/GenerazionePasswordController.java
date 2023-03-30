package com.example.generazionepassword;

import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class GenerazionePasswordController implements HomeGenerazionePassword{

    private String password;
    private int characterNumber;

    public GenerazionePasswordController(){
        this.password = new String();
        this.characterNumber = -1;
    }

    @Override
    public void generaPassword(Bitmap immagine) {
        //controllo che il numero di caratteri sia stato inserito
        byte[] bytePassword = new byte[this.characterNumber];
        if(characterNumber > 16 && characterNumber < 30){
            //genero la password
            //creo la struttura per immagazzinare i byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //converto l'immagine in array di byte
            immagine.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            int count = 0;
            byte temp;
            for(int i = 100; i < byteArray.length; i+=3){
                temp = byteArray[i];
                if((int)temp >= 33 && (int)temp <= 126 && !isPresent(bytePassword, count, temp)){
                    bytePassword[count] = temp;
                    count++;
                }
                if(count == this.characterNumber) break;
            }
            //errore: Non ho raggiunto i 20 caratteri diversi
            /*if(count < this.characterNumber){
                Toast.makeText(getApplicationContext(), "Error generating password: unable to read 20 different characters",
                        Toast.LENGTH_LONG).show();
            }*/
        }
        this.password = this.convert2String(bytePassword);
    }

    private String convert2String(byte[] password){
        String result = new String("");
        for(int i = 0; i < password.length; i++){
            result += (char) (password[i] & 0xFF);
        }
        return result;
    }

    private boolean isPresent(byte[] array, int maxIndex, byte element){
        for(int j = 0; j<maxIndex; j++){
            if(array[j] == element){
                return true;
            }
        }
        return false;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setCharacterNumber(int characterNumber){
        this.characterNumber = characterNumber;
    }
}
