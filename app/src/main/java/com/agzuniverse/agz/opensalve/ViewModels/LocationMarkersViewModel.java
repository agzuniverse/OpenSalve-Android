package com.agzuniverse.agz.opensalve.ViewModels;

import android.arch.lifecycle.ViewModel;

import com.agzuniverse.agz.opensalve.Modals.LocationMarker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationMarkersViewModel extends ViewModel {
    private List<LocationMarker> locationsOfCampsAndCollectionCentres = new ArrayList<>();
    private List<LocationMarker> locationsOfRequests = new ArrayList<>();

    //TODO fetch coordinates of all the markers from API
    public List<LocationMarker> getCampsAndCollectionCentres(String apiUrl) {
//        String[] titles = new String[]{"MEC", "RSET", "RSC", "GSouk"};
//        String[] snippets = new String[]{"camp#1", "camp#2", "collection center#3", "collection center#4"};
//        Double[] lats = new Double[]{9.9323, 9.9306, 9.9310, 9.9339};
//        Double[] lngs = new Double[]{76.2633, 76.2653, 76.2673, 76.2693};

//        for (int i = 0; i < titles.length && i < snippets.length && i < lats.length && i < lngs.length; i++) {
//            LocationMarker current = new LocationMarker(titles[i], snippets[i], lats[i], lngs[i]);
//            locationsOfCampsAndCollectionCentres.add(current);
//        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(apiUrl + "/api/camps/").build();
        try {
            Response response = client.newCall(request).execute();
            parse(response.body().string(), "camps");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locationsOfCampsAndCollectionCentres;
    }

    public List<LocationMarker> getRequests() {
        return locationsOfRequests;
    }

    private void parse(String res, String type) {
        try {
            JSONObject json = new JSONObject(res);
            JSONArray results = json.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject a = results.getJSONObject(i);
                if (type.equals("camps")) {
                    LocationMarker current = new LocationMarker(
                            a.getString("location"),
                            "camp#" + a.getString("id"),
                            a.getDouble("lat"),
                            a.getDouble("lng")
                    );
                    locationsOfCampsAndCollectionCentres.add(current);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
