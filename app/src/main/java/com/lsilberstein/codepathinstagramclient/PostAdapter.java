package com.lsilberstein.codepathinstagramclient;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsilberstein.codepathinstagramclient.Model.Post;
import com.lsilberstein.codepathinstagramclient.Utils.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lsilberstein on 10/21/15.
 */
public class PostAdapter extends ArrayAdapter<Post> {

    public PostAdapter(Context context, List<Post> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Post post = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_post, parent, false);
            viewHolder.userProfile = (ImageView) convertView.findViewById(R.id.ivUser);
            viewHolder.username = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.caption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.ivPhoto);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.userProfile.setImageResource(0);
        Picasso.with(getContext()).load(post.user.pictureUrl)
                .transform(new CircleTransformation()).into(viewHolder.userProfile);

        viewHolder.username.setText(post.user.username);

        String htmlCaption = "<font color=\"#125688\">"+post.user.username+"</font>  " + post.caption;
        viewHolder.caption.setText(Html.fromHtml(htmlCaption));

        viewHolder.photo.setImageResource(0);
        Picasso.with(getContext()).load(post.imageUrl).into(viewHolder.photo);

        return convertView;
    }

    private static class ViewHolder {
        public ImageView userProfile;
        public ImageView photo;
        public TextView username;
        public TextView caption;
    }
}
