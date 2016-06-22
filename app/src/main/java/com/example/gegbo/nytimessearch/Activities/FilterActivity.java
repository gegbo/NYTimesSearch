package com.example.gegbo.nytimessearch.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gegbo.nytimessearch.R;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //create a model called Filter Objects that contains all the options that I want configured (so that can be easier to be passed around)
        //find a way to convert the info from the filter object to create a search request.
    }
}
