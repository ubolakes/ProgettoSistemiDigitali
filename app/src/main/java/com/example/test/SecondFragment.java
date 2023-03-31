package com.example.test;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.example.generazionepassword.GenerazionePasswordController;
import com.example.test.databinding.FragmentSecondBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executor;

import com.example.utility.TestNistController;
import com.example.generazionepassword.HomeGenerazionePassword;

public class SecondFragment extends Fragment {

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> provider;
    private FragmentSecondBinding binding;

    public static final String EXTRA_INFO = "default";
    private Button btnCapture;
    private static final int Image_Capture_Code = 1;
    private GenerazionePasswordController passwordController;
    private TestNistController testNistController;
    private TextView password_tv, nistResult_tv, securityLevel_tv;

    private Bitmap bm; //la metto con scope globale in modo che si accessibile a tutti i metodi

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        passwordController = new GenerazionePasswordController();
        testNistController = new TestNistController();
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //camera
        previewView = getView().findViewById(R.id.preview_view);
        nistResult_tv = getView().findViewById(R.id.nist_result_tv);
        password_tv = getView().findViewById(R.id.password_tv);
        btnCapture = getView().findViewById(R.id.genera_password);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //catturo l'immagine
                bm = previewView.getBitmap();
                //chiedo all'utente la lunghezza della password
                showCustomDialog();
            }
        });

        provider = ProcessCameraProvider.getInstance(this.getContext());
        provider.addListener( () ->
        {
            try{
                ProcessCameraProvider cameraProvider = provider.get();
                startCamera(cameraProvider);
            } catch (Exception e)
            {
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //funzione per mostrare il dialogo
    private void showCustomDialog(){
        final Dialog dialog = new Dialog(this.getContext(), R.style.Dialog);
        //metto il titolo
        dialog.setTitle("Choose password length");
        //consente all'utente di cancellare il dialogo premendo al di fuori di esso
        dialog.setCancelable(true);
        //file xml del layout
        dialog.setContentView(R.layout.character_number_dialog);

        //inizializzo il dialogo
        final EditText numeroCaratteri_et = dialog.findViewById(R.id.numero_caratteri);
        Button confirm_bt = dialog.findViewById(R.id.confirm_bt);
        confirm_bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int numChar = -1;
                //controllo per evitare errori nel parsing
                if(!numeroCaratteri_et.getText().toString().isEmpty())
                        numChar = Integer.parseInt(numeroCaratteri_et.getText().toString());
                //controllo sul valore inserito
                if(numChar>=16 && numChar<=25 && numChar!=-1){
                    //posso generare la password
                    passwordController.setCharacterNumber(numChar);
                    passwordController.generaPassword(bm);
                    //faccio i test NIST
                    testNistController.testRandomness(passwordController.getPassword());
                    //mostro a schermo l'esito
                    password_tv.setText(passwordController.getPassword());
                    //modifico il colore della scritta in base al livello
                    String secLevel = testNistController.securityLevel();
                    if (secLevel.equals("Not Secure")) securityLevel_tv.setTextColor(Color.RED);
                    if (secLevel.equals("Moderately Secure")) securityLevel_tv.setTextColor(Color.YELLOW);
                    if (secLevel.equals("Highly Secure")) securityLevel_tv.setTextColor(Color.GREEN);
                    nistResult_tv.setText(secLevel);
                    //chiudo il dialog
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

}