package com.agzuniverse.agz.opensalve.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.agzuniverse.agz.opensalve.Modals.CampMetadata;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CampMgmtViewModel extends ViewModel {
    private CampMetadata data = null;

    public CampMetadata getCampMetadata(int id, String apiUrl, String type) {
        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
        Request request = new Request.Builder().url(apiUrl + "/api/camps/c/" + Integer.toString(id)).build();
        if (type.equals("collection_centers")) {
            request = new Request.Builder().url(apiUrl + "/api/collectioncentres/c/" + Integer.toString(id)).build();
        }
        try {
            Response response = client.newCall(request).execute();
            parse(response.body().string());
        } catch (IOException e) {
            Log.v("zxcv", "CRASH SHOULD HAVE HAPPEND NERE");
            e.printStackTrace();
        }
        return data;
    }

    private void parse(String s) {
        try {
            JSONObject json = new JSONObject(s);
            data = new CampMetadata(
                    json.getString("location"),
                    json.getString("incharge"),
                    json.getString("phone"),
                    new URL(json.getString("photo")),
                    new ArrayList<>(
                            Arrays.asList(
                                    json.getString("supplies").split(",")
                            )
                    ),
                    json.getInt("id")
            );
        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
