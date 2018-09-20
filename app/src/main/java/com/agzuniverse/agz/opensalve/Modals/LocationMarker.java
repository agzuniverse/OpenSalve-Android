package com.agzuniverse.agz.opensalve.Modals;

public class LocationMarker {

    private String title;
    private String snippet;
    private Double lat;
    private Double lng;

    public LocationMarker(String title, String snippet, Double lat, Double lng) {
        this.title = title;
        this.snippet = snippet;
        this.lat = lat;
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
