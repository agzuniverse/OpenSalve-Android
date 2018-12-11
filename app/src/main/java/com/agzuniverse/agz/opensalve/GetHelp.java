package com.agzuniverse.agz.opensalve;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agzuniverse.agz.opensalve.Modals.GetHelpData;
import com.agzuniverse.agz.opensalve.Utils.GlobalStore;
import com.agzuniverse.agz.opensalve.ViewModels.GetHelpViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetHelp extends AppCompatActivity {
    private EditText desc;
    private EditText name;
    private EditText contact;
    private int id;
    private GetHelpData data;
    private GetHelpViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id", 0);
        if (id == 0) {
            setContentView(R.layout.get_help);
            desc = findViewById(R.id.desc);
            name = findViewById(R.id.name);
            contact = findViewById(R.id.contact);
        } else {
            setContentView(R.layout.get_help_view);
            model = ViewModelProviders.of(this).get(GetHelpViewModel.class);
            fetchGetHelpDataAsync(id);

            if (GlobalStore.isVolunteer) {
                LinearLayout l = findViewById(R.id.get_help_manage_buttons);
                l.setVisibility(View.VISIBLE);
            }
        }
    }

    private void fetchGetHelpDataAsync(int id) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                setGetHelpData();
            }
        };
        Runnable runnable = () -> {
            data = model.getHelpData(id, getResources().getString(R.string.base_api_url));
            handler.sendEmptyMessage(0);
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void setGetHelpData() {
        TextView t = findViewById(R.id.name_view);
        t.setText(data.getName());
        t = findViewById(R.id.contact_view);
        t.setText(data.getContact());
        t = findViewById(R.id.desc_view);
        t.setText(data.getDesc());
        t = findViewById(R.id.req_status_view);
        t.setText(data.getStatus());
        if (data.getStatus().equals("Help is on the way")) {
            t.setTextColor(getResources().getColor(R.color.safe));
        } else t.setTextColor(getResources().getColor(R.color.danger));
        if (!data.isEvac()) {
            t = findViewById(R.id.evac_needed);
            t.setVisibility(View.GONE);
        }
        if (!data.isFoodWater()) {
            t = findViewById(R.id.foodwater_needed);
            t.setVisibility(View.GONE);
        }
        if (!data.isMedical()) {
            t = findViewById(R.id.medical_needed);
            t.setVisibility(View.GONE);
        }
        if (!data.isFirstAid()) {
            t = findViewById(R.id.firstaid_needed);
            t.setVisibility(View.GONE);
        }
        if (!data.isTransport()) {
            t = findViewById(R.id.transport_needed);
            t.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Permission granted
        } else {
            Toast.makeText(this, "Please grant location permission", Toast.LENGTH_LONG).show();
        }
    }

    public void submitRequest(View v) {
        String n = name.getText().toString();
        String c = contact.getText().toString();
        String nDesc = desc.getText().toString();

        if ((n.equals("") || c.equals("")) || !TextUtils.isDigitsOnly(c)) {
            Toast.makeText(GetHelp.this, "Please check your input", Toast.LENGTH_SHORT).show();
            return;
        }

        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("lat", location.getLatitude());
                                obj.put("lng", location.getLongitude());
                                obj.put("name", n);
                                obj.put("phone", c);
                                obj.put("desc", nDesc);
                                obj.put("location", "");
                                obj.put("status", "");
                                obj.put("need_rescue", String.valueOf(((CheckBox) findViewById(R.id.evac)).isChecked()));
                                obj.put("need_food_water", String.valueOf(((CheckBox) findViewById(R.id.foodwater)).isChecked()));
                                obj.put("need_medical", String.valueOf(((CheckBox) findViewById(R.id.medical)).isChecked()));
                                obj.put("need_first_aid", String.valueOf(((CheckBox) findViewById(R.id.firstaid)).isChecked()));
                                obj.put("need_transport", String.valueOf(((CheckBox) findViewById(R.id.transport)).isChecked()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Handler handler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    Toast.makeText(GetHelp.this, "Request submitted successfuly", Toast.LENGTH_SHORT).show();
                                    GetHelp.this.finish();
                                }
                            };
                            Runnable runnable = () -> {
                                String apiUrl = getResources().getString(R.string.base_api_url);
                                OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
                                Request request = new Request.Builder()
                                        .url(apiUrl + "/api/help/")
                                        .header("Content-Type", "application/json")
                                        .post(requestBody)
                                        .build();
                                try {
                                    Response response = client.newCall(request).execute();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                handler.sendEmptyMessage(0);
                            };
                            Thread async = new Thread(runnable);
                            async.start();
                        } else {
                            Toast.makeText(this, "Error getting location. Please turn on GPS.", Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }

    public void markResponderDispatched(View v) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                TextView status = findViewById(R.id.req_status_view);
                status.setTextColor(getResources().getColor(R.color.safe));
                status.setText("Teams have responded to the request.");
            }
        };
        Runnable runnable = () -> {
            //TODO send request to backend to change status
            handler.sendEmptyMessage(0);
        };
        Toast.makeText(GetHelp.this, "Changing status...", Toast.LENGTH_LONG).show();
        Thread async = new Thread(runnable);
        async.start();
    }

    public void markRequestResolved(View v) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(GetHelp.this, "Request closed successfully", Toast.LENGTH_LONG).show();
                GetHelp.this.finish();
            }
        };
        Runnable runnable = () -> {
            //TODO delete request from backend
            handler.sendEmptyMessage(0);
        };
        Thread async = new Thread(runnable);
        async.start();
    }
}
