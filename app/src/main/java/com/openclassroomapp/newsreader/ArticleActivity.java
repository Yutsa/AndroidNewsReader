package com.openclassroomapp.newsreader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * @author Édouard WILLISSECK
 */
public class ArticleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_layout);

        String title = getIntent().getStringExtra("title");
        setTitle(title);

        String url = getIntent().getStringExtra("link");


        WebView webView = (WebView) findViewById(R.id.webView);
        if (webView != null) {
            webView.loadUrl(url);
        }
    }
}
