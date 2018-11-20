package com.agzuniverse.agz.opensalve.widgets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import com.agzuniverse.agz.opensalve.R;

public class NewCampDialog extends DialogFragment {

    public interface UpdateMap {
        void onAddNewCamp(DialogFragment dialog);
    }

    UpdateMap listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (UpdateMap) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.add_new_camp, null))
                .setPositiveButton(R.string.okay, (dialogInterface, i) -> {
                    listener.onAddNewCamp(NewCampDialog.this);
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> NewCampDialog.this.getDialog().cancel());
        return builder.create();
    }
}
