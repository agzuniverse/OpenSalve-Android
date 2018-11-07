package com.agzuniverse.agz.opensalve.ViewModels;

import android.arch.lifecycle.ViewModel;

import com.agzuniverse.agz.opensalve.Modals.GetHelpData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetHelpViewModel extends ViewModel {
    private GetHelpData data;

    public GetHelpData getHelpData(int id, String apiUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(apiUrl + "/api/help/r/" + Integer.toString(id)).build();
        try {
            Response response = client.newCall(request).execute();
            parse(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void parse(String s) {
        try {
            JSONObject json = new JSONObject(s);
            data = new GetHelpData(
                    json.getBoolean("need_rescue"),
                    json.getBoolean("need_food_water"),
                    json.getBoolean("need_transport"),
                    json.getBoolean("need_first_aid"),
                    json.getBoolean("need_transport"),
                    json.getString("status"),
                    json.getString("desc"),
                    json.getString("name"),
                    json.getString("phone")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
