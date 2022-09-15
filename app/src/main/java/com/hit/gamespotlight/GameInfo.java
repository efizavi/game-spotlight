package com.hit.gamespotlight;

import com.google.protobuf.Timestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import proto.Genre;
import proto.Platform;

public class GameInfo implements Serializable {
     private String name;
     private String summary;
     private String imageUrl;
     private int releaseYear;
     private List<Genre> genres;
     private List<Platform> platforms;
     private int rating;

    public GameInfo(String name, String description, String imageUrl, Timestamp timestamp, List<Genre> genres, int rating, List<Platform> platforms) {
        this.name = name;
        this.summary = description;
        this.imageUrl = "https:"+imageUrl;
        this.releaseYear = new Date(timestamp.getSeconds()*1000).getYear() + 1900;
        this.genres = genres;
        this.rating = rating;
        this.platforms = platforms;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }


    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


    public String getGenresNames() {
        StringBuilder builder = new StringBuilder();
        int i=0;
        for (; i<genres.size()-1; i++) {
            final Genre genre = genres.get(i);
            builder.append(genre.getName());
            builder.append(", ");
        }
        if(i == genres.size()-1) {
            Genre lastGenre = genres.get(i);
            builder.append(lastGenre.getName());
        }
        return builder.toString();
    }

    public String getPlatformsNames(){
        StringBuilder builder = new StringBuilder();
        int i=0;
        for (; i<platforms.size()-1; i++) {
            final Platform platform = platforms.get(i);
            builder.append(platform.getAbbreviation());
            builder.append(", ");
        }
        if(i == platforms.size()-1) {
            Platform lastPlatform = platforms.get(i);
            builder.append(lastPlatform.getName());
        }
        return builder.toString();
    }

    public boolean hasGenre(String genreName){
        for (Genre g: genres) {
            if(g.getName().equals(genreName)){
                return true;
            }
        }
        return false;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }
}
