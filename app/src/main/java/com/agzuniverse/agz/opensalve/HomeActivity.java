package com.agzuniverse.agz.opensalve;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.agzuniverse.agz.opensalve.Modals.LocationMarker;
import com.agzuniverse.agz.opensalve.ViewModels.LocationMarkersViewModel;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private LocationMarkersViewModel model;

    private MapView mapView;
    private List<LocationMarker> locations = new ArrayList<>();
    private List<LocationMarker> locationsOfRequests = new ArrayList<>();
    private boolean showCamps = true;
    private boolean showCollection = true;
    private boolean showRequests = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        model = ViewModelProviders.of(this).get(LocationMarkersViewModel.class);

        android.support.v7.widget.Toolbar bar = findViewById(R.id.homeToolbar);
        setSupportActionBar(bar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.action_bar_edit_text);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        Mapbox.getInstance(this, getString(R.string.mapbox_api_token));

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                configMapOverlay(mapboxMap);
            }
        });

//        Intent debug = new Intent(this, CampMgmtScreen.class);
//        this.startActivity(debug);
    }

    public void configMapOverlay(MapboxMap map) {

        locations = model.getCampsAndCollectionCentres();

        IconFactory iconFactory = IconFactory.getInstance(HomeActivity.this);
        Drawable drawable = ContextCompat.getDrawable(HomeActivity.this, R.drawable.yellow_marker);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Icon iconYellow = iconFactory.fromBitmap(bitmap);
        drawable = ContextCompat.getDrawable(HomeActivity.this, R.drawable.blue_marker);
        bitmap = ((BitmapDrawable) drawable).getBitmap();
        Icon iconBlue = iconFactory.fromBitmap(bitmap);

        for (LocationMarker loc : locations) {
            if (loc.getSnippet().split("#")[0].equals("collection center") && showCollection) {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(loc.getLat(), loc.getLng()))
                        .title(loc.getTitle())
                        .snippet(loc.getSnippet())
                        .icon(iconYellow)
                );
            } else if (loc.getSnippet().split("#")[0].equals("camp") && showCamps) {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(loc.getLat(), loc.getLng()))
                        .title(loc.getTitle())
                        .snippet(loc.getSnippet())
                        .icon(iconBlue)
                );
            } else {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(loc.getLat(), loc.getLng()))
                        .title(loc.getTitle())
                        .snippet(loc.getSnippet())
                );
            }
        }

        //TODO fetch requests only when user flips the toggle to display them
        locationsOfRequests = model.getRequests();

        map.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String[] markerSnippet = marker.getSnippet().split("#");
                //TODO remove this debug line
                Toast.makeText(HomeActivity.this, markerSnippet[0], Toast.LENGTH_SHORT).show();
                if (markerSnippet[0].equals("camp")) {
                    Intent campIntent = new Intent(HomeActivity.this, CampMgmtScreen.class);
                    campIntent.putExtra("id", Integer.parseInt(markerSnippet[1]));
                    HomeActivity.this.startActivity(campIntent);
                } else if (markerSnippet[0].equals("collection center")) {

                    Intent collectionIntent = new Intent(HomeActivity.this, CollectionCentreScreen.class);
                    collectionIntent.putExtra("id", Integer.parseInt(markerSnippet[1]));
                    HomeActivity.this.startActivity(collectionIntent);
                } else if (markerSnippet[0].equals("request")) {
                    //TODO go to requests screen
                }
                return false;
            }
        });
    }

    public void goToGetHelpScreen(View v) {
        //Go to get help screen
    }

    public void goToGiveHelpScreen(View v) {
        //Go to give help screen
    }

    public void campCheckBoxClicked(View v) {
        if (((CheckBox) v).isChecked()) {
            showCamps = true;
        } else {
            showCamps = false;
        }
    }

    public void collectionCheckBoxClicked(View v) {
        if (((CheckBox) v).isChecked()) {
            showCollection = true;
        } else {
            showCollection = false;
        }
    }

    public void requestCheckBoxClicked(View v) {
        if (((CheckBox) v).isChecked()) {
            showRequests = true;
        } else {
            showRequests = false;
        }
    }
}
