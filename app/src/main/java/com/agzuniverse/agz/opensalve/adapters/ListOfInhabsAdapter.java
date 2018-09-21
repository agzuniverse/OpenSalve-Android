package com.agzuniverse.agz.opensalve.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agzuniverse.agz.opensalve.Modals.Person;
import com.agzuniverse.agz.opensalve.R;

import java.util.Collections;
import java.util.List;

public class ListOfInhabsAdapter extends RecyclerView.Adapter<ListOfInhabsAdapter.ListOfInhabsViewHolder> {

    private LayoutInflater inflater;
    private List<Person> persons = Collections.emptyList();

    public ListOfInhabsAdapter(Context context, List<Person> persons) {
        inflater = LayoutInflater.from(context);
        this.persons = persons;
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
        viewHolder.name.setText(current.getFirstName() + ' ' + current.getSecondName());
        viewHolder.age.setText(Integer.toString(current.getAge()));

    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    class ListOfInhabsViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView age;

        public ListOfInhabsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.person_name);
            age = itemView.findViewById(R.id.person_age);
        }
    }
}
