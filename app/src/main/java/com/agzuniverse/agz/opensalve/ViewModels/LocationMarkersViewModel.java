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

    public List<LocationMarker> getCampsAndCollectionCentres(String apiUrl) {
        OkHttpClient client = new OkHttpClient();
        Request requestCamps = new Request.Builder().url(apiUrl + "/api/camps/").build();
        Request requestCollectionCenters = new Request.Builder().url(apiUrl + "/api/collectioncentres/").build();
        try {
            Response response = client.newCall(requestCamps).execute();
            parse(response.body().string(), "camps");
            Response response2 = client.newCall(requestCollectionCenters).execute();
            parse(response2.body().string(), "collection_centers");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locationsOfCampsAndCollectionCentres;
    }

    public List<LocationMarker> getRequests() {
        //TODO this
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
                } else if (type.equals("collection_centers")) {
                    LocationMarker current = new LocationMarker(
                            a.getString("location"),
                            "collection center#" + a.getString("id"),
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
