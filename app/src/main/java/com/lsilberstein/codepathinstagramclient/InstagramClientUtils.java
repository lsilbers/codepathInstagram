package com.lsilberstein.codepathinstagramclient;

import android.content.Context;
import android.util.Log;

import com.lsilberstein.codepathinstagramclient.Model.InstagramUser;
import com.lsilberstein.codepathinstagramclient.Model.Likes;
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

                // get the likes data
                Likes likes = new Likes();
                int count = entry.getJSONObject("likes").getInt("count");
                likes.count = count;
                if(count < 10) {
                    // for small numbers of likes we will display the people who liked it
                    ArrayList<String> likers = new ArrayList<>();
                    JSONArray likeData = entry.getJSONObject("likes").getJSONArray("data");
                    for (i = 0; i < likeData.length(); i++){
                        likers.add(likeData.getJSONObject(i).getString("username"));
                    }
                    likes.topLikers = likers;
                }

                // get the age of the post
                long createdTime = entry.getLong("created_time");

                // construct the post and add it to the list
                Post post = new Post();
                post.caption = caption;
                post.user = new InstagramUser();
                post.user.username = username;
                post.user.pictureUrl = pictureUrl;
                post.imageUrl = image;
                post.type = type;
                post.likes = likes;
                post.createdTime = createdTime;
                posts.add(post);
            } catch (JSONException e) {
                Log.e("Utils", "Bad data for object number " + i);
            }
        }

        return posts;
    }


    public static String formatUsernameHtml(Context context, String username) {
        return "<font color=\""+context.getResources().getColor(R.color.username)+"\">"
                + username + "</font>";
    }

    public static String formatLikesHtml(Context context, int count) {
        return "<font color=\""+context.getResources().getColor(R.color.likes)+"\">" + count
                + "</font> " + context.getResources().getString(R.string.likes);
    }

    public static String formatLikesHtml(Context context, ArrayList<String> likers) {
        int likesColour = context.getResources().getColor(R.color.likes);
        int usernameColour = context.getResources().getColor(R.color.username);
        StringBuilder builder = new StringBuilder("<font color=\"")
                .append(usernameColour).append("\">");
        for (String username : likers) {
            builder.append(username).append(" ");
        }
        builder.append("</font>");
        builder.append("<font color=\"").append(likesColour).append("\">");
        if (likers.size() > 1) {
            builder.append(context.getResources().getString(R.string.likesMany));
        } else {
            builder.append(context.getResources().getString(R.string.likesSingle));
        }
        builder.append("</font>");
        return builder.toString();
    }

    private static String tagMatcher = "(#|@)([A-Za-z0-9-_]+)";
    public static String formatCaptionHtml(Context context, String caption){
        return caption.replaceAll(tagMatcher, "<font color=\""+context.getResources().getColor(R.color.tag)+"\">$1$2</font>");
    }
}
