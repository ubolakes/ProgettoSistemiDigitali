package com.example.utility;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.security.SecureRandom;
import java.util.Timer;

public class GestioneRumoreController implements ViewAggiungiRumore{

    private SecureRandom RNG;

    public GestioneRumoreController(){
        this.RNG = new SecureRandom();
        this.RNG.setSeed(System.currentTimeMillis());
    }

    @Override
    public Bitmap applicaRumore(Bitmap immagine) {
        Mat result = new Mat();
        Mat source = new Mat();
        Utils.bitmapToMat(immagine, source);
        float deviazione = this.RNG.nextFloat() + (float) 0.1;
        Bitmap processed_image = null;

        //aggiungo il rumore all'immagine
        Imgproc.GaussianBlur(source, result, new Size(3,3) ,deviazione);

        processed_image = Bitmap.createBitmap(result.cols(), result.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(result, processed_image);

        return processed_image;
    }
}
