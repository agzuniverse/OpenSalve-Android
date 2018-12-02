package com.agzuniverse.agz.opensalve;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.agzuniverse.agz.opensalve.Modals.News;
import com.agzuniverse.agz.opensalve.Utils.GlobalStore;
import com.agzuniverse.agz.opensalve.ViewModels.NewsViewModel;
import com.agzuniverse.agz.opensalve.adapters.NewsAdapter;
import com.agzuniverse.agz.opensalve.widgets.AddNewsDialog;

import java.util.List;

public class NewsScreen extends AppCompatActivity implements AddNewsDialog.AddNewsSubmit {
    private String token;
    private RecyclerView.Adapter newsAdapter;
    private List<News> data;
    private boolean showCloseButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);

        NewsViewModel model = ViewModelProviders.of(this).get(NewsViewModel.class);
        data = model.getNews();

        FloatingActionButton fab = findViewById(R.id.news_fab);
        fab.setOnClickListener((View view) -> addNewNews());

        if (GlobalStore.isVolunteer) {
            SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
            token = prefs.getString("token", "0");
            LinearLayout f = findViewById(R.id.news_fab_wrapper);
            f.setVisibility(View.VISIBLE);
            showCloseButton = true;
        }

        RecyclerView news = findViewById(R.id.news_list);
        RecyclerView.LayoutManager newsLayoutManager = new LinearLayoutManager(this);
        newsAdapter = new NewsAdapter(this, data, getColors(), showCloseButton);
        news.setAdapter(newsAdapter);
        news.setLayoutManager(newsLayoutManager);
    }

    private int[] getColors() {
        return getResources().getIntArray(R.array.card_backgrounds);
    }

    public void addNewNews() {
        DialogFragment diag = new AddNewsDialog();
        diag.show(getSupportFragmentManager(), "add_news");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Dialog dialogview = dialog.getDialog();
        EditText bodyText = dialogview.findViewById(R.id.new_news_body);
        EditText authorText = dialogview.findViewById(R.id.new_news_author);
        String body = bodyText.getText().toString();
        String author = authorText.getText().toString();
        News news = new News(body, author, 0);
        data.add(news);
        newsAdapter.notifyItemInserted(data.size());
        Runnable runnable = () -> {
            //TODO send new news to backend
        };
        Thread async = new Thread(runnable);
        async.start();
    }
}
