package com.lsilberstein.codepathinstagramclient;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.lsilberstein.codepathinstagramclient.Model.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class InstagramClientActivity extends AppCompatActivity {
    private static final String TAG = "InstagramClient";
    private ArrayList<Post> posts;
    private PostAdapter aPhotos;
    private ListView lvPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });

        // get the view
        lvPhotos = (ListView) findViewById(R.id.lvPhotos);

        posts = new ArrayList<>();
        // construct the adapter
        aPhotos = new PostAdapter(this, posts);
        // set the adapter for the view
        lvPhotos.setAdapter(aPhotos);
        // retrieve the data to display
        fetchPopularPhotos();

    }

    private void fetchPopularPhotos() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(InstagramClientUtils.getPopularUrl(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.i(TAG, "Successfully retrieved data");
                    ArrayList<Post> returnedPosts = InstagramClientUtils.toPhotos(response);
                    aPhotos.addAll(returnedPosts);
                    Log.i(TAG, "Added Photos");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "failed with status code " + statusCode + " and response string:\n" + responseString);
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
