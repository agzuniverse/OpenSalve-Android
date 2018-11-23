package com.agzuniverse.agz.opensalve;

import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationPicker extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_picker);

        MapView mapView = findViewById(R.id.locationPickerMap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> {
            // Create drop pin using custom image and statically Set drop pin in center of screen
            ImageView dropPinView = new ImageView(this);
            dropPinView.setImageResource(R.drawable.ic_location_on);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            float density = getResources().getDisplayMetrics().density;
            params.bottomMargin = (int) (12 * density);
            dropPinView.setLayoutParams(params);
            mapView.addView(dropPinView);
            //TODO on confirm location do the following
            dropPinView.setVisibility(View.INVISIBLE);
            // Get LatLng of selected location
            LatLng position = mapboxMap.getProjection().fromScreenLocation(
                    new PointF(dropPinView.getLeft() + (dropPinView.getWidth() / 2), dropPinView.getBottom())
            );

            // Remove previous address pin (if exists)
//            if (addressPin != null) {
//                if (mapboxMap != null && addressPin != null) {
//                    mapboxMap.removeMarker(addressPin);
//                }
//            }

//          Create new address pin
            Marker addressPin = mapboxMap.addMarker(new MarkerOptions().title("Loading address…").position(position));
            mapboxMap.selectMarker(addressPin);
            Geocoder gc = new Geocoder(this, Locale.getDefault());
            if (gc.isPresent()) {
                List<Address> list = null;
                try {
                    list = gc.getFromLocation(position.getLongitude(), position.getLatitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = list.get(0);
                StringBuffer str = new StringBuffer();
                str.append("Name: " + address.getLocality());
                String strAddress = str.toString();
                addressPin.setTitle(strAddress);
            }
            // Start the Geocoding…
            //Create Geocoding client
//            MapboxGeocoder client = new MapboxGeocoder.Builder()
//                    .setAccessToken(getResources().getString(R.string.mapbox_api_token))
//                    .setCoordinates(position.getLongitude(), position.getLatitude())
//                    .setType(GeocoderCriteria.TYPE_ADDRESS)
//                    .build();
//
//            //Place the request
//            client.enqueue(new Callback<GeocoderResponse>() {
//                @Override
//                public void onResponse(Call call, Response response) {
//                    List<GeocoderFeature> results = response.body().getClass();
//                    String address;
//                    if (results.size() > 0) {
//                        GeocoderFeature feature = results.get(0);
//                        address = feature.getPlaceName() + " " + feature.getText();
//                        //            Update Address Pin's InfoWindow With Address
//                        if (addressPin != null) {
//                            addressPin.setTitle(TextUtils.isEmpty(address) ? "No address found" : address);
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<GeocoderResponse> call, Throwable t) {
//                }
//            });
        });


    }
}
