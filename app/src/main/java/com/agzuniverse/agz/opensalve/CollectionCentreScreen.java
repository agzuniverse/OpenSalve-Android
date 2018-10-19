package com.agzuniverse.agz.opensalve;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.agzuniverse.agz.opensalve.Modals.CampMetadata;
import com.agzuniverse.agz.opensalve.Modals.SupplyNeededModel;
import com.agzuniverse.agz.opensalve.ViewModels.CampMgmtViewModel;
import com.agzuniverse.agz.opensalve.adapters.SuppliesNeededAdapter;

import java.io.IOException;
import java.util.List;

public class CollectionCentreScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_center);

        Bundle extraData = getIntent().getExtras();
        int id = extraData.getInt("id", 0);

        CampMgmtViewModel model = ViewModelProviders.of(this).get(CampMgmtViewModel.class);

        CampMetadata data = model.getCampMetadata(id, getResources().getString(R.string.base_api_url), "collection_centers");
        try {
            setCollectionMetadata(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<SupplyNeededModel> supplies = model.getSuppliesNeeded();

        RecyclerView list = findViewById(R.id.collection_supplies);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager listManager = new LinearLayoutManager(this);
        RecyclerView.Adapter listAdapter = new SuppliesNeededAdapter(this, supplies);
        list.setLayoutManager(listManager);
        list.setAdapter(listAdapter);
    }

    public void setCollectionMetadata(CampMetadata data) throws IOException {
        TextView collectionName = findViewById(R.id.collection_name);
        collectionName.setText(data.getCampName());
        TextView collectionManager = findViewById(R.id.collection_manager);
        collectionManager.setText(data.getCampManager());
        TextView collectionContact = findViewById(R.id.collection_contact);
        collectionContact.setText(data.getCampContact());
        ImageView collectionImage = findViewById(R.id.collection_image);
        //TODO do not do this on the main thread
//        Bitmap imageBitmap = BitmapFactory.decodeStream(data.getCampImageUrl().openConnection().getInputStream());
//        collectionImage.setImageBitmap(imageBitmap);
        collectionImage.setImageDrawable(getDrawable(R.drawable.shelter));
    }
}
