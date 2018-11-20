package com.agzuniverse.agz.opensalve.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.agzuniverse.agz.opensalve.Modals.Person;
import com.agzuniverse.agz.opensalve.R;

import java.util.Collections;
import java.util.List;

public class ListOfInhabsAdapter extends RecyclerView.Adapter<ListOfInhabsAdapter.ListOfInhabsViewHolder> {

    private LayoutInflater inflater;
    private List<Person> persons = Collections.emptyList();
    private boolean showCloseButton;

    public ListOfInhabsAdapter(Context context, List<Person> persons, boolean showCloseButton) {
        inflater = LayoutInflater.from(context);
        this.persons = persons;
        this.showCloseButton = showCloseButton;
    }

    @NonNull
    @Override
    public ListOfInhabsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.inhabitant, viewGroup, false);
        ListOfInhabsViewHolder holder = new ListOfInhabsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfInhabsViewHolder viewHolder, int i) {
        Person current = persons.get(i);
        viewHolder.name.setText(current.getName());
        viewHolder.age.setText(current.getBloodgrp());
        if (showCloseButton) {
            viewHolder.delete.setVisibility(View.VISIBLE);
            viewHolder.delete.setOnClickListener((View v) -> {
                int id = persons.get(i).getId();
                persons.remove(i);
                notifyItemRemoved(i);
                Runnable runnable = () -> {
                    //TODO send deleted news to backend
                };
                Thread async = new Thread(runnable);
                async.start();
            });
        }

    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    class ListOfInhabsViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView age;
        private FrameLayout delete;

        public ListOfInhabsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.person_name);
            age = itemView.findViewById(R.id.person_age);
            delete = itemView.findViewById(R.id.delete_inhab);
        }
    }
}
