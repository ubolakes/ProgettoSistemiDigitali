package com.example.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class NoticeDialogFragment extends DialogFragment {
    //notifica al fragment host
    public interface NoticeDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    //questa istanza comunica le azioni
    NoticeDialogListener listener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            listener = (NoticeDialogListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Character number selection")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //mando l'evento del click positivo all'host
                        listener.onDialogPositiveClick(NoticeDialogFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //mando l'evento del click negativo all'host
                        listener.onDialogNegativeClick(NoticeDialogFragment.this);
                    }
                });
        return builder.create();
    }

}
