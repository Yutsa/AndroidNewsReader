package com.openclassroomapp.newsreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements RSSArticleAdapter.URLLoader {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        getFragmentManager().beginTransaction()
                .add(R.id.listFragment, new MainFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            getFragmentManager().popBackStack();
        } else
            super.onBackPressed();
    }

    @Override
    public void load(String title, String link) {
        if (findViewById(R.id.articleFragment) != null) {
            Log.i("MainActivity", "Loading for tablets.");
            ArticleFragment articleFragment = ArticleFragment.create(title, link);
            getFragmentManager().beginTransaction()
                    .replace(R.id.articleFragment, articleFragment)
                    .addToBackStack(title)
                    .commit();
        } else {
            Log.i("MainActivity", "Loading for phones.");
            Intent intent = new Intent(this, ArticleActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("link", link);
            startActivity(intent);
        }
    }
}
