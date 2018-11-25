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

public class NewSupplyDialog extends DialogFragment {

    public interface AddSupplySubmit {
        void onAddNewSupply(DialogFragment dialog);
    }

    AddSupplySubmit listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (AddSupplySubmit) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.add_new_supply, null))
                .setPositiveButton(R.string.okay, (dialogInterface, i) -> listener.onAddNewSupply(NewSupplyDialog.this))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> NewSupplyDialog.this.getDialog().cancel());
        return builder.create();
    }
}
