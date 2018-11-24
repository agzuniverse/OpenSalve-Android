package com.agzuniverse.agz.opensalve;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class LocationPicker extends AppCompatActivity {
    private MapboxMap map;
    private Marker addressPin;
    private ImageView dropPinView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_picker);

        MapView mapView = findViewById(R.id.locationPickerMap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> {
            map = mapboxMap;
            // Create drop pin using custom image and statically Set drop pin in center of screen
            dropPinView = new ImageView(this);
            dropPinView.setImageResource(R.drawable.ic_location_on);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            float density = getResources().getDisplayMetrics().density;
            params.bottomMargin = (int) (12 * density);
            dropPinView.setLayoutParams(params);
            mapView.addView(dropPinView);

//            Geocoder gc = new Geocoder(this);
//            if (gc.isPresent()) {
//                List<Address> list = null;
//                try {
//                    list = gc.getFromLocation(76.2673, 9.9312, 1);
//                    Log.i("zxcv", list.toString());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Address address = list.get(0);
//                StringBuffer str = new StringBuffer();
//                str.append("Name: " + address.getLocality());
//                String strAddress = str.toString();
//                addressPin.setTitle(strAddress);
//            }
        });
    }

    public void confirmLocation(View v) {
        dropPinView.setVisibility(View.INVISIBLE);
        LatLng position = map.getProjection().fromScreenLocation(
                new PointF(dropPinView.getLeft() + (dropPinView.getWidth() / 2), dropPinView.getBottom())
        );
        addressPin = map.addMarker(new MarkerOptions().position(position));
        map.selectMarker(addressPin);
    }
}
