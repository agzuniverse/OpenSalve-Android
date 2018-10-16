package com.agzuniverse.agz.opensalve.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.agzuniverse.agz.opensalve.Modals.LocationMarker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationMarkersViewModel extends ViewModel {
    private List<LocationMarker> locationsOfCampsAndCollectionCentres = new ArrayList<>();
    private List<LocationMarker> locationsOfRequests = new ArrayList<>();

    //TODO fetch coordinates of all the markers from API
    public List<LocationMarker> getCampsAndCollectionCentres() {
//        String[] titles = new String[]{"MEC", "RSET", "RSC", "GSouk"};
//        String[] snippets = new String[]{"camp#1", "camp#2", "collection center#3", "collection center#4"};
//        Double[] lats = new Double[]{9.9323, 9.9306, 9.9310, 9.9339};
//        Double[] lngs = new Double[]{76.2633, 76.2653, 76.2673, 76.2693};

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://10.0.2.2:8000/api/camps/").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("NETWORK_ERROR", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.i("HERE", response.body().string());
                parse(response.body().string());
            }
        });

//        for (int i = 0; i < titles.length && i < snippets.length && i < lats.length && i < lngs.length; i++) {
//            LocationMarker current = new LocationMarker(titles[i], snippets[i], lats[i], lngs[i]);
//            locationsOfCampsAndCollectionCentres.add(current);
//        }
        return locationsOfCampsAndCollectionCentres;
    }

    public List<LocationMarker> getRequests() {
        return locationsOfRequests;
    }

    private void parse(String res) {
        try {
            JSONObject json = new JSONObject(res);
            JSONArray results = json.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject a = results.getJSONObject(i);
                LocationMarker current = new LocationMarker(
                        a.getString("location"),
                        a.getString("id"),
                        a.getDouble("lat"),
                        a.getDouble("lng")
                );
                locationsOfCampsAndCollectionCentres.add(current);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
