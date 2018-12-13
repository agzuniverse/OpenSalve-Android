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
    private int max_retries = 7;

    public List<LocationMarker> getCampsAndCollectionCentres(String apiUrl) {
        OkHttpClient client = new OkHttpClient();
        Request requestCamps = new Request.Builder().url(apiUrl + "/api/camps/").build();
        Request requestCollectionCenters = new Request.Builder().url(apiUrl + "/api/collectioncentres/").build();

        boolean campRequestSuccessful = false;
        int campRequestCounter = 0;
        boolean collectionRequestSuccessful = false;
        int collectionRequestCounter = 0;

        do {
            try {
                Response response = client.newCall(requestCamps).execute();
                parse(response.body().string(), "camps");
                campRequestSuccessful = true;
            } catch (IOException e) {
                campRequestCounter++;
                e.printStackTrace();
            }
        } while (!campRequestSuccessful && campRequestCounter <= max_retries);
        do {
            try {
                Response response2 = client.newCall(requestCollectionCenters).execute();
                parse(response2.body().string(), "collection_centers");
                collectionRequestSuccessful = true;
            } catch (IOException e) {
                collectionRequestCounter++;
                e.printStackTrace();
            }
        } while (!collectionRequestSuccessful && collectionRequestCounter <= max_retries);

        return locationsOfCampsAndCollectionCentres;
    }

    public List<LocationMarker> getRequests(String apiUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(apiUrl + "/api/help/").build();

        boolean helpReqSuccessful = false;
        int helpReqCount = 0;
        do {
            try {
                Response response = client.newCall(request).execute();
                parse(response.body().string(), "request");
                helpReqSuccessful = true;
            } catch (IOException e) {
                helpReqCount++;

                e.printStackTrace();
            }
        } while (!helpReqSuccessful && helpReqCount <= max_retries);
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
                } else if (type.equals("request")) {
                    LocationMarker current = new LocationMarker(
                            a.getString("name"),
                            "request#" + a.getString("id"),
                            a.getDouble("lat"),
                            a.getDouble("lng")
                    );
                    locationsOfRequests.add(current);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
