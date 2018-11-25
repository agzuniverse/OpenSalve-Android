package com.agzuniverse.agz.opensalve.widgets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.agzuniverse.agz.opensalve.R;

public class NewCampDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private View v;
    private boolean isCamp;

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
        v = inflater.inflate(R.layout.add_new_camp, null);

        Spinner spinner = v.findViewById(R.id.camp_or_collection_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.camp_or_collection_spinner_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(NewCampDialog.this);

        builder.setView(v)
                .setPositiveButton(R.string.okay, (dialogInterface, i) -> {

                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> NewCampDialog.this.getDialog().cancel());


        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button button = d.getButton(Dialog.BUTTON_POSITIVE);
            button.setOnClickListener((View v) -> {
                listener.onAddNewCamp(NewCampDialog.this);
            });
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String s = parent.getItemAtPosition(pos).toString();
        if (s.equals(getResources().getStringArray(R.array.camp_or_collection_spinner_options)[0])) {
            EditText t = v.findViewById(R.id.new_camp_name);
            t.setHint(R.string.new_camp_name);
            isCamp = true;
        } else {
            EditText t = v.findViewById(R.id.new_camp_name);
            t.setHint(R.string.new_collection_name);
            isCamp = false;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }
}
