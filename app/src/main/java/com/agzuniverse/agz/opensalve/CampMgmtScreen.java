package com.agzuniverse.agz.opensalve;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.agzuniverse.agz.opensalve.adapters.SuppliesNeededAdapter;

public class CampMgmtScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camp_mgmt_home);

        //TODO Set camp name, image, contact and camp manager's name
        //TODO fetch list of supplies needed for camp and display it
        //TODO Replace this dummy array with the fetched data
        String[] data = {"Snacks", "Drinking Water", "Clothes", "Paracetamol", "First Aid kits"};

        RecyclerView list = findViewById(R.id.camp_supplies);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager listManager = new LinearLayoutManager(this);
        RecyclerView.Adapter listAdapter = new SuppliesNeededAdapter(this, data);
        list.setAdapter(listAdapter);
        list.setLayoutManager(listManager);

    }
}
