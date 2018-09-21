package com.agzuniverse.agz.opensalve.ViewModels;

import android.arch.lifecycle.ViewModel;

import com.agzuniverse.agz.opensalve.Modals.LocationMarker;

import java.util.ArrayList;
import java.util.List;

public class LocationMarkersViewModel extends ViewModel {
    private List<LocationMarker> locationsOfCampsAndCollectionCentres = new ArrayList<>();
    private List<LocationMarker> locationsOfRequests = new ArrayList<>();

    //TODO fetch coordinates of all the markers from API
    public List<LocationMarker> getCampsAndCollectionCentres() {
        String[] titles = new String[]{"MEC", "RSET", "RSC", "GSouk"};
        String[] snippets = new String[]{"camp", "camp", "collection center", "collection center"};
        Double[] lats = new Double[]{9.9323, 9.9306, 9.9310, 9.9339};
        Double[] lngs = new Double[]{76.2633, 76.2653, 76.2673, 76.2693};
        for (int i = 0; i < titles.length && i < snippets.length && i < lats.length && i < lngs.length; i++) {
            LocationMarker current = new LocationMarker(titles[i], snippets[i], lats[i], lngs[i]);
            locationsOfCampsAndCollectionCentres.add(current);
        }
        return locationsOfCampsAndCollectionCentres;
    }

    public List<LocationMarker> getRequests() {
        return locationsOfRequests;
    }
}
