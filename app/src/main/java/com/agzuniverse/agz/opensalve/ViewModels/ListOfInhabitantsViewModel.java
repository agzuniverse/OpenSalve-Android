package com.agzuniverse.agz.opensalve.ViewModels;

import android.arch.lifecycle.ViewModel;

import com.agzuniverse.agz.opensalve.Modals.Person;

import java.util.ArrayList;
import java.util.List;

public class ListOfInhabitantsViewModel extends ViewModel {
    private List<Person> persons_fetched = new ArrayList<>();

    public List<Person> getListOfInhabs() {
        //TODO replace this dummy data with data fetched from API
        String[] fnames = new String[]{"Tony", "Steve", "Peter", "Natasha", "Peter", "Bruce", "Dead", "Steven"};
        String[] snames = new String[]{"Stark", "Rogers", "Quill", "Romanoff", "Parker", "Banner", "Pool", "Strange"};
        Integer[] ages = new Integer[]{50, 37, 42, 30, 21, 54, 45, 43};
        for (int i = 0; i < fnames.length && i < snames.length && i < ages.length; i++) {
            Person current = new Person(fnames[i], snames[i], ages[i]);
            persons_fetched.add(current);
        }
        return persons_fetched;
    }
}
