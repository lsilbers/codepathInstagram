package com.lsilberstein.codepathinstagramclient.Model;


import java.util.ArrayList;
import java.util.Date;

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
    public Date createdTime;
    public ArrayList<String> comments;
}
