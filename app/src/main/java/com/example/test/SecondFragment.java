package com.example.test;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
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
                //genera password
                Bitmap bm = previewView.getBitmap();
                passwordController.generaPassword(bm);
                password_tv.setText(passwordController.getPassword());
                //verifica NIST
                testNistController.testPassword(passwordController.getPassword());

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

}