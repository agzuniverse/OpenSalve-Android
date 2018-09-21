package com.agzuniverse.agz.opensalve;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.agzuniverse.agz.opensalve.Modals.LocationMarker;
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

    private MapView mapView;
    private List<LocationMarker> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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

//        Intent debug = new Intent(this, CollectionCentreScreen.class);
//        this.startActivity(debug);
    }

    public void configMapOverlay(MapboxMap map) {

        //TODO fetch coordinates of all the markers from API
        String[] titles = new String[]{"MEC", "RSET", "RSC", "GSouk"};
        String[] snippets = new String[]{"camp", "camp", "collection center", "collection center"};
        Double[] lats = new Double[]{9.9323, 9.9306, 9.9310, 9.9339};
        Double[] lngs = new Double[]{76.2633, 76.2653, 76.2673, 76.2693};
        for (int i = 0; i < titles.length && i < snippets.length && i < lats.length && i < lngs.length; i++) {
            LocationMarker current = new LocationMarker(titles[i], snippets[i], lats[i], lngs[i]);
            locations.add(current);
        }

        IconFactory iconFactory = IconFactory.getInstance(HomeActivity.this);
        Drawable drawable = ContextCompat.getDrawable(HomeActivity.this, R.drawable.yellow_marker);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Icon iconYellow = iconFactory.fromBitmap(bitmap);

        for (LocationMarker a : locations) {
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(a.getLat(), a.getLng()))
                    .title(a.getTitle())
                    .snippet(a.getSnippet())
                    .icon(iconYellow)
            );
        }

        map.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Toast.makeText(HomeActivity.this, marker.getSnippet(), Toast.LENGTH_SHORT).show();
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
}
