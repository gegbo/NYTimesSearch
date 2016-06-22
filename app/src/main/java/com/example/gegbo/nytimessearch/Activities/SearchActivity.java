package com.example.gegbo.nytimessearch.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.gegbo.nytimessearch.Adapters.ArticleArrayAdapter;
import com.example.gegbo.nytimessearch.EndlessRecyclerViewScrollListener;
import com.example.gegbo.nytimessearch.ItemClickSupport;
import com.example.gegbo.nytimessearch.Models.Article;
import com.example.gegbo.nytimessearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    public static final int SETTINGS_REQUEST_CODE = 5;

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpViews();
    }

    public void setUpViews() {
////        etQuery = (EditText) findViewById(R.id.etQuery);
//        gvResults = (GridView) findViewById(R.id.gvResults);
////        btnSearch = (Button) findViewById(R.id.btnSearch);
//
//        articles = new ArrayList<>();
//        adapter = new ArticleArrayAdapter(this,articles);
//        gvResults.setAdapter(adapter);
//
//        //hook up listener for grid click
//        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //create an intent to display the article
//                Intent i = new Intent(getApplicationContext(),ArticleActivity.class);
//                //get the article to display
//                Article article = articles.get(position);
//                //pass in that article into intent
//                i.putExtra("article",article);
//                //launch the activity
//                startActivity(i);
//            }
//        });

        // Lookup the recyclerview in activity layout
        RecyclerView rvArticles = (RecyclerView) findViewById(R.id.rvArticles);
        // Initialize articles
        articles = new ArrayList<>();
        // Create adapter passing in the sample user data
        adapter = new ArticleArrayAdapter(this,articles);
        // Attach the adapter to the recyclerview to populate items
        rvArticles.setAdapter(adapter);
        // Set layout manager to position the items
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        rvArticles.setLayoutManager(gridLayoutManager);

        // Add the scroll listener
        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadMoreArticles(page);
            }
        });

//        rvArticles.setOnClickListener(new AdapterView.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //create an intent to display the article
//                Intent i = new Intent(getApplicationContext(),ArticleActivity.class);
//                //get the article to display
//                Article article = articles.get(position);
//                //pass in that article into intent
//                i.putExtra("article",article);
//                //launch the activity
//                startActivity(i);
//            }
//        });

        ItemClickSupport.addTo(rvArticles).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        //create an intent to display the article
                        Intent i = new Intent(getApplicationContext(),ArticleActivity.class);
                        //get the article to display
                        Article article = articles.get(position);
                        //pass in that article into intent
                        i.putExtra("article", Parcels.wrap(article));
                        //launch the activity
                        startActivity(i);
                    }
                }
        );
    }

    // Append more data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    private void loadMoreArticles(int page) {
        // Send an API request to retrieve appropriate data using the offset value as a parameter.
        //  --> Deserialize API response and then construct new objects to append to the adapter
        //  --> Notify the adapter of the changes

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        SearchView searchView = (SearchView) findViewById(R.id.action_search);

        RequestParams params = new RequestParams();

        params.put("api-key","3931788f3f054e13b859ae0bbea30f54");
        params.put("page",page);
        if (searchView != null) {
            params.put("q",searchView.getQuery());
        }

        client.get(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
//                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG",articles.toString());
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } );
    }

    public void onArticleSearch(String query) {
        //String query = etQuery.getText().toString();

        //Toast.makeText(this,"searching for " + query, Toast.LENGTH_LONG).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();

        params.put("api-key","3931788f3f054e13b859ae0bbea30f54");
        params.put("page",0);
        params.put("q",query);

        client.get(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;

                try {
                    articles.clear();
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
//                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG",articles.toString());
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                onArticleSearch(query);

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.filter:
                Toast.makeText(this,"Clicked on settings button!",Toast.LENGTH_LONG).show();
                launchSettingsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchSettingsActivity() {
        Intent intent = new Intent(this, FilterActivity.class);

        //pass in the previous age
        //intent.putExtra("user",user);

        startActivityForResult(intent,SETTINGS_REQUEST_CODE);
    }
}
