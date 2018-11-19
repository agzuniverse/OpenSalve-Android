package com.agzuniverse.agz.opensalve;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.agzuniverse.agz.opensalve.Modals.News;
import com.agzuniverse.agz.opensalve.ViewModels.NewsViewModel;
import com.agzuniverse.agz.opensalve.adapters.NewsAdapter;

import java.util.List;

public class NewsScreen extends AppCompatActivity {
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);

        NewsViewModel model = ViewModelProviders.of(this).get(NewsViewModel.class);
        List<News> data = model.getNews();

        RecyclerView news = findViewById(R.id.news_list);
        RecyclerView.LayoutManager newsLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter newsAdapter = new NewsAdapter(this, data, getColors());
        news.setAdapter(newsAdapter);
        news.setLayoutManager(newsLayoutManager);

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        if (prefs.getInt("isVolunteer", 0) == 1) {
            LinearLayout f = findViewById(R.id.news_fab_wrapper);
            f.setVisibility(View.VISIBLE);
            token = prefs.getString("token", "0");
        }
    }

    private int[] getColors() {
        return getResources().getIntArray(R.array.card_backgrounds);
    }

    public void addNewNews() {

    }
}
