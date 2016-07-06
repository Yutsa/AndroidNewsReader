package com.openclassroomapp.newsreader;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * @author Édouard WILLISSECK
 */
public class ArticleFragment extends Fragment {
    public static ArticleFragment create(String title, String link) {
        Bundle args = new Bundle();
        args.putString("link", link);
        args.putString("title", title);
        ArticleFragment articleFragment = new ArticleFragment();
        articleFragment.setArguments(args);
        return articleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.article_layout, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getArguments().getString("title"));
        WebView webView = (WebView) view.findViewById(R.id.webView);
        if (webView != null) {
            webView.loadUrl(getArguments().getString("link"));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.article_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String link = getArguments().getString("link");
        switch (item.getItemId()) {
            case R.id.share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, link);
                startActivity(Intent.createChooser(intent, getString(R.string.share_article)));
                return true;
            case R.id.open_in_browser:
                Intent intent_browser = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent_browser);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
