package com.agzuniverse.agz.opensalve;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.agzuniverse.agz.opensalve.Modals.Person;
import com.agzuniverse.agz.opensalve.ViewModels.ListOfInhabitantsViewModel;
import com.agzuniverse.agz.opensalve.adapters.ListOfInhabsAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListOfInhabitants extends AppCompatActivity {

    private String query;
    private List<Person> persons_original = new ArrayList<>(), persons_filtered;
    private android.support.v7.widget.RecyclerView.Adapter inhabAdapter;
    private android.support.v7.widget.RecyclerView.LayoutManager inhabManager;
    private android.support.v7.widget.RecyclerView inhabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_inhabitants);

        //Set ViewModel
        ListOfInhabitantsViewModel model = ViewModelProviders.of(this).get(ListOfInhabitantsViewModel.class);

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

        persons_original = model.getListOfInhabs();
        persons_filtered = new ArrayList<>(persons_original);
        inhabs = findViewById(R.id.listOfInhabitants);
        inhabManager = new LinearLayoutManager(this);
        inhabAdapter = new ListOfInhabsAdapter(this, persons_filtered);
        inhabs.setLayoutManager(inhabManager);
        inhabs.setAdapter(inhabAdapter);
    }

    protected void filter(String s) {
        s = s.toLowerCase();
        persons_filtered.clear();
        for (Person person : persons_original) {
            if (person.getFirstName().toLowerCase().contains(s)) {
                persons_filtered.add(person);
            } else if (person.getSecondName().toLowerCase().contains(s)) {
                persons_filtered.add(person);
            } else if ((person.getFirstName().toLowerCase() + ' ' + person.getSecondName().toLowerCase()).contains(s)) {
                persons_filtered.add(person);
            }
        }
        inhabAdapter.notifyDataSetChanged();
    }
}
