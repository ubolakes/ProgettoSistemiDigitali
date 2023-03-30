package com.example.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.fragment.app.DialogFragment;

public class CharacterNumberDialog extends DialogFragment {
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
                        //prendo il numero inserito e lo passo al controller
                    }
                })
                //ha senso metterlo?
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CharacterNumberDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }


}
