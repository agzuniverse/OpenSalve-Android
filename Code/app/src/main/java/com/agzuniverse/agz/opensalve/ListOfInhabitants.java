package com.agzuniverse.agz.opensalve;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.agzuniverse.agz.opensalve.Modals.Person;
import com.agzuniverse.agz.opensalve.Utils.GlobalStore;
import com.agzuniverse.agz.opensalve.ViewModels.ListOfInhabitantsViewModel;
import com.agzuniverse.agz.opensalve.adapters.ListOfInhabsAdapter;
import com.agzuniverse.agz.opensalve.widgets.AddInhabDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ListOfInhabitants extends AppCompatActivity implements AddInhabDialog.AddInhabSubmit {

    private String query;
    private int id;
    private ListOfInhabitantsViewModel model;
    private List<Person> persons_original = new ArrayList<>(), persons_filtered;
    private android.support.v7.widget.RecyclerView.Adapter inhabAdapter;
    private android.support.v7.widget.RecyclerView.LayoutManager inhabManager;
    private android.support.v7.widget.RecyclerView inhabs;
    private boolean showCloseButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_inhabitants);

        Bundle extraData = getIntent().getExtras();
        id = extraData.getInt("id", 0);

        //Set ViewModel
        model = ViewModelProviders.of(this).get(ListOfInhabitantsViewModel.class);

        //Set EditText field for searching in Action Bar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.action_bar_edit_text);
        actionBar.setDisplayShowCustomEnabled(true);

        EditText search = actionBar.getCustomView().findViewById(R.id.appbar_search_field);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                query = charSequence.toString();
                filter(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        FloatingActionButton fab = findViewById(R.id.inhab_fab);
        fab.setOnClickListener((View view) -> addNewInhab());

        if (GlobalStore.isVolunteer) {
            LinearLayout f = findViewById(R.id.inhab_fab_wrapper);
            f.setVisibility(View.VISIBLE);
            showCloseButton = true;
        }

        getListOfInhabsAsync();
    }

    private void getListOfInhabsAsync() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                persons_filtered = new ArrayList<>(persons_original);
                inhabs = findViewById(R.id.listOfInhabitants);
                inhabManager = new LinearLayoutManager(ListOfInhabitants.this);
                inhabAdapter = new ListOfInhabsAdapter(ListOfInhabitants.this, persons_filtered, showCloseButton, id);
                inhabs.setLayoutManager(inhabManager);
                inhabs.setAdapter(inhabAdapter);
            }
        };
        Runnable runnable = () -> {
            persons_original = model.getListOfInhabs(id, getResources().getString(R.string.base_api_url));
            handler.sendEmptyMessage(0);
        };
        Thread async = new Thread(runnable);
        async.start();
    }

    protected void filter(String s) {
        s = s.toLowerCase();
        persons_filtered.clear();
        for (Person person : persons_original) {
            if ((person.getName().toLowerCase()).contains(s)) {
                persons_filtered.add(person);
            }
        }
        inhabAdapter.notifyDataSetChanged();
    }

    public void addNewInhab() {
        DialogFragment dialog = new AddInhabDialog();
        dialog.show(getSupportFragmentManager(), "AddInhabDialog");
    }

    @Override
    public void onAddNewInhab(DialogFragment diag) {
        Dialog d = diag.getDialog();
        EditText t = d.findViewById(R.id.new_inhab_name);
        String name = t.getText().toString();
        t = d.findViewById(R.id.new_inhab_secondary_attribute);
        String attrib = t.getText().toString();
        Person p = new Person(name, attrib, 0);
        persons_filtered.add(p);
        inhabAdapter.notifyItemInserted(persons_filtered.size());
        Runnable runnable = () -> {
            String apiUrl = getResources().getString(R.string.base_api_url);
            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("name", p.getName())
                    .addFormDataPart("blood_group", p.getBloodgrp())
                    .addFormDataPart("location", "null")
                    .build();
            Request request = new Request.Builder()
                    .url(apiUrl + "/api/camps/c/" + id + "/inhabitants")
                    .header("Authorization", "Token " + GlobalStore.token)
                    .header("Content-Type", " application/x-www-form-urlencoded")
                    .post(requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Log.i("qwe", response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Thread async = new Thread(runnable);
        async.start();
    }


}
