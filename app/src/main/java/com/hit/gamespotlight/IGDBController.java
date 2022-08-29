package com.hit.gamespotlight;

import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.api.igdb.exceptions.RequestException;
import com.api.igdb.request.IGDBWrapper;
import com.api.igdb.request.TwitchAuthenticator;
import com.api.igdb.utils.Endpoints;
import com.api.igdb.utils.TwitchToken;
import com.google.protobuf.InvalidProtocolBufferException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import proto.Game;
import proto.GameResult;
import proto.Genre;
import proto.GenreResult;

public class IGDBController {

    private IGDBWrapper wrapper;

    private String tId = "4tsmmlutcg0662gvu0u3gn4wgr9q6u";
    private String tSecret = "925wub6c3fhmd01ishbbrbomdsgy3p";
    private String igdbToken;
    private String tag = "IGDBController";
    private Object lock = new Object();

    public IGDBController(Context ctx) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "https://id.twitch.tv/oauth2/token?client_id=" + tId +
                "&client_secret=" + tSecret +
                "&grant_type=client_credentials";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
            try {
                igdbToken = response.getString("access_token");
            } catch (JSONException e) {
                Log.e(tag, "IGDBController: "+e.getMessage());
            }
            Log.i(tag, "onResponse: new IGDB Token "+igdbToken);
        }, error -> {
            Log.e(tag, "IGDBController: "+error.getMessage());
        });

        // TODO: Were stuck waiting forever for IGDB token, unless we supply one ourselves...
        igdbToken = "l0z5gt38cyo4tp2xs8o2v2y1bdwits";
        // TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        queue.add(req);
        Thread t = new Thread(() -> waitForToken());
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        wrapper = IGDBWrapper.INSTANCE;
        wrapper.setCredentials(tId, igdbToken);
    }

    public void waitForToken() {
        while (igdbToken == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public List<Game> getGameList() {
        try {
            byte[] bytes = new RetrieveTask<Game>(wrapper).execute(Endpoints.GAMES.toString(), "fields *;").get();
            return GameResult.parseFrom(bytes).getGamesList();
        } catch (ExecutionException | InterruptedException | InvalidProtocolBufferException ex) {
            Log.e(tag, "getGameList: "+ex.getMessage());
        }
        return Collections.emptyList();
    }

    public List<Genre> getGenreList() {
        try {
            byte[] bytes = new RetrieveTask<Genre>(wrapper).execute(Endpoints.GENRES.toString(), "fields *;").get();
            return GenreResult.parseFrom(bytes).getGenresList();
        } catch (ExecutionException | InterruptedException | InvalidProtocolBufferException ex) {
            Log.e(tag, "getGameList: "+ex.getMessage());
        }
        return Collections.emptyList();
    }


}

class RetrieveTask<T> extends AsyncTask<String, Void, byte[]> {
    private IGDBWrapper wrapper;
    public RetrieveTask(IGDBWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    protected byte[] doInBackground(String... queries) {
        byte[] bytes = new byte[0];
        try {
            bytes = wrapper.apiProtoRequest(Endpoints.valueOf(queries[0]), queries[1]);
        } catch (RequestException e) {
            Log.e("Failed to get list of "+queries[0], e.getMessage());
        }
        return bytes;
    }
}
