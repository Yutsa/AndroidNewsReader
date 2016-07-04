package com.openclassroomapp.newsreader;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;

/**
 * @author Édouard WILLISSECK
 */
public class MainFragment extends Fragment {
    private final ArrayList<XMLAsyncTask> xmlAsyncTasks = new ArrayList<>();
    private final ArrayList<String> urls = new ArrayList<>();
    private RSSArticleAdapter adapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView rv = (RecyclerView) view.findViewById(R.id.list);
        adapter = new RSSArticleAdapter();

        if (rv != null) {
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv.setAdapter(adapter);
        }

        /* Handling the progressbar */
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
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
    public void onDestroy() {
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            loadArticles();
        } else if (item.getItemId() == R.id.add_url) {
            createInputDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates the Dialog for entering a new URL.
     */
    private void createInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.add_url_string));

        final EditText input = new EditText(getActivity());
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
