package com.nicootech.capstoneandroidcourserafirstweek;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Album {
    String image_url;
    String album_name;

    public String getImage_url() {
        return image_url;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public Album(String image_url, String album_name) {
        this.image_url = image_url;
        this.album_name = album_name;
    }

    public static ArrayList<Album> parse(JSONObject json) throws JSONException {
        ArrayList<Album>  albums = new ArrayList<>();

        JSONObject jsonAlbums = json.optJSONObject("albums");
        if(jsonAlbums != null) {
            JSONArray jsonItems = jsonAlbums.optJSONArray("items");

            if (jsonItems!= null) {
                for (int i = 0; i < jsonItems.length(); i++) {
                    JSONObject item = jsonItems.optJSONObject(i);
                    String name = optString(item, "name");

                    JSONArray images = item.getJSONArray("images");
                    String image_url = "";
                    if (images != null && images.length()> 0) {
                        JSONObject image = images.optJSONObject(0);
                        image_url = optString(image, "url");
                    }

                    Album alb = new Album(image_url, name);
                    albums.add(alb);
                }
            }
        }

        return albums;
    }

    public static String optString(JSONObject jsonObject, String key) {
        // The regular optString() will cast the NULL value into a String
        // "null". To avoid that, we check for it and return null instead.
        if (jsonObject.isNull(key)) {
            return null;
        }

        return jsonObject.optString(key, null);
    }



}
