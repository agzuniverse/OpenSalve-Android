package com.agzuniverse.agz.opensalve.ViewModels;

import android.arch.lifecycle.ViewModel;

import com.agzuniverse.agz.opensalve.Modals.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsViewModel extends ViewModel {
    private List<News> news = new ArrayList<>();

    public List<News> getNews(String apiUrl) {
        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
        Request request = new Request.Builder().url(apiUrl + "/api/news/").build();
        try {
            Response response = client.newCall(request).execute();
            parse(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        News contact1 = new News("Electricity to Maradu will be cut off at midnight", "KSEB Engineer", 1);
//        News contact2 = new News("All educational institutions including professional colleges will have a holiday tomorrow", "Ernakulam District Collector", 2);
//        news.add(contact1);
//        news.add(contact2);
        return news;
    }

    public void parse(String data) {
        try {
            JSONArray json = new JSONObject(data).getJSONArray("results");
            for (int i = 0; i < json.length(); i++) {
                JSONObject j = json.getJSONObject(i);
                News curr = new News(j.getString("contents"), j.getString("title"), j.getInt("id"));
                news.add(curr);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
