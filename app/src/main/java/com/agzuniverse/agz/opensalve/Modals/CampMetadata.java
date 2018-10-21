package com.agzuniverse.agz.opensalve.Modals;

import java.net.URL;
import java.util.List;

public class CampMetadata {
    private String campName;
    private String campManager;
    private String campContact;
    private URL campImageUrl;
    private List<String> suppliesNeeded;

    public CampMetadata(String campName, String campManager, String campContact, URL campImageUrl, List<String> suppliesNeeded) {
        this.campName = campName;
        this.campManager = campManager;
        this.campContact = campContact;
        this.campImageUrl = campImageUrl;
        this.suppliesNeeded = suppliesNeeded;
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

    public List<String> getSuppliesNeeded() {
        return suppliesNeeded;
    }
}
