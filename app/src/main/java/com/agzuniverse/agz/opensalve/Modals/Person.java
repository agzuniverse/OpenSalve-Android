package com.agzuniverse.agz.opensalve.Modals;

public class Person {
    private String name;
    private String bloodgrp;
    private int id;

    public Person(String name, String bloodgrp, int id) {
        this.name = name;
        this.bloodgrp = bloodgrp;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getBloodgrp() {
        return bloodgrp;
    }
}
