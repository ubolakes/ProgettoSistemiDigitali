package com.example.test;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.generazionekey.GenerazioneKeyController;
import com.example.test.databinding.FragmentSecondBinding;
import com.example.test.databinding.FragmentThirdBinding;
import com.example.utility.GestioneRumoreController;
import com.example.utility.TestNistController;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executor;

public class ThirdFragment extends Fragment {

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> provider;

    public static final String EXTRA_INFO = "default";

    private Button btnCapture;
    private FragmentThirdBinding binding;
    private TextView keys_tv, nistResult_tv;

    private final static int numImages = 1;
    private TestNistController testNistController;
    private GenerazioneKeyController keyController;
    private GestioneRumoreController gestioneRumoreController;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ){
        testNistController = new TestNistController();
        keyController = new GenerazioneKeyController();
        gestioneRumoreController = new GestioneRumoreController();

        binding = FragmentThirdBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //camera
        previewView = getView().findViewById(R.id.preview_view);
        keys_tv = getView().findViewById(R.id.keys_tv);
        keys_tv.setMovementMethod(new ScrollingMovementMethod());
        nistResult_tv = getView().findViewById(R.id.nist_result_tv);
        btnCapture = getView().findViewById(R.id.genera_keys);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //pulisco gli output
                nistResult_tv.setText("");
                keys_tv.setText("");

                Bitmap[] images = new Bitmap[numImages];
                //prendo le immagini per generare il seed
                for(int k = 0; k<numImages; k++){
                    images[k] = gestioneRumoreController.applicaRumore(previewView.getBitmap()); //le salvo con il rumore già applicato
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //genero seed e chiavi
                keyController.generaSeed(images);
                keyController.generaChiavi();
                //stampo esiti
                keys_tv.setText("Private Key :\n" + keyController.getPrivateKey() + "\n"
                        + "Public Key :\n" + keyController.getPublicKey());
                //verifico randomicità del seed
                testNistController.testRandomness(keyController.getSeed());
                String secLevel = testNistController.securityLevel();
                if (secLevel.equals("Not Secure")) nistResult_tv.setTextColor(Color.RED);
                if (secLevel.equals("Moderately Secure")) nistResult_tv.setTextColor(Color.YELLOW);
                if (secLevel.equals("Highly Secure")) nistResult_tv.setTextColor(Color.GREEN);
                nistResult_tv.setText(secLevel);
            }
        });

        provider = ProcessCameraProvider.getInstance(this.getContext());
        provider.addListener( () ->
        {
            try{
                ProcessCameraProvider cameraProvider = provider.get();
                startCamera(cameraProvider);
            }catch(Exception e){
                e.printStackTrace();
            }
        }, getExecutor());
    }

    private void startCamera(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector camSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.bindToLifecycle((LifecycleOwner)this, camSelector, preview);
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this.getContext());
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}