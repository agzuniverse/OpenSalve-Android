package com.agzuniverse.agz.opensalve.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agzuniverse.agz.opensalve.Modals.SupplyNeededModel;
import com.agzuniverse.agz.opensalve.R;

import java.util.Collections;
import java.util.List;

public class SuppliesNeededAdapter extends RecyclerView.Adapter<SuppliesNeededAdapter.SuppliesNeededViewHolder> {

    private LayoutInflater inflater;
    private List<SupplyNeededModel> supplies = Collections.emptyList();

    public SuppliesNeededAdapter(Context context, List<SupplyNeededModel> supplies) {
        inflater = LayoutInflater.from(context);
        this.supplies = supplies;
    }

    @NonNull
    @Override
    public SuppliesNeededViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.supplies_needed_row, viewGroup, false);
        SuppliesNeededViewHolder holder = new SuppliesNeededViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SuppliesNeededViewHolder viewHolder, int i) {
        SupplyNeededModel current = supplies.get(i);
        viewHolder.supply.setText(current.supply);
    }

    @Override
    public int getItemCount() {
        return supplies.size();
    }

    class SuppliesNeededViewHolder extends RecyclerView.ViewHolder {
        public TextView supply;

        public SuppliesNeededViewHolder(@NonNull View itemView) {
            super(itemView);
            supply = itemView.findViewById(R.id.supply);
        }
    }
}
