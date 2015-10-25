package com.lsilberstein.codepathinstagramclient;

import android.content.Context;
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
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_post, parent, false);
        }
        Post post = getItem(position);
        ImageView ivUser = (ImageView) convertView.findViewById(R.id.ivUser);
        ivUser.setImageResource(0);
        Picasso.with(getContext()).load(post.user.pictureUrl).transform(new CircleTransformation()).into(ivUser);

        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        tvUsername.setText(post.user.username);
        TextView tvAuthor = (TextView) convertView.findViewById(R.id.tvAuthor);
        tvAuthor.setText(post.user.username);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        tvCaption.setText(post.caption);

        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        ivPhoto.setImageResource(0);

        Picasso.with(getContext()).load(post.imageUrl).into(ivPhoto);

        return convertView;
    }
}
