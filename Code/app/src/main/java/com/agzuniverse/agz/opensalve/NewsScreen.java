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
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.agzuniverse.agz.opensalve.Modals.News;
import com.agzuniverse.agz.opensalve.Utils.GlobalStore;
import com.agzuniverse.agz.opensalve.ViewModels.NewsViewModel;
import com.agzuniverse.agz.opensalve.adapters.NewsAdapter;
import com.agzuniverse.agz.opensalve.widgets.AddNewsDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewsScreen extends AppCompatActivity implements AddNewsDialog.AddNewsSubmit {
    private String token;
    private RecyclerView.Adapter newsAdapter;
    private List<News> data;
    private boolean showCloseButton = false;
    private NewsViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);

        model = ViewModelProviders.of(this).get(NewsViewModel.class);
        getNewsAsync();

        FloatingActionButton fab = findViewById(R.id.news_fab);
        fab.setOnClickListener((View view) -> addNewNews());

        if (GlobalStore.isVolunteer) {
            LinearLayout f = findViewById(R.id.news_fab_wrapper);
            f.setVisibility(View.VISIBLE);
            showCloseButton = true;
        }


    }

    public void getNewsAsync() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                RecyclerView news = findViewById(R.id.news_list);
                RecyclerView.LayoutManager newsLayoutManager = new LinearLayoutManager(NewsScreen.this);
                newsAdapter = new NewsAdapter(NewsScreen.this, data, getColors(), showCloseButton);
                news.setAdapter(newsAdapter);
                news.setLayoutManager(newsLayoutManager);
            }
        };
        Runnable runnable = () -> {
            data = model.getNews(getResources().getString(R.string.base_api_url));
            handler.sendEmptyMessage(0);
        };
        Thread async = new Thread(runnable);
        async.start();
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
        JSONObject json = new JSONObject();
        try {
            json.put("title", news.getAuthor());
            json.put("contents", news.getBody());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Runnable runnable = () -> {
            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
            RequestBody reqbody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.base_api_url) + "/api/news/")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Token " + GlobalStore.token)
                    .post(reqbody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                JSONObject res = new JSONObject(response.body().string());
                
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        };
        Thread async = new Thread(runnable);
        async.start();
    }
}
