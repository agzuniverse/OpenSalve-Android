package com.agzuniverse.agz.opensalve.ViewModels;

import android.arch.lifecycle.ViewModel;

import com.agzuniverse.agz.opensalve.Modals.News;

import java.util.ArrayList;
import java.util.List;

public class NewsViewModel extends ViewModel {
    private List<News> news = new ArrayList<>();

    public List<News> getNews() {
        //TODO fetch contacts from backend API
        News contact1 = new News("Fire Force", "1234567890");
        News contact2 = new News("Fire Force", "1234567890");
        news.add(contact1);
        news.add(contact2);
        return news;
    }
}
