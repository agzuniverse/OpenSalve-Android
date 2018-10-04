package com.agzuniverse.agz.opensalve.ViewModels;

import android.arch.lifecycle.ViewModel;

import com.agzuniverse.agz.opensalve.Modals.CampMetadata;
import com.agzuniverse.agz.opensalve.Modals.SupplyNeededModel;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CampMgmtViewModel extends ViewModel {
    private List<SupplyNeededModel> supplies = new ArrayList<>();

    public CampMetadata getCampMetadata(int id) {
        //TODO Set camp name, image, contact and camp manager's name after fetching from API using ID
        URL imageUrl = null;
        try {
            imageUrl = new URL("https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/PanoBunker.jpg/250px-PanoBunker.jpg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        CampMetadata data = new CampMetadata("MEC", "John Doe", "911", imageUrl);
        return data;
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
