package com.openclassroomapp.newsreader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oc.rss.fake.FakeNews;
import com.oc.rss.fake.FakeNewsList;

import java.util.List;
import java.util.zip.Inflater;

/**
 * @author Édouard WILLISSECK
 *
 * This is the Adapter for the list of articles from FakeNews.jar
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<FakeNews> news = FakeNewsList.all;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cell_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FakeNews newsToDisplay = news.get(position);
        holder.display(newsToDisplay);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private FakeNews currentNews;
        private final Context context;

        public MyViewHolder(final View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,
                            ArticleActivity.class);
                    intent.putExtra("title", currentNews.title);
                    intent.putExtra("content", currentNews.htmlContent);
                    context.startActivity(intent);

                }
            });
        }

        public void display(FakeNews news) {
            currentNews = news;
            title.setText(news.title);
        }
    }
}
