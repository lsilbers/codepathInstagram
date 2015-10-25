package com.lsilberstein.codepathinstagramclient;

import android.util.Log;

import com.lsilberstein.codepathinstagramclient.Model.InstagramUser;
import com.lsilberstein.codepathinstagramclient.Model.Post;

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

    public static ArrayList<Post> toPhotos(JSONObject response) throws JSONException {
        ArrayList<Post> posts = new ArrayList<>();
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
                String pictureUrl = entry.getJSONObject("user").getString("profile_picture");

                // get the image url
                String image = entry.getJSONObject("images").getJSONObject("standard_resolution").getString("url");

                // construct the post and add it to the list
                Post post = new Post();
                post.caption = caption;
                post.user = new InstagramUser();
                post.user.username = username;
                post.user.pictureUrl = pictureUrl;
                post.imageUrl = image;
                post.type = type;
                posts.add(post);
            } catch (JSONException e) {
                Log.e("Utils", "Bad data for object number " + i);
            }
        }

        return posts;
    }
}
