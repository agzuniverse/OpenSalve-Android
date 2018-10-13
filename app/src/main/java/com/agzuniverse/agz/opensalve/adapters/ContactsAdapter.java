package com.agzuniverse.agz.opensalve.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agzuniverse.agz.opensalve.Modals.Contact;
import com.agzuniverse.agz.opensalve.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    private List<Contact> data = new ArrayList<>();
    private LayoutInflater inflater;

    public ContactsAdapter(Context context, List<Contact> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.contact, viewGroup, false);
        ContactsViewHolder holder = new ContactsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder viewHolder, int i) {
        Contact current = data.get(i);
        viewHolder.number.setText(current.getNumber());
        viewHolder.desc.setText(current.getDesc());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView desc, number;

        private ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.contact_desc);
            number = itemView.findViewById(R.id.contact_number);
        }
    }
}
