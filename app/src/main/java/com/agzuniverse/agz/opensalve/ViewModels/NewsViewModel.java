package com.agzuniverse.agz.opensalve.ViewModels;

import android.arch.lifecycle.ViewModel;

import com.agzuniverse.agz.opensalve.Modals.News;

import java.util.ArrayList;
import java.util.List;

public class NewsViewModel extends ViewModel {
    private List<News> news = new ArrayList<>();

    public List<News> getNews() {
        //TODO fetch contacts from backend API
        News contact1 = new News("Electricity to Maradu will be cut off at midnight", "KSEB Engineer");
        News contact2 = new News("All educational institutions including professional colleges will have a holiday tomorrow", "Ernakulam District Collector");
        news.add(contact1);
        news.add(contact2);
        return news;
    }
}
