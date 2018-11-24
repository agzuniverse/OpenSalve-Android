package com.agzuniverse.agz.opensalve;

import android.app.Dialog;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.agzuniverse.agz.opensalve.widgets.NewCampDialog;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationPicker extends AppCompatActivity implements NewCampDialog.UpdateMap {
    private MapboxMap map;
    private Marker addressPin;
    private ImageView dropPinView;
    private LatLng position;

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
        position = map.getProjection().fromScreenLocation(
                new PointF(dropPinView.getLeft() + (dropPinView.getWidth() / 2), dropPinView.getBottom())
        );
        addressPin = map.addMarker(new MarkerOptions().position(position));
        map.selectMarker(addressPin);
        openNewLocDialog();
    }

    public void openNewLocDialog() {
        DialogFragment dialog = new NewCampDialog();
        dialog.show(getSupportFragmentManager(), "NewCampDialog");
    }

    @Override
    public void onAddNewCamp(DialogFragment dialog) {
        //TODO handle cancel
        Dialog d = dialog.getDialog();
        JSONObject json = new JSONObject();
        try {
            EditText t = d.findViewById(R.id.new_camp_name);
            json.put("location", t.getText().toString());
            t = d.findViewById(R.id.new_camp_manager);
            json.put("incharge", t.getText().toString());
            t = d.findViewById(R.id.new_camp_contact);
            json.put("phone", t.getText().toString());
            json.put("lat", position.getLatitude());
            json.put("lng", position.getLongitude());
            //TODO add image

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //TODO send lat and lng to homeactivity to display immediately
                finish();
            }
        };
        Runnable runnable = () -> {
            //TODO make POST request to backend with json
            handler.sendEmptyMessage(0);
        };
        Thread async = new Thread(runnable);
        async.start();

    }
}
