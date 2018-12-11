package com.agzuniverse.agz.opensalve.widgets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.agzuniverse.agz.opensalve.R;

public class ConfirmDeleteCollectionDialog extends DialogFragment {
    private int id;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        }
        builder.setMessage(R.string.confirm_delete)
                .setPositiveButton(R.string.okay, (dialogInterface, i) -> {
                    Runnable runnable = () -> {
                        //TODO delete collection center from backend by passing id
                        getActivity().finish();
                    };
                    Thread async = new Thread(runnable);
                    async.start();
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> ConfirmDeleteCollectionDialog.this.getDialog().cancel()
                );
        return builder.create();
    }
}
