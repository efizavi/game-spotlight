package com.hit.gamespotlight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private IGDBController controller;
    private final String tId = "4tsmmlutcg0662gvu0u3gn4wgr9q6u";
    private final String tSecret = "925wub6c3fhmd01ishbbrbomdsgy3p";

    public IGDBController getController(){
        return this.controller;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://id.twitch.tv/oauth2/token?client_id=" + tId +
                "&client_secret=" + tSecret +
                "&grant_type=client_credentials";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            try {
                String igdbToken = response.getString("access_token");
                Log.i(getLocalClassName(), "onResponse: new IGDB Token "+igdbToken);
                controller = new IGDBController(igdbToken, tId);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainerView, new NavigationFragment());
                transaction.commit();

            } catch (JSONException e) {
                Log.e(getLocalClassName(), e.getMessage());
            }
        }, e -> Log.e(getLocalClassName(), e.getMessage()));
        queue.add(req);

    }
}