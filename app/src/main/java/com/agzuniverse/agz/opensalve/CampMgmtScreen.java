package com.agzuniverse.agz.opensalve;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.agzuniverse.agz.opensalve.Modals.CampMetadata;
import com.agzuniverse.agz.opensalve.Modals.SupplyNeededModel;
import com.agzuniverse.agz.opensalve.ViewModels.CampMgmtViewModel;
import com.agzuniverse.agz.opensalve.adapters.SuppliesNeededAdapter;

import java.util.List;

public class CampMgmtScreen extends AppCompatActivity {

    private CampMgmtViewModel model;
    private CampMetadata data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camp_mgmt_home);

        Bundle extraData = getIntent().getExtras();
        int id = extraData.getInt("id", 0);

        model = ViewModelProviders.of(this).get(CampMgmtViewModel.class);
        fetchCampMetadataAsync(id);
        List<SupplyNeededModel> supplies = model.getSuppliesNeeded();

        RecyclerView list = findViewById(R.id.camp_supplies);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager listManager = new LinearLayoutManager(this);
        RecyclerView.Adapter listAdapter = new SuppliesNeededAdapter(this, supplies);
        list.setAdapter(listAdapter);
        list.setLayoutManager(listManager);

    }

    public void fetchCampMetadataAsync(int id) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                setCampMetadata(data);
            }
        };
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                data = model.getCampMetadata(id, getResources().getString(R.string.base_api_url));
                handler.sendEmptyMessage(0);
            }
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
    }

    public void goToListOfInhabitants(View v) {
        Intent listOfInhabs = new Intent(this, ListOfInhabitants.class);
        this.startActivity(listOfInhabs);
    }
}
