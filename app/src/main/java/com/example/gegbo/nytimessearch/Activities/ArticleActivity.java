package com.example.gegbo.nytimessearch.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.gegbo.nytimessearch.Article;
import com.example.gegbo.nytimessearch.R;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Article article = (Article) getIntent().getSerializableExtra("article");

        WebView webview = (WebView) findViewById(R.id.wvArticle);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webview.loadUrl(article.getWeburl());
    }
}
