package com.agzuniverse.agz.opensalve.Modals;

import java.net.URL;

public class CampMetadata {
    private String campName;
    private String campManager;
    private String campContact;
    private URL campImageUrl;

    public CampMetadata(String campName, String campManager, String campContact, URL campImageUrl) {
        this.campName = campName;
        this.campManager = campManager;
        this.campContact = campContact;
        this.campImageUrl = campImageUrl;
    }

    public String getCampName() {
        return campName;
    }

    public String getCampManager() {
        return campManager;
    }

    public String getCampContact() {
        return campContact;
    }

    public URL getCampImageUrl() {
        return campImageUrl;
    }
}
