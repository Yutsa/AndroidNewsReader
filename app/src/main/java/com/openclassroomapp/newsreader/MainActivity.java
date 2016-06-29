package com.openclassroomapp.newsreader;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<XMLAsyncTask> xmlAsyncTasks = new ArrayList<>();
    private final ArrayList<String> urls = new ArrayList<>();
    private RSSArticleAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.title);

        RecyclerView rv = (RecyclerView) findViewById(R.id.list);
        adapter = new RSSArticleAdapter();

        if (rv != null) {
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(adapter);
        }

        /* Handling the progressbar */
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


        addUrl("http://www.phoronix.com/rss.php");
        addUrl("http://www.notebookcheck.net/RSS-Feed-Notebook-Reviews.8156.0.html");
        addUrl("http://www.lemonde.fr/rss/une.xml");
        loadArticles();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (XMLAsyncTask xmlAsyncTask : xmlAsyncTasks) {
            if ((xmlAsyncTask != null) && (xmlAsyncTask.getStatus() != AsyncTask.Status.FINISHED)) {
                xmlAsyncTask.cancel(true);
                Log.i("OC News Reader", "Task canceled.");
            }
        }
    }

    private void addUrl(String url) {
        if ((url != null) && !url.trim().equals("")) {
            urls.add(url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            loadArticles();
        } else if (item.getItemId() == R.id.add_url) {
            createInputDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadArticles() {
        if (adapter.hasDocuments())
            adapter.clearDocuments();

        progressBar.setVisibility(View.VISIBLE);

        for (String url :
                urls) {
            XMLAsyncTask task = new XMLAsyncTask(adapter);
            xmlAsyncTasks.add(task);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        }
    }

    /**
     * Creates the Dialog for entering a new URL.
     */
    private void createInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_url_string));

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.validate),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addUrl(input.getText().toString());
                        loadArticles();
                    }
                });

        builder.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        builder.show();
    }
}
