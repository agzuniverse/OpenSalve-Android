package com.agzuniverse.agz.opensalve.Modals;

public class Person {
    private String firstName;
    private String secondName;
    private Integer age;

    public Person(String firstName, String secondName, Integer age) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public Integer getAge() {
        return age;
    }
}
