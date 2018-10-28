package com.agzuniverse.agz.opensalve;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.agzuniverse.agz.opensalve.Modals.CampMetadata;
import com.agzuniverse.agz.opensalve.ViewModels.CampMgmtViewModel;
import com.agzuniverse.agz.opensalve.adapters.SuppliesNeededAdapter;

public class CollectionCentreScreen extends AppCompatActivity {

    private CampMgmtViewModel model;
    private CampMetadata data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_center);

        Bundle extraData = getIntent().getExtras();
        int id = extraData.getInt("id", 0);

        model = ViewModelProviders.of(this).get(CampMgmtViewModel.class);
        fetchCollectionMetadataAsync(id);
    }

    //TODO handle case when this returns null
    public void fetchCollectionMetadataAsync(int id) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                setCollectionMetadata(data);
            }
        };
        Runnable runnable = () -> {
            data = model.getCampMetadata(id, getResources().getString(R.string.base_api_url), "collection_centers");
            handler.sendEmptyMessage(0);
        };
        Thread async = new Thread(runnable);
        async.start();
    }

    public void setCollectionMetadata(CampMetadata data) {
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

        RecyclerView list = findViewById(R.id.collection_supplies);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager listManager = new LinearLayoutManager(this);
        RecyclerView.Adapter listAdapter = new SuppliesNeededAdapter(this, data.getSuppliesNeeded());
        list.setLayoutManager(listManager);
        list.setAdapter(listAdapter);
    }
}
