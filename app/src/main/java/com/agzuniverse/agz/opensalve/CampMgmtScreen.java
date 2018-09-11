package com.agzuniverse.agz.opensalve;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.agzuniverse.agz.opensalve.Modals.SupplyNeededModel;
import com.agzuniverse.agz.opensalve.adapters.SuppliesNeededAdapter;

import java.util.ArrayList;
import java.util.List;

public class CampMgmtScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camp_mgmt_home);

        //TODO Set camp name, image, contact and camp manager's name
        //TODO fetch list of supplies needed for camp and display it
        //TODO Replace this dummy array with the fetched data
        List<SupplyNeededModel> supplies = new ArrayList<>();
        String[] data = {"Snacks", "Drinking Water", "Clothes", "Paracetamol", "First Aid kits"};
        for (int i = 0; i < data.length; i++) {
            SupplyNeededModel current = new SupplyNeededModel();
            current.supply = data[i];
            supplies.add(current);
        }


        RecyclerView list = findViewById(R.id.camp_supplies);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager listManager = new LinearLayoutManager(this);
        RecyclerView.Adapter listAdapter = new SuppliesNeededAdapter(this, supplies);
        list.setAdapter(listAdapter);
        list.setLayoutManager(listManager);

    }

    public void goToListOfInhabitants(View v) {
        Intent listOfInhabs = new Intent(this, ListOfInhabitants.class);
        this.startActivity(listOfInhabs);
    }
}
