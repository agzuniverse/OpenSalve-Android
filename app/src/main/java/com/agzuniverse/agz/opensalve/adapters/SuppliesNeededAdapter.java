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
import com.agzuniverse.agz.opensalve.Utils.GlobalStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SuppliesNeededAdapter extends RecyclerView.Adapter<SuppliesNeededAdapter.SuppliesNeededViewHolder> {

    private LayoutInflater inflater;
    private List<String> supplies;
    private boolean showClosebutton;
    private Context context;
    private int id;
    private boolean isCamp;

    public SuppliesNeededAdapter(Context context, List<String> supplies, boolean showClosebutton, int id, boolean isCamp) {
        inflater = LayoutInflater.from(context);
        this.supplies = supplies;
        this.showClosebutton = showClosebutton;
        this.context = context;
        this.id = id;
        this.isCamp = isCamp;
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
                    JSONObject json = new JSONObject();
                    try {
                        json.put("supply", current);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
                    Request request;
                    if (isCamp) {
                        request = new Request.Builder()
                                .url(context.getResources().getString(R.string.base_api_url) + "/api/camps/c/" + id + "/stock/delete")
                                .header("Authorization", "Token " + GlobalStore.token)
                                .post(requestBody)
                                .build();
                    } else {
                        request = new Request.Builder()
                                .url(context.getResources().getString(R.string.base_api_url) + "/api/collectioncentres/c/" + id + "/stock/delete")
                                .header("Authorization", "Token " + GlobalStore.token)
                                .post(requestBody)
                                .build();
                    }

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
            closeButton = itemView.findViewById(R.id.delete_supply);
        }
    }
}
