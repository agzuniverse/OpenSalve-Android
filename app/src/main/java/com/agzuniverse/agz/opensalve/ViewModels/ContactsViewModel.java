package com.agzuniverse.agz.opensalve.ViewModels;

import android.arch.lifecycle.ViewModel;

import com.agzuniverse.agz.opensalve.Modals.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsViewModel extends ViewModel {
    private List<Contact> contacts = new ArrayList<>();

    public List<Contact> getContacts() {
        //TODO fetch contacts from backend API
        Contact contact1 = new Contact("Fire Force", "1234567890");
        Contact contact2 = new Contact("Fire Force", "1234567890");
        contacts.add(contact1);
        contacts.add(contact2);
        return contacts;
    }
}
