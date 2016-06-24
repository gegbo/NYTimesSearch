package com.example.gegbo.nytimessearch.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.gegbo.nytimessearch.Adapters.ArticleArrayAdapter;
import com.example.gegbo.nytimessearch.EndlessRecyclerViewScrollListener;
import com.example.gegbo.nytimessearch.Fragments.FilterSettingsFragment;
import com.example.gegbo.nytimessearch.ItemClickSupport;
import com.example.gegbo.nytimessearch.Models.Article;
import com.example.gegbo.nytimessearch.Models.Filter;
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

public class SearchActivity extends AppCompatActivity implements FilterSettingsFragment.FragmentSettingsListener {

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    RecyclerView rvArticles;
    StaggeredGridLayoutManager gridLayoutManager;
    private boolean isTopArticles = true;
    Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpViews();
        displayTopArticles();
        //showEditDialog();
    }

    public void setUpViews() {

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        // Lookup the recyclerview in activity layout
        rvArticles = (RecyclerView) findViewById(R.id.rvArticles);
        // Initialize articles
        articles = new ArrayList<>();
        // Create adapter passing in the sample user data
        adapter = new ArticleArrayAdapter(this,articles);
        // Attach the adapter to the recyclerview to populate items
        rvArticles.setAdapter(adapter);
        // Set layout manager to position the items
        gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        rvArticles.setLayoutManager(gridLayoutManager);

        // Add the scroll listener
        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list

                if(!isTopArticles)
                {
                    loadMoreArticles(page);
                }
            }
        });

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
        fetchArticles(page);
    }

    public void onArticleSearch() {
        articles.clear();
        adapter.notifyDataSetChanged();
        fetchArticles(0);
    }

    public void fetchArticles(final int page) {

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        SearchView searchView = (SearchView) findViewById(R.id.action_search);

        RequestParams params = new RequestParams();

        params.put("q",searchView.getQuery());
        if(filter != null) {
            params.put("fq","news_desk:(\"Sports\")"); //Why isn't this working!!?
            params.put("begin_date",filter.getBeginDate());
            params.put("sort",filter.getSort());
        }
        params.put("page",page);
        params.put("api-key","8bf8d85b59c44f64b56aadd0e9958a0b");



        client.get(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } );
    }

    public void displayTopArticles() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/topstories/v2/home.json";

        RequestParams params = new RequestParams();

        params.put("api-key","3931788f3f054e13b859ae0bbea30f54");

        client.get(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONArray("results");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
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
                isTopArticles = false;
                // perform query here
                onArticleSearch();

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
                showEditDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterSettingsFragment editNameDialogFragment = FilterSettingsFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable("filter",Parcels.wrap(filter));
        editNameDialogFragment.setArguments(bundle);
        editNameDialogFragment.show(fm,"fragment_edit_name");
    }


    @Override
    public void onSetFilter(Filter f) {
        filter = f;
        Log.d("dateFilter",filter.getBeginDate());
        Toast.makeText(this,"Filter has been set!",Toast.LENGTH_SHORT).show();
        onArticleSearch();
    }
}
