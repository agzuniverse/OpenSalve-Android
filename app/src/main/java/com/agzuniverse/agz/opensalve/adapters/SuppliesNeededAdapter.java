package com.agzuniverse.agz.opensalve.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.agzuniverse.agz.opensalve.R;

import java.util.List;

public class SuppliesNeededAdapter extends RecyclerView.Adapter<SuppliesNeededAdapter.SuppliesNeededViewHolder> {

    private LayoutInflater inflater;
    private List<String> supplies;
    private boolean showClosebutton;

    public SuppliesNeededAdapter(Context context, List<String> supplies, boolean showClosebutton) {
        inflater = LayoutInflater.from(context);
        this.supplies = supplies;
        this.showClosebutton = showClosebutton;
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
        String current = supplies.get(i);
        viewHolder.supply.setText(current);
        if (showClosebutton) {
            viewHolder.closeButton.setOnClickListener((View v) -> {
                supplies.remove(i);
                notifyItemRemoved(i);
                Runnable runnable = () -> {
                    //TODO send deleted supply item to backend
                };
                Thread async = new Thread(runnable);
                async.start();
            });
            viewHolder.closeButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return supplies.size();
    }

    class SuppliesNeededViewHolder extends RecyclerView.ViewHolder {
        public TextView supply;
        public FrameLayout closeButton;

        public SuppliesNeededViewHolder(@NonNull View itemView) {
            super(itemView);
            supply = itemView.findViewById(R.id.supply);
        }
    }
}
