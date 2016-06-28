package com.openclassroomapp.newsreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private XMLAsyncTask xmlAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.title);

        RecyclerView rv = (RecyclerView) findViewById(R.id.list);
        RSSArticleAdapter adapter = new RSSArticleAdapter();

        if (rv != null) {
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(adapter);
        }

        xmlAsyncTask = new XMLAsyncTask(adapter);
        xmlAsyncTask.execute("http://www.phoronix.com/rss.php");
        //new XMLAsyncTask(adapter).execute("http://www.notebookcheck.net/RSS-Feed-Notebook-Reviews.8156.0.html");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (xmlAsyncTask != null) {
            xmlAsyncTask.cancel(true);
        }
    }
}
