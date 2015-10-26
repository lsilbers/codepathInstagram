package com.lsilberstein.codepathinstagramclient.Model;


import java.util.ArrayList;

/**
 * Created by lsilberstein on 10/21/15.
 */
public class Post {
    public String type;
    public InstagramUser user;
    public Likes likes;
    public String caption;
    public String imageUrl;
    //"created_time": "1296655883"
    public long createdTime;
    public ArrayList<String> comments;
}
