package com.example.generazionekey;

import android.graphics.Bitmap;

import com.google.common.primitives.Bytes;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class GenerazioneKeyController implements HomeGenerazioneKey{

    private static int keySize = 2048;
    private byte[] seed;
    private KeyPair chiavi;
    private KeyPairGenerator generator;

    public GenerazioneKeyController(){
        try{
            this.generator = KeyPairGenerator.getInstance("RSA");
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    @Override
    public void generaSeed(Bitmap[] immagini) {
        MessageDigest messageDigest;
        ByteArrayOutputStream stream;
        byte[] byteArray;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            //prendo le immagini, ne faccio gli hash e li concateno
            for(int k = 0; k < immagini.length; k++){
                //converto la bitmap in array di byte
                stream = new ByteArrayOutputStream();
                immagini[k].compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();

                //ne faccio l'hash e li concateno
                this.seed = Bytes.concat(this.seed, messageDigest.digest(byteArray));
            }
        }catch (NoSuchAlgorithmException e){}
    }

    @Override
    public void generaChiavi() {
        SecureRandom initializer = new SecureRandom(this.seed);
        this.generator.initialize(keySize, initializer);
        //genero la coppia di chiavi
        this.chiavi = this.generator.generateKeyPair();
    }

    public String getPublicKey(){
        return this.chiavi.getPublic().toString();
    }

    public String getPrivateKey(){
        return this.chiavi.getPrivate().toString();
    }

    public String getSeed(){
        return new String(this.seed, StandardCharsets.UTF_8);
    }

}
