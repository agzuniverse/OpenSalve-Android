package com.agzuniverse.agz.opensalve;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;

import com.agzuniverse.agz.opensalve.Modals.LocationMarker;
import com.agzuniverse.agz.opensalve.ViewModels.LocationMarkersViewModel;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

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
    private DrawerLayout drawer;
    private String query = "";

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
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        drawer = findViewById(R.id.drawer_layout);

        EditText search = actionBar.getCustomView().findViewById(R.id.appbar_search_field);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                query = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        search.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                refreshMapOverlay();
            }
            return false;
        });

        Mapbox.getInstance(this, getString(R.string.mapbox_api_token));

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        getMarkers();

        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(menuItem -> {
            drawer.closeDrawers();
            if (menuItem.getTitle().toString() == getResources().getString(R.string.imp_contacts)) {
                Intent intent = new Intent(HomeActivity.this, Helplines.class);
                HomeActivity.this.startActivity(intent);
            }
            return true;
        });

//        Intent debug = new Intent(this, CampMgmtScreen.class);
//        this.startActivity(debug);
    }

    public void getMarkers() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mapView.getMapAsync(mapboxMap -> configMapOverlay(mapboxMap));
            }
        };
        Runnable runnable = () -> {
            locations = model.getCampsAndCollectionCentres(getString(R.string.base_api_url));
            locationsOfRequests = model.getRequests(getString(R.string.base_api_url));
            handler.sendEmptyMessage(0);
        };
        Thread async = new Thread(runnable);
        async.start();
    }

    //Make the ham icon open side drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void configMapOverlay(MapboxMap map) {
        IconFactory iconFactory = IconFactory.getInstance(HomeActivity.this);
        Drawable drawable = ContextCompat.getDrawable(HomeActivity.this, R.drawable.yellow_marker);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Icon iconYellow = iconFactory.fromBitmap(bitmap);
        drawable = ContextCompat.getDrawable(HomeActivity.this, R.drawable.blue_marker);
        bitmap = ((BitmapDrawable) drawable).getBitmap();
        Icon iconBlue = iconFactory.fromBitmap(bitmap);

        for (LocationMarker loc : locations) {
            if (query.equals("")) {
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
                }
            } else {
                if (loc.getSnippet().split("#")[0].equals("collection center")
                        && showCollection
                        && loc.getTitle().toLowerCase().contains(query)) {
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(loc.getLat(), loc.getLng()))
                            .title(loc.getTitle())
                            .snippet(loc.getSnippet())
                            .icon(iconYellow)
                    );
                } else if (loc.getSnippet().split("#")[0].equals("camp")
                        && showCamps
                        && loc.getTitle().toLowerCase().contains(query)) {
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(loc.getLat(), loc.getLng()))
                            .title(loc.getTitle())
                            .snippet(loc.getSnippet())
                            .icon(iconBlue)
                    );
                }
            }
        }

        if (showRequests) {
            for (LocationMarker loc : locationsOfRequests) {
                if (query.equals("")) {
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(loc.getLat(), loc.getLng()))
                            .title(loc.getTitle())
                            .snippet(loc.getSnippet())
                    );
                } else {
                    if (loc.getTitle().toLowerCase().contains(query)) {
                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(loc.getLat(), loc.getLng()))
                                .title(loc.getTitle())
                                .snippet(loc.getSnippet())
                        );
                    }
                }
            }
        }

        map.setOnMarkerClickListener(marker -> {
            String[] markerSnippet = marker.getSnippet().split("#");
            if (markerSnippet[0].equals("camp")) {
                Intent campIntent = new Intent(HomeActivity.this, CampMgmtScreen.class);
                campIntent.putExtra("id", Integer.parseInt(markerSnippet[1]));
                HomeActivity.this.startActivity(campIntent);
            } else if (markerSnippet[0].equals("collection center")) {
                Intent collectionIntent = new Intent(HomeActivity.this, CollectionCentreScreen.class);
                collectionIntent.putExtra("id", Integer.parseInt(markerSnippet[1]));
                HomeActivity.this.startActivity(collectionIntent);
            } else if (markerSnippet[0].equals("request")) {
                Intent reqIntent = new Intent(HomeActivity.this, GetHelp.class);
                reqIntent.putExtra("id", Integer.parseInt(markerSnippet[1]));
                HomeActivity.this.startActivity(reqIntent);
            }
            return false;
        });
    }

    public void goToGetHelpScreen(View v) {
        Intent intent = new Intent(this, GetHelp.class);
        intent.putExtra("id", 0);
        this.startActivity(intent);
    }

    public void campCheckBoxClicked(View v) {
        if (((CheckBox) v).isChecked()) {
            showCamps = true;
            refreshMapOverlay();
        } else {
            showCamps = false;
            refreshMapOverlay();
        }
    }

    public void collectionCheckBoxClicked(View v) {
        if (((CheckBox) v).isChecked()) {
            showCollection = true;
            refreshMapOverlay();
        } else {
            showCollection = false;
            refreshMapOverlay();
        }
    }

    public void requestCheckBoxClicked(View v) {
        if (((CheckBox) v).isChecked()) {
            showRequests = true;
            refreshMapOverlay();
        } else {
            showRequests = false;
            refreshMapOverlay();
        }
    }

    public void refreshMapOverlay() {
        mapView = findViewById(R.id.mapView);
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.clear();
            configMapOverlay(mapboxMap);
        });
    }
}
