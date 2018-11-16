package com.agzuniverse.agz.opensalve;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.agzuniverse.agz.opensalve.Modals.News;
import com.agzuniverse.agz.opensalve.ViewModels.NewsViewModel;
import com.agzuniverse.agz.opensalve.adapters.NewsAdapter;

import java.util.List;

public class NewsScreen extends AppCompatActivity {
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
    }

    private int[] getColors() {
        return getResources().getIntArray(R.array.card_backgrounds);
    }
}
