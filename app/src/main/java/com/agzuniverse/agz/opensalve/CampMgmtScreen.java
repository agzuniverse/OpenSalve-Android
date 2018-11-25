package com.agzuniverse.agz.opensalve;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.agzuniverse.agz.opensalve.Modals.CampMetadata;
import com.agzuniverse.agz.opensalve.ViewModels.CampMgmtViewModel;
import com.agzuniverse.agz.opensalve.adapters.SuppliesNeededAdapter;
import com.agzuniverse.agz.opensalve.widgets.ConfirmDeleteCampDialog;
import com.agzuniverse.agz.opensalve.widgets.NewSupplyDialog;

import java.util.List;

public class CampMgmtScreen extends AppCompatActivity implements NewSupplyDialog.AddSupplySubmit {

    private CampMgmtViewModel model;
    private CampMetadata data;
    private int id;
    private String token;
    private List<String> supplies;
    private RecyclerView.Adapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camp_mgmt_home);

        Bundle extraData = getIntent().getExtras();
        id = extraData.getInt("id", 0);

        model = ViewModelProviders.of(this).get(CampMgmtViewModel.class);
        fetchCampMetadataAsync(id);

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        if (prefs.getInt("isVolunteer", 0) == 1) {
            token = prefs.getString("token", "0");
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
        }
    }

    //TODO handle case when this returns null
    public void fetchCampMetadataAsync(int id) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                setCampMetadata(data);
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
        //TODO do not do this on the main thread
//        Bitmap imageBitmap = BitmapFactory.decodeStream(data.getCampImageUrl().openConnection().getInputStream());
//        campImage.setImageBitmap(imageBitmap);
        campImage.setImageDrawable(getDrawable(R.drawable.shelter));

        supplies = data.getSuppliesNeeded();
        RecyclerView list = findViewById(R.id.camp_supplies);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager listManager = new LinearLayoutManager(this);
        listAdapter = new SuppliesNeededAdapter(this, supplies);
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
        Runnable runnable = () -> {
            //TODO send s to the backend to add to list of supplies for this camp
        };
        Thread async = new Thread(runnable);
        async.start();
        supplies.add(s);
        listAdapter.notifyItemInserted(supplies.size());
    }
}
