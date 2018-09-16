package com.agzuniverse.agz.opensalve;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.agzuniverse.agz.opensalve.Modals.CampMetadata;
import com.agzuniverse.agz.opensalve.Modals.SupplyNeededModel;
import com.agzuniverse.agz.opensalve.adapters.SuppliesNeededAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CollectionCentreScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_center);

        List<SupplyNeededModel> supplies = new ArrayList<>();
        String[] data = {"Snacks", "Drinking Water", "Clothes", "Paracetamol", "First Aid kits"};
        for (int i = 0; i < data.length; i++) {
            SupplyNeededModel current = new SupplyNeededModel();
            current.supply = data[i];
            supplies.add(current);
        }

        RecyclerView list = findViewById(R.id.collection_supplies);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager listManager = new LinearLayoutManager(this);
        RecyclerView.Adapter listAdapter = new SuppliesNeededAdapter(this, supplies);
        list.setLayoutManager(listManager);
        list.setAdapter(listAdapter);
    }

    public void setCollectionMetadata(CampMetadata data) throws IOException {
        TextView collectionName = findViewById(R.id.collection_name);
        collectionName.setText(data.campName);
        TextView collectionManager = findViewById(R.id.collection_manager);
        collectionManager.setText(data.campManager);
        TextView collectionContact = findViewById(R.id.collection_contact);
        collectionContact.setText(data.campContact);
        ImageView collectionImage = findViewById(R.id.collection_image);
        Bitmap imageBitmap = BitmapFactory.decodeStream(data.campImageUrl.openConnection().getInputStream());
        collectionImage.setImageBitmap(imageBitmap);
    }
}
