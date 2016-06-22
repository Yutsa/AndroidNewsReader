package com.openclassroomapp.newsreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.w3c.dom.Document;

public class MainActivity extends AppCompatActivity {

    private XMLAsyncTask xmlAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("RMS feed");

        RecyclerView rv = (RecyclerView) findViewById(R.id.list);
        RSSArticleAdapter adapter = new RSSArticleAdapter();

        if (rv != null) {
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(adapter);
        }

        xmlAsyncTask = new XMLAsyncTask(adapter);
        xmlAsyncTask.execute("https://stallman.org/rss/rss.xml");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (xmlAsyncTask != null) {
            xmlAsyncTask.cancel(true);
        }
    }
}
