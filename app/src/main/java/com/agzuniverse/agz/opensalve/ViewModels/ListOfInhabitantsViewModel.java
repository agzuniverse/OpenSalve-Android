package com.agzuniverse.agz.opensalve.ViewModels;

import android.arch.lifecycle.ViewModel;

import com.agzuniverse.agz.opensalve.Modals.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListOfInhabitantsViewModel extends ViewModel {
    private List<Person> persons_fetched = new ArrayList<>();

    public List<Person> getListOfInhabs(int id, String apiUrl) {
        //TODO replace this dummy data with data fetched from API
        String[] fnames = new String[]{"Tony", "Steve", "Peter", "Natasha", "Peter", "Bruce", "Dead", "Steven"};
        String[] snames = new String[]{"Stark", "Rogers", "Quill", "Romanoff", "Parker", "Banner", "Pool", "Strange"};
        String[] bloodgrp = new String[]{"50", "37", "42", "30", "21", "54", "45", "43"};

        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(apiUrl + "/api/camps/c/" + Integer.toString(id) + "/inhabitants").build();
        try {
            Response res = client.newCall(req).execute();
            parse(res.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        for (int i = 0; i < fnames.length && i < snames.length && i < bloodgrp.length; i++) {
//            Person current = new Person(fnames[i], snames[i], bloodgrp[i]);
//            persons_fetched.add(current);
//        }
        return persons_fetched;
    }

    private void parse(String res) {
        try {
            JSONArray a = new JSONObject(res).getJSONArray("results");
            for (int i = 0; i < a.length(); i++) {
                JSONObject j = a.getJSONObject(i);
                Person current = new Person(j.getString("name"), j.getString("blood_group"));
                persons_fetched.add(current);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
