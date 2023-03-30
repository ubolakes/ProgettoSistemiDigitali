package com.example.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

public class CharacterNumberDialog extends DialogFragment {

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
    public Dialog onCreateDialog(Bundle SavedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Character number selection");

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.character_number_dialog, null))
                //bottone per esito positivo
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dalla parte del fragment prendo il valore nella casella di testo
                        listener.onDialogPositiveClick(CharacterNumberDialog.this);
                        System.out.println("CIAONE");
                    }
                })
                //ha senso metterlo?
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //mando l'evento del click negativo all'host
                        listener.onDialogNegativeClick(CharacterNumberDialog.this);
                        //chiudo la finestra di dialogo
                        CharacterNumberDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }



}
