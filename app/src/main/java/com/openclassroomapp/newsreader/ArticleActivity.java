package com.openclassroomapp.newsreader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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

        ArticleFragment articleFragment = ArticleFragment.create(
                title, getIntent().getStringExtra("link"));

        getFragmentManager().beginTransaction()
                .add(android.R.id.content, articleFragment)
                .commit();
    }
}
