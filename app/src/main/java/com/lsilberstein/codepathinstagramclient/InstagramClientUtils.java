package com.lsilberstein.codepathinstagramclient;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Contacts Instagram to get photos
 */
public class InstagramClientUtils {
    public static final String CLIENT_ID = "95990fcf9e8343e1aefab4a99f3cc1ba";
    public static final String POPULAR_ENDPOINT = "https://api.instagram.com/v1/media/popular";
    public static final String PARAM_CLIENT_ID = "?client_id=";


    public static String getPopularUrl() {
        return POPULAR_ENDPOINT+PARAM_CLIENT_ID+CLIENT_ID;
    }

    public static ArrayList<Photo> toPhotos(JSONObject response) throws JSONException {
        ArrayList<Photo> photos = new ArrayList<>();
        JSONObject entry = null;
        JSONArray data = response.getJSONArray("data");
        for (int i = 0; i < data.length(); i++) {
            try {
                /* data: => [x] =>
                            type = “image” || “video”
                            caption =>
                                text
                            user =>
                                username
                                profile_picture (url)
                            images =>
                                standard_resolution =>
                                    url
                */
                entry = data.getJSONObject(i);

                // Only care about images for now:
                String type = entry.getString("type");
                if (!type.equals("image")) {
                    continue;
                }

                // get the caption
                String caption = entry.getJSONObject("caption").getString("text");

                // get the username
                String username = entry.getJSONObject("user").getString("username");

                // get the image url
                String image = entry.getJSONObject("images").getJSONObject("standard_resolution").getString("url");

                // construct the photo and add it to the list
                Photo photo = new Photo();
                photo.caption = caption;
                photo.user = username;
                photo.imageUrl = image;
                photo.type = type;
                photos.add(photo);
            } catch (JSONException e) {
                Log.e("Utils", "Bad data for object number " + i);
            }
        }

        return photos;
    }
}
