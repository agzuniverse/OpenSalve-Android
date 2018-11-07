package com.agzuniverse.agz.opensalve.Modals;

public class GetHelpData {
    private boolean evac;
    private boolean foodWater;
    private boolean transport;
    private boolean firstAid;
    private boolean medical;
    private String status;
    private String desc;
    private String name;
    private String contact;

    public GetHelpData(boolean evac, boolean foodWater, boolean transport, boolean firstAid, boolean medical, String status, String desc, String name, String contact) {
        this.evac = evac;
        this.foodWater = foodWater;
        this.transport = transport;
        this.firstAid = firstAid;
        this.medical = medical;
        this.status = status;
        this.desc = desc;
        this.name = name;
        this.contact = contact;
    }

    public boolean isEvac() {
        return evac;
    }

    public boolean isFoodWater() {
        return foodWater;
    }

    public boolean isTransport() {
        return transport;
    }

    public boolean isFirstAid() {
        return firstAid;
    }

    public boolean isMedical() {
        return medical;
    }

    public String getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
}
