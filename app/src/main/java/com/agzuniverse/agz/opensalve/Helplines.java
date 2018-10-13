package com.agzuniverse.agz.opensalve;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.agzuniverse.agz.opensalve.Modals.Contact;
import com.agzuniverse.agz.opensalve.ViewModels.ContactsViewModel;
import com.agzuniverse.agz.opensalve.adapters.ContactsAdapter;

import java.util.List;

public class Helplines extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpline);

        ContactsViewModel model = ViewModelProviders.of(this).get(ContactsViewModel.class);
        List<Contact> data = model.getContacts();

        RecyclerView contacts = findViewById(R.id.contacts_list);
        RecyclerView.LayoutManager contactsLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter contactsAdapter = new ContactsAdapter(this, data);
        contacts.setAdapter(contactsAdapter);
        contacts.setLayoutManager(contactsLayoutManager);
    }
}
