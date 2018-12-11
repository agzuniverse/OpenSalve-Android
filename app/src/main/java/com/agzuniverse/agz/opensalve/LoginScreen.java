package com.agzuniverse.agz.opensalve;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agzuniverse.agz.opensalve.Utils.GlobalStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginScreen extends AppCompatActivity {
    private String username;
    private String password;
    private JSONObject json;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (GlobalStore.isVolunteer) {
            setContentView(R.layout.logout_screen);
            TextView t = findViewById(R.id.username_goes_here);
            t.setText(GlobalStore.logged_in_as);
        } else setContentView(R.layout.login_screen);
    }

    public void logoutUser(View v) {
        GlobalStore.token = "0";
        GlobalStore.logged_in_as = "";
        GlobalStore.isVolunteer = false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        Toast.makeText(LoginScreen.this, "Logout successful", Toast.LENGTH_SHORT).show();
        LoginScreen.this.finish();
    }

    public void loginUser(View v) {
        EditText e = findViewById(R.id.username);
        username = e.getText().toString();
        e = findViewById(R.id.password);
        password = e.getText().toString();
        if (username.equals("") || password.equals("")) {
            Toast.makeText(LoginScreen.this, "Username/Password empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String token = null;
                try {
                    token = json.getString("token");
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginScreen.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", token);
                    editor.commit();
                    GlobalStore.isVolunteer = true;
                    GlobalStore.logged_in_as = username;
                    GlobalStore.token = token;
                    Toast.makeText(LoginScreen.this, "Login successful", Toast.LENGTH_SHORT).show();
                    LoginScreen.this.finish();
                } catch (JSONException e1) {
                    Toast.makeText(LoginScreen.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
                    e1.printStackTrace();
                }

            }
        };
        Runnable runnable = () -> {
            String basicAuthString = Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
            String apiUrl = getResources().getString(R.string.base_api_url);
            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
            Request request = new Request.Builder().url(apiUrl + "/api/accounts/login")
                    .header("Authorization", "Basic " + basicAuthString)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                json = new JSONObject(response.body().string());
                handler.sendEmptyMessage(0);
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        Toast.makeText(LoginScreen.this, "Logging in...", Toast.LENGTH_SHORT).show();
        thread.start();
    }
}
