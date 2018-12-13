package com.agzuniverse.agz.opensalve.Modals;

import java.util.List;

public class CampMetadata {
    private int id;
    private String campName;
    private String campManager;
    private String campContact;
    private List<String> suppliesNeeded;

    public CampMetadata(String campName, String campManager, String campContact, List<String> suppliesNeeded, int id) {
        this.campName = campName;
        this.campManager = campManager;
        this.campContact = campContact;
        this.suppliesNeeded = suppliesNeeded;
        this.id = id;
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

    public List<String> getSuppliesNeeded() {
        return suppliesNeeded;
    }

    public int getId() {
        return id;
    }
}
