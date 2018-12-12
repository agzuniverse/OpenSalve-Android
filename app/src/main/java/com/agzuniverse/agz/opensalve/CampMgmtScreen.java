package com.agzuniverse.agz.opensalve;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agzuniverse.agz.opensalve.Modals.CampMetadata;
import com.agzuniverse.agz.opensalve.Utils.GlobalStore;
import com.agzuniverse.agz.opensalve.ViewModels.CampMgmtViewModel;
import com.agzuniverse.agz.opensalve.adapters.SuppliesNeededAdapter;
import com.agzuniverse.agz.opensalve.widgets.ConfirmDeleteCampDialog;
import com.agzuniverse.agz.opensalve.widgets.NewSupplyDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CampMgmtScreen extends AppCompatActivity implements NewSupplyDialog.AddSupplySubmit {

    private CampMgmtViewModel model;
    private CampMetadata data;
    private int id;
    private List<String> supplies;
    private RecyclerView.Adapter listAdapter;
    private boolean showClosebutton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camp_mgmt_home);

        Bundle extraData = getIntent().getExtras();
        id = extraData.getInt("id", 0);

        model = ViewModelProviders.of(this).get(CampMgmtViewModel.class);

        if (GlobalStore.isVolunteer) {
            FrameLayout f = findViewById(R.id.delete_camp_button);
            f.setVisibility(View.VISIBLE);
            f.setOnClickListener((View v) -> {
                DialogFragment dialog = new ConfirmDeleteCampDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "ConfirmDeleteCampDialog");
            });
            LinearLayout fab = findViewById(R.id.new_supply_fab);
            fab.setVisibility(View.VISIBLE);
            showClosebutton = true;
        }
        fetchCampMetadataAsync(id);
    }

    public void fetchCampMetadataAsync(int id) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (data != null)
                    setCampMetadata(data);
                else
                    Toast.makeText(CampMgmtScreen.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        };
        Runnable runnable = () -> {
            data = model.getCampMetadata(id, getResources().getString(R.string.base_api_url), "camps");
            handler.sendEmptyMessage(0);
        };
        Thread async = new Thread(runnable);
        async.start();
    }

    public void setCampMetadata(CampMetadata data) {
        TextView campName = findViewById(R.id.camp_name);
        campName.setText(data.getCampName());
        TextView campManager = findViewById(R.id.camp_manager);
        campManager.setText(data.getCampManager());
        TextView campContact = findViewById(R.id.camp_contact);
        campContact.setText(data.getCampContact());
        ImageView campImage = findViewById(R.id.camp_image);
        campImage.setImageDrawable(getDrawable(R.drawable.shelter));

        supplies = data.getSuppliesNeeded();
        RecyclerView list = findViewById(R.id.camp_supplies);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager listManager = new LinearLayoutManager(this);
        listAdapter = new SuppliesNeededAdapter(this, supplies, showClosebutton, id, true);
        list.setAdapter(listAdapter);
        list.setLayoutManager(listManager);
    }

    public void goToListOfInhabitants(View v) {
        Intent listOfInhabs = new Intent(this, ListOfInhabitants.class);
        listOfInhabs.putExtra("id", id);
        this.startActivity(listOfInhabs);
    }

    public void openAddNewSupplyDialog(View v) {
        DialogFragment dialog = new NewSupplyDialog();
        dialog.show(getSupportFragmentManager(), "NewSupplyDialog");
    }

    @Override
    public void onAddNewSupply(DialogFragment dialog) {
        Dialog d = dialog.getDialog();
        EditText t = d.findViewById(R.id.new_supply);
        String s = t.getText().toString();
        JSONObject json = new JSONObject();
        try {
            json.put("supply", s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Runnable runnable = () -> {
            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.base_api_url) + "/api/camps/c/" + id + "/stock")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Token " + GlobalStore.token)
                    .post(requestBody)
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
        supplies.add(s);
        listAdapter.notifyItemInserted(supplies.size());
    }
}
