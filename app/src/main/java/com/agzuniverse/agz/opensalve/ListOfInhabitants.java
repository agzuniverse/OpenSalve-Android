package com.agzuniverse.agz.opensalve;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.agzuniverse.agz.opensalve.Modals.Person;

import java.util.ArrayList;
import java.util.List;

public class ListOfInhabitants extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_inhabitants);

        //TODO replace this dummy data with data fetched from API
        List<Person> persons = new ArrayList<>();
        String[] fnames = {"Tony", "Steve", "Peter", "Natasha", "Peter", "Bruce", "Dead", "Steven"};
        String[] snames = {"Stark", "Rogers", "Quill", "Romanoff", "Parker", "Banner", "Pool", "Strange"};
        Integer[] ages = {50, 37, 42, 30, 21, 54, 45, 43};
        for (int i = 0; i < fnames.length && i < snames.length && i < ages.length; i++) {
            Person current = new Person();
            current.firstName = fnames[i];
            current.secondName = snames[i];
            current.age = ages[i];
            persons.add(current);
        }

        RecyclerView inhabs = findViewById(R.id.listOfInhabitants);
        RecyclerView.LayoutManager inhabManager = new LinearLayoutManager(this);
        RecyclerView.Adapter inhabAdapter = new ListOfInhabsAdapter(this, persons);
        inhabs.setLayoutManager(inhabManager);
        inhabs.setAdapter(inhabAdapter);
    }
}
