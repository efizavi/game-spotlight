package com.hit.gamespotlight;

import android.os.AsyncTask;
import android.util.Log;

import com.api.igdb.exceptions.RequestException;
import com.api.igdb.request.IGDBWrapper;
import com.api.igdb.utils.Endpoints;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import proto.Game;
import proto.GameResult;
import proto.Genre;
import proto.GenreResult;

public class IGDBController {

    private final IGDBWrapper wrapper;
    private final String tag = "IGDBController";

    public IGDBController(String igdbToken, String twitchId) {
        wrapper = IGDBWrapper.INSTANCE;
        wrapper.setCredentials(twitchId, igdbToken);
    }

    public List<Game> getGameList() {
        try {
            byte[] bytes = new RetrieveTask(wrapper).execute(Endpoints.GAMES.toString(),
                    "fields name, cover.url, first_release_date, total_rating_count, platforms.abbreviation, summary,genres.*, total_rating, slug; where total_rating > 0;limit 300;").get();
            return GameResult.parseFrom(bytes).getGamesList();
        } catch (ExecutionException | InterruptedException | InvalidProtocolBufferException ex) {
            Log.e(tag, "getGameList: "+ex.getMessage());
        }
        return Collections.emptyList();
    }

    public List<Game> getGameList(String searchString) {
        try {
            byte[] bytes = new RetrieveTask(wrapper).execute(Endpoints.GAMES.toString(), "fields name, cover.url, first_release_date, total_rating_count, platforms.abbreviation, summary,genres.*, total_rating, slug; limit 20; search \""+searchString+"\";").get();
            return GameResult.parseFrom(bytes).getGamesList();
        } catch (ExecutionException | InterruptedException | InvalidProtocolBufferException ex) {
            Log.e(tag, "getGameList: "+ex.getMessage());
        }
        return Collections.emptyList();
    }

    public List<Genre> getGenreList() {
        try {
            byte[] bytes = new RetrieveTask(wrapper).execute(Endpoints.GENRES.toString(), "fields *;").get();
            return GenreResult.parseFrom(bytes).getGenresList();
        } catch (ExecutionException | InterruptedException | InvalidProtocolBufferException ex) {
            Log.e(tag, "getGenreList: "+ex.getMessage());
        }
        return Collections.emptyList();
    }


}

class RetrieveTask extends AsyncTask<String, Void, byte[]> {
    private final IGDBWrapper wrapper;
    public RetrieveTask(IGDBWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    protected byte[] doInBackground(String... queries) {
        byte[] bytes = new byte[0];
        try {
            bytes = wrapper.apiProtoRequest(Endpoints.valueOf(queries[0]), queries[1]);
        } catch (RequestException e) {
            int x = 9;
//            Log.e("Failed to get list of "+queries[0], e.getMessage());
        }
        return bytes;
    }
}
