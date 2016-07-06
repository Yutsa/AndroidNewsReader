package com.openclassroomapp.newsreader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Édouard WILLISSECK
 *
 * This is the Adapater for the article list gotten from the Document (DOM).
 */
public class RSSArticleAdapter extends RecyclerView.Adapter<RSSArticleAdapter.ArticleViewHolder>
        implements XMLAsyncTask.DocumentConsumer {

    private final ArrayList<RSSArticle> articleList = new ArrayList<>();
    private URLLoader urlLoader;

    interface URLLoader {
        void load(String title, String link);
    }

    public RSSArticleAdapter(URLLoader urlLoader) {
        this.urlLoader = urlLoader;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cell_layout, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        holder.display(articleList.get(position));
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    @Override
    public void addArticle(ArrayList<RSSArticle> articles) {
        if (articles != null)
            articleList.addAll(articles);
        Collections.sort(articleList, new Comparator<RSSArticle>() {
            @Override
            public int compare(RSSArticle lhs, RSSArticle rhs) {
                return rhs.getDate().compareTo(lhs.getDate());
            }
        });
        notifyDataSetChanged();
    }

    public void clearDocuments() {
        articleList.clear();
        Log.i("RSSArticleAdapter", "Cleared documents");
    }

    public boolean hasDocuments() {
        return !articleList.isEmpty();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView date;
        private final Context context;
        private RSSArticle currentArticle;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.article_date);
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    urlLoader.load(currentArticle.getTitle(), currentArticle.getLink());
                }
            });
        }

        public void display(RSSArticle article) {
            currentArticle = article;
            title.setText(article.getTitle());
            date.setText(article.getDateAsString());
        }
    }
}
