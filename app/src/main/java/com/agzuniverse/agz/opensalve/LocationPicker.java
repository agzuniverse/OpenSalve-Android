package com.agzuniverse.agz.opensalve;

import android.app.Dialog;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.agzuniverse.agz.opensalve.Utils.GlobalStore;
import com.agzuniverse.agz.opensalve.widgets.NewCampDialog;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LocationPicker extends AppCompatActivity implements NewCampDialog.UpdateMap {
    private MapboxMap map;
    private ImageView dropPinView;
    private LatLng position;
    private boolean isCamp;

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
        position = map.getProjection().fromScreenLocation(
                new PointF(dropPinView.getLeft() + (dropPinView.getWidth() / 2), dropPinView.getBottom())
        );
        openNewLocDialog();
    }

    public void openNewLocDialog() {
        DialogFragment dialog = new NewCampDialog();
        dialog.show(getSupportFragmentManager(), "NewCampDialog");
    }

    @Override
    public void onAddNewCamp(DialogFragment dialog) {
        Dialog d = dialog.getDialog();
        JSONObject json = new JSONObject();
        EditText x = d.findViewById(R.id.new_camp_name);
        EditText y = d.findViewById(R.id.new_camp_manager);
        EditText z = d.findViewById(R.id.new_camp_contact);
        if (x.getText().toString().equals("") || y.getText().toString().equals("") || z.getText().toString().equals("")) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
        } else {
            try {
                json.put("location", x.getText().toString());
                GlobalStore.title = x.getText().toString();
                json.put("incharge", y.getText().toString());
                json.put("phone", z.getText().toString());
                json.put("lat", position.getLatitude());
                json.put("lng", position.getLongitude());
                json.put("photo", "null");
                json.put("supplies", "");
                json.put("capacity", "50");
                json.put("number_of_people", "0");
                //TODO add image
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Spinner spin = d.findViewById(R.id.camp_or_collection_spinner);
            String s = spin.getSelectedItem().toString();
            isCamp = s.equals(getResources().getStringArray(R.array.camp_or_collection_spinner_options)[0]);
            if (isCamp) {
                Toast.makeText(this, "Registering new camp..", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Registering new collection center..", Toast.LENGTH_LONG).show();
            }
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    GlobalStore.lat = position.getLatitude();
                    GlobalStore.lng = position.getLongitude();
                    GlobalStore.newDataPresent = true;
                    finish();
                }
            };
            Runnable runnable = () -> {
                String apiUrl = getResources().getString(R.string.base_api_url);
                String reqUrl = apiUrl;
                int id = 1;
                if (isCamp) {
                    reqUrl = reqUrl + "/api/camps/";
                } else {
                    reqUrl = reqUrl + "/api/collectioncentres/";
                }

                OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

                Request request = new Request.Builder()
                        .url(reqUrl)
                        .header("Authorization", "Token " + GlobalStore.token)
                        .header("Content-Type", " application/json")
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    Log.i("qwe", response.body().string());
                    //TODO get ID in response
//                    JSONObject extractId = new JSONObject(response.body().string());
//                    id = extractId.getInt("id");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (isCamp) {
                    GlobalStore.snippet = "camp#" + String.valueOf(id);
                } else {
                    GlobalStore.snippet = "collection center#" + String.valueOf(id);
                }

                handler.sendEmptyMessage(0);
            };
            Thread async = new Thread(runnable);
            async.start();
            d.dismiss();
        }
    }
}
