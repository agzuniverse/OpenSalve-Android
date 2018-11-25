package com.agzuniverse.agz.opensalve;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agzuniverse.agz.opensalve.Modals.CampMetadata;
import com.agzuniverse.agz.opensalve.ViewModels.CampMgmtViewModel;
import com.agzuniverse.agz.opensalve.adapters.SuppliesNeededAdapter;
import com.agzuniverse.agz.opensalve.widgets.NewSupplyDialog;

import java.util.List;

public class CollectionCentreScreen extends AppCompatActivity implements NewSupplyDialog.AddSupplySubmit {

    private CampMgmtViewModel model;
    private CampMetadata data;
    private String token;
    private RecyclerView.Adapter listAdapter;
    private List<String> supplies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_center);

        Bundle extraData = getIntent().getExtras();
        int id = extraData.getInt("id", 0);

        model = ViewModelProviders.of(this).get(CampMgmtViewModel.class);
        fetchCollectionMetadataAsync(id);

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        if (prefs.getInt("isVolunteer", 0) == 1) {
            token = prefs.getString("token", "0");
//            FrameLayout f = findViewById(R.id.delete_camp_button);
//            f.setVisibility(View.VISIBLE);
//            f.setOnClickListener((View v) -> {
//                DialogFragment dialog = new ConfirmDeleteCampDialog();
//                Bundle bundle = new Bundle();
//                bundle.putInt("id", id);
//                dialog.setArguments(bundle);
//                dialog.show(getSupportFragmentManager(), "ConfirmDeleteCampDialog");
//            });
            LinearLayout fab = findViewById(R.id.new_supply_fab_collection);
            fab.setVisibility(View.VISIBLE);
        }
    }

    //TODO handle case when this returns null
    public void fetchCollectionMetadataAsync(int id) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (data != null)
                    setCollectionMetadata(data);
                else
                    Toast.makeText(CollectionCentreScreen.this, "Network error", Toast.LENGTH_SHORT).show();
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

        supplies = data.getSuppliesNeeded();
        RecyclerView list = findViewById(R.id.collection_supplies);
        list.setHasFixedSize(true);
        RecyclerView.LayoutManager listManager = new LinearLayoutManager(this);
        listAdapter = new SuppliesNeededAdapter(this, supplies);
        list.setLayoutManager(listManager);
        list.setAdapter(listAdapter);
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
