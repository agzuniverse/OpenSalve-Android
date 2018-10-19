package com.agzuniverse.agz.opensalve.ViewModels;

import android.arch.lifecycle.ViewModel;

import com.agzuniverse.agz.opensalve.Modals.CampMetadata;
import com.agzuniverse.agz.opensalve.Modals.SupplyNeededModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CampMgmtViewModel extends ViewModel {
    private CampMetadata data;
    private List<SupplyNeededModel> supplies = new ArrayList<>();

    public CampMetadata getCampMetadata(int id, String apiUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(apiUrl + "/api/camps/c/" + Integer.toString(id)).build();
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
            data = new CampMetadata(
                    json.getString("location"),
                    json.getString("incharge"),
                    json.getString("phone"),
                    new URL(json.getString("photo"))
            );
        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public List<SupplyNeededModel> getSuppliesNeeded() {
        //TODO fetch list of supplies needed for camp from API and display it
        String[] data = {"Snacks", "Drinking Water", "Clothes", "Paracetamol", "First Aid kits"};
        for (int i = 0; i < data.length; i++) {
            SupplyNeededModel current = new SupplyNeededModel(data[i]);
            supplies.add(current);
        }
        return supplies;
    }
}
