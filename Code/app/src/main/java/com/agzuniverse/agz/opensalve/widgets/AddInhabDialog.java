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

import java.util.Objects;

public class AddInhabDialog extends DialogFragment {

    public interface AddInhabSubmit {
        void onAddNewInhab(DialogFragment dialog);
    }

    AddInhabSubmit listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (AddInhabSubmit) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.add_new_inhab_diag, null))
                .setPositiveButton(R.string.okay, (dialogInterface, i) -> listener.onAddNewInhab(AddInhabDialog.this))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> AddInhabDialog.this.getDialog().cancel());
        return builder.create();
    }
}
