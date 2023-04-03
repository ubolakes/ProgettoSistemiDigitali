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
import java.util.regex.Pattern;

public class GenerazioneKeyController implements HomeGenerazioneKey{

    //vale per MD5
    //128 bit == 16 byte
    private static int hashOutputLength = 16;
    private static int keySize = 2048;
    private byte[] seed;
    private KeyPair chiavi;
    private KeyPairGenerator generator;

    public GenerazioneKeyController(){
        this.seed = new byte[0];
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

        //prendo le bitmap
        //le converto in array di byte e li concateno
        for(int i = 0; i < immagini.length; i++) {
            stream = new ByteArrayOutputStream();
            immagini[i].compress(Bitmap.CompressFormat.PNG, 100, stream);
            this.seed = concat(this.seed, stream.toByteArray());
        }

        /*VECCHIA IMPLEMENTAZIONE*/
        /*
        this.seed = new byte[immagini.length * hashOutputLength];

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
    */
    }

    //metodo per concatenare due array di byte
    private byte[] concat(byte[] array1, byte[] array2){
        int length = array1.length + array2.length;

        byte[] result = new byte[length];
        int pos = 0;
        for (byte element : array1) {
            result[pos] = element;
            pos++;
        }

        for (byte element : array2) {
            result[pos] = element;
            pos++;
        }
        return result;
    }

    @Override
    public void generaChiavi() {
        SecureRandom initializer = new SecureRandom(this.seed);
        this.generator.initialize(keySize, initializer);
        //genero la coppia di chiavi
        this.chiavi = this.generator.generateKeyPair();
    }

    public String getPublicKey(){
        String mod = this.chiavi.getPublic().toString().split(",")[0].split("=")[1];
        String exp = this.chiavi.getPublic().toString().split("=")[2].split(Pattern.quote("}"))[0];
        return "Module: " + mod + "\nExponent: " + exp;
    }

    public String getPrivateKey() {
        String mod = this.chiavi.getPrivate().toString().split(",")[0].split("=")[1];
        String exp = this.chiavi.getPrivate().toString().split("=")[2].split(Pattern.quote("}"))[0];
        return "Module: " + mod + "\nExponent: " + exp;
    }

    public String getSeed(){
        return new String(this.seed, StandardCharsets.UTF_8);
    }

}
