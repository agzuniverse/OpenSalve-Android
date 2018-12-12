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
import com.agzuniverse.agz.opensalve.Utils.GlobalStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListOfInhabsAdapter extends RecyclerView.Adapter<ListOfInhabsAdapter.ListOfInhabsViewHolder> {

    private LayoutInflater inflater;
    private List<Person> persons = Collections.emptyList();
    private boolean showCloseButton;
    private Context context;
    private int camp_id;

    public ListOfInhabsAdapter(Context context, List<Person> persons, boolean showCloseButton, int id) {
        inflater = LayoutInflater.from(context);
        this.persons = persons;
        this.showCloseButton = showCloseButton;
        this.context = context;
        this.camp_id = id;
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
                    OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
                    Request request = new Request.Builder()
                            .url(context.getResources().getString(R.string.base_api_url) + "/api/camps/c/" + camp_id + "/inhabitants/" + id)
                            .header("Authorization", "Token " + GlobalStore.token)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        JSONObject res = new JSONObject(response.body().string());

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
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
